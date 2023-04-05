package cn.dev.core.model;

import cn.dev.commons.id.IdHelper;
import cn.dev.clock.CommonTimeClock;
import cn.dev.exception.IdentityJoinException;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

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
public class Identity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1558445437734267014L;
    /**
     * 身份Id
     */
    private final String id;
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

    private volatile boolean lockState =false;


    public Identity() {
        this.id = IdHelper.nextBase64Id();
        this.group = null;
        this.initTime = CommonTimeClock.currentTimeMillis();
    }

    public long getGroupTime() {
        return groupTime;
    }

    public String getId() {
        return id;
    }

    private synchronized boolean lock(){
        if (this.lockState == false) {
            this.lockState =true;
        }else{
            return false;
        }
        return true;
    }

    private synchronized void setGroup(String group){
        if(lockState){
            this.group = group;
            this.unLock();
        }

    }

    private synchronized boolean unLock(){
        this.lockState = false;
        return true;
    }

    public String getGroup() {
        return group;
    }

    /**
     * 将多个 Identity 分为一组,如果其中任意一个对象已经存在分组，那么将会抛出 IdentityJoinException
     * 及一个 Identity 对象 只允许归纳到一个分组
     * @param identities
     * @return 返回  groupId
     * @throws IdentityJoinException
     */
    public static final String joinGroup(Identity... identities) throws IdentityJoinException {


        return null;
    }




    public boolean is(String id){
        return this.id.equals(id);
    }
    public boolean is(Identity identity){
        if (Objects.nonNull(identity)) {
            return this.id.equals(identity.getId());
        }
        return false;
    }

    public long getInitTime() {
        return initTime;
    }
}
