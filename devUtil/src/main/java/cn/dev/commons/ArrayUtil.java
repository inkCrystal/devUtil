package cn.dev.commons;

import cn.dev.commons.verification.VerificationTool;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayUtil {


    private static <T> T[]genericsArray(int len ,T[]... array){
        Class<T> targetClass = null;
        for (T[] ts : array) {
            for (T t : ts) {
                targetClass = (Class<T>) t.getClass();
                return genericsArray(targetClass,len);
            }
        }
        throw new RuntimeException("无法推断出数组的正确类型");
    }

    private static  <T> T[] genericsArray(Class<T> targetClass , int len){
        return (T[]) Array.newInstance(targetClass, len);
    }

    private static <T> Class<T> findTargetClass(T[] array){
        if(array ==null){
            throw new RuntimeException("无法推断准确的范型数据，可能数组为空或者全部为NULL");
        }
        for (T t : array) {
            if (t!=null) {
                return (Class<T>) t.getClass();
            }
        }
        throw new RuntimeException("无法推断准确的范型数据，可能数组为空或者全部为NULL");
    }

    private static <T> T[] ToTArray(Object[] array ,Class<T> targetClass){
        final T[] ts = genericsArray(targetClass, array.length);
        for (int i = 0; i < array.length; i++) {
            ts[i]= (T) array[i];
        }
        return ts;
    }

    private static void throwIfAllNull(Object[] array){
        if (isAllNull(array)) {
            throw new RuntimeException("当前数组全部为NULL");
        }
    }

    /**
     * 去除重复项目
     * @param array
     * @return
     * @param <T>
     */
    public static<T> T[] distinct(T[] array){
        VerificationTool.throwIfNull(array);
        if (array.length == 0) {
            return array;
        }
        Class<T> targetClass = findTargetClass(array);
        Object[] objects = Arrays.stream(array).distinct().toArray();
        return ToTArray(objects,targetClass);
    }

    /**
     * 移除 NULL
     * @param array
     * @return
     * @param <T>
     */
    public static  <T> T[]  removeNull(T[] array){
        VerificationTool.throwIfNull(array);
        if(isAllNull(array)){
            throw new RuntimeException("当前数组的值全部为NULL");
        }
        Class<T> targetClass = findTargetClass(array);
        Object[] objects = Arrays.stream(array).filter(Objects::nonNull).toArray();
        return ToTArray(objects,targetClass);
    }

    public static boolean isAllNull(Object[] array){
        if (array == null) {
            return true;
        }
        for (Object o : array) {
            if (o!=null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数组顺序排序
     * @param array
     * @return
     * @param <T>
     */
    public static  <T> T[]  stored(T[] array){
        VerificationTool.throwIfNull(array);
        if(isAllNull(array)){
            throw new RuntimeException("当前数组的值全部为NULL");
        }
        Class<T> targetClass = findTargetClass(array);
        Object[] objects = Arrays.stream(array).filter(Objects::nonNull).sorted().toArray();
        return ToTArray(objects,targetClass);
    }


    /**
     * 去重并排序
     * @param array
     * @return
     * @param <T>
     */
    public static  <T> T[] distinctStored(T[] array){
        VerificationTool.throwIfNull(array);
        Class<T> targetClass = findTargetClass(array);
        Object[] objects = Arrays.stream(array).filter(Objects::nonNull).distinct().sorted().toArray();
        return ToTArray(objects,targetClass);
    }


    /**
     * 倒叙 排序数组
     * @param array
     * @return
     * @param <T>
     */
    public static  <T> T[]  storedDesc(T[] array){
        VerificationTool.throwIfNull(array);
        return reverse(stored(array));
    }

    /**
     * 去掉重复数据，并 倒叙排序
     * @param array
     * @return
     * @param <T>
     */
    public static  <T> T[]  distinctStoredDesc(T[] array){
        VerificationTool.throwIfNull(array);
        return reverse(distinctStored(array));
    }

    /**
     * 数组反转顺序
     * @param array
     * @return
     * @param <T>
     */
    public static <T> T[] reverse(T[] array){
        VerificationTool.throwIfNull(array);
        array = removeNull(array);
        for (int i = 0; i < array.length/2; i++) {
            T t = array[i];
            array[i] =array[array.length -1 -i];
            array[array.length -1 -i] = t ;
        }
        return array;
    }

    /**
     * 插入到指定位置
     * @param array
     * @param t
     * @param index
     * @return
     * @param <T>
     */
    public static <T> T[] insertToIndex(T[] array , T t ,int index){
        VerificationTool.throwIfNull(array);
        VerificationTool.throwIfMatch(index,(i)->{
           return i >array.length;
        });
        final Class<?> aClass = t.getClass();
        T[] na = (T[]) genericsArray(aClass, array.length+1);
        for (int i = 0; i < array.length + 1; i++) {
            if(i < index) {
                na[i] = array[i];
            }else if(i == index){
                na[i] = t;
            }else{
                na[i] = array[i -1];
            }
        }
        return  na;
    }

    /**
     * 插入到数组开头
     * @param array
     * @param t
     * @return
     * @param <T>
     */
    public static <T> T[] insertToFirst(T[] array , T t){
        VerificationTool.throwIfNull(array);
        return insertToIndex(array, t, 0);
    }

    /**
     * 插入到数组末尾
     * @param array
     * @param t
     * @return
     * @param <T>
     */
    public static <T> T[] insertToLast(T[] array , T t){
        return insert(array,t);
    }

    public static <T> T[] insert(T[] array , T t){
        VerificationTool.throwIfNull(array);
        return insertToIndex(array,t,array.length);
    }

    public static <T> T[] removeByIndex(T[] array ,int index){
        VerificationTool.throwIfNull(array);
        VerificationTool.throwIfNotMatch(index, t->t<array.length&&index>=0);
        array[index] = null;
        return removeNull(array);
    }

    public static long[] removeByIndex(long[] array ,int index){
        VerificationTool.throwIfNull(array);
        VerificationTool.throwIfNotMatch(index, t->t<array.length&&index>=0);
        long[] na =new long[array.length -1];
        for (int i = 0; i < na.length; i++) {
            if(i<index){
                na[i] = array[i];
            }else{
                na[i] = array[i+1];
            }
        }
        return na;
    }
    public static int[] removeByIndex(int[] array ,int index){
        VerificationTool.throwIfNull(array);
        VerificationTool.throwIfNotMatch(index, t->t<array.length&&index>=0);
        int[] na =new int[array.length -1];
        for (int i = 0; i < na.length; i++) {
            if(i<index){
                na[i] = array[i];
            }else{
                na[i] = array[i+1];
            }
        }
        return na;
    }

    public static double[] removeByIndex(double[] array ,int index){
        VerificationTool.throwIfNull(array);
        VerificationTool.throwIfNotMatch(index, t->t<array.length&&index>=0);
        double[] na =new double[array.length -1];
        for (int i = 0; i < na.length; i++) {
            if(i<index){
                na[i] = array[i];
            }else{
                na[i] = array[i+1];
            }
        }
        return na;
    }

    public static float[] removeByIndex(float[] array ,int index){
        VerificationTool.throwIfNull(array);
        VerificationTool.throwIfNotMatch(index, t->t<array.length&&index>=0);
        float[] na =new float[array.length -1];
        for (int i = 0; i < na.length; i++) {
            if(i<index){
                na[i] = array[i];
            }else{
                na[i] = array[i+1];
            }
        }
        return na;
    }
    public static short[] removeByIndex(short[] array ,int index){
        VerificationTool.throwIfNull(array);
        VerificationTool.throwIfNotMatch(index, t->t<array.length&&index>=0);
        short[] na =new short[array.length -1];
        for (int i = 0; i < na.length; i++) {
            if(i<index){
                na[i] = array[i];
            }else{
                na[i] = array[i+1];
            }
        }
        return na;
    }

    public static Integer[] ofBasicDataArray(int[] array){
        VerificationTool.throwIfNull(array);
        Integer[] arr = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static Float[] ofBasicDataArray(float[] array){
        VerificationTool.throwIfNull(array);
        Float[] arr = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    public static Double[] ofBasicDataArray(double[] array){
        VerificationTool.throwIfNull(array);
        Double[] arr = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }


    public static Long[] ofBasicDataArray(long[] array){
        VerificationTool.throwIfNull(array);
        Long[] arr = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static Short[] ofBasicDataArray(short[] array){
        VerificationTool.throwIfNull(array);
        Short[] arr = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static Byte[] ofBasicDataArray(byte[] array){
        VerificationTool.throwIfNull(array);
        Byte[] arr = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static Character[] ofBasicDataArray(char[] array){
        VerificationTool.throwIfNull(array);
        Character[] arr = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static int[] toBasicDataArray(Integer[] array){
        VerificationTool.throwIfNull(array);
        int[] arr =new int[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static long[] toBasicDataArray(Long[] array){
        VerificationTool.throwIfNull(array);
        long[] arr =new long[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    public static double[] toBasicDataArray(Double[] array){
        VerificationTool.throwIfNull(array);
        double[] arr =new double[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static short[] toBasicDataArray(Short[] array){
        VerificationTool.throwIfNull(array);
        short[] arr =new short[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static char[] toBasicDataArray(Character[] array){
        VerificationTool.throwIfNull(array);
        char[] arr =new char[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }


    public static byte[] toBasicDataArray(Byte[] array){
        VerificationTool.throwIfNull(array);
        byte[] arr =new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }


    /**
     * 转为 基础数据类型数组
     * @param array
     * @return
     */
    public static float[] toBasicDataArray(Float[] array){
        VerificationTool.throwIfNull(array);
        float[] arr =new float[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    /**
     * 合并2个数组
     * @param arr1
     * @param arr2
     * @return
     * @param <T>
     */
    public static <T> T[] mergeArray(T[] arr1,T[] arr2){
        return mergeArrays(arr1,arr1);
    }

    /**
     * 合并多个数组 为一个新的数组
     * @param arrays
     * @return
     * @param <T>
     */
    public static <T> T[] mergeArrays(T[]... arrays){
        VerificationTool.throwIfNull(arrays);
        int totalLength =  0 ;
        for (T[] arr : arrays) {
            VerificationTool.throwIfNull(arr);
            totalLength += arr.length;
        }
        T[] na =  genericsArray(totalLength,arrays);
        int index = 0;
        for (T[] arr : arrays) {
            for (T t : arr) {
                na[index] = t;
                index ++ ;
            }
        }
        return na;
    }


    /**
     * 是否包含NULL
     * @param array
     * @return
     */
    public static boolean isContainsNull(Object[] array){
        for (Object o : array) {
            if (o ==null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 一处匹配规则的项目 ，返回一个
     * @param array
     * @param predicate
     * @return
     * @param <T>
     */
    public static <T> T[] removeIfMatch(T[] array ,Predicate predicate){
        VerificationTool.throwIfNull(array);
        final Class<T> targetClass = findTargetClass(array);
        Object[] objects = Arrays.stream(array)
                .filter(t -> !predicate.test(t)).toArray();
        return ToTArray(objects,targetClass);
    }

    /**
     * 找出最小值
     * @param array
     * @return
     * @param <T>
     */
    public static <T extends Comparable> T findMinValue(T[] array){
        VerificationTool.throwIfNull(array);
        VerificationTool.throwIfArrayIsEmpty(array);
        T min = null;
        for (T t : array) {
            if(min == null){
                min = t ;
            }
            if (t.compareTo(min)<0) {
                min = t;
            }
        }
        return min;
    }

    /**
     * 找出最大值
     * @param array
     * @return
     * @param <T>
     */
    public static <T extends Comparable> T findMaxValue(T[] array){
        VerificationTool.throwIfNull(array);
        VerificationTool.throwIfArrayIsEmpty(array);
        T max = null;
        for (T t : array) {
            if(max == null){
                max = t ;
            }
            if (t.compareTo(max)>0) {
                max = t;
            }
        }
        return max;
    }

    /**
     * 统计 符合条件的 数量
     * @param array
     * @param predicate
     * @return
     * @param <T>
     */
    public static <T> long countByPredicate( T[] array,Predicate<T> predicate){
        VerificationTool.throwIfNull(array);
        return Arrays.stream(array).filter(predicate).count();
    }

    /**
     * 按条件 筛选出 子数组
     * @param array
     * @param predicate
     * @return
     * @param <T>
     */
    public static <T> T[] subArrayByPredicate(T[] array ,Predicate<T> predicate){
        VerificationTool.throwIfNull(array);
        final Class<T> targetClass = findTargetClass(array);
        final Object[] objects = Arrays.stream(array).filter(predicate).toArray();
        return ToTArray(objects,targetClass);
    }



    /**
     * 通过 function 修改 array中的值
     * @param array
     * @param function
     * @return
     * @param <T>
     */
    public static <T> T[] changeValuesByFunction(T[] array , Function<T,T> function){
        VerificationTool.throwIfNull(array);
        for (int i = 0; i < array.length; i++) {
            array[i] = function.apply(array[i]);
        }
        return array;
    }


    /**
     * 获取任意一个 不是 null的数据
     * @param array
     * @return
     * @param <T>
     */
    public static <T> Optional<T> getAnyNotNull(T[] array){
        VerificationTool.throwIfNull(array);
        return Arrays.stream(array)
                .filter(Objects::nonNull)
                .findAny();
    }

    /**
     * 按条件从 数据源中 初始化 array
     * @param predicate
     * @param values
     * @param <T>
     */
    public static <T> T[] ofPredicate(Predicate<T> predicate ,T... values){
        return subArrayByPredicate(values, predicate);
    }


    public static boolean isContains(int[] arr ,int value){
        VerificationTool.throwIfNull(arr);
        for (int i : arr) {
            if(i == value){
                return true;
            }
        }
        return false;
    }


    public static boolean isContains(long[] arr ,long value){
        VerificationTool.throwIfNull(arr);
        for (var i : arr) {
            if(i == value){
                return true;
            }
        }
        return false;
    }

    public static boolean isContains(short[] arr ,short value){
        VerificationTool.throwIfNull(arr);
        for (var i : arr) {
            if(i == value){
                return true;
            }
        }
        return false;
    }

    public static boolean isContains(double[] arr ,double value){
        VerificationTool.throwIfNull(arr);
        for (var i : arr) {
            if(i == value){
                return true;
            }
        }
        return false;
    }
    public static boolean isContains(float[] arr ,float value){
        VerificationTool.throwIfNull(arr);
        for (var i : arr) {
            if(i == value){
                return true;
            }
        }
        return false;
    }


    /**
     * 获取下一个值，不包含 与 currentValue 相等的值
     * @param array
     * @param currentValue
     * @return
     * @param <T>
     */
    public static <T> T nextValue(T[] array,T currentValue){
        boolean b = false;
        for (T t : array) {
            if(b && !t.equals(currentValue)){
                return t;
            }
            if (t.equals(currentValue)) {
                b =true;
            }
        }
        return null;

    }

    public static <T> List<T> toList(T[] array){
        VerificationTool.throwIfNull(array,"原始数组为NULL");
        List<T> list =new ArrayList<>();
        for (T t : array) {
            list.add(t);
        }
        return list;
    }

    public static <T> T[] fromList(List<T> list){
        VerificationTool.throwIfNull(list,"原始列表为NULL");
        VerificationTool.throwIfCollectionIsEmpty(list,"原始列表size为0");
        T[] array = (T[]) genericsArray(list.get(0).getClass(), list.size());
        return list.toArray(array);
    }

}
