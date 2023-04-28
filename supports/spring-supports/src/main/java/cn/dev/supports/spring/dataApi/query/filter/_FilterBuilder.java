package cn.dev.supports.spring.dataApi.query.filter;

import cn.dev.supports.spring.dataApi.query.OpEnum;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public class _FilterBuilder {

    private static final _FilterBuilder Builder = new _FilterBuilder();

    public static _FilterBuilder getBuilder(){
        return new _FilterBuilder() ;
    }

    protected static final OrBuilder Or = OrBuilder.OrBuilder;

    private _FilterBuilder(){}

    static class OrBuilder {
        protected static final OrBuilder OrBuilder = new OrBuilder();
        private OrBuilder(){}
        public Mono<_Filter> whereEqual(String key, Serializable value){
            return Mono.just(Builder.whereEqual(key, value));
        }

        public Mono<_Filter> whereNotEqual(String key, Serializable value){
            return Mono.just(Builder.whereNotEqual(key, value));
        }

        public Mono<_Filter> whereIn(String key, Serializable... values){
            return Mono.just(Builder.whereIn(key, values));
        }

        public Mono<_Filter> whereNotIn(String key, Serializable... values){
            return Mono.just(Builder.whereNotIn(key, values));
        }

        public Mono<_Filter> whereLike(String key, String value){
            return Mono.just(Builder.whereLike(key, value));
        }

        public Mono<_Filter> whereNotLike(String key, String value){
            return Mono.just(Builder.whereNotLike(key, value));
        }

        public Mono<_Filter> whereGreaterThan(String key, Number value){
            return Mono.just(Builder.whereGreaterThan(key, value));
        }

        public Mono<_Filter> whereGreaterThanOrEqual(String key, Number value){
            return Mono.just(Builder.whereGreaterThanOrEqual(key, value));
        }

        public Mono<_Filter> whereLessThan(String key, Number value){
            return Mono.just(Builder.whereLessThan(key, value));
        }

        public Mono<_Filter> whereLessThanOrEqual(String key, Number value){
            return Mono.just(Builder.whereLessThanOrEqual(key, value));
        }

        public Mono<_Filter> whereBetweenAnd(String key, Number min, Number max){
            return Mono.just(Builder.whereBetweenAnd(key, min, max));
        }

        public Mono<_Filter> whereNotBetween(String key, Number min, Number max){
            return Mono.just(Builder.whereNotBetweenAnd(key, min, max));
        }

        public Mono<_Filter> whereIsNull(String key){
            return Mono.just(Builder.whereIsNull(key));
        }

        public Mono<_Filter> whereIsNotNull(String key){
            return Mono.just(Builder.whereIsNotNull(key));
        }

        public Mono<_Filter> whereLessThanKey(String key, String key2){
            return Mono.just(Builder.whereLessThanKey(key, key2));
        }

        public Mono<_Filter> whereLessThanOrEqualKey(String key, String key2){
            return Mono.just(Builder.whereLessThanOrEqualKey(key, key2));
        }

        public Mono<_Filter> whereGreaterThanKey(String key, String key2){
            return Mono.just(Builder.whereGreaterThanKey(key, key2));
        }

        public Mono<_Filter> whereGreaterThanOrEqualKey(String key, String key2){
            return Mono.just(Builder.whereGreaterThanOrEqualKey(key, key2));
        }




    }




    // 初始构建 链路中的 第一个 filter
    public _Filter whereEqual(String key, Serializable value){
        return _Filter.build(key, OpEnum.EQUAL, value);
    }

    public _Filter whereNotEqual(String key, Serializable value){
        return _Filter.build(key, OpEnum.NOT_EQUAL, value);
    }

    public _Filter whereIn(String key, Serializable... values){
        if(values == null || values.length == 0){
            throw new IllegalArgumentException("values length must > 0");
        }
        if(values.length == 1){
            return whereEqual(key, values[0]);
        }
        return _Filter.build(key, OpEnum.IN, values);
    }

    public _Filter whereNotIn(String key, Serializable... values){
        if(values == null || values.length == 0){
            throw new IllegalArgumentException("values length must > 0");
        }
        if(values.length == 1){
            return whereNotEqual(key, values[0]);
        }
        return _Filter.build(key, OpEnum.NOT_IN, values);
    }

    public _Filter whereLike(String key, String value){
        return _Filter.build(key, OpEnum.LIKE, value);
    }

    public _Filter whereNotLike(String key, String value){
        return _Filter.build(key, OpEnum.NOT_LIKE, value);
    }

    public _Filter whereLessThan(String key, Number value){
        return _Filter.build(key, OpEnum.LESS_THAN, value);
    }

    public _Filter whereLessThanOrEqual(String key, Number value){
        return _Filter.build(key, OpEnum.LESS_THAN_OR_EQUAL, value);
    }



    public _Filter whereGreaterThan(String key, Number value){
        return _Filter.build(key, OpEnum.GREATER_THAN, value);
    }

    public _Filter whereGreaterThanOrEqual(String key, Number value){
        return _Filter.build(key, OpEnum.GREATER_THAN_OR_EQUAL, value);
    }

    @Deprecated
    public _Filter whereIsNull(String key){
        return _Filter.build(key, OpEnum.IS_NULL);
    }

    @Deprecated
    public _Filter whereIsNotNull(String key){
        return _Filter.build(key, OpEnum.IS_NOT_NULL);
    }

    public _Filter whereBetweenAnd(String key, Serializable value1, Serializable value2){
        return _Filter.build(key, OpEnum.BETWEEN_AND, value1, value2);
    }

    public _Filter whereNotBetweenAnd(String key, Serializable value1, Serializable value2){
        return _Filter.build(key, OpEnum.NOT_BETWEEN_AND, value1, value2);
    }

    @Deprecated
    public _Filter whereEqualKey(String key , String key2){
        return _Filter.build(key, OpEnum.EQUAL, key2)
                .markParamIsKey();
    }

    @Deprecated
    public _Filter whereNotEqualKey(String key , String key2){
        return _Filter.build(key, OpEnum.NOT_EQUAL,key2)
                .markParamIsKey();
    }


    public _Filter whereLessThanKey(String key , String key2){
        return _Filter.build(key, OpEnum.LESS_THAN,key2)
                .markParamIsKey();
    }

    public _Filter whereLessThanOrEqualKey(String key , String key2){
        return _Filter.build(key, OpEnum.LESS_THAN_OR_EQUAL,key2)
                .markParamIsKey();
    }

    public _Filter whereGreaterThanKey(String key , String key2){
        return _Filter.build(key, OpEnum.GREATER_THAN,key2)
                .markParamIsKey();
    }

    public _Filter whereGreaterThanOrEqualKey(String key , String key2){
        return _Filter.build(key, OpEnum.GREATER_THAN_OR_EQUAL,key2)
                .markParamIsKey();
    }

    @Deprecated
    public _Filter whereBetweenAndKey(String key , String keyFrom, String keyTo){
        return _Filter.build(key, OpEnum.BETWEEN_AND,keyFrom,keyTo)
                .markParamIsKey();
    }


}
