package com.dev.task;


import com.dev.function.BaseFunction;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/16 13:41
 * @description : 任务组队列，再需要多个任务 协调联合、串行、并行时候 的 中间缓存容器工具
 */
public class TaskGroupQueue {
    static class GroupQueue<F extends BaseFunction>{
        private static Map<String,Queue> holder =new ConcurrentHashMap<>();


    }

}

