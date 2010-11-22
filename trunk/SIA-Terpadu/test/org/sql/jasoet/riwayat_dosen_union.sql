--Pertama buat view table ini dulu --------------------------------------------------------------------------------------
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
-------------------------------------------------------------------------------------------------------------------------

-------------------tabel dosen berdasarkan Kode Unit Kerja---------------------------------------------------------------
SELECT riwayatDosen.kodePegawai,
riwayatDosen.namaPegawai,
IF(MAX(unitPegawai.tgl_sk_unit)<=>NULL,'',MAX(unitPegawai.tgl_sk_unit))as tahun_sk,
unkerja.Nama_unit_kerja
 FROM tempo.riwayatDosen riwayatDosen
INNER JOIN personalia.pegawai pegawai ON (riwayatDosen.kodePegawai= pegawai.kdPegawai)
INNER JOIN personalia.unit_peg unitPegawai ON (pegawai.kdPegawai=unitPegawai.kdPegawai)
INNER JOIN kamus.unKerja unkerja ON (unitPegawai.kd_unit=unkerja.kd_unit_kerja)
WHERE unkerja.Kd_unit_kerja='16053140'
GROUP BY riwayatDosen.kodePegawai
--------------------------------------------------------------------------------------------------------------------------


-------------Menu pop up riwayat dosen------------------------------------------------------------------------------------
SELECT riwayatDosen.riwayat,
    riwayatDosen.namaPegawai,
    riwayatDosen.tahun,
    riwayatDosen.keterangan,
    pegawai.Alamat
FROM tempo.riwayatDosen riwayatDosen
INNER JOIN personalia.pegawai pegawai ON (riwayatDosen.kodePegawai= pegawai.kdPegawai)
INNER JOIN personalia.unit_peg unitPegawai ON (pegawai.kdPegawai=unitPegawai.kdPegawai)
INNER JOIN kamus.unKerja unkerja ON (unitPegawai.kd_unit=unkerja.kd_unit_kerja)
WHERE unkerja.Kd_unit_kerja='16053140' AND riwayatDosen.kodePegawai='00961'
-------------------------------------------------------------------------------------------------------------------------