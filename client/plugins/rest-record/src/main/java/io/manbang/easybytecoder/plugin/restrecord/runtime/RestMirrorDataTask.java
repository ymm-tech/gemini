package io.manbang.easybytecoder.plugin.restrecord.runtime;

import io.manbang.easybytecoder.plugin.restrecord.runtime.model.RESTMirrorData;
import io.manbang.easybytecoder.runtimecommonapi.utils.EasyByteCoderResourceObjectPool;
import io.manbang.easybytecoder.runtimecommonapi.utils.FileUtils;

public class RestMirrorDataTask implements Runnable {
    private RequestInterceptorWrapper request;
    private ResponseInterceptorWrapper response;
    private String traceId;

    public RestMirrorDataTask(RequestInterceptorWrapper request, ResponseInterceptorWrapper response, String traceId) {
        this.request = request;
        this.response = response;
        this.traceId = traceId;
    }

    @Override
    public void run() {
        RESTMirrorData buildMainReqMirrorData = RestTrafficDataBuilder.buildMainReqMirrorData(request, response, traceId);
        String json = EasyByteCoderResourceObjectPool.getGsonSerializer().serialize(buildMainReqMirrorData);
        FileUtils.writeNIO("./traces/"+traceId+".txt",json);
    }
}
