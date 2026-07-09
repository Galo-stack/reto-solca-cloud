param(
    [string[]]$Services = @("microservicio-pacientes", "microservicio-consultas", "microservicio-laboratorio", "microservicio-imagenologia", "microservicio-repositorio", "microservicio-seguridad", "microservicio-auditoria"),
    [switch]$SkipTests = $true
)

$RootDir = Split-Path -Parent $PSScriptRoot
$MavenArgs = @("clean", "package", "-q")
if ($SkipTests) {
    $MavenArgs += "-DskipTests"
}

$Results = @()
$Global:ErrorCount = 0

foreach ($Service in $Services) {
    $ServicePath = Join-Path $RootDir $Service
    if (-not (Test-Path $ServicePath)) {
        Write-Host "[!] Service directory not found: $ServicePath" -ForegroundColor Red
        continue
    }

    Write-Host "[*] Building $Service..." -ForegroundColor Cyan
    $startTime = Get-Date

    Push-Location $ServicePath
    try {
        $Output = & mvn $MavenArgs 2>&1
        $exitCode = $LASTEXITCODE
        
        $elapsed = (Get-Date) - $startTime
        $duration = "{0:mm}:{0:ss}" -f $elapsed

        if ($exitCode -eq 0) {
            Write-Host "[+] $Service built successfully ($duration)" -ForegroundColor Green
            $Results += [PSCustomObject]@{ Service = $Service; Status = "OK"; Duration = $duration }
        } else {
            Write-Host "[-] $Service FAILED ($duration)" -ForegroundColor Red
            $Results += [PSCustomObject]@{ Service = $Service; Status = "FAIL"; Duration = $duration }
            $Global:ErrorCount++
            Write-Host $Output -ForegroundColor Red
        }
    } finally {
        Pop-Location
    }
}

Write-Host "`n========================================" -ForegroundColor Yellow
Write-Host " BUILD SUMMARY" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow
$Results | ForEach-Object {
    $color = if ($_.Status -eq "OK") { "Green" } else { "Red" }
    Write-Host ("[{0}] {1} ({2})" -f $_.Status, $_.Service, $_.Duration) -ForegroundColor $color
}
Write-Host "========================================" -ForegroundColor Yellow
Write-Host "Total: $($Results.Count) | OK: $(($Results | Where-Object { $_.Status -eq 'OK' }).Count) | Fail: $Global:ErrorCount" -ForegroundColor $(if ($Global:ErrorCount -eq 0) { "Green" } else { "Red" })
if ($Global:ErrorCount -gt 0) { exit 1 }
