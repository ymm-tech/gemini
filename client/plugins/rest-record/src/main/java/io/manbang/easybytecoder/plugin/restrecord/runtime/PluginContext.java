package io.manbang.easybytecoder.plugin.restrecord.runtime;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PluginContext {

    private static ThreadLocal<Map<String, Serializable>> requestContext = new TransmittableThreadLocal();


    public static void setTraceId(String traceId) {
        putContext("TRACE_ID", traceId);
    }

    public static String getTraceId() {
        return getContext("TRACE_ID");
    }

    public static void putContext(String key, String value) {
        if (requestContext.get() == null) {
            Map data = new HashMap<String, Serializable>();
            requestContext.set(data);
        }
        requestContext.get().put(key, value);
    }

    public static String getContext(String key) {
        if (requestContext.get() == null) {
            Map data = new HashMap<String, Serializable>();
            requestContext.set(data);
        }
        return (String) requestContext.get().get(key);
    }

    public static void clearContext() {
        requestContext.remove();
    }


    public static void setTraceId() {
        putContext("TRACE_ID", generateDefaultTraceId());
    }
    public static String generateDefaultTraceId() {
        return Thread.currentThread().getName() + "#" + UUID
                .randomUUID().toString() + "#" + System.nanoTime();
    }

}