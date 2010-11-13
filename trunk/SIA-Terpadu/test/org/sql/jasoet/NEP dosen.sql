  SELECT resume_kategori.npp as npp,
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
   WHERE resume_kategori.ta LIKE '2008%' AND unit_peg.Kd_unit LIKE '%1609%'
GROUP BY resume_kategori.npp