package cn.dev.core.object;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.util.Pool;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class kryoSerializerUtil {
    private static Pool<Kryo> kryoPool = new Pool<Kryo>(true, false, 16) {
        protected Kryo create() {
            Kryo kryo = new Kryo();
//            kryo.setRegistrationRequired(false);
            return kryo;
        }
    };
    Pool<Output> outputPool = new Pool<Output>(true, false, 16) {
        protected Output create () {
            return new Output(1024, -1);
        }
    };





}
