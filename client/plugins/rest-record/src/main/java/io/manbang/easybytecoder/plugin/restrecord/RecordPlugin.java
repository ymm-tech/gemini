package io.manbang.easybytecoder.plugin.restrecord;

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
@ResourceToImport({
        "io.manbang.easybytecoder.runtimecommonapi.utils.EasyByteCoderResourceObjectPool",
        "io.manbang.easybytecoder.plugin.restrecord.runtime.ResponseInterceptorWrapper",
        "io.manbang.easybytecoder.plugin.restrecord.runtime.RequestInterceptorWrapper",
        "io.manbang.easybytecoder.plugin.restrecord.runtime.PluginContext",
        "io.manbang.easybytecoder.plugin.restrecord.runtime.ServletRecordHandler",
        "javax.servlet.http.HttpServletResponse",
        "javax.servlet.http.HttpServletRequest"
})
@ModifyClassName("javax/servlet/http/HttpServlet")
public class RecordPlugin implements TrafficHandler {


    @ModifyMethod(methodName="service",paramDecl={"javax.servlet.http.HttpServletRequest","javax.servlet.http.HttpServletResponse"},checkParam = true)
    public String modifyBefore() {

        return  "PluginContext.setTraceId();" +
                "if ( PluginContext.getTraceId() != null ) { " +
                "$2 = new ResponseInterceptorWrapper((HttpServletResponse)$2);" +
                "$1 = new RequestInterceptorWrapper((HttpServletRequest)$1);}";
    }

    @ModifyMethod(methodName="service",paramDecl={"javax.servlet.http.HttpServletRequest","javax.servlet.http.HttpServletResponse"},
            checkParam = true ,pattern= CodePatternEnum.After)
    public String modifyAfter() {
        return  "ServletRecordHandler.handleAfter($1, $2);";
    }

}
