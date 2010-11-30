CREATE OR REPLACE VIEW tempo.dosenStudi(kdPegawai,Nama_peg,Kd_unit_kerja,Kd_jenjang,Nama_unit_kerja,Univ)AS
SELECT ps.kdPegawai AS kdPegawai,
        LTRIM(CONCAT(pp.Gelar_depan,' ',pp.Nama_peg,' ',pp.Gelar_blk)) AS Nama_peg,
           pu.kd_unit AS Kd_unit_kerja,
           MAX(ps.Jenjang) AS Kd_jenjang,
          ku.Nama_unit_kerja AS Nama_unit_kerja,
            IF(ps.Univ<=>NULL,'DATA BELUM DI ISI ',ps.Univ) AS Univ
             FROM (((personalia.unit_peg pu
                 INNER JOIN kamus.unkerja ku ON (pu.kd_unit = ku.Kd_unit_kerja))
                 INNER JOIN personalia.pegawai pp ON (pp.kdPegawai = pu.kdPegawai))
                 INNER JOIN personalia.studi ps ON (ps.kdPegawai = pp.kdPegawai))
                 WHERE ps.`statusLulus`='N'
                 AND (pp.Status_keluar like '1' or pp.Status_keluar like '6' or pp.Status_keluar like '7')
                 GROUP BY pp.Nama_peg;

SELECT ds.kdPegawai,ds.Nama_peg,ds.Kd_unit_kerja,ds.Kd_jenjang,
ds.Nama_unit_kerja,kj.Nm_jenjang,ds.Univ AS Univ
FROM tempo.dosenStudi ds
INNER JOIN kamus.jenjang kj ON ( kj.Kd_jenjang = ds.Kd_Jenjang)

Group by ds.kdPegawai;