package cn.dev.core.clean;

import java.io.IOException;

public interface IAutoCLeanBuilder<T extends AutoCloseable> {

    T newInstance() throws Exception;

}
