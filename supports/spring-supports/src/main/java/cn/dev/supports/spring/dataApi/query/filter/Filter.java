package cn.dev.supports.spring.dataApi.query.filter;

import cn.dev.supports.spring.dataApi.DataModel;
import cn.dev.supports.spring.dataApi.query.OpEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.parser.ParseException;
import org.json.JSONException;
import reactor.core.publisher.Mono;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 *  一个Sql 查询的链式Filter，支持链式编辑
 *      同时支持 转换为JSONString 和 通过JSONString 来构建Filter。
 *      Filter是链式编辑的，不允许在中间插入Filter，只能在链路的最后一个Filter后面插入Filter。
 * @author Jason.Mao
 * @since  2021/6/18 15:36
 */
public class Filter<T extends DataModel> implements Serializable {
    @Serial
    private static final long serialVersionUID =  -59177127794277L;


    // 外一层的 filterBean ，即 该 filter 是 一个 OR的 （内部filter）
    private Filter<T> outerBean;

    // 该filter 后面接了 一个 AND （*） 或 OR的 （内部filter） 链路中的 第一个filter
    private Filter<T> innerBean;

    // filter 链路中的上一个filterBean
    private Filter<T> pre;

    /**下一个链路节点  ---by jason @ 2023/4/26 14:56 */
    private Filter<T> next;

    //**操作key   ---by jason @ 2023/4/26 14:56 */
    private String key;

    //**操作符  ---by jason @ 2023/4/26 14:55 */
    private OpEnum op ;

    //**生产的 filterQuery片段  ---by jason @ 2023/4/26 14:55 */
    private String filterQuery ;

    //**参数  ---by jason @ 2023/4/26 14:55 */
    private Serializable[] values;

    /**是否以 key 作为参数   ---by jason @ 2023/4/26 14:55 */
    private boolean keyParam = false;

    public static FilterBuilder builder(){
        return FilterBuilder.getBuilder();
    }

    public static FilterBuilder.OrBuilder orBuilder(){
        return FilterBuilder.Or;
    }

    protected Filter<T> markParamIsKey() {
        this.keyParam = true;
        this.filterQuery = OpEnum.toFilterQuery(this);
        return this;
    }

    private void setKey(String key) {
        this.key =  key;
    }

    private void setOp(OpEnum op) {
        this.op =  op;
    }

    public void setNext(Filter<T> next) {
        this.next = next;
    }

    private void setValues(Serializable[] values) {
        this.values =  values;
    }

    private void setPre(Filter<T> pre) {
        this.pre =  pre;
    }

    public String getKey() {
        return key;
    }

    public boolean isKeyParam() {
        return keyParam;
    }

    public OpEnum getOp() {
        return op;
    }

    /**
     * 获取当作参数的值
     * @return
     */
    public Serializable[] getParamTypeValues() {
        if(this.keyParam){
            return new Serializable[]{};
        }
        return this.values;
    }

    public Serializable getKeyTypeValue() {
        if(this.keyParam){
            return null;
        }
        return this.values[0];
    }

