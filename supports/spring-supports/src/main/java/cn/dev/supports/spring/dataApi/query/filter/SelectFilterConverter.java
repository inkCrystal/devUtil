package cn.dev.supports.spring.dataApi.query.filter;

import cn.dev.supports.spring.dataApi.DataModel;
import cn.dev.supports.spring.dataApi.query.OpEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.*;

public class SelectFilterConverter {


    public static String toJson(Node filter) throws JSONException, JsonProcessingException {
        List<Map<String,Object>> list = new ArrayList<>();
        entryToMap(list,filter);
        return new ObjectMapper().writeValueAsString(list);
    }


    private static void entryToMap(List<Map<String,Object>> list, Node entry ){
        Map<String,Object> map = new HashMap<>();
        map.put("k",entry.key());
        map.put("op",entry.opEnum().toString());
        map.put("paramType",entry.isKeyParam()?"k":"v");
        map.put("params",entry.readValuesWithOutCheck());
        if (entry.innerNode().isPresent()) {
            List<Map<String,Object>> orList =new ArrayList<>();
            entryToMap(orList,entry.innerNode().get());
            map.put("orFilter",orList);
        }
        list.add(map);
        if (entry.nextNode().isPresent()) {
            entryToMap(list,entry.nextNode().get());
        }
    }


    public static AvailableFilter fromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String,Object>> list = objectMapper.readValue(json, List.class);
        Filter filter = null;
        for (Map<String, Object> map : list) {
            filter = appenFilter(filter, map);
        }
        return filter.toAvailableFilter();
    }

    private static Filter appenFilter(Filter preFilter , Map<String,Object> map){
        String key = (String) map.get("k");
        OpEnum op = OpEnum.valueOf((String) map.get("op"));
        String paramType = (String) map.get("paramType");
        boolean isKeyParam = "k".equals(paramType);
        List<Map<String,Object>> params = (List<Map<String,Object>>) map.getOrDefault("params", Collections.emptyList());
        List<Map<String,Object>> orFilter = (List<Map<String,Object>>) map.getOrDefault("orFilter", Collections.emptyList());
        Filter filter =  Filter.build(key,op,params.toArray(new Serializable[0]));
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

    private static void appendOr(Filter outFilter ,List<Map<String,Object>> orFilter){
        Map<String, Object> map = orFilter.get(0);
        Filter filter = appenFilter(null, map);
        outFilter.thenOrFilter(Mono.just(filter));
        if(orFilter.size()>1){
            for (int i = 1; i <orFilter.size() ; i++) {
                filter = appenFilter(filter,orFilter.get(i));
            }
        }
    }


    public static void main(String[] args) throws JSONException, JsonProcessingException {
        // select keys from tb where () and ()
        final SelectFilterBuilder<DataModel> dataModelSelectFilterBuilder =
                SelectFilterBuilder.getBuilder(DataModel.class).thenNotEqual("id", 12344)
                        .thenOR().thenEqual("id", 12313).breakOR()
                .thenIn("age", 1, 2, 3)
                        .thenOR().thenEqual("name", "张三").thenEqualKey("name", "name").breakOR();


        System.out.println(toJson(dataModelSelectFilterBuilder.getFirstEntry()));
    }


}
