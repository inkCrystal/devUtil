package cn.dev.supports.spring.dataApi.query.filter;

import cn.dev.supports.spring.dataApi.query.OpEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.*;

public class _FilterConverter {


    public static String toJson(_Filter filter) throws JSONException, JsonProcessingException {
        List<Map<String,Object>> list = new ArrayList<>();
        entryToMap(list,filter);
        return new ObjectMapper().writeValueAsString(list);
    }


    private static void entryToMap(List<Map<String,Object>> list, _Filter entry ){
        Map<String,Object> map = new HashMap<>();
        map.put("k",entry.getKey());
        map.put("op",entry.getOp().toString());
        map.put("paramType",entry.isKeyParam()?"k":"v");
        map.put("params",entry.getAllTypeValues());
        if (entry.getInnerBean()!=null) {
            List<Map<String,Object>> orList =new ArrayList<>();
            entryToMap(orList,entry.getInnerBean());
            map.put("orFilter",orList);
        }
        list.add(map);
        if (entry.getNext()!=null) {
            entryToMap(list,entry.getNext());
        }
    }


    public static AvailableFilter fromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String,Object>> list = objectMapper.readValue(json, List.class);
        _Filter filter = null;
        for (Map<String, Object> map : list) {
            filter = appenFilter(filter, map);
        }
        return filter.toAvailableFilter();
    }

    private static _Filter appenFilter(_Filter preFilter , Map<String,Object> map){
        String key = (String) map.get("k");
        OpEnum op = OpEnum.valueOf((String) map.get("op"));
        String paramType = (String) map.get("paramType");
        boolean isKeyParam = "k".equals(paramType);
        List<Map<String,Object>> params = (List<Map<String,Object>>) map.getOrDefault("params", Collections.emptyList());
        List<Map<String,Object>> orFilter = (List<Map<String,Object>>) map.getOrDefault("orFilter", Collections.emptyList());
        _Filter filter =  _Filter.build(key,op,params.toArray(new Serializable[0]));
        if(isKeyParam){
            filter.markParamIsKey();
        }
        if(preFilter != null){
            preFilter.appendNextFilter(filter);
        }
        if(!orFilter.isEmpty()){
            appendOr(filter,orFilter);
        }
        return filter;
    }

    private static void appendOr(_Filter outFilter , List<Map<String,Object>> orFilter){
        Map<String, Object> map = orFilter.get(0);
        _Filter filter = appenFilter(null, map);
        outFilter.thenOrFilter(Mono.just(filter));
        if(orFilter.size()>1){
            for (int i = 1; i <orFilter.size() ; i++) {
                filter = appenFilter(filter,orFilter.get(i));
            }
        }
    }


    public static void main(String[] args) throws JSONException, JsonProcessingException {
        // select keys from tb where () and ()
        final _Filter bean =
                _Filter.builder().whereLessThan("da", 12.2)
                        .thenEqual("name","周伟")
                            .thenOr(_Filter.orBuilder().whereBetweenAnd("id", 1,2)).thenEqual("a","1d")
                                .thenOr(_Filter.orBuilder().whereBetweenAnd("id", 1,2)).breakOr()
                            .breakOr()
                        .thenLessThanKey("keyLess","KeyBig")
                        .thenBetweenAnd("id", 1,2)

        ;
        String filterQuery1 = bean.toAvailableFilter().getFilterQuery();
        final List<Serializable> valueList = bean.toAvailableFilter().getValueList();
        System.out.println(filterQuery1);
        for (int i = 0; i < valueList.size(); i++) {
            filterQuery1 = filterQuery1.replaceFirst("\\?",valueList.get(i).toString());
        }

        System.out.println(filterQuery1);
        final String json = toJson(bean.findStart());
        System.out.println(json);
        final List<Map<String,Object>> list = new ObjectMapper().readValue(json, List.class);
        final AvailableFilter availableFilter = fromJson(json);
        System.out.println(availableFilter.getFilterQuery());


    }


}
