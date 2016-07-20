package com.postel.user.logbook2.config;

/**
 * Created by user on 6/28/2016.
 */
public class Koneksi {

    //Alamat URL tempat kita meletakkan script PHP di PC Server
    // Link untuk INSERT Data
    public static final String URL_ADD="http://10.1.125.185/log_book/create.php";

    // Link Untuk Tampil Data
    public static final String URL_GET_ALL = "http://10.1.125.185/log_book/read.php";

    // Filed yang digunakan untuk dikirimkan ke Database, sesuaikan saja dengan Field di Tabel Mahasiswa
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_NAMA = "nama";
    public static final String KEY_EMP_EMAIL = "email";
    public static final String KEY_EMP_TLP = "tlp";
    public static final String KEY_EMP_PERUSAHAAN = "perusahaan";
    public static final String KEY_EMP_KEPERLUAN = "keperluan";
    public static final String KEY_EMP_KETERANGAN = "keterangan";

    // Tags Format JSON
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_TLP = "tlp";
    public static final String TAG_PERUSAHAAN = "perusahaan";
    public static final String TAG_KEPERLUAN = "keperluan";
    public static final String TAG_KETERANGAN = "keterangan";
    public static final String TAG_JAM_MASUK = "jam_masuk";
    public static final String TAG_JAM_KELUAR = "jam_keluar";
    public static final String TAG_STATUS = "status";
    public static final String TAG_FOTO = "foto";
    public static final String TAG_KTP = "ktp";
    public static final String TAG_TTD = "ttd";
    public static final String TAG_WAKTU = "waktu";

}
