--SELECT timpenilaidp3.idTim as kodeTimPenilai,
--       timpenilaidp3.kdPegawaiYgDinilai as kdpegawaidinilai ,
--       pegawai.Nama_peg as namaPegawai,
--       Max(nilaisubkomponenpegawai.Nilai)as nilai,
--       unit_peg.kd_unit as kodeUnit,
--       timpenilaidp3.tahunPenilaian
--
--  FROM ((personalia.pegawai pegawai
--         INNER JOIN personalia.unit_peg unit_peg
--            ON (pegawai.kdPegawai = unit_peg.kdPegawai))
--        INNER JOIN personalia.timpenilaidp3 timpenilaidp3
--           ON (timpenilaidp3.kdPegawaiYgDinilai = pegawai.kdPegawai))
--       INNER JOIN personalia.nilaisubkomponenpegawai nilaisubkomponenpegawai
--          ON (timpenilaidp3.idTim = nilaisubkomponenpegawai.idTim)
--group by timpenilaidp3.kdPegawaiYgDinilai


-- nilai rata diambul dari nilai yg diberikan oleh masing2 tim dan dibagi menjadi jumlah tim yg menilai..
SELECT timpenilaidp3.kdPegawaiYgDinilai as kodePegawai,
        pegawai.Nama_peg as namaPegawai,
       SUM(nilaisubkomponenpegawai.Nilai)/count(nilaisubkomponenpegawai.Nilai)as nilaiDP3,
       unit_peg.kd_unit as kodeUnit,
       unkerja.Nama_unit_kerja as namaUnit,
       timpenilaidp3.tahunPenilaian as tahunPenilaian
       
  FROM (((personalia.pegawai pegawai
          INNER JOIN personalia.unit_peg unit_peg
             ON (pegawai.kdPegawai = unit_peg.kdPegawai))
         INNER JOIN personalia.timpenilaidp3 timpenilaidp3
            ON (timpenilaidp3.kdPegawaiYgDinilai = pegawai.kdPegawai))
        INNER JOIN personalia.nilaisubkomponenpegawai nilaisubkomponenpegawai
           ON (timpenilaidp3.idTim = nilaisubkomponenpegawai.idTim))
       INNER JOIN kamus.unkerja unkerja
          ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit)
WHERE unit_peg.kd_unit LIKE '%1608%' and timpenilaidp3.tahunPenilaian = 2010;
