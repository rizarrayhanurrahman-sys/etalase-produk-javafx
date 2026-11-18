# Etalase Produk

Aplikasi desktop JavaFX untuk mengelola katalog produk makanan dan minuman. Data disimpan di database MySQL (XAMPP) dan bisa ditambah, diubah, serta dihapus.

## Fitur

- Tambah produk (nama, harga, foto)
- Tampilan katalog dalam bentuk kartu produk
- Ubah produk lewat tombol Ubah di kartu
- Hapus produk dengan dialog konfirmasi
- Pilihan 10 foto makanan dan minuman
- Penyimpanan data ke MySQL

## Pilihan Foto

| Makanan | Minuman |
|---|---|
| Roti | Susu |
| Wagyu Steak | Kopi |
| Sushi Platter | Matcha Latte |
| Truffle Pasta | Berry Juice |
| Croissant | |
| Tiramisu | |

## Screenshot

### Tampilan awal
![Tampilan awal](screenshots/tampilan-awal.jpeg)

### Tambah produk
![Tambah produk](screenshots/tambah-produk.jpeg)

### Ubah produk
![Ubah produk](screenshots/ubah-produk.jpeg)

### Hapus produk
![Hapus produk](screenshots/hapus-produk.jpeg)

### Database di phpMyAdmin
![Database](screenshots/database.jpeg)

## Kebutuhan

- JDK 17 atau lebih baru
- Apache NetBeans (atau IDE lain yang mendukung Maven)
- XAMPP (Apache dan MySQL)

## Cara Menjalankan

### 1. Clone project

```
git clone https://github.com/rizarrayhanurrahman-sys/etalase-produk-javafx.git
```

Lalu buka folder `etalase-produk-javafx` di NetBeans (File > Open Project).

### 2. Siapkan database

1. Buka XAMPP Control Panel, klik **Start** pada **Apache** dan **MySQL**.
2. Buka `http://localhost/phpmyadmin`.
3. Buat database baru bernama `etalase_db`.
4. Buka tab **SQL**, jalankan perintah berikut untuk membuat user aplikasi:

```sql
CREATE USER 'etalase'@'localhost' IDENTIFIED BY 'etalase123';
GRANT ALL PRIVILEGES ON etalase_db.* TO 'etalase'@'localhost';
FLUSH PRIVILEGES;
```

Tabel `produk` dibuat otomatis oleh aplikasi saat pertama kali dijalankan.

Pengaturan koneksi ada di `src/main/java/db/Database.java`. Sesuaikan `USER` dan `PASSWORD` jika berbeda.

### 3. Jalankan aplikasi

Di NetBeans: klik kanan project, pilih **Clean and Build**, lalu **Run**.

Atau lewat terminal di folder project:

```
mvn clean javafx:run
```

## Cara Memakai

1. Isi nama produk, harga, dan pilih foto, lalu klik **Simpan Produk**.
2. Untuk mengubah data, klik **Ubah** pada kartu produk, ubah isinya, lalu klik **Simpan Perubahan**. Klik **Batal** untuk membatalkan.
3. Untuk menghapus, klik **Hapus** pada kartu produk, lalu konfirmasi dengan **Ya, hapus**.

## Contoh Data

| Nama | Harga | Foto |
|---|---|---|
| Croissant | 35000 | croissant |
| Matcha Latte | 35000 | matcha-latte |


## Struktur Project

```
src/main/java
    main/         MainApp dan Launcher
    controller/   KatalogController
    data/         ProdukRepository
    db/           Database
    model/        Produk
src/main/resources
    view/         katalog.fxml dan styles/app.css
    image/        foto produk (makanan dan minuman)
```