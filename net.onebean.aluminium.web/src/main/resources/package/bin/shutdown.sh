#!/bin/sh

#======================================================================
# 项目停服shell脚本
# 通过项目名称查找到PID
# 然后kill -9 pid
#
# author: Heisenberg
# date: 2018-12-2
#======================================================================

# 项目名称
APPLICATION="@project.name@"

# 进入bin目录
cd `dirname $0`
# 返回到上一级项目根目录路径
cd ..
# 打印项目根目录绝对路径
# `pwd` 执行系统命令并获得结果
BASE_PATH=`pwd`

PID=$(ps -ef | grep "${BASE_PATH}" | grep -v grep | grep -v .sh |awk '{ print $2 }')
if [[ -z "$PID" ]]
then
    echo ${APPLICATION} is already stopped
else
    echo kill  ${PID}
    kill -9 ${PID}
    echo ${APPLICATION} stopped successfully
fi