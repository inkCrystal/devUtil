package cn.dev.task.runner;

import cn.dev.core.model.Identity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于处理 function 和 task 的事件 后续操作
 */
public class FunctionEventBus {

    private static final Map<String,functionEntry> functionEntryHolder =new ConcurrentHashMap<>();
    private static final Map<String,taskEntry> taskEntryHolder =new ConcurrentHashMap<>();

    protected static void register(Identity identity, IFunction function ,FunctionState state,FunctionResult functionResult){
        functionEntryHolder.put(
                identity.getId()+ "_" + state.name(),
                new functionEntry(identity,function,functionResult));
    }


    record taskEntry(Identity identity,ITaskFunction taskFunction,TaskFuture future){}
    record functionEntry(Identity identity,IFunction function,FunctionResult result){}



}
