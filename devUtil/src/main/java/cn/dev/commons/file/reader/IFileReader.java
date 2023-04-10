package cn.dev.commons.file.reader;

import java.io.File;
import java.util.stream.Stream;

public interface IFileReader extends AutoCloseable{

    Stream<String> readAsStream(String filePath);
    Stream<String> readAsStream(File file);

}
