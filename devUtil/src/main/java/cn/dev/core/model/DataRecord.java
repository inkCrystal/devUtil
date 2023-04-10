package cn.dev.core.model;

import java.lang.reflect.Type;

public record DataRecord(Type type, Object value) {


    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

}
