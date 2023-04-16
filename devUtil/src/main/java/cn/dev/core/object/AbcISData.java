package cn.dev.core.object;

import cn.dev.core.model.DataRecord;
import cn.dev.core.model.Identity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AbcISData implements Serializable {

    @Serial
    private static final long serialVersionUID = 5926454145065363516L;

    private LocalDateTime initTime ;

    private String name ;

    int limit ;

    private String[] childNames ;

    private BigDecimal decimal = new BigDecimal(1233.2314);

    private String parentName ;

    final Map<String,LocalDateTime> lastEventTimeMap =new HashMap<>();

    private Identity identity;

    int version ;

    Date createTime;



    public AbcISData() {
    }

    public AbcISData(String name, int limit, String[] childNames, BigDecimal decimal, String parentName, Identity identity, int version, Date createTime ) {
        this.name = name;
        this.limit = limit;
        this.childNames = childNames;
        this.decimal = decimal;
        this.parentName = parentName;
        this.identity = identity;
        this.version = version;
        this.createTime = createTime;
//        this.record = record;
        System.out.println("初始化啦");
    }

    public LocalDateTime getInitTime() {
        return initTime;
    }

    public void setInitTime(LocalDateTime initTime) {
        this.initTime = initTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String[] getChildNames() {
        return childNames;
    }

    public void setChildNames(String[] childNames) {
        this.childNames = childNames;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Map<String, LocalDateTime> getLastEventTimeMap() {
        return lastEventTimeMap;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }



    @Override
    public String toString() {
        return "AbcISData{" +
                "initTime=" + initTime +
                ", name='" + name + '\'' +
                ", limit=" + limit +
                ", childNames=" + Arrays.toString(childNames) +
                ", decimal=" + decimal +
                ", parentName='" + parentName + '\'' +
                ", lastEventTimeMap=" + lastEventTimeMap +
                ", identity=" + identity +
                ", version=" + version +
                ", createTime=" + createTime +
                '}';
    }
}
