package io.manbang.easybytecoder.plugin.replaytask.runtime.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FinalRspDiff {

    /**
     * 字段路径，如：pageExtraInfoData.bottomButton.url
     */
    private String fieldPath;

    /**
     * 线上录制原始值
     */
    private String originValue;

    /**
     * 重放时返回响应值
     */
    private String replayValue;

    /**
     * 结果比对差异类型
     * com.ymm.opendoom.trafficdatamodel.Enum.DataDiffTypeEnum
     */
    private int diffType;


    @Override
    public int hashCode() {
        return fieldPath.hashCode()+this.getDiffType();
    }
}
