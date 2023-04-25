package cn.dev.utils.supports.dataApi.query.key;

public class SelectKeys {

    private static final SelectKeys ALL = new SelectKeys(new String[]{"*"});

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

    public String toSelectKeysString(){
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
}
