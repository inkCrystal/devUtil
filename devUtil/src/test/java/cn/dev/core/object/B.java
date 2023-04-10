package cn.dev.core.object;

import org.apache.poi.hslf.util.LocaleDateFormat;

import java.time.LocalDateTime;

public class B {
    private int admin;
    private int sex ;

    public LocalDateTime localDateTime;

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return "B{" +
                "admin=" + admin +
                ", sex=" + sex +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
