CREATE OR REPLACE VIEW tempo.riwayatdosen(riwayat,kodePegawai,namaPegawai,tahun,keterangan) AS
SELECT 'STUDI LANJUT' AS riwayat,
        studi.kdPegawai AS kodePegawai,
       pegawai.Nama_peg AS namaPegawai,
       IF(YEAR(studi.Tgl_selesai_studi) <=> NULL,'',YEAR(studi.Tgl_selesai_studi)) AS tahun ,
       CONCAT(IF(jenjang.Nm_jenjang<=>NULL,' ',jenjang.Nm_jenjang),' ',IF(studi.Univ<=>NULL,' ',studi.Univ)) as keterangan
FROM (kamus.jenjang jenjang
        INNER JOIN personalia.studi studi
           ON (jenjang.Kd_jenjang = studi.Jenjang))
       INNER JOIN personalia.pegawai pegawai
          ON (studi.kdPegawai = pegawai.kdPegawai)
 WHERE pegawai.Status_keluar='1' OR pegawai.Status_keluar='6' OR  pegawai.Status_keluar='7'
AND studi.Tgl_selesai_studi AND pegawai.AdmEdu='2'

UNION ALL
SELECT 'PENDIDIKAN' AS riwayat,
       pendidikan.kdPegawai AS kodePegawai,
       pegawai.Nama_peg AS namaPegawai,
       YEAR(pendidikan.Tgl_ijasah) as tahun,
       CONCAT(jenjang.Nm_jenjang,'  ',
       pendidikan.Prodi) as keterangan

       FROM (personalia.pendidikan pendidikan
        INNER JOIN kamus.jenjang jenjang
           ON (pendidikan.Jenjang = jenjang.Kd_jenjang))
       INNER JOIN personalia.pegawai pegawai
          ON (pendidikan.kdPegawai = pegawai.kdPegawai)
 WHERE  pegawai.Status_keluar='1' OR pegawai.Status_keluar='6' OR  pegawai.Status_keluar='7' AND pegawai.AdmEdu='2'
UNION ALL
SELECT 'JABATAN' as riwayat,
        pejabat.kdPegawai AS kodePegawai,
       pegawai.Nama_peg AS namaPegawai,
       YEAR(pejabat.tgl_sk_angkat_jabat)as tahun ,
       IF(jabatan.Nama_jab<=>NULL,'',jabatan.Nama_jab)as keterangan
  FROM (personalia.pejabat pejabat
        INNER JOIN kamus.jabatan jabatan
           ON (pejabat.kode_jab = jabatan.Kd_jab))
       INNER JOIN personalia.pegawai pegawai
          ON (pejabat.kdPegawai = pegawai.kdPegawai)
WHERE pegawai.Status_keluar='1' OR pegawai.Status_keluar='6' OR  pegawai.Status_keluar='7'
    AND YEAR(pejabat.tgl_sk_angkat_jabat) AND pegawai.AdmEdu='2'

SELECT * FROM tempo.riwayatDosen riwayatDosen
WHERE  riwayatDosen.kodePegawai='01248'

