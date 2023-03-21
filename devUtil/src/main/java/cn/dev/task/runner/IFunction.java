package cn.dev.task.runner;

public interface IFunction<D,V> {

    V apply(D d);

}
