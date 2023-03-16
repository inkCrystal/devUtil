package com.dev.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/16 13:35
 * @description : TODO
 */
@FunctionalInterface
public interface BaseFunction<V> {
    String taskId = null;
    V apply(V v);


    default String taskId(){
        return this.taskId();
    }
    default void setTaskId(String id){
        if(this.taskId == null){
        }
    }


}
