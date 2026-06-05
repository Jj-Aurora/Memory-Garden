#!/usr/bin/env bash
# ============================================================
# 记忆花园 (Memory Garden) - 构建脚本 (Linux/macOS)
# ============================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DEPLOY_DIR="$(dirname "$SCRIPT_DIR")"
PROJECT_DIR="$(dirname "$DEPLOY_DIR")"

GREEN='\033[0;32m'
NC='\033[0m'
log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }

# 1. 构建前端
log_info "Building frontend..."
cd "$PROJECT_DIR/memory-garden-web"
[ ! -d "node_modules" ] && npm ci
npx vite build

# 2. 构建后端
log_info "Building backend..."
cd "$PROJECT_DIR/memory-garden-server"
mvn clean package -DskipTests -B

# 3. 清理并重建 deploy 目录
log_info "Assembling deploy directory..."
rm -rf "$DEPLOY_DIR/backend" "$DEPLOY_DIR/frontend"
mkdir -p "$DEPLOY_DIR/backend" "$DEPLOY_DIR/frontend"

# 后端 JAR
cp "$PROJECT_DIR/memory-garden-server/target/memory-garden-server-1.0.0.jar" "$DEPLOY_DIR/backend/"

# 前端静态资源
cp -r "$PROJECT_DIR/memory-garden-web/dist/"* "$DEPLOY_DIR/frontend/"

# SQL
cp "$PROJECT_DIR/memory-garden-server/src/main/resources/db/memory_garden.sql" "$DEPLOY_DIR/sql/"

# 生产配置
cp "$PROJECT_DIR/memory-garden-server/src/main/resources/application-prod.yml" "$DEPLOY_DIR/config/"

log_info "Build complete. Deploy directory: $DEPLOY_DIR/"
