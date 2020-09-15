package io.manbang.easybytecoder.plugin.replaytask;

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
        "io.manbang.easybytecoder.plugin.replaytask.runtime.Task",
        "io.manbang.easybytecoder.plugin.replaytask.runtime.model.RESTMirrorData",
        "javax.servlet.http.HttpServletResponse",
        "javax.servlet.http.HttpServletRequest"
})
@ModifyClassName("org/springframework/core/io/DefaultResourceLoader")
public class ReplayPlugin implements TrafficHandler {
    @ModifyMethod(methodName="clearResourceCaches",pattern= CodePatternEnum.Before)
    public String modifyAfter() {
        return  "new Task().replayTask();";
    }

}
