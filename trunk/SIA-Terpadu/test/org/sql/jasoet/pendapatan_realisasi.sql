-- REALISASI PENDAPATAN dari PROGRAM STUDI TEKNIK INFORMATIKA tahun 2010--
SELECT tg531420102.kd_tagih,
         tagihan.nama_tagih,
         SUM(tg531420102.jumlah) as realisasi,
         tg531420102.tgl_bayar
    FROM db_5314.tg531420102 tg531420102 INNER JOIN kamus.tagihan tagihan
            ON (tg531420102.kd_tagih = tagihan.Kd_tagih)
   WHERE (tg531420102.kd_tagih != tg531420102.tgl_bayar)
         AND(tg531420102.tgl_bayar IS NOT NULL)
GROUP BY tg531420102.kd_tagih
UNION ALL
SELECT tg531420101.kd_tagih,
         tagihan.nama_tagih,
         SUM(tg531420101.jumlah) as realisasi,
         tg531420101.tgl_bayar
    FROM db_5314.tg531420101 tg531420101 INNER JOIN kamus.tagihan tagihan
            ON (tg531420101.kd_tagih = tagihan.Kd_tagih)
   WHERE (tg531420101.kd_tagih != tg531420101.tgl_bayar)
         AND(tg531420101.tgl_bayar IS NOT NULL)
GROUP BY tg531420101.kd_tagih
-----------------------------------------------------------------------------------------------------------
-----REALISASI PENDAPATAN dari PROGRAM STUDI TEKNIK MESIN tahun 2010-------------
SELECT tg521420102.kd_tagih,
         tagihan.nama_tagih,
         SUM(tg521420102.jumlah) as realisasi,
         tg521420102.tgl_bayar
    FROM db_5214.tg521420102 tg521420102 INNER JOIN kamus.tagihan tagihan
            ON (tg521420102.kd_tagih = tagihan.Kd_tagih)
   WHERE (tg521420102.kd_tagih != tg521420102.tgl_bayar)
         AND(tg521420102.tgl_bayar IS NOT NULL)
GROUP BY tg521420102.kd_tagih
UNION ALL
SELECT tg521420101.kd_tagih,
         tagihan.nama_tagih,
         SUM(tg521420101.jumlah) as realisasi,
         tg521420101.tgl_bayar
    FROM db_5214.tg521420101 tg521420101 INNER JOIN kamus.tagihan tagihan
            ON (tg521420101.kd_tagih = tagihan.Kd_tagih)
   WHERE (tg521420101.kd_tagih != tg521420101.tgl_bayar)
         AND(tg521420101.tgl_bayar IS NOT NULL)
GROUP BY tg521420101.kd_tagih