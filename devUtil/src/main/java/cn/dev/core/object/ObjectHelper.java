package cn.dev.core.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectHelper implements IObjectHelper{
    private static final Logger logger = LoggerFactory.getLogger(ObjectHelper.class);

    @Override
    public <T extends Serializable> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        return null;
    }

    @Override
    public <T extends Serializable> Map<String, Object> beanToMap(T t) {
        return null;
    }

    @Override
    public <T extends Serializable> byte[] serialize(T t) {
        return new byte[0];
    }

    @Override
    public <T extends Serializable> T deserialize(byte[] bytes, Class<T> tClass) {
        return null;
    }

    @Override
    public <T extends Serializable> void serializeToFile(T t, String filePath) {

    }

    @Override
    public <T extends Serializable> T deserializeFromFile(String filePath, Class<T> tClass) {
        return null;
    }

    @Override
    public <T extends Serializable> void serializeToString(T t) {

    }

    @Override
    public <T extends Serializable> T deserializeFromString(String str, Class<T> tClass) {
        return null;
    }

    @Override
    public <T extends Serializable> T deepClone(T t) {
        return null;
    }

    @Override
    public List<Field> getFields(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        for( Field field : clazz.getDeclaredFields()){
            list.add(field);
        }
        while (!clazz.getSuperclass().toString().contains("java.lang.Object")){
            clazz = clazz.getSuperclass() ;
            for( Field field : clazz.getDeclaredFields()){
                boolean containsName = false;
                for(int i = 0 ; i < list.size() ; i++){
                    if(list.get(i).getName().equals(field.getName())){
                        containsName = true;
                        break;
                    }
                }
                if(!containsName){
                    list.add(field);
                }
            }
        }
        return list;
    }

    @Override
    public void setValue(Object obj, Field field, Object value) throws Exception {

    }


    public static void main(String[] args) throws Throwable {

        final ObjectHelper objectHelper = new ObjectHelper();
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        final MethodType type = MethodType.methodType(void.class, String.class,int.class);
//        final MethodHandle setA = lookup.findVirtual(A.class, "setA", type);



    }




}
