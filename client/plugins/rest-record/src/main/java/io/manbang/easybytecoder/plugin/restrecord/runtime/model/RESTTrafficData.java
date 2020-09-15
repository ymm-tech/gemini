package io.manbang.easybytecoder.plugin.restrecord.runtime.model;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author GaoYang
 * 2018/12/20
 */
@Data
public class RESTTrafficData implements Serializable {
    private String responseContent;
    private HashMap<String, String> requestHeaders;
    private String requestUri;
    private String requestMethod;
    private String requestBody;
    private String requestQueryParams;
}
