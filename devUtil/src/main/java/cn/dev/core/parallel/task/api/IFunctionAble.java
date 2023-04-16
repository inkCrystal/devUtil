package cn.dev.core.parallel.task.api;

/**
 * 基础 函数化的
 */
public sealed interface IFunctionAble
//        extends AutoCloseable
        permits IFunction,ITaskFunction{

//    @Override
//    default void close() throws Exception{};
}