    /**
     * 不区分 值的类型 获取 所有的变量值
     * @return
     */
    public Serializable[] getAllTypeValues(){
        return this.values;
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

    public Filter<T> getNext() {
        return next;
    }

    public Filter<T> getInnerBean() {
        return innerBean;
    }

    public Filter<T> getOuterBean() {
        return outerBean;
    }

    public Filter<T> getPre() {
        return pre;
    }

//    protected Filter queryInit(String filterQuery) {
//        if(1> 0) {
////            this is do nothing for test ;
//            return this;
//        }
//        if(this.filterQuery == null) {
//            this.filterQuery =  key + " " + filterQuery;
//        }else {
//            throw new RuntimeException("不可重复initQueryString");
//        }
//        return this;
//    }

    protected static <T extends DataModel> Filter<T> build(String key, OpEnum op, Serializable... values){
        if (values!=null) {
            //构建前 检查 ！
            for (Serializable value : values) {
                if (value == null) {
                    throw new RuntimeException("构建Filter(" + key + " " + op + " "+ Arrays.toString(values) +")时发现错误：传递参数中不可存在NULL！");
                }
            }
        }
        Filter<T> filterBean =  new Filter<T>();
        filterBean.setKey(key);
        filterBean.setOp(op);
        if (Objects.nonNull(values) && values.length>0) {
            filterBean.setValues(values);
        }
        op.validParams(values);
        filterBean.filterQuery = OpEnum.toFilterQuery(filterBean);
        return filterBean;
    }


    protected void appendNextFilter(Filter<T> filterBean){
        if (this.next!= null) {
            throw new RuntimeException("不可在链路中间插入新的filter节点");
        }
        filterBean.setPre( this );
        this.setNext(filterBean);
    }

    /**
     * 拼接 一个 ... AND  key =  value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenEqual(String key , Serializable value){
        Filter<T> filterBean = builder().whereEqual(key, value);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key != value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenNotEqual(String key , Serializable value){
        Filter<T> filterBean = builder().whereNotEqual(key, value);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key IN (values) ,返回 链路中 最后一个filter
     * @param key
     * @param values
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenIn(String key , Serializable... values){
        if(values==null || values.length==0){
            return this;
        }
        if(values.length ==1){
            return this.thenEqual(key, values[0]);
        }
        Filter<T> filterBean = builder().whereIn(key, values);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key NOT IN (values) ,返回 链路中 最后一个filter
     * @param key
     * @param values
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenNotIn(String key , Serializable... values){
        if(values==null || values.length==0){
            return this;
        }
        if(values.length ==1){
            return this.thenNotEqual(key, values[0]);
        }
        Filter<T> filterBean = builder().whereNotIn(key, values);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key LIKE value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter thenLike(String key , String value){
        Filter filterBean = builder().whereLike(key, value);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key NOT LIKE value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenNotLike(String key , String value){
        Filter<T> filterBean = builder().whereNotLike(key, value);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key < value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenLessThan(String key , Number value){
        Filter<T> filterBean = builder().whereLessThan(key, value);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key <= value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenLessThanOrEqual(String key , Number value){
        Filter<T> filterBean = builder().whereLessThanOrEqual(key, value);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

/**
     * 拼接 一个 ... AND  key > value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenGreaterThan(String key , Number value){
        Filter<T> filterBean = builder().whereGreaterThan(key, value);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

/**
     * 拼接 一个 ... AND  key >= value ,返回 链路中 最后一个filter
     * @param key
     * @param value
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenGreaterThanOrEqual(String key , Number value){
        Filter<T> filterBean = builder().whereGreaterThanOrEqual(key, value);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

/**
     * 拼接 一个 ... AND  key BETWEEN value1 AND value2 ,返回 链路中 最后一个filter
     * @param key
     * @param value1
     * @param value2
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenBetweenAnd(String key , Serializable value1, Serializable value2){
        Filter<T> filterBean = builder().whereBetweenAnd(key, value1, value2);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key =  key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    @Deprecated
    public Filter<T> thenEqualKey(String key , String key2){
        Filter<T> filterBean = builder().whereEqualKey(key, key2);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key != key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    @Deprecated
    public Filter<T> thenNotEqualKey(String key , String key2){
        Filter<T> filterBean = builder().whereNotEqualKey(key, key2);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key < key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenLessThanKey(String key , String key2){
        Filter<T> filterBean = builder().whereLessThanKey(key, key2);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key <= key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenLessThanOrEqualKey(String key , String key2){
        Filter<T> filterBean = builder().whereLessThanOrEqualKey(key, key2);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key > key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenGreaterThanKey(String key , String key2){
        Filter<T> filterBean = builder().whereGreaterThanKey(key, key2);
        this.appendNextFilter(filterBean);
        return filterBean;
    }

    /**
     * 拼接 一个 ... AND  key >= key2 ,返回 链路中 最后一个filter
     * @param key
     * @param key2
     * @return 返回 链路中 最后一个filter
     */
    public Filter<T> thenGreaterThanOrEqualKey(String key , String key2){
        Filter<T> filterBean = builder().whereGreaterThanOrEqualKey(key, key2);
        this.appendNextFilter(filterBean);
        return filterBean;
    }


    /**
     * 添加 Or 逻辑的 Filter 片段， 即 WHERE ID >0 OR  + [( 新增条件片段，通过breakOr 返回上一个节点 )]
     * @param filterMono
     * @return 返回 链路中 最后一个filter
     * @throws RuntimeException 传递进来的条件，必须是Builder Or构建的。不然抛出 RuntimeException
     */
    public Filter<T> thenOrFilter(Mono<Filter<T>> filterMono){
        Filter<T> filterBean = filterMono.block();
        if(filterBean.outerBean ==null &&  filterBean.pre == null){
            if(this.getInnerBean()!=null){
                throw new RuntimeException("非法操作，该节点已经有绑定的Or条件，不可绑定多个");
            }
            filterBean.setOuterBean(this);
            this.setInnerBean(filterBean);
            return filterBean;
        }else{
            System.out.println("filterBean.outerBean = " + filterBean.outerBean);
            System.out.println("filterBean.innerBean = " + filterBean.innerBean);
            System.out.println("filterBean.pre = " + filterBean.pre);
            throw new RuntimeException("参数错误,入参必须是FilterBuilder 全新构造得，如： builder().whereEqual(\"name\",\"张三\") ");
        }
    }


    /**
     * same as thenAppendOrFilter
     * @param publisher
     * @return
     */
    public Filter<T> thenOr(Mono<Filter<T>> publisher){
//        Filter filterBean = publisher.block();
        return this.thenOrFilter(publisher);
    }


    /**
     * 跳出 当前的 Or 片段，返回到 进入 该片段的最后一个链表节点，返回的节点可以继续操作。
     * @return
     */
    public Filter<T> breakOr(){
        Filter<T> filterBean = this.findBreak();
        if(filterBean!=null){
            return filterBean;
        }
        throw new RuntimeException("该filter链路中没有可以Break的节点");
    }

    private Filter<T> findBreak(){
        Filter<T> f = this;
        while (f.getPre()!=null){
            f = f.getPre();
        }
        if(f.getOuterBean()!=null){
            return f.getOuterBean();
        }
        return outerBean;


    }
    public Filter<T> findStart(){
        Filter<T> bean = this;
        while (bean.getOuterBean()!=null){
            bean = bean.getOuterBean();
        }
        while (bean.getPre() != null){
            bean = bean.getPre();
        }
        return bean;
    }

    /**
     * 转化成 可用的 AvailableFilter，以便执行查询
     * @return AvailableFilter
     */
    public AvailableFilter<T> toAvailableFilter(){
        Filter<T> bean = findStart();
        StringBuilder sb =new StringBuilder(" WHERE ");
        List<Serializable> values = new ArrayList<>();
        this.append(bean,sb,values);
        return new AvailableFilter(sb.toString(),values);
    }

    private void appendInner(Filter<T> bean , StringBuilder sb, List<Serializable> values ){
        sb.append(" OR (");
        this.append(bean,sb,values);
        sb.append(")");
    }


    private void append(Filter<T> bean, StringBuilder sb, List<Serializable> values ){
        if(bean.getPre() != null){
            sb.append(" AND ");
        }
        sb.append( bean.getFilterQuery());
        if (bean.getParamTypeValues() != null) {
            values.addAll(Arrays.asList(bean.getParamTypeValues()));
        }
        if(bean.getInnerBean()!=null){
            this.appendInner(bean.getInnerBean(),sb,values);
        }
        if(bean.getNext()!= null){
            this.append(bean.getNext(),sb,values);
        }
    }


    @Deprecated
    public static void main(String[] args) throws JSONException, ParseException, JsonProcessingException {


        final Filter bean =
                builder().whereLessThan("da", 12.2)
                        .thenBetweenAnd("id", 1, 2313)
                    .thenEqual("name", "张三")
                    .thenOrFilter(orBuilder().whereEqual("name", "李四"))
                        .thenGreaterThan("age",12)
                        .thenLike("name", "王五%")
                        .thenEqualKey("name","shortName")
                        .breakOr()
                    .thenIn("sex",1)
                        .thenOrFilter(orBuilder().whereLessThan("age", 12))
                        .thenEqual("name", "王五")
                        .thenOrFilter(orBuilder().whereEqual("name", "赵六"))
                        .breakOr()
                    .thenEqual("ll",12).breakOr();
        String filterQuery1 = bean.toAvailableFilter().getFilterQuery();
        final List<Serializable> valueList = bean.toAvailableFilter().getValueList();
        for (int i = 0; i < valueList.size(); i++) {
            filterQuery1 = filterQuery1.replaceFirst("\\?",valueList.get(i).toString());
        }

        System.out.println(bean);
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(bean);

        System.out.println(s );

    }




}
