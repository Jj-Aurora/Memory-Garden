#!/usr/bin/env bash
# ============================================================
# 记忆花园 (Memory Garden) - 启动脚本 (Linux/macOS)
# ============================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 加载环境变量
if [ -f "$SCRIPT_DIR/.env" ]; then
    set -a; source "$SCRIPT_DIR/.env"; set +a
    echo "[INFO] Loaded .env file"
else
    echo "[WARN] .env file not found, using defaults or system environment variables"
fi

# 必需环境变量检查
MISSING=0
if [ -z "${JWT_SECRET:-}" ]; then
    echo "[ERROR] JWT_SECRET is required"
    MISSING=1
fi
if [ -z "${DB_PASSWORD:-}" ]; then
    echo "[ERROR] DB_PASSWORD is required"
    MISSING=1
fi
if [ "$MISSING" -eq 1 ]; then
    echo ""
    echo "Copy .env.example to .env and fill in the values:"
    echo "  cp $SCRIPT_DIR/.env.example $SCRIPT_DIR/.env"
    exit 1
fi

# 默认值
SERVER_PORT="${SERVER_PORT:-8080}"
DB_URL="${DB_URL:-jdbc:mysql://localhost:3306/memory_garden?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true}"
DB_USERNAME="${DB_USERNAME:-root}"

echo "[INFO] Starting Memory Garden on port $SERVER_PORT..."

exec java \
    -Xms256m -Xmx512m \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active=prod \
    -Dserver.port="$SERVER_PORT" \
    -Dspring.config.additional-location="file:$SCRIPT_DIR/config/" \
    -DJWT_SECRET="$JWT_SECRET" \
    -DDB_URL="$DB_URL" \
    -DDB_USERNAME="$DB_USERNAME" \
    -DDB_PASSWORD="$DB_PASSWORD" \
    -jar "$SCRIPT_DIR/backend/memory-garden-server-1.0.0.jar"
