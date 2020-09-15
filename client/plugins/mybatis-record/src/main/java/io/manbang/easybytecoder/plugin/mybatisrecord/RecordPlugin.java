package io.manbang.easybytecoder.plugin.mybatisrecord;

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
@ModifyClassName("org/apache/ibatis/executor/statement/PreparedStatementHandler")
@ResourceToImport({"io.manbang.easybytecoder.plugin.mybatisrecord.runtime.NewMybatisRecorder",
        "io.manbang.easybytecoder.traffichandler.utils.HessianSerializer"})
public class RecordPlugin  implements TrafficHandler {


    @ModifyMethod(methodName = "query",paramDecl = {"java.sql.Statement","org.apache.ibatis.session.ResultHandler"},
            checkParam = true ,pattern = CodePatternEnum.After)
    public  String recordQuery(){
        return "NewMybatisRecorder.handleQueryRecord($1, $_);";
    }
    @ModifyMethod(methodName = "update",paramDecl = {"java.sql.Statement","org.apache.ibatis.session.ResultHandler"},
            checkParam = true ,pattern = CodePatternEnum.After)
    public String recordUpdate(){
        return "NewMybatisRecorder.handleUpdateRecord($0, $1, $_);";
    }
}
