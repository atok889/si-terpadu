SELECT s.tglUjian,IF(s.tgl_awal_ambil<=>NULL,'',s.tgl_awal_ambil)as tgl_awal_ambil,
s.nomor_mhs AS NIM,DATE_FORMAT(IF(s.tglUjian,'%Y%m') AS tanggal_ujian,
DATE_FORMAT(s.tgl_awal_ambil,'%Y%m') AS tanggal_ambil,
PERIOD_DIFF(DATE_FORMAT(s.tglUjian,'%Y%m'),DATE_FORMAT(s.tgl_awal_ambil,'%Y%m')) AS lama_pengerjaan
FROM db_2114.skr2114 WHERE s.nomor_mhs LIKE "2005"

SELECT skr2114.nomor_mhs, skr2114.tgl_awal_ambil, skr2114.tglUjian
  FROM db_2114.skr2114 skr2114
 WHERE (skr2114.tgl_awal_ambil != '00/00/0000') AND(skr2114.tglUjian != '00/00/0000')

--prodi akutansi--
SELECT skr2114.nomor_mhs,
       skr2114.ambil,
       skr2114.tgl_awal_ambil,
       skr2114.tglUjian,
       PERIOD_DIFF(DATE_FORMAT(skr2114.tglUjian,'%Y%m'),DATE_FORMAT(skr2114.tgl_awal_ambil,'%Y%m')) AS Lama_pengerjaan
  FROM db_2114.skr2114 skr2114
 WHERE (skr2114.tgl_awal_ambil != '00-00-0000')
       AND(skr2114.tglUjian != '00-00-0000')
