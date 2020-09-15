package io.manbang.easybytecoder.plugin.replaytask.runtime;

import io.manbang.easybytecoder.plugin.replaytask.runtime.model.DataDiffTypeEnum;
import io.manbang.easybytecoder.plugin.replaytask.runtime.model.FinalRspDiff;
import io.manbang.easybytecoder.plugin.replaytask.runtime.model.RESTMirrorData;
import io.manbang.easybytecoder.plugin.replaytask.runtime.util.CommonUtils;
import io.manbang.easybytecoder.runtimecommonapi.utils.EasyByteCoderResourceObjectPool;
import io.manbang.easybytecoder.runtimecommonapi.utils.FileUtils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.json.JSONException;
import org.skyscreamer.jsonassert.FieldComparisonFailure;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Task {

    public void replayTask() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("replayTask").daemon(true).build());
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> trades = FileUtils.getFileName("./traces");
                for (String trade : trades) {
                    String json = FileUtils.readNIO("./traces/" + trade);
                    RESTMirrorData restMirrorData = EasyByteCoderResourceObjectPool.getGsonSerializer().deserialize(json, RESTMirrorData.class);
                    String replayUri = restMirrorData.getRequestUri();

                    String replayerResult = new CommonUtils().sendHttpReqForMirror(restMirrorData);

                    String uri = restMirrorData.getRequestUri();

                    Set<FinalRspDiff> finalRspDiffs = new HashSet<>();
                    JSONCompareResult compareResult = null;

                    try {
                        compareResult = JSONCompare.compareJSON(restMirrorData.getResponseContent(), replayerResult, JSONCompareMode.NON_EXTENSIBLE);
                    } catch (JSONException e) {
                        if(!replayerResult.equals(restMirrorData.getResponseContent())){
                            finalRspDiffs.add(FinalRspDiff.builder()
                                    .fieldPath("/")
                                    .diffType(DataDiffTypeEnum.FIELD_VALUE_DIFF.getTypeCode())
                                    .originValue(restMirrorData.getResponseContent())
                                    .replayValue(replayerResult)
                                    .build());
                        }
                        return;
                    }

                    mergeOneTyeFinalRspResult(compareResult.getFieldFailures().iterator(), finalRspDiffs, DataDiffTypeEnum.FIELD_VALUE_DIFF.getTypeCode());
                    mergeOneTyeFinalRspResult(compareResult.getFieldMissing().iterator(), finalRspDiffs, DataDiffTypeEnum.FIELD_MISSED.getTypeCode());
                    mergeOneTyeFinalRspResult(compareResult.getFieldUnexpected().iterator(), finalRspDiffs, DataDiffTypeEnum.FIELD_OTHER_EXCEPTION.getTypeCode());

                    //不一致的对比结果
                    System.out.println(finalRspDiffs);
                }
            }
        }, 1, 2, TimeUnit.SECONDS);
    }



    private void mergeOneTyeFinalRspResult(Iterator<FieldComparisonFailure> failuresIto, Set<FinalRspDiff> finalRspDiffs, int diffType) {

        while (failuresIto.hasNext()) {
            FieldComparisonFailure fieldComparisonFailure = failuresIto.next();


            //当字段缺少时的忽略
            if (fieldComparisonFailure.getField().equals("") ) {
                continue;
            }

            //当增加字段时的忽略
            if (fieldComparisonFailure.getField().equals("")) {
                continue;
            }



            String fieldPath = "";

            //不一致的字段
            if (diffType == DataDiffTypeEnum.FIELD_VALUE_DIFF.getTypeCode()) {
                fieldPath = "" + fieldComparisonFailure.getField();
            }
            //丢失的字段
            if (diffType == DataDiffTypeEnum.FIELD_MISSED.getTypeCode()) {
                fieldPath = "" + fieldComparisonFailure.getField() + fieldComparisonFailure.getExpected();
            }

            //如果是缺少的字段
            if (diffType == DataDiffTypeEnum.FIELD_OTHER_EXCEPTION.getTypeCode()) {
                fieldPath = "" + fieldComparisonFailure.getField() + "." + fieldComparisonFailure.getActual();
            }




            finalRspDiffs.add(FinalRspDiff.builder()
                    .fieldPath(fieldPath)
                    .diffType(diffType)
                    .originValue("" + fieldComparisonFailure.getExpected())
                    .replayValue("" + fieldComparisonFailure.getActual())
                    .build());


        }
    }

}
