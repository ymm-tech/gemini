package io.manbang.easybytecoder.plugin.restrecord.runtime;

import io.manbang.easybytecoder.runtimecommonapi.utils.EasyByteCoderResourceObjectPool;
import io.manbang.easybytecoder.runtimecommonapi.utils.ExecuteThreadPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRecordHandler {

    public static void handleAfter(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (PluginContext.getTraceId() == null) {
                return;
            }
            if (!(request instanceof RequestInterceptorWrapper)) {
                return;
            }

            RestMirrorDataTask task = new RestMirrorDataTask(
                    (RequestInterceptorWrapper) request,
                    (ResponseInterceptorWrapper) response,
                    PluginContext.getTraceId());


            ExecuteThreadPool.runAysnc(task);
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().info("task:{}", task);
        } catch (Exception e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("handleAfter error:", e);
        } finally {
            PluginContext.clearContext();
        }
    }


}
