package cn.dev.parallel.task.runner;

import cn.dev.commons.log.DLog;
import cn.dev.core.parallel.task.runner.AbstractTaskRunner;
import cn.dev.core.parallel.task.runner.FunctionResult;
import org.junit.jupiter.api.Test;

class AbstractTaskRunnerTest {


    private static AbstractTaskRunner runner;
    public static AbstractTaskRunner getRunner() {
        if(runner == null) {
            runner = new AbstractTaskRunner();
        }
        return runner;
    }


    @Test
    void apply() {

        String result = "INPUT ";
        final FunctionResult<String> apply = getRunner().apply(result, (d) -> {
            DLog.info("输入 ： "  + d);
            return "test" + result;
        });
        apply.thenApplyIfSuccess((d) -> {
            DLog.info(" success call 输入 ： "  + d);
            return d ;
        });
    }

    @Test
    void execute() {

        getRunner().execute(()->{
            DLog.info("执行任务");
        }).thenExecuteIfDone(()->{
            DLog.info("任务执行完成");
        });
    }

    @Test
    void testExecute() {
    }
}