package io.manbang.easybytecoder.plugin.restrecord.runtime.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author GaoYang
 * 2018/12/26
 */
@Data
@ToString
public class TrafficDataModel implements Serializable {
    private String serviceProviderAppName;
    private String traceId;
    private String requestId;
    private String uri;
    private long sampleTimeStamp;
    private Object dataContentObj;
    private int serviceType;

}
