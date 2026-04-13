# Docker 部署指南 - 单容器方案

本文档介绍如何在 Linux 系统下使用 Docker 单容器方案部署 Lin CMS 项目。

## 项目结构

```
lin-cms/
├── server/              # 后端项目 (Spring Boot)
├── web/                 # 前端项目 (Vue 3)
├── Dockerfile.build     # 单容器 Dockerfile（前后端合一）
├── docker-compose.yml   # Docker Compose 配置
├── docker-build.sh      # 构建脚本
└── DOCKER.md           # 本文档
```

## 环境要求

- Docker 20.10+
- Docker Compose 2.0+（可选）
- Linux 操作系统 (Ubuntu/CentOS/Debian 等)

## 快速开始

### 方法一：使用 Docker Compose（推荐）

1. **进入项目目录**
   ```bash
   cd lin-cms
   ```

2. **使用构建脚本**
   ```bash
   chmod +x docker-build.sh
   ./docker-build.sh
   # 选择选项 1: 构建并启动服务
   ```

3. **或手动执行**
   ```bash
   # 构建并启动
   docker-compose up -d --build
   
   # 查看日志
   docker-compose logs -f
   ```

4. **访问应用**
   - 前端: http://localhost
   - 后端 API: http://localhost:5000
   - H2 Console: http://localhost:5000/h2-console

### 方法二：使用纯 Docker

```bash
# 构建镜像
docker build -f Dockerfile.build -t lin-cms:latest .

# 运行容器
docker run -d \
  --name lin-cms \
  -p 80:80 \
  -p 5000:5000 \
  --restart unless-stopped \
  lin-cms:latest
```

## 常用命令

### Docker Compose 命令

```bash
# 启动服务
docker-compose up -d

# 停止服务
docker-compose down

# 查看日志
docker-compose logs -f

# 重启服务
docker-compose restart

# 重新构建
docker-compose up -d --build

# 清理容器和卷
docker-compose down -v
```

### Docker 命令

```bash
# 查看运行中的容器
docker ps

# 查看所有容器
docker ps -a

# 进入容器内部
docker exec -it lin-cms /bin/sh

# 查看容器日志
docker logs lin-cms

# 停止容器
docker stop lin-cms

# 删除容器
docker rm lin-cms

# 删除镜像
docker rmi lin-cms:latest
```

## 配置说明

### 单容器 Dockerfile 特点

- **基础镜像**: `eclipse-temurin:21-jre-alpine`（运行阶段）
- **工作目录**: `/app`
- **暴露端口**: 
  - `80`: 前端 Nginx 服务
  - `5000`: 后端 Spring Boot 服务
- **非 root 用户**: `appuser:appgroup`
- **健康检查**: 每 30 秒检查后端服务

### 构建阶段

1. **后端构建**: 使用 `eclipse-temurin:21-jdk-alpine` + Maven
2. **前端构建**: 使用 `node:18-alpine`
3. **运行阶段**: 使用 `eclipse-temururin:21-jre-alpine` + Nginx

### Nginx 配置

```nginx
server {
    listen 80;
    
    # 前端静态文件
    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
    }
    
    # 后端 API 代理
    location /cms {
        proxy_pass http://127.0.0.1:5000;
    }
}
```

## 生产环境部署

### 1. 修改配置

编辑 `docker-compose.yml`：

```yaml
services:
  lin-cms:
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    restart: always
    # 可选：限制资源
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
```

### 2. 使用外部数据库（可选）

如需使用 MySQL 替代 H2，修改 `server/src/main/resources/application-prod.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://mysql:3306/lin-cms
    username: root
    password: your-password
```

然后更新 `docker-compose.yml`：

```yaml
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: your-password
      MYSQL_DATABASE: lin-cms
    volumes:
      - mysql-data:/var/lib/mysql
    
  lin-cms:
    depends_on:
      - mysql
```

### 3. 使用 Nginx 反向代理（推荐）

生产环境建议在前端再加一层 Nginx：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        proxy_pass http://localhost:80;
    }
    
    location /cms {
        proxy_pass http://localhost:5000;
    }
}
```

## 故障排查

### 1. 端口被占用

```bash
# 查看端口占用
sudo lsof -i :80
sudo lsof -i :5000

# 停止占用端口的进程
sudo kill -9 <PID>
```

### 2. 容器无法启动

```bash
# 查看容器日志
docker logs lin-cms

# 进入容器检查
docker exec -it lin-cms /bin/sh

# 检查服务状态
docker exec lin-cms ps aux
```

### 3. 内存不足

```bash
# 查看 Docker 内存使用
docker system df

# 清理未使用的资源
docker system prune -a
```

## 性能优化

### 1. JVM 参数优化

编辑 `Dockerfile.build`，修改启动命令：

```dockerfile
ENTRYPOINT ["java", \
    "-Xms512m", \
    "-Xmx1024m", \
    "-XX:+UseG1GC", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "/app/server.jar"]
```

### 2. Nginx 性能优化

在 `Dockerfile.build` 中添加：

```nginx
worker_processes auto;
worker_connections 1024;
gzip on;
gzip_types text/plain text/css application/json application/javascript;
```

## 安全建议

1. **修改默认密码**: 修改 `root` 用户的默认密码
2. **使用 HTTPS**: 生产环境配置 SSL 证书
3. **限制端口访问**: 使用防火墙限制端口访问
4. **定期更新**: 定期更新基础镜像和依赖

## 默认账号

- **用户名**: `root`
- **密码**: `123456`

**注意**: 生产环境请务必修改默认密码！

## 技术支持

如有问题，请查看：
- 容器日志: `docker logs lin-cms`
- 项目文档: `server/README.md` 和 `web/README.md`
