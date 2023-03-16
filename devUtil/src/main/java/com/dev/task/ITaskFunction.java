package com.dev.task;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/16 10:17
 * @description :
 */
@FunctionalInterface
public interface ITaskFunction {

    void run();


//    default ITaskFunction compose(ITaskFunction taskFunction){
//
//    }

    default ITaskFunction thenExecute(ITaskFunction taskFunction){
        Objects.requireNonNull(taskFunction);
        return new ITaskFunction() {
            @Override
            public void run() {
                taskFunction.run();
            }
        };
    }

}
