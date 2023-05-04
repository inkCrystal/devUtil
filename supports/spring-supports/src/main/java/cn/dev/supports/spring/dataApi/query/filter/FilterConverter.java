package cn.dev.supports.spring.dataApi.query.filter;

import cn.dev.supports.spring.dataApi.DataModel;
import cn.dev.supports.spring.dataApi.query.OpEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;

import java.io.Serializable;
import java.util.*;

public class FilterConverter {


    public static String toJson(Class type,Node filter) throws JSONException, JsonProcessingException {
        List<Map<String,Object>> list = new ArrayList<>();
        nodeToMap(list,filter);
        Map<String,Object> map = new HashMap<>();
        map.put("body",list);
        map.put("type",type.getName());
        return new ObjectMapper().writeValueAsString(map);
    }



    private static void nodeToMap(List<Map<String,Object>> list, Node entry ){
        Map<String,Object> map = new HashMap<>();
        map.put("k",entry.key());
        map.put("op",entry.opEnum().toString());
        map.put("paramType",entry.isKeyParam()?"k":"v");
        map.put("params",entry.readValuesWithOutCheck());
        if (entry.innerNode().isPresent()) {
            List<Map<String,Object>> orList =new ArrayList<>();
            nodeToMap(orList,entry.innerNode().get());
            map.put("orFilter",orList);
        }
        list.add(map);
        if (entry.nextNode().isPresent()) {
            nodeToMap(list,entry.nextNode().get());
        }
    }

    public static AvailableFilter fromJsonStr(String json ,Class<?> targetClass) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> mainMap = (Map<String, Object>) objectMapper.readValue(json, Map.class);
        return fromMap(mainMap,targetClass);
    }


    public static AvailableFilter fromJsonStr(String json) throws JsonProcessingException, ClassNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> mainMap = (Map<String, Object>) objectMapper.readValue(json, Map.class);
        String type = (String) mainMap.get("type");
        Class<?> aClass = Class.forName(type);
        return fromMap(mainMap,aClass);
    }


    private static AvailableFilter fromMap(Map<String,Object> mainMap ,Class<?> targetClass){
        List<Map<String, Object>> list = (List<Map<String, Object>>) mainMap.get("body");
        var builder = FilterBuilder.getBuilder(targetClass);
        for (Map<String, Object> entryMap : list) {
            callBuild(builder,entryMap);
        }
        return builder.toAvailableFilter();
    }

    private static <T extends DataModel>void callBuild(FilterBuilder<T> builder , Map<String,Object> entryMap){
        OpEnum op = OpEnum.valueOf((String) entryMap.get("op"));
        String key = (String) entryMap.get("k");
        String paramType = (String) entryMap.get("paramType");
        boolean isKeyParam = "k".equals(paramType);
        List<Serializable>params = (List<Serializable>) entryMap.getOrDefault("params", Collections.emptyList());
        switch (op){
            case IN -> builder.thenIn(key,params.toArray(new Serializable[0]));
            case NOT_IN -> builder.thenNotIn(key,params.toArray(new Serializable[0]));
            case LIKE -> builder.thenLike(key,(String) params.get(0));
            case NOT_LIKE -> builder.thenNotLike(key,(String) params.get(0));
            case IS_NULL -> builder.thenIsNull(key);
            case IS_NOT_NULL -> builder.thenIsNotNull(key);
            case BETWEEN_AND -> builder.thenBetweenAnd(key,params.get(0),params.get(1));
            /////////////////////////////////////////////////////////////////////////
            case EQUAL ->{
                if (isKeyParam) {
                    builder.thenEqualKey(key, (String) params.get(0));
                } else {
                    builder.thenEqual(key, params.get(0));
                }
            }
            case NOT_EQUAL -> {
                if (isKeyParam) {
                    builder.thenNotEqualKey(key, (String) params.get(0));
                } else {
                    builder.thenNotEqual(key, params.get(0));
                }
            }
            case GREATER_THAN -> {
                if (isKeyParam) {
                    builder.thenGreaterThanKey(key, (String) params.get(0));
                } else {
                    builder.thenGreaterThan(key, (Number) params.get(0));
                }
            }
            case GREATER_THAN_OR_EQUAL ->{
                if (isKeyParam) {
                    builder.thenGreaterThanOrEqualKey(key, (String) params.get(0));
                } else {
                    builder.thenGreaterThanOrEqual(key, (Number) params.get(0));
                }
            }
            case LESS_THAN ->{
                if (isKeyParam) {
                    builder.thenLessThanKey(key, (String) params.get(0));
                } else {
                    builder.thenLessThan(key, (Number) params.get(0));
                }
            }
            case LESS_THAN_OR_EQUAL -> {
                if (isKeyParam) {
                    builder.thenLessThanOrEqualKey(key, (String) params.get(0));
                } else {
                    builder.thenLessThanOrEqual(key, (Number) params.get(0));
                }
            }
            default -> {}
        }
        List<Map<String,Object>> orFilter = (List<Map<String,Object>>) entryMap.getOrDefault("orFilter", Collections.emptyList());
        if (orFilter!=null && orFilter.size()>0) {
            builder.thenOR();
            for (Map<String, Object> orEntryMap : orFilter) {
                callBuild(builder,orEntryMap);
            }
            builder.breakOR();
        }
    }


    private static Node mapToNode(Map<String, Object> entryMap ){
        String key = (String) entryMap.get("k");
        OpEnum op = OpEnum.valueOf((String) entryMap.get("op"));
        String paramType = (String) entryMap.get("paramType");
        boolean isKeyParam = "k".equals(paramType);
        List<Map<String,Object>> params = (List<Map<String,Object>>) entryMap.getOrDefault("params", Collections.emptyList());
        List<Map<String,Object>> orFilter = (List<Map<String,Object>>) entryMap.getOrDefault("orFilter", Collections.emptyList());
        return new Node(key,op,isKeyParam,params.toArray(new Serializable[0]));
    }

    private static Node buildNode(Node preNode ,Map<String,Object> entryMap){
        String key = (String) entryMap.get("k");
        OpEnum op = OpEnum.valueOf((String) entryMap.get("op"));
        String paramType = (String) entryMap.get("paramType");
        boolean isKeyParam = "k".equals(paramType);
        List<Map<String,Object>> params = (List<Map<String,Object>>) entryMap.getOrDefault("params", Collections.emptyList());
        List<Map<String,Object>> orFilter = (List<Map<String,Object>>) entryMap.getOrDefault("orFilter", Collections.emptyList());
        Node node = new Node(key,op,isKeyParam,params.toArray(new Serializable[0]));
        if(preNode != null){
            preNode.appendNext(node);
        }
        if(!orFilter.isEmpty()){
            buildOrNode(node,orFilter);
        }
        return node;
    }

    private static void buildOrNode(Node outNode ,List<Map<String,Object>> orFilter){
        Map<String, Object> map = orFilter.get(0);
        Node node = buildNode(null, map);
        node = outNode.appendOrNoteEntry(node);
        if(orFilter.size()>1){
            for (int i = 1; i <orFilter.size() ; i++) {
                node = buildNode(node, orFilter.get(i));
            }
        }
    }


