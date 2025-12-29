Info Ngopi Lurr
Deskripsi Proyek

Info Ngopi Lurr adalah aplikasi rekomendasi tempat ngopi berbasis Java Desktop yang membantu pengguna menentukan pilihan coffee shop ketika mereka bingung mencari tempat ngopi yang sesuai.
Aplikasi ini menyediakan informasi coffee shop serta fitur pencarian dan filter berdasarkan preferensi pengguna, seperti lokasi, rating, dan rentang harga. Sistem mendukung dua jenis pengguna, yaitu Admin dan User, dengan hak akses yang berbeda.

Tujuan Aplikasi

1. Membantu pengguna menemukan coffee shop yang sesuai dengan kebutuhan mereka
2. Menyediakan sistem pengelolaan data coffee shop yang terstruktur
3. Menerapkan autentikasi dan pembagian hak akses dalam aplikasi desktop Java


Fitur Utama

Autentikasi dan Keamanan

1. Registrasi dan login pengguna
2. Validasi username dan email
3. Hak akses berbeda antara Admin dan User
4. Registrasi Admin menggunakan Admin Code
5. Penyimpanan password menggunakan hashing

Fitur Admin

1. Menambahkan data coffee shop
2. Mengedit data coffee shop
3. Menghapus data coffee shop
4. Melihat daftar seluruh user dan admin
5. Pengelolaan hak akses

Fitur User

1. Melihat daftar coffee shop
2. Pencarian berdasarkan lokasi
3. Filter berdasarkan rating
4. Filter berdasarkan rentang harga
5. Menandai coffee shop sebagai favorit (opsional)



Teknologi yang Digunakan

1. Java (JDK 11 atau lebih baru)
2. Java Swing sebagai GUI Framework
3. MySQL sebagai database
4. JDBC untuk koneksi database
  

Cara Menjalankan Aplikasi
Prasyarat

1.  Java JDK 11 atau lebih tinggi
2.  MySQL Server
3.  IDE Java (NetBeans, IntelliJ IDEA, atau Eclipse)

Menjalankan Aplikasi

1. Jalankan file `Main.java` melalui IDE
2. Atau compile dan run secara manual

Alur Penggunaan
Registrasi User

1. Buka aplikasi
2. Pilih menu Register
3. Isi data diri
4. Pilih role User
5. Klik Register

Registrasi Admin

1. Pilih menu Register
2. Isi data diri
3. Pilih role Admin
4. Masukkan Admin Code yang valid
5. Klik Register

Login
1. Masukkan username dan password
2. Sistem akan mengarahkan ke dashboard sesuai peran

Antarmuka Aplikasi
Login

1. Validasi input
2. Login menggunakan tombol Enter
3. Navigasi ke halaman registrasi

Register

1. Validasi panjang username
2. Validasi format email
3. Validasi kekuatan password
4. Field Admin Code muncul otomatis saat memilih role Admin

Admin Dashboard

1. Tabel data coffee shop
2. Fitur CRUD
3. Logout dengan konfirmasi

Troubleshooting

1. Koneksi database gagal
   Pastikan MySQL berjalan dan konfigurasi koneksi benar

2. Tabel tidak ditemukan
   Pastikan seluruh script SQL sudah dijalankan

3. Error versi Java
   Gunakan JDK 11 atau lebih tinggi

 Lisensi

Proyek ini dibuat untuk keperluan pembelajaran dan portofolio.


Pengembang

- Nama: Dwi ferdianto dan tim
- Email: dwiferdianto69@gmail.com
- Perguruan tinggi: Universitas Ary Ginanjar



Info Ngopi Lurr membantu kamu menemukan tempat ngopi tanpa mikir terlalu lama.
