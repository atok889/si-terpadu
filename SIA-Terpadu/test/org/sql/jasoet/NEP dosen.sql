  SELECT
    resume_kategori.npp as npp,
        pegawai.Nama_peg as namaPegawai,
        unit_peg.Kd_unit as kodeUnit,
         unkerja.Nama_unit_kerja as namaUnit,
         AVG(resume_kategori.skor) AS rataRata,
         resume_kategori.ta AS tahunAjaran
    FROM ((kamus.unkerja unkerja
           INNER JOIN kamus.unit_peg unit_peg
              ON (unkerja.Kd_unit_kerja = unit_peg.Kd_unit))
          INNER JOIN personalia.pegawai pegawai
             ON (pegawai.NPP = unit_peg.NPP))
         INNER JOIN evaluasi.resume_kategori resume_kategori
            ON (resume_kategori.npp = pegawai.NPP)
   GROUP BY resume_kategori.npp;

-- Baru senin 15 november 2010
SELECT resume_kategori.skor,
       unit_peg.kd_unit,
       resume_kategori.npp,
       pegawai.Nama_peg,
       pegawai.AdmEdu,
       resume_kategori.ta
  FROM (personalia.pegawai pegawai
        INNER JOIN personalia.unit_peg unit_peg
           ON (pegawai.NPP = unit_peg.npp))
       INNER JOIN evaluasi.resume_kategori resume_kategori
          ON (resume_kategori.npp = pegawai.NPP)
 WHERE (pegawai.AdmEdu = '1') AND (resume_kategori.ta LIKE '20082%');

-- LTRIM(CONCAT(pegawai.Gelar_depan,' ',pegawai.Nama_peg,' ',pegawai.Gelar_blk)) as namaPegawai,

SELECT pegawai.npp as npp,
    pegawai.Nama_peg  as namaPegawai,
    unit_peg.Kd_unit as kodeUnit,
    unkerja.Nama_unit_kerja as namaUnit,
    AVG(resume_kategori.skor) AS rataRata,
    resume_kategori.ta AS tahunAjaran
FROM personalia.pegawai pegawai
    INNER JOIN personalia.unit_peg unit_peg
        ON (pegawai.NPP = unit_peg.NPP)
    INNER JOIN kamus.unkerja unkerja
        ON (unit_peg.Kd_unit = unkerja.Kd_unit_kerja)
    INNER JOIN evaluasi.resume_kategori resume_kategori
        ON (resume_kategori.npp = pegawai.NPP)
WHERE
pegawai.AdmEdu = '2' AND
pegawai.Status_keluar = '1' AND
resume_kategori.ta='20082'  AND
unit_peg.Kd_unit='16053140'
GROUP BY unit_peg.npp
ORDER BY pegawai.Nama_peg ASC;

SELECT --resume_kategori.npp as npp,
    --LTRIM(CONCAT(pegawai.Gelar_depan,' ',pegawai.Nama_peg,' ',pegawai.Gelar_blk)) as namaPegawai,
    --unit_peg.Kd_unit as kodeUnit,
    --unkerja.Nama_unit_kerja as namaUnit,
    AVG(resume_kategori.skor) AS rataRata
    --resume_kategori.ta AS tahunAjaran
FROM evaluasi.resume_kategori resume_kategori
INNER JOIN personalia.pegawai pegawai ON (resume_kategori.npp = pegawai.NPP)
INNER JOIN kamus.unit_peg unit_peg  ON (pegawai.NPP = unit_peg.NPP)
INNER JOIN kamus.unkerja unkerja ON (unkerja.Kd_unit_kerja=unit_peg.Kd_unit)
WHERE resume_kategori.ta='20082' AND unit_peg.Kd_unit LIKE '%5314%'
GROUP BY resume_kategori.npp;

SELECT * FROM evaluasi.resume_kategori resume_kategori WHERE resume_kategori.npp='P.1474';

SELECT * FROM personalia.pegawai pegawai
INNER JOIN kamus.unit_peg  unit_peg ON (pegawai.NPP = unit_peg.NPP)
WHERE unit_peg.Kd_unit LIKE '%5314%';