//
//
//    public static AvailableFilter fromJson(String json) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<Map<String,Object>> list = objectMapper.readValue(json, List.class);
//        Filter filter = null;
//        for (Map<String, Object> map : list) {
//            filter = appenFilter(filter, map);
//        }
//        return filter.toAvailableFilter();
//    }
//
//
//
//    private static Filter appenFilter(Filter preFilter , Map<String,Object> map){
//        String key = (String) map.get("k");
//        OpEnum op = OpEnum.valueOf((String) map.get("op"));
//        String paramType = (String) map.get("paramType");
//        boolean isKeyParam = "k".equals(paramType);
//        List<Map<String,Object>> params = (List<Map<String,Object>>) map.getOrDefault("params", Collections.emptyList());
//        List<Map<String,Object>> orFilter = (List<Map<String,Object>>) map.getOrDefault("orFilter", Collections.emptyList());
//        Filter filter =  Filter.build(key,op,params.toArray(new Serializable[0]));
//        if(isKeyParam){
//            filter.markParamIsKey();
//        }
//        if(preFilter != null){
//            preFilter.appendNextFilter(filter);
//        }
//        if(!orFilter.isEmpty()){
//            appendOr(filter,orFilter);
//        }
//        return filter;
//    }
//
//    private static void appendOr(Filter outFilter ,List<Map<String,Object>> orFilter){
//        Map<String, Object> map = orFilter.get(0);
//        Filter filter = appenFilter(null, map);
//        outFilter.thenOrFilter(Mono.just(filter));
//        if(orFilter.size()>1){
//            for (int i = 1; i <orFilter.size() ; i++) {
//                filter = appenFilter(filter,orFilter.get(i));
//            }
//        }
//    }


    public static void main(String[] args) throws JSONException, JsonProcessingException, ClassNotFoundException {


        // select keys from tb where () and ()
        final FilterBuilder<DataModel> dataModelFilterBuilder =
                FilterBuilder.getBuilder(DataModel.class).thenNotEqual("id", 12344)
                        .thenOR().thenEqual("id", 12313).breakOR()
                .thenIn("age", 1, 2, 3).thenLessThan("age", 133).thenLessThanOrEqual("age", 133).thenGreaterThanKey("dateofBirth","bith")
                        .thenOR().thenEqual("name", "张三").thenEqualKey("name", "name").breakOR()
                .thenBetweenAnd("num", 1, 2).thenGreaterThan("num", 1);



//        System.out.println(new ObjectMapper().writeValueAsString(dataModelSelectFilterBuilder.getFirstEntry()));
        final String json = toJson(dataModelFilterBuilder.getModelType(), dataModelFilterBuilder.getFirstEntry());
        System.out.println(json);
        final AvailableFilter availableFilter = fromJsonStr(json);
        System.out.println(dataModelFilterBuilder.toAvailableFilter().getFilterQuery());
        System.out.println(availableFilter.getFilterQuery());


    }


}
