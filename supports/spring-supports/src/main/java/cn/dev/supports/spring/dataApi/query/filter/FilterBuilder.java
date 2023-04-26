package cn.dev.supports.spring.dataApi.query.filter;

import cn.dev.supports.spring.dataApi.query.OpEnum;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public class FilterBuilder {

    protected static final FilterBuilder Builder = new FilterBuilder();

    protected static final OrBuilder Or = OrBuilder.OrBuilder;

    private FilterBuilder(){}

    static class OrBuilder {
        protected static final OrBuilder OrBuilder = new OrBuilder();
        private OrBuilder(){}
        public Mono<Filter> whereEqual(String key, Serializable value){
            return Mono.just(Builder.whereEqual(key, value));
        }

        public Mono<Filter> whereNotEqual(String key, Serializable value){
            return Mono.just(Builder.whereNotEqual(key, value));
        }

        public Mono<Filter> whereIn(String key, Serializable... values){
            return Mono.just(Builder.whereIn(key, values));
        }

        public Mono<Filter> whereNotIn(String key, Serializable... values){
            return Mono.just(Builder.whereNotIn(key, values));
        }

        public Mono<Filter> whereLike(String key, String value){
            return Mono.just(Builder.whereLike(key, value));
        }

        public Mono<Filter> whereNotLike(String key, String value){
            return Mono.just(Builder.whereNotLike(key, value));
        }

        public Mono<Filter> whereGreaterThan(String key, Number value){
            return Mono.just(Builder.whereGreaterThan(key, value));
        }

        public Mono<Filter> whereGreaterThanOrEqual(String key, Number value){
            return Mono.just(Builder.whereGreaterThanOrEqual(key, value));
        }

        public Mono<Filter> whereLessThan(String key, Number value){
            return Mono.just(Builder.whereLessThan(key, value));
        }

        public Mono<Filter> whereLessThanOrEqual(String key, Number value){
            return Mono.just(Builder.whereLessThanOrEqual(key, value));
        }

        public Mono<Filter> whereBetweenAnd(String key, Number min, Number max){
            return Mono.just(Builder.whereBetweenAnd(key, min, max));
        }

        public Mono<Filter> whereNotBetween(String key, Number min, Number max){
            return Mono.just(Builder.whereNotBetweenAnd(key, min, max));
        }

        public Mono<Filter> whereIsNull(String key){
            return Mono.just(Builder.whereIsNull(key));
        }

        public Mono<Filter> whereIsNotNull(String key){
            return Mono.just(Builder.whereIsNotNull(key));
        }

        public Mono<Filter> whereLessThanKey(String key, String key2){
            return Mono.just(Builder.whereLessThanKey(key, key2));
        }

        public Mono<Filter> whereLessThanOrEqualKey(String key, String key2){
            return Mono.just(Builder.whereLessThanOrEqualKey(key, key2));
        }

        public Mono<Filter> whereGreaterThanKey(String key, String key2){
            return Mono.just(Builder.whereGreaterThanKey(key, key2));
        }

        public Mono<Filter> whereGreaterThanOrEqualKey(String key, String key2){
            return Mono.just(Builder.whereGreaterThanOrEqualKey(key, key2));
        }




    }




    // 初始构建 链路中的 第一个 filter
    public Filter whereEqual(String key, Serializable value){
        return Filter.build(key, OpEnum.EQUAL, value);
    }

    public Filter whereNotEqual(String key, Serializable value){
        return Filter.build(key, OpEnum.NOT_EQUAL, value);
    }

    public Filter whereIn(String key, Serializable... values){
        if(values == null || values.length == 0){
            throw new IllegalArgumentException("values length must > 0");
        }
        if(values.length == 1){
            return whereEqual(key, values[0]);
        }
        return Filter.build(key, OpEnum.IN, values);
    }

    public Filter whereNotIn(String key, Serializable... values){
        if(values == null || values.length == 0){
            throw new IllegalArgumentException("values length must > 0");
        }
        if(values.length == 1){
            return whereNotEqual(key, values[0]);
        }
        return Filter.build(key, OpEnum.NOT_IN, values);
    }

    public Filter whereLike(String key, String value){
        return Filter.build(key, OpEnum.LIKE, value);
    }

    public Filter whereNotLike(String key, String value){
        return Filter.build(key, OpEnum.NOT_LIKE, value);
    }

    public Filter whereLessThan(String key, Number value){
        return Filter.build(key, OpEnum.LESS_THAN, value);
    }

    public Filter whereLessThanOrEqual(String key, Number value){
        return Filter.build(key, OpEnum.LESS_THAN_AND_EQUAL, value);
    }



    public Filter whereGreaterThan(String key, Number value){
        return Filter.build(key, OpEnum.GREATER_THAN, value);
    }

    public Filter whereGreaterThanOrEqual(String key, Number value){
        return Filter.build(key, OpEnum.GREATER_THAN_AND_EQUAL, value);
    }

    @Deprecated
    public Filter whereIsNull(String key){
        return Filter.build(key, OpEnum.IS_NULL);
    }

    @Deprecated
    public Filter whereIsNotNull(String key){
        return Filter.build(key, OpEnum.IS_NOT_NULL);
    }

    public Filter whereBetweenAnd(String key, Serializable value1, Serializable value2){
        return Filter.build(key, OpEnum.BETWEEN_AND, value1, value2);
    }

    public Filter whereNotBetweenAnd(String key, Serializable value1, Serializable value2){
        return Filter.build(key, OpEnum.NOT_BETWEEN_AND, value1, value2);
    }

    @Deprecated
    public Filter whereEqualKey(String key , String key2){
        return Filter.build(key, OpEnum.EQUAL, key2)
                .markParamIsKey();
    }

    @Deprecated
    public Filter whereNotEqualKey(String key , String key2){
        return Filter.build(key, OpEnum.NOT_EQUAL,key2)
                .markParamIsKey();
    }


    public Filter whereLessThanKey(String key , String key2){
        return Filter.build(key, OpEnum.LESS_THAN,key2)
                .markParamIsKey();
    }

    public Filter whereLessThanOrEqualKey(String key , String key2){
        return Filter.build(key, OpEnum.LESS_THAN_AND_EQUAL,key2)
                .markParamIsKey();
    }

    public Filter whereGreaterThanKey(String key , String key2){
        return Filter.build(key, OpEnum.GREATER_THAN,key2)
                .markParamIsKey();
    }

    public Filter whereGreaterThanOrEqualKey(String key , String key2){
        return Filter.build(key, OpEnum.GREATER_THAN_AND_EQUAL,key2)
                .markParamIsKey();
    }

    @Deprecated
    public Filter whereBetweenAndKey(String key , String keyFrom, String keyTo){
        return Filter.build(key, OpEnum.BETWEEN_AND,keyFrom,keyTo)
                .markParamIsKey();
    }


}
