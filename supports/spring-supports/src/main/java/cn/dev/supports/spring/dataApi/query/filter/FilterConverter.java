package cn.dev.supports.spring.dataApi.query.filter;

import cn.dev.supports.spring.dataApi.DataModel;
import cn.dev.supports.spring.dataApi.query.OpEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.parser.JSONParser;
import org.json.JSONException;

import java.io.Serializable;
import java.util.*;

public class FilterConverter {

    private static final String KEY_BODY = "body";
    private static final String KEY_CLASS_TYPE ="type";
    private static final String KEY_ITEM_KEY = "k";
    private static final String KEY_ITEM_VALUE = "v";
    private static final String KEY_ITEM_VALUE_TYPE = "vType";
    private static final String KEY_ITEM_OP = "op";
    private static final String KEY_OR_BODY = "body";




    public static String toJson(Class type,Node filter) throws JSONException, JsonProcessingException {
        List<Map<String,Object>> list = new ArrayList<>();
        nodeToMap(list,filter);
        Map<String,Object> map = new HashMap<>();
        map.put(KEY_BODY,list);
        map.put(KEY_CLASS_TYPE,type.getName());
        return new ObjectMapper().writeValueAsString(map);
    }



    private static void nodeToMap(List<Map<String,Object>> list, Node entry ){
        Map<String,Object> map = new HashMap<>();
        map.put(KEY_ITEM_KEY,entry.key());
        map.put(KEY_ITEM_OP,entry.opEnum().toString());
        map.put(KEY_ITEM_VALUE_TYPE,entry.isKeyParam()?KEY_ITEM_KEY:"v");
        map.put(KEY_ITEM_VALUE,entry.readValuesWithOutCheck());
        if (entry.innerNode().isPresent()) {
            List<Map<String,Object>> orList =new ArrayList<>();
            nodeToMap(orList,entry.innerNode().get());
            map.put(KEY_OR_BODY,orList);
        }
        list.add(map);
        if (entry.nextNode().isPresent()) {
            nodeToMap(list,entry.nextNode().get());
        }
    }

    public Map<String,Object>  jsonToMap(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> mainMap = (Map<String, Object>) objectMapper.readValue(json, Map.class);
        if(mainMap.containsKey(KEY_BODY)){
            if (mainMap.get(KEY_BODY) instanceof List) {
                return mainMap;
            }
        }else{

        }

        return null;




    }




