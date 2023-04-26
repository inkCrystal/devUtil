package cn.dev.supports.spring.dataApi;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IDataReactorApi<T extends DataModel> {

    Mono<T> findById(Long id);

    Mono<List<T>> findListByIds(List<Long> ids);

    Mono<List<T>> findListByKeys(Long[] ids);

    R2dbcRepository<T, Long> getRepository();
}
