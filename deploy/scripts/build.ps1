# 记忆花园 (Memory Garden) - 构建脚本 (Windows)
param()

$ErrorActionPreference = "Stop"
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectDir = Split-Path -Parent (Split-Path -Parent $ScriptDir)

function Write-Status($msg) { Write-Host "[INFO] $msg" -ForegroundColor Green }

# 1. 构建前端
Write-Status "Building frontend..."
Push-Location "$ProjectDir\memory-garden-web"
try {
    if (-not (Test-Path "node_modules")) { npm ci }
    npx vite build
} finally { Pop-Location }

# 2. 构建后端
Write-Status "Building backend..."
Push-Location "$ProjectDir\memory-garden-server"
try {
    mvn clean package -DskipTests -B
} finally { Pop-Location }

# 3. 清理并重建 deploy 目录
Write-Status "Assembling deploy directory..."
$DeployDir = Split-Path -Parent $ScriptDir
if (Test-Path "$DeployDir\backend") { Remove-Item -Recurse -Force "$DeployDir\backend" }
if (Test-Path "$DeployDir\frontend") { Remove-Item -Recurse -Force "$DeployDir\frontend" }
New-Item -ItemType Directory -Force -Path "$DeployDir\backend","$DeployDir\frontend" | Out-Null

# 后端 JAR
Copy-Item "$ProjectDir\memory-garden-server\target\memory-garden-server-1.0.0.jar" "$DeployDir\backend\"

# 前端静态资源
Copy-Item -Recurse "$ProjectDir\memory-garden-web\dist\*" "$DeployDir\frontend\"

# SQL
Copy-Item "$ProjectDir\memory-garden-server\src\main\resources\db\memory_garden.sql" "$DeployDir\sql\" -Force

# 生产配置
Copy-Item "$ProjectDir\memory-garden-server\src\main\resources\application-prod.yml" "$DeployDir\config\" -Force

Write-Status "Build complete. Deploy directory: $DeployDir\"
