package cn.dev.core.parallel.share;

public interface IThreadSharer {


    /**
     * 构建需要共享的数据
     * @return
     */
    TaskShareData toShareData();

}
