#!/bin/bash
# Docker Desktop 镜像加速器配置脚本

echo "正在配置 Docker Desktop 镜像加速器..."

# Docker Desktop on macOS 的配置文件位置
DOCKER_CONFIG="$HOME/.docker/daemon.json"

# 创建 .docker 目录（如果不存在）
mkdir -p "$HOME/.docker"

# 创建或更新 daemon.json
cat > "$DOCKER_CONFIG" << 'EOF'
{
  "builder": {
    "gc": {
      "defaultKeepStorage": "20GB",
      "enabled": true
    }
  },
  "experimental": false,
  "registry-mirrors": [
    "https://docker.mirrors.ustc.edu.cn",
    "https://hub-mirror.c.163.com",
    "https://mirror.baidubce.com",
    "https://ccr.ccs.tencentyun.com"
  ]
}
EOF

echo "配置文件已创建: $DOCKER_CONFIG"
echo ""
echo "配置内容:"
cat "$DOCKER_CONFIG"
echo ""
echo ""
echo "=========================================="
echo "重要: 请手动重启 Docker Desktop"
echo "=========================================="
echo ""
echo "重启步骤:"
echo "1. 点击屏幕顶部菜单栏的 Docker 图标"
echo "2. 选择 'Restart' 或 'Quit Docker Desktop'"
echo "3. 重新打开 Docker Desktop"
echo ""
echo "重启后，运行以下命令验证配置:"
echo "  docker info | grep -A 5 'Registry Mirrors'"
