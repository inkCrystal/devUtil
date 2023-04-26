package cn.dev.supports.spring.dataApi;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface IDataApi<T extends DataModel> extends R2dbcRepository<T, Long>{

    T getById(Long id);







}
