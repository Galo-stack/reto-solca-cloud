param(
    [switch]$Volumes = $false,
    [string]$ProjectName = "reto-solca-cloud"
)

$RootDir = Split-Path -Parent $PSScriptRoot
$ComposeFile = Join-Path $RootDir "docker-compose.yml"

Push-Location $RootDir
try {
    $Args = @("-p", $ProjectName, "down")
    if ($Volumes) { $Args += "-v" }
    
    Write-Host "[*] Stopping services..." -ForegroundColor Cyan
    & docker-compose $Args

    if ($LASTEXITCODE -eq 0) {
        Write-Host "[+] Services stopped successfully" -ForegroundColor Green
    } else {
        Write-Host "[-] Failed to stop services" -ForegroundColor Red
        exit 1
    }
} finally {
    Pop-Location
}
