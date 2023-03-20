package cn.dev.task;

import java.util.function.Function;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/16 10:13
 * @description : 任务计时器 ，用于 计算 一段代码消耗的时间 长度
 */
public class TaskTimer {


    public static final long costMillis(ITaskFunction taskFunction){
        long start = System.currentTimeMillis();
        taskFunction.run();
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static final long costMills(Function function){
        long start = System.currentTimeMillis();
//        function.apply()
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static void main(String[] args) {
        ITaskFunction taskFunction = ()->{
            System.out.println("hello ");
        };
        taskFunction.thenExecute(()->{
            System.out.println("then 1 ");
        }).thenExecute(()->{
            System.out.println("thrn 2 ");
        });
        costMillis(taskFunction);

        Function<Integer, Integer> times2 = i -> i*2;
        Function<Integer, Integer> squared = i -> i*i;

        System.out.println(times2.apply(4));
        System.out.println(squared.apply(4));

        System.out.println(times2.compose(squared).apply(4));  //32                先4×4然后16×2,先执行apply(4)，在times2的apply(16),先执行参数，再执行调用者。
        System.out.println(times2.andThen(squared).apply(4));  //64               先4×2,然后8×8,先执行times2的函数，在执行squared的函数。

        System.out.println(Function.identity().compose(squared).apply(4));


    }


}
