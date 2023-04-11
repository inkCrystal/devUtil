package cn.dev.commons.file;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class SizeUnit implements Serializable {
    @Serial
    private static final long serialVersionUID = -7268474748458019592L;
    private static final long SIZE_BYTE = 1;
    private static final long SIZE_KB = 1024;
    private static final long SIZE_MB = SIZE_KB * 1024;
    private static final long SIZE_GB = SIZE_MB * 1024;
    private static final long SIZE_TB = SIZE_GB * 1024;
    private static final long SIZE_PB = SIZE_TB * 1024;
    private static final long SIZE_EB = SIZE_PB * 1024;
    private static final long SIZE_ZB = SIZE_EB * 1024;
    private static final long SIZE_YB = SIZE_ZB * 1024;
    private static final long[] sizeArray =
            {SIZE_BYTE, SIZE_KB, SIZE_MB, SIZE_GB, SIZE_TB, SIZE_PB, SIZE_EB, SIZE_ZB, SIZE_YB};
    private static final String[] unitNameArray =
            {"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
    private final int type ;

    private String unit ;

    private SizeUnit(int type, long value) {
        this.type = type;
        this.unit = unitNameArray[type];
    }
    private SizeUnit(int type){
        this.type = type;
        this.unit = unitNameArray[type];
    }

    public static final SizeUnit BYTE = new SizeUnit(0);
    public static final SizeUnit KB = new SizeUnit(1);
    public static final SizeUnit MB = new SizeUnit(2);
    public static final SizeUnit GB = new SizeUnit(3);
    public static final SizeUnit TB = new SizeUnit(4);
    public static final SizeUnit PB = new SizeUnit(5);
    public static final SizeUnit EB = new SizeUnit(6);
    public static final SizeUnit ZB = new SizeUnit(7);
    public static final SizeUnit YB = new SizeUnit(8);

    private BigDecimal toTargetUnitValue(int targetType , Number value){
        int dif = targetType - type;
        BigDecimal number = new BigDecimal(value.doubleValue());
        while (dif > 0){
            number = number.divide(new BigDecimal(1024));
            dif--;
        }
        while (dif < 0){
            number = number.multiply(new BigDecimal(1024));
            dif++;
        }
        return number;
    }
    public long toByte(Number size){
        return toTargetUnitValue(0,size).longValue();
    }

    public Number toKB(Number size){
        return toTargetUnitValue(1,size);
    }
    public Number toMB(Number size){
        return toTargetUnitValue(2,size);
    }
    public Number toGB(Number size){
        return toTargetUnitValue(3,size);
    }
    public Number toTB(Number size){
        return toTargetUnitValue(4,size);
    }
    public Number toPB(Number size){
        return toTargetUnitValue(5,size);
    }
    public Number toEB(Number size){
        return toTargetUnitValue(6,size);
    }
    public Number toZB(Number size){
        return toTargetUnitValue(7,size);
    }
    public Number toYB(Number size){
        return toTargetUnitValue(8,size);
    }
    public String getUnit() {
        return unit;
    }

    public Number ofByte(Number size){
        return BYTE.toTargetUnitValue(type,size);
    }

    public Number ofKB(Number size){
        return KB.toTargetUnitValue(type,size);
    }

    public Number ofMB(Number size){
        return MB.toTargetUnitValue(type,size);
    }

    public Number ofGB(Number size){
        return GB.toTargetUnitValue(type,size);
    }

    public Number ofTB(Number size){
        return TB.toTargetUnitValue(type,size);
    }

    public Number ofPB(Number size){
        return PB.toTargetUnitValue(type,size);
    }

    public Number ofEB(Number size){
        return EB.toTargetUnitValue(type,size);
    }

    public Number ofZB(Number size){
        return ZB.toTargetUnitValue(type,size);
    }

    public Number ofYB(Number size){
        return YB.toTargetUnitValue(type,size);
    }


}
