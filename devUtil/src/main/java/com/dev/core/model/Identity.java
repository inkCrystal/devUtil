package com.dev.core.model;

import com.dev.commons.RandomUtil;
import com.dev.commons.datetime.TimeMillisClock;
import com.dev.commons.id.IdHelper;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/16 13:48
 * @description : 身份对象模型
 *  一般来说，一个身份对象是一次性，且唯一的 。
 *  但是我们有些时候 需要在组合 联合一些事务处理的时候，需要针对 身份对象组成组。
 *  这个时候，我们可以通过 group 来将不同的 身份标识在一起，比如 ，串联，并行一些 任务 事件
 *  不论如何，我们这个 身份对象是 唯一的，且 只允许 加入一个组，一旦加入了组，即 不可改变
 */
public class Identity {

    /**
     * 身份Id
     */
    private String id;
    /**
     * 身份组
     */
    private String group = null;
    /**
     * 初始化时间
     */
    private long initTime ;
    /**
     * 加入到group组的时间
     */
    private long groupTime ;


    public Identity() {
        this.id = IdHelper.nextBase64Id();
        this.group = null;
        this.initTime = TimeMillisClock.currentTimeMillis();
    }
}
