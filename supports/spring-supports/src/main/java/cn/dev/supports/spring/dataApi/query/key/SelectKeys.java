package cn.dev.supports.spring.dataApi.query.key;

public class SelectKeys {

    private static final SelectKeys ALL = new SelectKeys(new String[]{"*"});

    private int skip  = 0 ;
    private int limit =10;
    private final String[] keyArray;

    private SelectKeys(String[] keys) {
        this.keyArray = keys;
    }


    public static SelectKeys select(String... keys) {
        return new SelectKeys(keys);
    }

    public static SelectKeys selectAll() {
        return ALL;
    }

    public static SelectKeys selectId() {
        return new SelectKeys(new String[]{"id"});
    }

    public String selectKeyQueryString(){
        StringBuilder sb = new StringBuilder();
        int index = 0 ;
        for (String key : keyArray) {
            if(index>0){
                sb.append(",");
            }
            sb.append(key);
            index ++ ;
        }
        return sb.toString();
    }

    public String selectKeyQueryStringWithAlias(String alias){
        StringBuilder sb = new StringBuilder();
        int index = 0 ;
        for (String key : keyArray) {
            if(index>0){
                sb.append(",");
            }
            sb.append(alias).append(".").append(key);
            index ++ ;
        }
        return sb.toString();
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public int getSkip() {
        return skip;
    }
}
