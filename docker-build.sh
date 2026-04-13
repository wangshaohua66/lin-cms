#!/bin/bash
# Docker 构建脚本 - 单容器方案
# 用于在 Linux 系统下构建和运行 Lin CMS 项目

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  Lin CMS Docker 构建脚本 (单容器)${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 检查 Docker 是否安装
if ! command -v docker &> /dev/null; then
    echo -e "${RED}错误: Docker 未安装${NC}"
    echo "请先安装 Docker: https://docs.docker.com/get-docker/"
    exit 1
fi

# 检查 Docker Compose 是否安装
if ! command -v docker-compose &> /dev/null; then
    echo -e "${YELLOW}警告: Docker Compose 未安装，尝试使用 docker compose${NC}"
    DOCKER_COMPOSE="docker compose"
else
    DOCKER_COMPOSE="docker-compose"
fi

# 显示菜单
echo "请选择操作:"
echo "1) 构建并启动服务"
echo "2) 仅构建镜像"
echo "3) 启动服务"
echo "4) 停止服务"
echo "5) 查看日志"
echo "6) 清理容器和镜像"
echo "7) 使用纯 Docker 运行（不使用 Compose）"
echo "q) 退出"
echo ""
read -p "请输入选项 [1-7/q]: " choice

case $choice in
    1)
        echo -e "${YELLOW}正在构建并启动服务...${NC}"
        $DOCKER_COMPOSE down
        $DOCKER_COMPOSE build --no-cache
        $DOCKER_COMPOSE up -d
        echo ""
        echo -e "${GREEN}服务已启动!${NC}"
        echo -e "前端访问: ${GREEN}http://localhost${NC}"
        echo -e "后端 API: ${GREEN}http://localhost:5000${NC}"
        ;;
    2)
        echo -e "${YELLOW}正在构建镜像...${NC}"
        $DOCKER_COMPOSE build --no-cache
        echo -e "${GREEN}镜像构建完成!${NC}"
        ;;
    3)
        echo -e "${YELLOW}正在启动服务...${NC}"
        $DOCKER_COMPOSE up -d
        echo ""
        echo -e "${GREEN}服务已启动!${NC}"
        echo -e "前端访问: ${GREEN}http://localhost${NC}"
        echo -e "后端 API: ${GREEN}http://localhost:5000${NC}"
        ;;
    4)
        echo -e "${YELLOW}正在停止服务...${NC}"
        $DOCKER_COMPOSE down
        echo -e "${GREEN}服务已停止!${NC}"
        ;;
    5)
        echo -e "${YELLOW}查看日志...${NC}"
        $DOCKER_COMPOSE logs -f
        ;;
    6)
        echo -e "${YELLOW}正在清理容器和镜像...${NC}"
        $DOCKER_COMPOSE down -v --rmi all
        echo -e "${GREEN}清理完成!${NC}"
        ;;
    7)
        echo -e "${YELLOW}使用纯 Docker 运行...${NC}"
        docker build -f Dockerfile.build -t lin-cms:latest .
        docker run -d \
            --name lin-cms \
            -p 80:80 \
            -p 5000:5000 \
            --restart unless-stopped \
            lin-cms:latest
        echo -e "${GREEN}容器已启动!${NC}"
        echo -e "前端访问: ${GREEN}http://localhost${NC}"
        echo -e "后端 API: ${GREEN}http://localhost:5000${NC}"
        ;;
    q|Q)
        echo "退出"
        exit 0
        ;;
    *)
        echo -e "${RED}无效选项${NC}"
        exit 1
        ;;
esac
