package cn.dev.supports.spring.dataApi;

import cn.dev.supports.spring.dataApi.query.ExecutableQuery;
import cn.dev.supports.spring.dataApi.query.filter.AvailableFilter;
import cn.dev.supports.spring.dataApi.query.filter.Filter;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.List;

public interface IDataApi<T extends DataModel> extends R2dbcRepository<T, Long>{

    T getById(Long id);

    List<T> findList(AvailableFilter filter);

    Long count(ExecutableQuery executableQuery);








}
