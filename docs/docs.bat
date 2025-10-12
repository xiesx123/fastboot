@echo off
REM 设置 UTF-8 编码防止中文乱码
chcp 65001 > nul

REM 检查第一个参数是否存在
if "%1"=="" (
    set "Mode=release"
) else (
    set "Mode=%1"
)

REM 获取当前目录名称
for %%I in (.) do set "CurrentFolder=%%~nI"

REM 判断是否在 docs 目录
if /I "%CurrentFolder%" NEQ "docs" (
    if exist "docs" (
        cd docs
    ) else (
        echo 当前目录下未找到 docs 文件夹！
        pause
        exit /b
    )
)

REM 根据参数执行命令
if /I "%Mode%"=="dev" (
    npm run docs:dev
) else if /I "%Mode%"=="build" (
    npm run docs:build
)  else if /I "%Mode%"=="preview" (
    npm run docs:build
    npm run docs:preview
) else if /I "%Mode%"=="release" (
    npm run docs:release
) else (
    echo 无效参数：%Mode%
    echo 请使用 dev 或 release 作为参数
    pause
    exit /b
)