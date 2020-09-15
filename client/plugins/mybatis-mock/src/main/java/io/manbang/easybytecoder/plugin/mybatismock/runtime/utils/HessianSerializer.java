package io.manbang.easybytecoder.plugin.mybatismock.runtime.utils;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class HessianSerializer {

    private static HessianSerializerFactory serializerFactory = new HessianSerializerFactory();

    static {
        serializerFactory.init();
    }


    public static InputStream serialize(Object obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        serializerFactory.setAllowNonSerializable(true);
        out.setSerializerFactory(serializerFactory);

        out.startMessage();
        out.writeObject(obj);
        out.completeMessage();
        out.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
        return inputStream;

    }

    public static Object deserialize(InputStream input) {
        Hessian2Input in = new Hessian2Input(input);
        serializerFactory.setAllowNonSerializable(true);
        in.setSerializerFactory(serializerFactory);
        try {
            in.startMessage();
            Object ret = in.readObject();
            in.completeMessage();

            in.close();
            input.close();
            return ret;
        } catch (Exception e) {
           e.printStackTrace();
        }

        return null;


    }


}
