#!/bin/bash

# 全 VPS 部署脚本

echo "开始部署到 VPS..."

# 1. 拉取最新代码
echo "拉取最新代码..."
cd ~/dsa-system
git fetch --all
git reset --hard origin/master
git clean -fd
git pull origin master

# 2. 构建前端
echo "构建前端..."
cd frontend
npm install
npm run build

# 3. 复制前端文件到 web 目录
echo "复制前端文件..."
mkdir -p /var/www/dsa-system
rm -rf /var/www/dsa-system/*
cp -r ~/dsa-system/frontend/dist/* /var/www/dsa-system/

# 4. 重启后端
echo "重启后端..."
cd ~/dsa-system
pkill -f dsa-system
chmod +x ./restart-app.sh
./restart-app.sh

# 5. 重启 Nginx
echo "重启 Nginx..."
systemctl reload nginx

echo "部署完成！"
