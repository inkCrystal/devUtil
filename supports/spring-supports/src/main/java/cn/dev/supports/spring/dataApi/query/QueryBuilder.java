package cn.dev.supports.spring.dataApi.query;

import cn.dev.supports.spring.dataApi.query.filter.FilterBuilder;

public class QueryBuilder {

    private ExecutableQuery query ;
    public QueryBuilder() {
        query =new ExecutableQuery();
    }

    public FilterBuilder  filterBuilder(){
        return FilterBuilder.getBuilder();
    }




}
