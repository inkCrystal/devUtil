package cn.dev.supports.spring.dataApi;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class DataModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1175320829612928409L;
    @Id
    private Long id;



}