    public static <T extends DataModel> AvailableFilter fromJsonStr(String json ,Class<T> targetClass) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> mainMap = (Map<String, Object>) objectMapper.readValue(json, Map.class);
        return fromMap(mainMap,targetClass);
    }


    public static AvailableFilter fromJsonStr(String json) throws JsonProcessingException, ClassNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> mainMap = (Map<String, Object>) objectMapper.readValue(json, Map.class);
        String type = (String) mainMap.get(KEY_CLASS_TYPE);
        Class<?> aClass = Class.forName(type);
        return fromMap(mainMap,(Class<? extends DataModel>) aClass);
    }


    private static <T extends DataModel>AvailableFilter fromMap(Map<String,Object> mainMap ,Class<T> targetClass){
        List<Map<String, Object>> list = (List<Map<String, Object>>) mainMap.get("body");
        var builder = FilterBuilder.getBuilder(targetClass);
        for (Map<String, Object> entryMap : list) {
            callBuild(builder,entryMap);
        }
        return builder.toAvailableFilter();
    }

    private static <T extends DataModel>void callBuild(FilterBuilder<T> builder , Map<String,Object> entryMap){
        OpEnum op = OpEnum.valueOf((String) entryMap.get(KEY_ITEM_OP));
        String key = (String) entryMap.get(KEY_ITEM_KEY);
        String paramType = (String) entryMap.get(KEY_ITEM_VALUE_TYPE);
        boolean isKeyParam = KEY_ITEM_KEY.equals(paramType);
        List<Serializable>params = (List<Serializable>) entryMap.getOrDefault(KEY_ITEM_VALUE, Collections.emptyList());
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
        List<Map<String,Object>> orFilter = (List<Map<String,Object>>) entryMap.getOrDefault(KEY_OR_BODY, Collections.emptyList());
        if (orFilter!=null && orFilter.size()>0) {
            builder.thenOR();
            for (Map<String, Object> orEntryMap : orFilter) {
                callBuild(builder,orEntryMap);
            }
            builder.breakOR();
        }
    }


    private static Node mapToNode(Map<String, Object> entryMap ){
        String key = (String) entryMap.get(KEY_ITEM_KEY);
        OpEnum op = OpEnum.valueOf((String) entryMap.get(KEY_ITEM_OP));
        String paramType = (String) entryMap.get(KEY_ITEM_VALUE_TYPE);
        boolean isKeyParam = KEY_ITEM_KEY.equals(paramType);
        List<Map<String,Object>> params = (List<Map<String,Object>>) entryMap.getOrDefault(KEY_ITEM_VALUE, Collections.emptyList());
        List<Map<String,Object>> orFilter = (List<Map<String,Object>>) entryMap.getOrDefault(KEY_OR_BODY, Collections.emptyList());
        return new Node(key,op,isKeyParam,params.toArray(new Serializable[0]));
    }

    private static Node buildNode(Node preNode ,Map<String,Object> entryMap){
        String key = (String) entryMap.get(KEY_ITEM_KEY);
        OpEnum op = OpEnum.valueOf((String) entryMap.get(KEY_ITEM_OP));
        String paramType = (String) entryMap.get(KEY_ITEM_VALUE_TYPE);
        boolean isKeyParam = KEY_ITEM_KEY.equals(paramType);
        List<Map<String,Object>> params = (List<Map<String,Object>>) entryMap.getOrDefault(KEY_ITEM_VALUE, Collections.emptyList());
        List<Map<String,Object>> orFilter = (List<Map<String,Object>>) entryMap.getOrDefault(KEY_OR_BODY, Collections.emptyList());
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


    public static void main(String[] args) throws JSONException, JsonProcessingException, ClassNotFoundException {


        // select keys from tb where () and ()

        final FilterBuilder<TestBean> dataModelFilterBuilder = FilterBuilder.getBuilder(TestBean.class).thenNotEqual("id", 12344)
                .thenOR().thenEqual("id", 12313).breakOR();
        dataModelFilterBuilder
                .thenIn("age", 1, 2, 3).thenLessThan("age", 133)
                .thenLessThanOrEqual("age", 133).thenGreaterThanKey("dateofBirth", "dateofBirth")
                .thenOR().thenEqual("name", "张三").thenEqualKey("name", "name").breakOR()
                .thenBetweenAnd("num", 1, 2).thenGreaterThan("num", 1);
        System.out.println(dataModelFilterBuilder.getModelType());


//        System.out.println(new ObjectMapper().writeValueAsString(dataModelSelectFilterBuilder.getFirstEntry()));
        final String json = toJson(dataModelFilterBuilder.getModelType(), dataModelFilterBuilder.getFirstEntry());
        System.out.println(json);
        final AvailableFilter availableFilter = fromJsonStr(json);
        System.out.println(dataModelFilterBuilder.toAvailableFilter().getFilterQuery());
        System.out.println(availableFilter.getFilterQuery());

        System.out.println( fromJsonStr("{\"body\":[{\"op\":\"IN\",\"vType\":\"v\",\"v\":[1,2,3],\"body\":[{\"op\":\"EQUAL\",\"vType\":\"v\",\"v\":[1234]},{\"op\":\"BETWEEN_AND\",\"vType\":\"v\",\"v\":[3,100]}]},{\"op\":\"LESS_THAN\",\"vType\":\"v\",\"v\":[18]}]}"));


    }

    class jsonCheckResult{
        /**检查是否通过  ---by jason @ 2023/5/5 13:26 */
        private boolean success;
        private String fixedJson ;
        private Map<String,Object> jsonMap;

        public jsonCheckResult(boolean success, String fixedJson) {
            this.success = success;
            this.fixedJson = fixedJson;
        }

        public void setJsonMap(Map<String, Object> jsonMap) {
            this.jsonMap = jsonMap;
        }

        public Map<String, Object> getJsonMap() {
            return jsonMap;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getFixedJson() {
            return fixedJson;
        }
    }

}
