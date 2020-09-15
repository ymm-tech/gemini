package io.manbang.easybytecoder.plugin.mybatismock.runtime;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import io.manbang.easybytecoder.plugin.mybatismock.runtime.model.CommonMirrorData;
import io.manbang.easybytecoder.plugin.mybatismock.runtime.utils.HessianSerializer;
import io.manbang.easybytecoder.plugin.mybatismock.runtime.utils.SqlDataGetterUtil;
import io.manbang.easybytecoder.plugin.restmock.runtime.PluginContext;

import io.manbang.easybytecoder.runtimecommonapi.utils.EasyByteCoderResourceObjectPool;
import io.manbang.easybytecoder.runtimecommonapi.utils.FileUtils;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Statement;
import java.util.List;


public class NewMybatisMocker {


    public static int getUpdateResponse(PreparedStatementHandler preparedHandler, Statement sqlStmt) {
        try {
            String clazz = Thread.currentThread().getStackTrace()[2].getClassName();
            String method = Thread.currentThread().getStackTrace()[2].getMethodName();
            String mapperSqlStr = SqlDataGetterUtil.getMapperSql(sqlStmt);
            Object params = SqlDataGetterUtil.getSqlParams(sqlStmt);
            Object paramObject = null;
            try {
                paramObject = preparedHandler.getBoundSql().getParameterObject();

            } catch (Throwable e) {
                EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("NewMybatisMocker getParameterObject:FAILED!" + e.getMessage());
            }

            Object intRet = getSubCommonMirrorData(clazz + "#" + method, paramObject, mapperSqlStr, params);
            return (Integer) intRet;
        } catch (Exception e) {
            return -1;
        }


    }

    public static List<Object> getQueryResponse(Statement sqlStmt) {
        try {
            String clazz = Thread.currentThread().getStackTrace()[2].getClassName();
            String method = Thread.currentThread().getStackTrace()[2].getMethodName();
            String mapperSqlStr = SqlDataGetterUtil.getMapperSql(sqlStmt);

            return (List<Object>) getSubCommonMirrorData(clazz + "#" + method, null, mapperSqlStr, SqlDataGetterUtil.getStmtLogClassName(sqlStmt));
        } catch (Exception e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("NewMybatisMocker getQueryResponse:FAILED!" + e.getMessage());
            return null;
        }

    }


    private static Object getSubCommonMirrorData(String methodReferenceUri, Object paramObject, Object... inParams) {

        //获取CommonMirrorData
        //匹配
//        CommonMirrorData data = DoomResourceObjectPool.getUtils().selectBestMatchSubCommonMockData(methodReferenceUri, commonDatas, inParams);
//        if (paramObject != null) {
//            replaceParamObject(paramObject, data.getParamObject());
//        }

        //返回paramObject
//        return data.getResult();
        String readNIO = FileUtils.readNIO("./traces/mirror/" + PluginContext.getTraceId() + ".txt");

        readNIO=readNIO.replace("\"@type\":\"io.manbang.easybytecoder.plugin.mybatisrecord.runtime.model.CommonMirrorData\",","");
        CommonMirrorData data = EasyByteCoderResourceObjectPool.getJsonSerializer().deserialize(readNIO, CommonMirrorData.class);

        //CommonMirrorData data = EasyByteCoderResourceObjectPool.getGsonSerializer().deserialize(readNIO,CommonMirrorData.class);

        return data.getResult();
    }

}
