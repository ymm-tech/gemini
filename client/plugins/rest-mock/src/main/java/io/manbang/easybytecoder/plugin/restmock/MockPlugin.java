package io.manbang.easybytecoder.plugin.restmock;

import com.google.auto.service.AutoService;
import io.manbang.easybytecoder.traffichandler.TrafficHandler;
import io.manbang.easybytecoder.traffichandler.annotation.ModifyClassName;
import io.manbang.easybytecoder.traffichandler.annotation.ModifyMethod;
import io.manbang.easybytecoder.traffichandler.annotation.ResourceToImport;

@AutoService(TrafficHandler.class)
@ModifyClassName("javax/servlet/http/HttpServlet")
@ResourceToImport({"javax.servlet.http.HttpServletResponse","io.manbang.easybytecoder.plugin.restmock.runtime.PluginContext"})
public class MockPlugin implements TrafficHandler {

    @ModifyMethod(methodName = "service",
            paramDecl = {"javax.servlet.http.HttpServletRequest","javax.servlet.http.HttpServletResponse"},
            checkParam = true)
    public String mockHttp() {
        return " PluginContext.setTraceId($1.getHeader(\"traceId\"));";
    }

}
