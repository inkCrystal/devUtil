package cn.dev.supports.spring.dataApi.query.filter;

import cn.dev.supports.spring.dataApi.DataModel;
import org.junit.jupiter.api.Test;

class SelectFilterBuilderTest {

    @Test
    void toAvailableFilter() {
        final AvailableFilter availableFilter = FilterBuilder.getBuilder(DataModel.class)
                .thenEqual("id", 123)
                .thenBetweenAnd("score", 1, 100)
                .thenIn("sex", 1, 2, 3)
                .thenOR().thenEqual("name", "张三")
                    .thenOR().thenEqualKey("name", "name")
                .breakOR()
                .breakOR()
                .thenGreaterThan("age", 18)
                .thenGreaterThanKey("money","minMoney")
                .toAvailableFilter();

        System.out.println(availableFilter.getFilterQuery() + " <<< \n " + availableFilter.getValueList().size());

    }
}