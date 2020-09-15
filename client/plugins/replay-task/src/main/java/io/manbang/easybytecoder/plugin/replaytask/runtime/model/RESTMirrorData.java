package io.manbang.easybytecoder.plugin.replaytask.runtime.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class RESTMirrorData implements Serializable {
    private String traceId;
    private String reqMethod = "POST";
    private String responseContent;
    private HashMap<String, String> requestHeaders;
    private String requestUri;
    private String requestMethod;
    private String requestBody;
    private String requestQueryParams;
    private Long timestamp;
}
