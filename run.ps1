# ==============================================================================
# BlueMoon BFMS - 1-Click Start Script for PowerShell
# Tu dong doc cac bien tu file .env va khoi dong Spring Boot
# ==============================================================================

if (Test-Path .env) {
    Get-Content .env | Where-Object { $_ -match '^[^#=]+=' } | ForEach-Object {
        $name, $val = $_.Split('=', 2)
        [System.Environment]::SetEnvironmentVariable($name.Trim(), $val.Trim(), 'Process')
    }
    Write-Host "[BlueMoon] Da nap thanh cong bien moi truong tu file .env" -ForegroundColor Green
} else {
    Write-Host "[BlueMoon] Khong tim thay file .env, su dung cau hinh mac dinh" -ForegroundColor Yellow
}

Write-Host "[BlueMoon] Dang khoi dong ung dung Spring Boot..." -ForegroundColor Cyan
.\mvnw.cmd spring-boot:run
