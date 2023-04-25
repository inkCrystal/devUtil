package cn.dev.utils.supports.dataApi.query.filter;

import cn.dev.utils.supports.dataApi.query.OpEnum;
import org.json.JSONException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;


public class Filter implements Serializable {
    @Serial
    private static final long serialVersionUID =  -59177127794277L;

    // 外一层的 filterBean ，即 该 filter 是 一个 AND （*） 或 OR的 （内部filter）
    private Filter outerBean;

    // 该filter 后面接了 一个 AND （*） 或 OR的 （内部filter） 链路中的 第一个filter
    private Filter innerBean;

    // filter 链路中的上一个filterBean
    private Filter pre;

    private Filter next;

    private String key;

    private OpEnum op ;

    private String filterQuery ;

    private Serializable[] values;


    private void setKey(String key) {
        this.key =  key;
    }

    private void setOp(OpEnum op) {
        this.op =  op;
    }

    public void setNext(Filter next) {
        this.next = next;
    }

    private void setValues(Serializable[] values) {
        this.values =  values;
    }

    private void setPre(Filter pre) {
        this.pre =  pre;
    }

    public Filter getNext() {
        return next;
    }

    public Filter getInnerBean() {
        return innerBean;
    }

    public Filter getOuterBean() {
        return outerBean;
    }

    public Filter getPre() {
        return pre;
    }

    public OpEnum getOp() {
        return op;
    }

    public Serializable[] getValues() {
        return values;
    }

    public String getFilterQuery() {
        return filterQuery;
    }

    private void setOuterBean(Filter outerBean) {
        this.outerBean =  outerBean;
    }

    private void setInnerBean(Filter innerBean) {
        this.innerBean =  innerBean;
    }

    protected Filter queryInit(String filterQuery) {
        if(this.filterQuery == null) {
            this.filterQuery =  key + " " + filterQuery;
        }else {
            throw new RuntimeException("不可重复initQueryString");
        }
        return this;
    }

    protected static Filter build(String key, OpEnum op, Serializable... values){
        Filter filterBean =  new Filter();
        filterBean.setKey(key);
        filterBean.setOp(op);
        if (Objects.nonNull(values) && values.length>0) {
            filterBean.setValues(values);
        }
        return filterBean;
    }






    /**
     * 拼接 一个 ... AND  key =  value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenEqual(String key , Serializable value){
        Filter filterBean = FilterBuilder.whereEqual(key, value);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key != value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenNotEqual(String key , Serializable value){
        Filter filterBean = FilterBuilder.whereNotEqual(key, value);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key IN (values) ,返回 链路中 最后一个filter
     * @param key
     * @param values
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenIn(String key , Serializable... values){
        Filter filterBean = FilterBuilder.whereIn(key, values);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key NOT IN (values) ,返回 链路中 最后一个filter
     * @param key
     * @param values
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenNotIn(String key , Serializable... values){
        Filter filterBean = FilterBuilder.whereNotIn(key, values);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key LIKE value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenLike(String key , String value){
        Filter filterBean = FilterBuilder.whereLike(key, value);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key NOT LIKE value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenNotLike(String key , String value){
        Filter filterBean = FilterBuilder.whereNotLike(key, value);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key < value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenLessThan(String key , Number value){
        Filter filterBean = FilterBuilder.whereLessThan(key, value);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key <= value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenLessThanEqual(String key , Number value){
        Filter filterBean = FilterBuilder.whereLessThanEqual(key, value);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

/**
     * 拼接 一个 ... AND  key > value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenGreaterThan(String key , Number value){
        Filter filterBean = FilterBuilder.whereGreaterThan(key, value);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

/**
     * 拼接 一个 ... AND  key >= value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenGreaterThanEqual(String key , Number value){
        Filter filterBean = FilterBuilder.whereGreaterThanEqual(key, value);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

/**
     * 拼接 一个 ... AND  key BETWEEN value1 AND value2 ,返回 链路中 最后一个filter
     * @param key
     * @param value1
     * @param value2
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenBetweenAnd(String key , Serializable value1, Serializable value2){
        Filter filterBean = FilterBuilder.whereBetweenAnd(key, value1, value2);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key =  key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    @Deprecated
    public Filter thenEqualKey(String key , String key2){
        Filter filterBean = FilterBuilder.whereEqualKey(key, key2);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key != key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    @Deprecated
    public Filter thenNotEqualKey(String key , String key2){
        Filter filterBean = FilterBuilder.whereNotEqualKey(key, key2);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key > key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenLessThanKey(String key , String key2){
        Filter filterBean = FilterBuilder.whereLessThanKey(key, key2);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key <= key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenLessThanEqualKey(String key , String key2){
        Filter filterBean = FilterBuilder.whereLessThanEqualKey(key, key2);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key > key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenGreaterThanKey(String key , String key2){
        Filter filterBean = FilterBuilder.whereGreaterThanKey(key, key2);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key >= key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenGreaterThanEqualKey(String key , String key2){
        Filter filterBean = FilterBuilder.whereGreaterThanEqualKey(key, key2);
        filterBean.setPre(this);
        this.setNext(filterBean);
        return filterBean;
    }


    /**
     * 添加 Or 逻辑的 Filter
     * @param filterBean
     * @return
     */
    public Filter thenAppendOrFilter(Filter filterBean){
        if(filterBean.outerBean ==null && filterBean.innerBean ==null && filterBean.pre == null){
            filterBean.setOuterBean(this);
            this.setInnerBean(filterBean);
            return filterBean;
        }else{

            throw new RuntimeException("参数错误,入参必须是FilterBuilder 全新构造得，如： FilterBuilder.whereEqual(\"name\",\"张三\") ");
        }
    }


