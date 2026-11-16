param(
    [Parameter(Mandatory = $true)]
    [ValidatePattern('^https://github\.com/.+/.+\.git$')]
    [string]$RepositoryUrl
)

$ErrorActionPreference = 'Stop'

if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    throw 'Git belum terpasang. Install Git for Windows terlebih dahulu.'
}

if (-not (Test-Path '.git')) {
    git init -b main
    if ($LASTEXITCODE -ne 0) { throw 'Gagal menjalankan git init.' }
}

# Pastikan file rahasia lokal tidak ikut masuk ke commit.
if (Test-Path '.env') {
    Write-Host 'File .env ditemukan. File ini diabaikan oleh .gitignore dan tidak akan diunggah.' -ForegroundColor Yellow
}

git add .
if ($LASTEXITCODE -ne 0) { throw 'Gagal menjalankan git add.' }

git diff --cached --quiet
if ($LASTEXITCODE -eq 1) {
    git commit -m 'P5 Cashflow application'
    if ($LASTEXITCODE -ne 0) { throw 'Gagal membuat commit. Periksa konfigurasi nama dan email Git.' }
} elseif ($LASTEXITCODE -ne 0) {
    throw 'Gagal memeriksa perubahan Git.'
} else {
    Write-Host 'Tidak ada perubahan baru untuk di-commit.'
}

$existingRemote = git remote get-url origin 2>$null
if ($LASTEXITCODE -eq 0 -and $existingRemote) {
    if ($existingRemote -ne $RepositoryUrl) {
        throw "Remote origin sudah mengarah ke $existingRemote. Hapus/ganti remote tersebut secara manual sebelum melanjutkan."
    }
} else {
    git remote add origin $RepositoryUrl
    if ($LASTEXITCODE -ne 0) { throw 'Gagal menambahkan remote origin.' }
}

git push -u origin main
if ($LASTEXITCODE -ne 0) { throw 'Push gagal. Pastikan repository GitHub sudah dibuat dan autentikasi GitHub sudah siap.' }

Write-Host 'Berhasil push ke GitHub.' -ForegroundColor Green
