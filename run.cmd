@echo off
REM ==============================================================================
REM BlueMoon BFMS - 1-Click Start Script for CMD / Windows Terminal
REM Tu dong doc cac bien tu file .env va khoi dong Spring Boot
REM ==============================================================================

if exist .env (
    echo [BlueMoon] Da tim thay file .env, dang nap bien moi truong...
    for /f "usebackq tokens=1,* delims==" %%A in (.env) do (
        if not "%%A"=="" (
            set "LINE=%%A"
            if not "!LINE:~0,1!"=="#" (
                set "%%A=%%B"
            )
        )
    )
)

echo [BlueMoon] Dang khoi dong Spring Boot...
call mvnw.cmd spring-boot:run
