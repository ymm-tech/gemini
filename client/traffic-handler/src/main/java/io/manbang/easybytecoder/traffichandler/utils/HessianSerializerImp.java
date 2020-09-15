package io.manbang.easybytecoder.traffichandler.utils;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import io.manbang.easybytecoder.runtimecommonapi.serialize.HessianSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author xujie
 */
public class HessianSerializerImp  implements HessianSerializer {

    private static HessianSerializerFactory serializerFactory = new HessianSerializerFactory();

    static {
        serializerFactory.init();
    }


    @Override
    public  InputStream serialize(Object obj) throws Exception {
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

    @Override
    public  Object deserialize(InputStream input) {
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
