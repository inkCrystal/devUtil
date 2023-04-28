package cn.dev.commons.verification;

import cn.dev.exception.VerificationException;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class AssertTool {


    public static <T> void throwIfNotMatch(T t,Predicate<T> predicate) {
        throwIfNotMatch(t,predicate, "目标校验未匹配预期");
    }

    public static <T> void throwIfNotMatch(T t,Predicate<T> predicate, String message) {
        if (!predicate.test(t)) {
            throw new VerificationException(message);
        }
    }

    public static <T> void throwIfMatch(T t,Predicate<T> predicate) {
        throwIfMatch(t,predicate, "目标校验未匹配预期");
    }

    public static <T> void throwIfMatch(T t,Predicate<T> predicate, String message) {
        if (predicate.test(t)) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfNull(Object obj) {
        throwIfNull(obj, "目标对象为NULL");
    }

    public static void throwIfNull(Object obj, String message) {
        if (obj == null) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfNotNull(Object obj) {
        throwIfNotNull(obj, "目标对象不为NULL");
    }

    public static void throwIfNotNull(Object obj, String message) {
        if (obj != null) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfFalse(Boolean b) {
        throwIfFalse(b, "目标结果不是并非预期的 TRUE");
    }

    public static void throwIfFalse(Boolean b, String message) {
        if (!b) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfTrue(Boolean b) {
        throwIfTrue(b, "目标结果不是并非预期的 FALSE");
    }

    public static void throwIfTrue(Boolean b, String message) {
        if (b) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfStringIsEmpty(String str) {
        throwIfStringIsEmpty(str, "目标字符串为空");
    }

    public static void throwIfStringIsEmpty(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfStringIsNotEmpty(String str) {
        throwIfStringIsNotEmpty(str, "目标字符串不为空");
    }

    public static void throwIfStringIsNotEmpty(String str, String message) {
        if (str != null && !str.isEmpty()) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfCollectionIsEmpty(Collection<?> col) {
        throwIfCollectionIsEmpty(col, "Collection is Empty");
    }

    public static void throwIfCollectionIsEmpty(Collection<?> col, String message) {
        if (col == null || col.isEmpty()) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfCollectionIsNotEmpty(Collection<?> col) {
        throwIfCollectionIsNotEmpty(col, "Collection is not Empty");
    }

    public static void throwIfCollectionIsNotEmpty(Collection<?> col, String message) {
        if (col != null && !col.isEmpty()) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfArrayIsEmpty(Object[] arr) {
        throwIfArrayIsEmpty(arr, "Array is Empty");
    }

    public static void throwIfArrayIsEmpty(Object[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfArrayIsNotEmpty(Object[] arr) {
        throwIfArrayIsNotEmpty(arr, "Array is not Empty");
    }

    public static void throwIfArrayIsNotEmpty(Object[] arr, String message) {
        if (arr != null && arr.length > 0) {
            throw new VerificationException(message);
        }
    }


    public static void throwIfMapIsEmpty(Map<?,?> map) {
        throwIfMapIsEmpty(map, "Map is Empty");
    }

    public static void throwIfMapIsEmpty(Map<?,?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfMapIsNotEmpty(Map<?,?> map) {
        throwIfMapIsNotEmpty(map, "Map is not Empty");
    }

    public static void throwIfMapIsNotEmpty(Map<?,?> map, String message) {
        if (map != null && !map.isEmpty()) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfNotEquals(Object obj1, Object obj2) {
        throwIfNotEquals(obj1, obj2, "目标对象不相等");
    }

    public static void throwIfNotEquals(Object obj1, Object obj2, String message) {
        if (!obj1.equals(obj2)) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfEquals(Object obj1, Object obj2) {
        throwIfEquals(obj1, obj2, "目标对象相等");
    }

    public static void throwIfEquals(Object obj1, Object obj2, String message) {
        if (obj1.equals(obj2)) {
            throw new VerificationException(message);
        }
    }

    public static void throwIfNotEqual(Object obj1, Object obj2) {
        throwIfNotEqual(obj1, obj2, "目标对象不相等");
    }

    public static void throwIfNotEqual(Object obj1, Object obj2, String message) {
        if (!obj1.equals(obj2)) {
            throw new VerificationException(message);
        }
    }

    public static <T> void throwIfInstanceOf(T obj, Class<?> clazz) {
        throwIfInstanceOf(obj, clazz, "目标对象["+obj.getClass().getSimpleName()+"]是预期类型["+clazz.getSimpleName()+"]");
    }

    public static <T> void throwIfInstanceOf(T obj, Class<?> clazz, String message) {
        if (clazz.isInstance(obj)) {
            throw new VerificationException(message);
        }
    }

    public static <T> void throwIfNotInstanceOf(T t, Class<?> clazz) {
        throwIfNotInstanceOf(t, clazz, "目标对象["+t.getClass().getSimpleName()+"]不是预期类型["+clazz.getSimpleName()+"]");
    }

    public static <T> void throwIfNotInstanceOf(T t, Class<?> clazz, String message) {
        if (!clazz.isInstance(t)) {
            throw new VerificationException(message);
        }
    }

}
