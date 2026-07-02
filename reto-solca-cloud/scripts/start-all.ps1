param(
    [switch]$Build = $false,
    [switch]$Detached = $true,
    [string]$ProjectName = "reto-solca-cloud"
)

$RootDir = Split-Path -Parent $PSScriptRoot
$ComposeFile = Join-Path $RootDir "docker-compose.yml"

if (-not (Test-Path $ComposeFile)) {
    Write-Host "[!] docker-compose.yml not found at $ComposeFile" -ForegroundColor Red
    exit 1
}

Push-Location $RootDir
try {
    if ($Build) {
        Write-Host "[*] Building images and starting services..." -ForegroundColor Cyan
        $Args = @("-p", $ProjectName, "up", "--build")
        if ($Detached) { $Args += "-d" }
        & docker-compose $Args
    } else {
        Write-Host "[*] Starting services from existing images..." -ForegroundColor Cyan
        $Args = @("-p", $ProjectName, "up")
        if ($Detached) { $Args += "-d" }
        & docker-compose $Args
    }

    if ($LASTEXITCODE -eq 0) {
        Write-Host "[+] Services started successfully" -ForegroundColor Green
        Write-Host "    phpMyAdmin: http://localhost:8080" -ForegroundColor Yellow
        Write-Host "    Pacientes:  http://localhost:8081/api/actuator/health" -ForegroundColor Yellow
        Write-Host "    Consultas:  http://localhost:8082/api/actuator/health" -ForegroundColor Yellow
        Write-Host "    Laboratorio: http://localhost:8083/api/actuator/health" -ForegroundColor Yellow
        Write-Host "    Imagenologia: http://localhost:8084/api/actuator/health" -ForegroundColor Yellow
        Write-Host "    Repositorio: http://localhost:8085/api/actuator/health" -ForegroundColor Yellow
    } else {
        Write-Host "[-] Failed to start services" -ForegroundColor Red
        exit 1
    }
} finally {
    Pop-Location
}
