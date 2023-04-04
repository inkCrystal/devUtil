package cn.dev.core.parallel.task.api;

/**
 * 基础 FUNCTION ，基于 IFunctionAble 的基础 Function
 * 最常用的 一个 Fcuntion
 * @param <PARAM> 输入参数
 * @param <RESULT> 返回值
 */
public non-sealed interface IFunction<PARAM, RESULT> extends IFunctionAble{

    RESULT apply(PARAM d);

}
