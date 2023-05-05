package cn.dev.supports.spring.dataApi.query.filter;

import cn.dev.supports.spring.dataApi.DataModel;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class TestBean extends DataModel  {
    @Serial
    private static final long serialVersionUID = -2539239396203346622L;
    private int age ;
    private int num;
    private String name ;
    private LocalDate dateofBirth;
}
