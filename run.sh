#!/usr/bin/env bash
# ==============================================================================
# BlueMoon BFMS - 1-Click Start Script for Linux / macOS / Git Bash
# Tu dong doc cac bien tu file .env va khoi dong Spring Boot
# ==============================================================================

if [ -f .env ]; then
    echo "[BlueMoon] Da tim thay file .env, dang nap bien moi truong..."
    export $(grep -v '^#' .env | xargs)
else
    echo "[BlueMoon] Khong tim thay file .env, su dung cau hinh mac dinh."
fi

echo "[BlueMoon] Dang khoi dong ung dung Spring Boot..."
./mvnw spring-boot:run
