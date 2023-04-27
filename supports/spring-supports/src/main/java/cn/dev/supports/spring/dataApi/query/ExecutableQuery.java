package cn.dev.supports.spring.dataApi.query;

import cn.dev.supports.spring.dataApi.query.filter.AvailableFilter;
import cn.dev.supports.spring.dataApi.query.key.SelectKeys;
import cn.dev.supports.spring.dataApi.query.update.AvailableUpdater;

public class ExecutableQuery {


    private boolean isUpdate ;

    private boolean isDelete ;

    private boolean isSelect ;

    private Class entityClass;
    private AvailableUpdater availableUpdater;
    private AvailableFilter availableFilter;
    private SelectKeys keys;

    public ExecutableQuery( ) {

    }

    public void setKeys(SelectKeys keys) {
        this.keys = keys;
    }

    public void setAvailableFilter(AvailableFilter availableFilter) {
        this.availableFilter = availableFilter;
    }

    public void setAvailableUpdater(AvailableUpdater availableUpdater) {
        this.availableUpdater = availableUpdater;
    }

    public AvailableFilter getCompleteFilter() {
        return availableFilter;
    }

    public SelectKeys getKeys() {
        return keys;
    }


    private String combination(String... parts){
        StringBuilder stringBuilder = new StringBuilder();
        for (String part : parts) {
            stringBuilder.append(part).append(" ");
        }
        return stringBuilder.toString();
    }
    public String sql(){
        String tb = entityClass.getSimpleName();
        if (isSelect) {
            return combination(
                    "SELECT", keys.selectKeyQueryString(), "FROM",tb, availableFilter.getFilterQuery()
            );
        }else if(isDelete){
            return combination(
                    "DELETE FROM", tb, availableFilter.getFilterQuery()
            );
        } else if(isUpdate){
            return combination(
                    "UPDATE", tb,  availableUpdater.getUpdateQuery(), availableFilter.getFilterQuery()
            );
        }else {
            throw new RuntimeException("not support");
        }

    }


}
