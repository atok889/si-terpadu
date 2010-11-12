  SELECT resume_kategori.npp,
         pegawai.Nama_peg,
         unit_peg.Kd_unit,
         unkerja.Nama_unit_kerja,
         SUM(resume_kategori.skor) AS SUMskor,
         COUNT(resume_kategori.npp) AS jumlah,
         AVG(resume_kategori.skor) AS rata2,
         resume_kategori.ta
    FROM ((kamus.unkerja unkerja
           INNER JOIN kamus.unit_peg unit_peg
              ON (unkerja.Kd_unit_kerja = unit_peg.Kd_unit))
          INNER JOIN personalia.pegawai pegawai
             ON (pegawai.NPP = unit_peg.NPP))
         INNER JOIN evaluasi.resume_kategori resume_kategori
            ON (resume_kategori.npp = pegawai.NPP)
   WHERE resume_kategori.ta LIKE "2008%"
GROUP BY resume_kategori.npp