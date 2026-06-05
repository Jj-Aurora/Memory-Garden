# ============================================================
# 多阶段构建 Dockerfile - 记忆花园 (Memory Garden)
# 阶段1: 前端构建 (Node.js)
# 阶段2: 后端构建 (Maven + JDK 8)
# 阶段3: 运行时镜像 (JRE 8, 非root用户, 无源码/构建工具)
# ============================================================

# -------------------- 阶段1: 前端构建 --------------------
FROM node:18-alpine AS frontend-builder

WORKDIR /build/web
COPY memory-garden-web/package.json memory-garden-web/package-lock.json ./
# 依赖缓存：仅 package.json 变化时重新安装
RUN npm ci
COPY memory-garden-web/ ./
RUN npx vite build

# -------------------- 阶段2: 后端构建 --------------------
FROM maven:3.8-openjdk-8 AS backend-builder

WORKDIR /build/server
COPY memory-garden-server/pom.xml ./
# 依赖缓存：仅 pom.xml 变化时重新下载依赖
RUN mvn dependency:go-offline -B
COPY memory-garden-server/src ./src
RUN mvn clean package -DskipTests -B

# -------------------- 阶段3: 运行时 --------------------
FROM openjdk:8-jre-alpine

LABEL maintainer="jLU"
LABEL description="Memory Garden - Gamified Spaced Repetition Learning System"

# 安装 curl 用于健康检查，然后清理
RUN apk add --no-cache curl && \
    rm -rf /var/cache/apk/*

# 创建非 root 用户运行应用
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# 创建应用目录
WORKDIR /app

# 从构建阶段复制产物（不含源码和构建工具）
COPY --from=backend-builder /build/server/target/memory-garden-server-1.0.0.jar ./app.jar

# 前端静态资源复制到 /dist
COPY --from=frontend-builder /build/web/dist ./dist

# 创建外部配置目录（用于挂载配置文件）
RUN mkdir -p /app/config && chown -R appuser:appgroup /app

# 切换到非 root 用户
USER appuser

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/api/user/login || exit 1

# 启动命令：激活 prod profile，支持外部配置覆盖
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=prod", \
    "-Dspring.config.additional-location=file:/app/config/", \
    "-jar", "app.jar"]
