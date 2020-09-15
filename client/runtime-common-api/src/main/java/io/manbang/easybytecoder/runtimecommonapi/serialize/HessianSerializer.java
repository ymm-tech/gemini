package io.manbang.easybytecoder.runtimecommonapi.serialize;

import java.io.InputStream;

public interface HessianSerializer {
    Object deserialize(InputStream input);

     InputStream serialize(Object obj) throws Exception;
}
