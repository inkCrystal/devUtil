package cn.dev.supports.spring.dataApi.query.update;

import java.io.Serializable;

public class AvailableUpdater {

    private String updateQuery ;
    private Serializable[] values ;

    protected AvailableUpdater(String updateQuery, Serializable[] values) {
        this.updateQuery = updateQuery;
        this.values = values;
    }

    public Serializable[] getValues() {
        return values;
    }

    public String getUpdateQuery() {
        return updateQuery;
    }
}
