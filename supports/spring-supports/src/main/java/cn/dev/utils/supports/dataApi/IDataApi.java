package cn.dev.utils.supports.dataApi;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IDataApi<T extends DataModel> extends R2dbcRepository<T, Long>{

    T getById(Long id);







}
