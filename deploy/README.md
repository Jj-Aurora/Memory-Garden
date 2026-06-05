# 记忆花园 - 部署包

本目录包含记忆花园系统的完整部署资源，可直接用于生产环境部署。

## 目录结构

```
deploy/
├── backend/                            # 后端可执行文件
│   └── memory-garden-server-1.0.0.jar  # Spring Boot 可执行 JAR
│
├── frontend/                           # 前端静态资源
│   ├── index.html                      # 入口页面
│   └── assets/                         # JS/CSS/SVG 等静态资源
│
├── sql/                                # 数据库脚本
│   └── memory_garden.sql               # 初始化脚本（建表+基础数据）
│
├── config/                             # 配置文件
│   ├── application-prod.yml            # 后端生产环境配置
│   └── nginx.conf                      # Nginx 反向代理配置模板
│
├── docker/                             # Docker 部署文件
│   ├── Dockerfile                      # 多阶段构建镜像
│   ├── docker-compose.yml              # 容器编排（含 MySQL）
│   └── .dockerignore                   # Docker 忽略文件
│
├── scripts/                            # 构建脚本
│   ├── build.sh                        # Linux/macOS 重新构建
│   └── build.ps1                       # Windows 重新构建
│
├── start.sh                            # 启动脚本 (Linux/macOS)
├── start.bat                           # 启动脚本 (Windows)
├── .env.example                        # 环境变量模板
└── README.md                           # 本文件
```

## 快速部署

### 方式一：手动部署

```bash
# 1. 配置环境变量
cp .env.example .env
# 编辑 .env，填入 JWT_SECRET 和 DB_PASSWORD

# 2. 初始化数据库
mysql -u root -p -e "CREATE DATABASE memory_garden DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -u root -p memory_garden < sql/memory_garden.sql

# 3. 启动后端
# Linux/macOS:
chmod +x start.sh && ./start.sh
# Windows:
start.bat

# 4. 部署前端到 Nginx
# 将 frontend/ 目录复制到 Nginx 的 html 目录
# 使用 config/nginx.conf 配置反向代理
```

### 方式二：Docker 部署

```bash
# 1. 配置环境变量
cp .env.example .env
# 编辑 .env，填入 JWT_SECRET 和 DB_PASSWORD

# 2. 启动服务（含 MySQL）
cd docker/
docker compose --env-file ../.env up -d

# 3. 查看日志
docker compose logs -f app
```

## 环境变量

| 变量名 | 必需 | 默认值 | 说明 |
|--------|------|--------|------|
| `JWT_SECRET` | 是 | 无 | JWT 签名密钥，至少 32 字符 |
| `DB_PASSWORD` | 是 | 无 | MySQL 数据库密码 |
| `DB_URL` | 否 | `jdbc:mysql://localhost:3306/memory_garden?...` | 数据库连接 URL |
| `DB_USERNAME` | 否 | `root` | 数据库用户名 |
| `SERVER_PORT` | 否 | `8080` | 后端服务端口 |
| `SWAGGER_ENABLE` | 否 | `false` | 是否启用 API 文档 |

## 重新构建

如需从源码重新构建部署包：

```bash
# Linux/macOS
chmod +x scripts/build.sh && scripts/build.sh

# Windows
scripts\build.ps1
```

## 端口说明

| 服务 | 默认端口 | 说明 |
|------|---------|------|
| 后端 API | 8080 | Spring Boot 服务 |
| 前端页面 | 80 | Nginx 代理 |
| MySQL | 3306 | 数据库（Docker 部署时映射为 3307） |

## 注意事项

1. **JWT_SECRET** 必须设置为强随机密钥，切勿使用默认值
2. **DB_PASSWORD** 生产环境必须设置强密码
3. `.env` 文件包含敏感信息，不应提交到版本控制
4. 前端部署需配置 Nginx 将 `/api` 请求代理到后端 8080 端口
5. Docker 部署时数据库主机名使用 `mysql`（容器名），手动部署使用 `localhost`
