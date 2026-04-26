#!/bin/bash

# 驾校管理系统后端重启脚本

APP_NAME="dsa-system"
JAR_FILE="dsa-system-0.0.1-SNAPSHOT.jar"
APP_DIR="$HOME/dsa-system"
PID_FILE="$APP_DIR/app.pid"
LOG_FILE="$APP_DIR/app.log"

# 切换到应用目录
cd $APP_DIR

# 检查是否已有进程在运行
if [ -f $PID_FILE ]; then
    OLD_PID=$(cat $PID_FILE)
    if ps -p $OLD_PID > /dev/null; then
        echo "Stopping old process (PID: $OLD_PID)..."
        kill $OLD_PID
        sleep 5
        # 如果进程还在运行，强制杀死
        if ps -p $OLD_PID > /dev/null; then
            echo "Force killing process..."
            kill -9 $OLD_PID
        fi
    fi
fi

# 备份旧的JAR文件
if [ -f $JAR_FILE ]; then
    BACKUP_FILE="${JAR_FILE}.backup.$(date +%Y%m%d_%H%M%S)"
    echo "Backing up old JAR to $BACKUP_FILE"
    cp $JAR_FILE $BACKUP_FILE
fi

# 启动新的应用
echo "Starting new application..."
nohup java -jar $JAR_FILE > $LOG_FILE 2>&1 &
NEW_PID=$!

# 保存PID
echo $NEW_PID > $PID_FILE

echo "Application started with PID: $NEW_PID"
echo "Log file: $LOG_FILE"

# 等待几秒检查应用是否成功启动
sleep 10
if ps -p $NEW_PID > /dev/null; then
    echo "Application is running successfully!"
else
    echo "ERROR: Application failed to start. Check log file: $LOG_FILE"
    exit 1
fi
