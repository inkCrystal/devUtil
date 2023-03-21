package cn.dev.structs;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

public class ReadOnlyList<T> implements Serializable {

    private int size = 0 ;
    private T[] array;
    private ReadOnlyList() {
        array= (T[]) new Object[]{};
    }
    private ReadOnlyList(List<T> list){
       init((T[]) list.toArray(new Object[0]));
    }
    private ReadOnlyList(T[] ts){
        init(ts);
    }



    private void init(T[]  ts){
        if(ts == null || ts.length == 0 ){
            array= (T[]) new Object[]{};
        }
        size = ts.length;
        array = ts;
    }

    public static <T> ReadOnlyList<T> ofList(List<T> list){
        return new ReadOnlyList<>(list);
    }

    public static final <T> ReadOnlyList<T> of(T... t){
        return new ReadOnlyList(t);
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean contains(Object o) {
        return indexOfRange(o, 0, size)>0;
    }
    int indexOfRange(Object o, int start, int end) {
        Object[] es = array;
        if (o == null) {
            for (int i = start; i < end; i++) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = start; i < end; i++) {
                if (o.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @NotNull
    public Iterator<T> iterator() {
        return Arrays.stream(array).iterator();
    }

    @NotNull
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @NotNull
    public <T> T[] toArray(@NotNull T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(array, size, a.getClass());
        }
        System.arraycopy(array, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }



    public T get(int index) {
        return array[index];
    }


    public int indexOf(Object o) {
        return indexOfRange(o, 0, size);
    }

    public int lastIndexOf(Object o) {
        return lastIndexOfRange(o,0,size);
    }

    int lastIndexOfRange(Object o, int start, int end) {
        Object[] es = array;
        if (o == null) {
            for (int i = end - 1; i >= start; i--) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = end - 1; i >= start; i--) {
                if (o.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }


    public List<T> toList(){
        return List.of(array);
    }

}
