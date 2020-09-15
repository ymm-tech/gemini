package io.manbang.easybytecoder.plugin.mybatisrecord.runtime;


import io.manbang.easybytecoder.plugin.mybatisrecord.runtime.model.CommonMirrorData;
import io.manbang.easybytecoder.plugin.mybatisrecord.runtime.utils.SqlDataGetterUtil;
import io.manbang.easybytecoder.plugin.restrecord.runtime.PluginContext;
import io.manbang.easybytecoder.runtimecommonapi.utils.EasyByteCoderResourceObjectPool;
import io.manbang.easybytecoder.runtimecommonapi.utils.FileUtils;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;

import java.io.InputStream;
import java.sql.Statement;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class NewMybatisRecorder {
    private static AtomicInteger specialCounter = new AtomicInteger(0);
    private static ThreadPoolExecutor sendToOssThreadPool = new ThreadPoolExecutor(10, 100, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100));


    public static void handleUpdateRecord(PreparedStatementHandler preparedHandler, Statement proxy, int retVal) {
        try {
            Object paramObject = null;

            try {
                paramObject = preparedHandler.getBoundSql().getParameterObject();

            } catch (Throwable e) {
                EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("NewMybatisRecorder getParameterObject: FAILED!" + e.getMessage());
            }

            doHandleRecord(proxy, (Integer) retVal, paramObject);
        } catch (Exception e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("NewMybatisRecorder handleRecord update error:", e);
        }
    }

    public static void handleQueryRecord(Statement proxy, Object retVal) {
        try {
            doHandleRecord(proxy, retVal, null);
        } catch (Exception e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("NewMybatisRecorder handleRecord query error:", e);
        }
    }

    public static void doHandleRecord(Statement proxy, Object retVal, Object paramObject) {
        final String mapperSqlStr = SqlDataGetterUtil.getMapperSql(proxy);
        String clazz = Thread.currentThread().getStackTrace()[3].getClassName();
        String method = Thread.currentThread().getStackTrace()[3].getMethodName();

        // Object params = SqlDataGetterUtil.getSqlParams(proxy);
        CommonMirrorData commonMirrorData = buildSubCommonMirrorData(clazz + "#" + method, retVal, paramObject, mapperSqlStr);
//        InputStream ips = null;
//        byte[] bytes=null;
//        try {
//            ips = EasyByteCoderResourceObjectPool.getHessianSerializer().serialize(commonMirrorData);
//            bytes = new byte[ips.available()];
//            ips.read(bytes);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        String result = EasyByteCoderResourceObjectPool.getJsonSerializer().serializeWithClassInfo(commonMirrorData);
        FileUtils.writeNIO("./traces/mirror/" + PluginContext.getTraceId() + ".txt", result);
    }


    private static CommonMirrorData buildSubCommonMirrorData(String methodReferenceUri, Object retVal, Object paramObj, Object... inParams) {
        CommonMirrorData data = new CommonMirrorData();
        data.setMethodReferenceUri(methodReferenceUri);
        data.setInParams(inParams);
        data.setParamObject(paramObj);
        data.setResult(retVal);
        data.setTraceId(PluginContext.getTraceId());
        return data;

    }

}