    public Filter breakOr(){
        Filter filterBean = this.findBreak();
        if(filterBean!=null){
            return filterBean;
        }
        throw new RuntimeException("该filter链路中没有可以Break的节点");
    }

    private Filter findBreak(){
        Filter f = this;
        while (f.getPre()!=null){
            f = f.getPre();
        }
        if(f.getOuterBean()!=null){
            return f.getOuterBean();
        }
        return outerBean;


    }


    public CompleteFilter toFullFilter(){
        Filter bean = this;
        while (bean.getOuterBean()!=null){
            bean = bean.getOuterBean();
        }
        while (bean.getPre() != null){
            bean = bean.getPre();
        }
        StringBuilder sb =new StringBuilder(" WHERE ");
        List<Serializable> values = new ArrayList<>();
        this.append(bean,sb,values);
        return new CompleteFilter(sb.toString(),values);
    }

    private void appendInner(Filter bean , StringBuilder sb, List<Serializable> values ){
        sb.append(" OR (");
        this.append(bean,sb,values);
        sb.append(")");
    }


    private void append(Filter bean, StringBuilder sb, List<Serializable> values ){

        if(bean.getPre() != null){
            sb.append(" AND ");
        }
        sb.append( bean.getFilterQuery());
        if (bean.getValues() != null) {
            values.addAll(Arrays.asList(bean.getValues()));
        }
        if(bean.getInnerBean()!=null){
            this.appendInner(bean.getInnerBean(),sb,values);
        }
        if(bean.getNext()!= null){
            this.append(bean.getNext(),sb,values);
        }
    }




    @Deprecated
    public static void main(String[] args) throws JSONException {

        final Filter bean =
                FilterBuilder.whereLessThan("da", 12.2)
                        .thenBetweenAnd("id", 1, 2313)
                    .thenEqual("name", "张三")
                    .thenAppendOrFilter(FilterBuilder.whereEqual("name", "李四"))
                        .thenGreaterThan("age",12)
                        .thenLike("name", "王五%")
                        .thenEqualKey("name","shortName")
                        .breakOr()
                    .thenIn("sex",1)
                        .thenAppendOrFilter(FilterBuilder.whereLessThan("age", 12))
                        .thenEqual("name", "王五")
                        .thenAppendOrFilter(FilterBuilder.whereEqual("name", "赵六"))
                        .breakOr()
                    .thenEqual("ll",12).breakOr();
        String filterQuery1 = bean.toFullFilter().getFilterQuery();
        final List<Serializable> valueList = bean.toFullFilter().getValueList();
        for (int i = 0; i < valueList.size(); i++) {
            filterQuery1 = filterQuery1.replaceFirst("\\?",valueList.get(i).toString());
        }
        System.out.println(filterQuery1);

        FilterConverter.toJson(bean);

    }



    static R2dbcEntityTemplate dbcEntityTemplate(){
        return new R2dbcEntityTemplate(null);
    };

}
