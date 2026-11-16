# P5 PBO 2026 — Cashflow

Aplikasi pencatatan pemasukan dan pengeluaran dengan **Spring Boot 4.1.2**, **Vaadin 25.3.4**, **Java 21**, **Maven**, **JDBC PostgreSQL**, dan struktur **Clean Architecture**.

- Folder/artifact: `ifs25019-p5-pbo2026-springboot-cashflow`
- Package: `org.delcom.ifs25019springbootcashflow`
- Database: `pbo2026_cashflow`

## 1. Persiapan

Pasang JDK 21, Maven 3.9+, dan Docker Desktop (untuk menjalankan PostgreSQL dengan mudah). Periksa:

```powershell
java -version
mvn -version
docker --version
```

## 2. Jalankan database PostgreSQL

Buka PowerShell pada folder proyek:

```powershell
docker compose up -d postgres
```

Database `pbo2026_cashflow` otomatis dibuat dengan user/password lokal `postgres` / `postgres`.

## 3. Jalankan aplikasi

Salin konfigurasi contoh menjadi `.env`, kemudian sesuaikan bila diperlukan:

```powershell
Copy-Item .env.example .env
```

Jalankan:

```powershell
.\run-local.ps1
```

Script membaca `.env` dan menjalankan `mvn spring-boot:run`. Buka **http://localhost:8080**.

Jika ingin menjalankan Maven secara langsung, set environment variable terlebih dahulu:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/pbo2026_cashflow"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
$env:ROOT_USERNAME="admin"
$env:ROOT_PASSWORD="GantiPasswordAdmin123!"
mvn spring-boot:run
```

Akun ADMIN dibuat otomatis dari `ROOT_USERNAME` dan `ROOT_PASSWORD`. Registrasi dari halaman `/register` membuat akun `USER`. Ganti password admin default sebelum aplikasi dipakai bersama.

## 4. Fitur yang tersedia

- Registrasi, login, logout, dan role `ADMIN` / `USER`.
- Dashboard saldo, total pemasukan, dan total pengeluaran.
- Tambah, edit, hapus, cari, dan urutkan transaksi.
- Unggah bukti transaksi (gambar/PDF) dan foto profil.
- Perubahan nama dan password dari halaman profil.
- Pengguna hanya mengelola transaksi miliknya sendiri.
- ADMIN dapat melihat daftar pengguna dan meninjau transaksi setiap pengguna secara read-only.
- JDBC repository untuk runtime dan generic `IRepository<T, ID>` dengan InMemory adapter untuk pengujian.

## 5. Pengujian dan laporan

```powershell
mvn clean verify
```

Laporan JaCoCo dibuat di `target/site/jacoco/index.html`. Tes unit use case/domain dan tes repository JDBC menggunakan H2 tersedia di `src/test`.

## 6. Push ke GitHub

Buat repository kosong di GitHub. Lalu:

```powershell
git init -b main
git add .
git commit -m "P5 Cashflow application"
git remote add origin https://github.com/USERNAME/NAMA-REPOSITORY.git
git push -u origin main
```

Jangan commit `.env`, password, atau file upload. `.gitignore` sudah mengabaikan file tersebut.

## 7. Deploy ke Render

1. Push kode ke GitHub.
2. Di Render pilih **New → Blueprint** dan hubungkan repository.
3. Render membaca `render.yaml`, membuat PostgreSQL dan Docker Web Service.
4. Atur `ROOT_PASSWORD` sebagai secret yang kuat.
5. Untuk menyimpan upload secara persisten, pasang persistent disk pada service dan arahkan `UPLOAD_DIR` ke mount path disk tersebut. Filesystem container biasa dapat hilang saat deploy/restart.

## 8. Jenkins CI/CD

`Jenkinsfile` berisi tahap build/test, Trivy filesystem scan, build Docker image, dan opsi publish/deploy. Jika ingin mengaktifkan publish, siapkan credential Jenkins ID `dockerhub` (username/password). Untuk deploy Render, siapkan secret text credential ID `render-deploy-hook`. Jenkins agent perlu memiliki Maven, Docker, Trivy, dan akses jaringan.

## Catatan

Script `mvnw`/`mvnw.cmd` pada paket ini hanya meneruskan perintah ke Maven yang sudah terpasang; gunakan `mvn` jika wrapper tidak tersedia. Build dan tes belum dapat saya eksekusi di lingkungan penyusunan paket karena Maven tidak terpasang dan akses unduhan dependensi tidak tersedia. Jalankan `mvn clean verify` di komputer sebelum deployment.

## 9. Push praktis ke repository GitHub milik Anda

Buat repository baru yang **kosong** di GitHub (jangan centang README, .gitignore, atau License). Setelah itu buka PowerShell pada folder proyek dan jalankan:

```powershell
.\push-to-github.ps1 -RepositoryUrl "https://github.com/USERNAME/NAMA-REPOSITORY.git"
```

Ganti URL dengan URL repository yang baru Anda buat. Script akan menginisialisasi Git jika belum ada, menambahkan file, membuat commit, memasang remote `origin`, lalu push ke branch `main`. Pastikan Git for Windows sudah terpasang dan nama/email Git sudah dikonfigurasi. File `.env` dan folder `uploads/` tidak akan ikut di-push.
