package cn.dev.exception;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class IdentityJoinException extends RuntimeException{
    public IdentityJoinException(String message) {
        super(message);
    }


    public static final IdentityJoinException build(List<String> errors) {
        String[] strings = errors.toArray(String[]::new);
        String ids = Arrays.toString(strings);
        return new IdentityJoinException(ids + "已经加入到其他分组而导致合并到Identity分组失败");
    }

    public IdentityJoinException(Throwable cause) {
        super(cause);
    }
}
