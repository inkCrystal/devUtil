package cn.dev.supports.spring.dataApi.query;

import cn.dev.supports.spring.dataApi.query.filter.AvailableFilter;
import cn.dev.supports.spring.dataApi.query.key.SelectKeys;
import cn.dev.supports.spring.dataApi.query.update.AvailableUpdater;

public class ExecutableQuery {


    private boolean isUpdate ;

    private boolean isDelete ;

    private boolean isSelect ;


    private AvailableUpdater availableUpdater;
    private AvailableFilter availableFilter;
    private SelectKeys keys;


    public ExecutableQuery(AvailableFilter availableFilter, SelectKeys keys) {
        this.availableFilter = availableFilter;
        this.keys = keys;
    }

    public AvailableFilter getCompleteFilter() {
        return availableFilter;
    }

    public SelectKeys getKeys() {
        return keys;
    }


}
