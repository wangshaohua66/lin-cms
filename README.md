# Lin CMS 全栈项目

这是一个前后端整合的全栈 CMS 项目，后端基于 Spring Boot，前端基于 Vue 3 + Element Plus。
## 默认登录账号密码
- 账号：root
- 密码：123456

## 项目结构

```
fullstack/
├── server/          # 后端项目 (Spring Boot)
│   ├── src/         # 源代码
│   ├── pom.xml      # Maven 配置
│   └── README.md    # 后端说明文档
│
├── web/             # 前端项目 (Vue 3)
│   ├── src/         # 源代码
│   ├── public/      # 静态资源
│   ├── package.json # NPM 配置
│   └── README.md    # 前端说明文档
│
└── README.md        # 本文件
```

## 快速开始

### 环境要求

- Java 8+
- Maven 3.6+
- Node.js 14+
- MySQL 5.7+

### 1. 启动后端服务

```bash
cd server
mvn spring-boot:run
```

后端服务默认运行在 http://localhost:5000

### 2. 启动前端开发服务器

```bash
cd web
npm install
npm run serve
```

前端开发服务器默认运行在 http://localhost:8080

### 3. 访问应用

打开浏览器访问 http://localhost:8080

## 配置说明

### 后端配置

后端配置文件位于 `server/src/main/resources/`：
- `application.yml` - 主配置文件
- `application-dev.yml` - 开发环境配置
- `application-prod.yml` - 生产环境配置

**数据库配置**：修改 `application-dev.yml` 中的数据源配置：
```yaml
spring:
  datasource:
    username: "your-username"
    password: "your-password"
    url: jdbc:mysql://localhost:3306/lin-cms?useSSL=false&serverTimezone=UTC&characterEncoding=UTF8
```

### 前端配置

前端配置文件位于 `web/`：
- `.env.development` - 开发环境配置
- `.env.production` - 生产环境配置
- `vue.config.js` - Vue CLI 配置

**API 地址配置**：修改 `.env.development` 中的后端地址：
```
VUE_APP_BASE_URL = 'http://localhost:5000/'
```

## 构建部署

### 后端构建

```bash
cd server
mvn clean package
```

构建后的 JAR 文件位于 `server/target/` 目录。

### 前端构建

```bash
cd web
npm run build
```

构建后的静态文件位于 `web/dist/` 目录。

### 生产部署

1. 构建后端 JAR 包
2. 构建前端静态资源
3. 将前端 `dist` 目录内容部署到 Web 服务器或 CDN
4. 启动后端服务

## 技术栈

### 后端
- Spring Boot 2.5.2
- MyBatis Plus 3.4.1
- MySQL
- JWT 认证

### 前端
- Vue 3.2
- Element Plus 2.3.8
- Vue Router 4
- Vuex 4
- Axios

## 更多信息

- [后端详细文档](server/README.md)
- [前端详细文档](web/README.md)

## License

MIT
# lin-cms
