#!/bin/bash

### 修改自己的目的参数
name=/Users/xujie/work/ymm/jar
currd=$(cd `dirname $0`; pwd)

function clearFile() {
    for file in `ls $1`
        do
            if [ -d $1"/"$file  ];   then
                echo "deleting directory: " $1"/"$file
                rm -ef $1"/"$file
            else
                echo "deleting file: " $1"/"$file
                rm -rf $1"/"$file
            fi
        done
}

echo $name
echo $currd

clearFile $name


### 修改成自己的地址与包名
cp $currd/common/target/common-1.0.0-jar-with-dependencies.jar $name/common.jar
cp $currd/client/agent/target/agent-1.0.0-jar-with-dependencies.jar $name/agent.jar
cp $currd/client/client-bootstrap/target/client-bootstrap-1.0.0-jar-with-dependencies.jar $name/bootstrap.jar
cp $currd/client/plugins/rest-record/target/rest-record-1.0.0-jar-with-dependencies.jar $name/restrecord.jar
cp $currd/client/plugins/replay-task/target/replay-task-1.0.0-jar-with-dependencies.jar $name/replaytask.jar
cp $currd/client/plugins/rest-mock/target/rest-mock-1.0.0-jar-with-dependencies.jar $name/restmock.jar
cp $currd/client/plugins/mybatis-record/target/mybatis-record-1.0.0-jar-with-dependencies.jar $name/mybatisrecord.jar
cp $currd/client/plugins/mybatis-mock/target/mybatis-mock-1.0.0-jar-with-dependencies.jar $name/mybatismock.jar

