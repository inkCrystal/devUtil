package cn.dev.utils.supports.dataApi.query.filter;

import java.io.Serializable;
import java.util.List;

public class CompleteFilter {

    private final String filterQuery ;

    private final List<Serializable> valueList ;

    private String jsonString ;

    protected CompleteFilter(String filterQuery, List<Serializable> valueList) {
        this.filterQuery = filterQuery;
        this.valueList = valueList;
    }

    public String getFilterQuery() {
        return filterQuery;
    }

    public List<Serializable> getValueList() {
        return valueList;
    }

}
