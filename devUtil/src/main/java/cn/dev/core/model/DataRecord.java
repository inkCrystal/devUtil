package cn.dev.core.model;

import java.io.Serializable;
import java.lang.reflect.Type;

public record DataRecord(Type type, Object value) implements Serializable {


    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

}
