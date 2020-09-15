package io.manbang.easybytecoder.plugin.mybatismock.runtime.utils;

import io.manbang.easybytecoder.runtimecommonapi.utils.EasyByteCoderResourceObjectPool;
import io.manbang.easybytecoder.runtimecommonapi.utils.StringUtils;
import org.apache.ibatis.logging.jdbc.PreparedStatementLogger;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;


public class SqlDataGetterUtil {

    public static final String IBATIS_PREPARED_STATEMENT = "org.apache.ibatis.logging.jdbc.PreparedStatementLogger";

    public static final String CJ_JDBC_PREPARED_STATEMENT = "com.mysql.cj.jdbc.ClientPreparedStatement";
    public static String getMapperSql(Statement proxy) {
        try {
            if (proxy.toString().contains(IBATIS_PREPARED_STATEMENT)) {
                return getStmtSql((Proxy) proxy);
            }
            if (proxy.toString().contains(CJ_JDBC_PREPARED_STATEMENT)) {
                int index = proxy.toString().indexOf(":");
                String left = proxy.toString().substring(index + 1);
                return left;
            }

        } catch (Throwable e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("NewMybatisRecorder error for getMapperSql:", e);
        }
        return "";
    }


    public static Object getSqlParams(Object proxy) {
        try {

            Field paramsField = proxy.getClass().getDeclaredField("params");
            paramsField.setAccessible(true);
            Object params = paramsField.get(proxy);
            return params;


        } catch (Throwable e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("NewMybatisRecorder error for getSqlParams:", e);
        }
        return Collections.emptyList();
    }

    private static String getStmtSql(Proxy proxy) {
        try {
            Field h = Proxy.class.getDeclaredField("h");
            h.setAccessible(true);
            PreparedStatementLogger stmt = (PreparedStatementLogger) h.get(proxy);


            String sql = "";
            if (stmt.getPreparedStatement() instanceof PreparedStatement) {
                PreparedStatement statement = stmt.getPreparedStatement();
                Field field = statement.getClass().getDeclaredField("sql");
                field.setAccessible(true);
                sql = (String) field.get(statement);
            }
            return sql;
        } catch (Exception e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("GroupPreparedStatement getStmtSql failed for: mybatis:" + proxy.getClass().getName());

            return StringUtils.EMPTY;
        }
    }

    public static String getStmtLogClassName(Statement proxy) {
        if (proxy instanceof Proxy && proxy.toString().contains(IBATIS_PREPARED_STATEMENT)) {
            try {
                Field h = Proxy.class.getDeclaredField("h");
                h.setAccessible(true);
                PreparedStatementLogger stmt = (PreparedStatementLogger) h.get(proxy);
                Field statementLog = stmt.getClass().getSuperclass().getDeclaredField("statementLog");
                statementLog.setAccessible(true);
                Object object = statementLog.get(stmt);
                Field logfi = statementLog.get(stmt).getClass().getDeclaredField("log");
                logfi.setAccessible(true);
                Object object2 = logfi.get(object);
                Field loggerfi = object2.getClass().getDeclaredField("logger");
                loggerfi.setAccessible(true);
                org.slf4j.spi.LocationAwareLogger s = (org.slf4j.spi.LocationAwareLogger) loggerfi.get(object2);
                return s.getName();
            } catch (Exception e) {
                EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("GroupPreparedStatement getStmtLogClassName failed for: mybatis:" + proxy.getClass().getName());
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * 替换调用DAO的方法参数，主要目的是为了给参数带上主键值
     *
     * @param parameterObject
     * @param originalParamObject
     */
    public static void replaceParamObject(Object parameterObject, Object originalParamObject) {
        // 如果是parameterObject是map类型，说明参数是一个集合
        if (parameterObject.getClass().equals(DefaultSqlSession.StrictMap.class)) {
            DefaultSqlSession.StrictMap strictMap = (DefaultSqlSession.StrictMap) parameterObject;
            if (strictMap.containsKey("collection")) {
                Collection collection = (Collection) strictMap.get("collection");
                Object[] objects = collection.toArray();
                DefaultSqlSession.StrictMap originalParamStrictMap = (DefaultSqlSession.StrictMap) originalParamObject;
                Collection originalObject = (Collection) originalParamStrictMap.get("collection");
                Object[] originalObjects = originalObject.toArray();
                for (int i = 0; i < objects.length; i++) {
                    BeanUtils.copyProperties(originalObjects[i], objects[i]);
                }
            } else if (strictMap.containsKey("array")) {
                Collection collection = (Collection) strictMap.get("array");
                Object[] objects = collection.toArray();
                DefaultSqlSession.StrictMap originalParamStrictMap = (DefaultSqlSession.StrictMap) originalParamObject;
                Collection originalObject = (Collection) originalParamStrictMap.get("array");
                Object[] originalObjects = originalObject.toArray();
                for (int i = 0; i < objects.length; i++) {
                    BeanUtils.copyProperties(originalObjects[i], objects[i]);
                }
            }
        } else {
            BeanUtils.copyProperties(originalParamObject, parameterObject);
        }
    }
}
