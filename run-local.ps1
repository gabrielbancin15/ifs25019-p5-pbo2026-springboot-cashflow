$ErrorActionPreference = 'Stop'
$envFile = Join-Path $PSScriptRoot '.env'
if (!(Test-Path $envFile)) {
    Copy-Item (Join-Path $PSScriptRoot '.env.example') $envFile
    Write-Host 'File .env dibuat dari .env.example. Silakan cek password database, lalu jalankan ulang script ini.' -ForegroundColor Yellow
    exit 1
}
Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()
    if ($line -and !$line.StartsWith('#') -and $line.Contains('=')) {
        $parts = $line.Split('=', 2)
        [Environment]::SetEnvironmentVariable($parts[0].Trim(), $parts[1].Trim(), 'Process')
    }
}
Write-Host 'Menjalankan Cashflow pada http://localhost:8080 ...' -ForegroundColor Green
mvn spring-boot:run
