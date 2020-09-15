package io.manbang.easybytecoder.plugin.mybatismock;

import com.google.auto.service.AutoService;

import io.manbang.easybytecoder.traffichandler.TrafficHandler;
import io.manbang.easybytecoder.traffichandler.annotation.ModifyClassName;
import io.manbang.easybytecoder.traffichandler.annotation.ModifyMethod;
import io.manbang.easybytecoder.traffichandler.annotation.ResourceToImport;
import io.manbang.easybytecoder.traffichandler.annotation.constant.CodePatternEnum;

/**
 * @author xujie
 */
@AutoService(TrafficHandler.class)
@ResourceToImport({"io.manbang.easybytecoder.plugin.mybatismock.runtime.NewMybatisMocker",
        "io.manbang.easybytecoder.plugin.restmock.runtime.PluginContext"})
@ModifyClassName("org/apache/ibatis/executor/statement/PreparedStatementHandler")
public class MockPlugin implements TrafficHandler {

    @ModifyMethod(methodName = "update", paramDecl = {"java.sql.Statement"}, checkParam = true, pattern = CodePatternEnum.Before)
    public String updateMock() {

        return "   if(PluginContext.getTraceId()!=null){\n" +
                "            return NewMybatisMocker.getUpdateResponse($0, $1);\n" +
                "        }";
    }

    @ModifyMethod(methodName = "query", paramDecl = {"java.sql.Statement", "org.apache.ibatis.session.ResultHandler"}, checkParam = true)
    public String queryMock() {

        return "     if (PluginContext.getTraceId()!=null) {\n" +
                "            return NewMybatisMocker.getQueryResponse($1);\n" +
                "        }";
    }

}
