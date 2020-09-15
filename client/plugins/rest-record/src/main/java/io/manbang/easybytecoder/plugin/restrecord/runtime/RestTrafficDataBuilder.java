package io.manbang.easybytecoder.plugin.restrecord.runtime;


import io.manbang.easybytecoder.plugin.restrecord.runtime.model.RESTMirrorData;
import io.manbang.easybytecoder.runtimecommonapi.utils.EasyByteCoderResourceObjectPool;

import java.nio.charset.Charset;

/**
 * @author GaoYang 2018/12/26
 */
public class RestTrafficDataBuilder {

    public static RESTMirrorData buildMainReqMirrorData(RequestInterceptorWrapper req, ResponseInterceptorWrapper rsp, String traceId) {
        RESTMirrorData restMirrorData = new RESTMirrorData();
        try {
            restMirrorData.setReqMethod(req.getHttpMethod());
            restMirrorData.setRequestHeaders(req.getRequestHeaders());
            restMirrorData.setRequestBody(new String(req.getBody()));
            restMirrorData.setRequestMethod(req.getHttpMethod());
            restMirrorData.setRequestUri(req.getUri().getPath());
            restMirrorData.setRequestQueryParams(req.getDoGetQueryParams());
            try {
                EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().debug("RestTrafficDataBuilder:buildMainReqMirrorData response encoding:" + rsp.getCharacterEncoding());
                EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().debug("RestTrafficDataBuilder:buildMainReqMirrorData response default encoding:" + Charset.defaultCharset());
                restMirrorData.setResponseContent(new String(rsp.getClone(), Charset.defaultCharset()));
            } catch (Throwable e) {
                EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("doing8 building TrafficDataModel...", e);
            }
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().debug("doing9 building traceId..." + traceId);
            restMirrorData.setTraceId(traceId);
            restMirrorData.setTimestamp(System.currentTimeMillis());
        } catch (Throwable e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("error building TrafficDataModel...:", e);
        }
        return restMirrorData;
    }

}
