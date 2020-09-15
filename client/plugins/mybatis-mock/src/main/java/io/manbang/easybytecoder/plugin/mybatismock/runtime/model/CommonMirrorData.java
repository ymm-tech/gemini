package io.manbang.easybytecoder.plugin.mybatismock.runtime.model;

import lombok.Data;

import java.io.Serializable;

@Data
/**
 * @author GaoYang
 * 2018/12/20
 */
public class CommonMirrorData implements Serializable {
    private String traceId;
    private String methodReferenceUri;
    private Object[] inParams;
    private Object result;
    private Object paramObject;
    /**
     * matched when replay
     */
    private Boolean replayMatched = Boolean.FALSE;

}