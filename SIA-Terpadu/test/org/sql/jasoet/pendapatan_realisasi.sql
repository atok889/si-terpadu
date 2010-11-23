-----REALISASI PENDAPATAN dari PROGRAM STUDI TEKNIK MESIN tahun 2010-------------
SELECT tg521420102.kd_tagih as kodeTagih,
         tagihan.nama_tagih as namaTagih,
         SUM(tg521420102.jumlah) as realisasi,
         tg521420102.tgl_bayar as tanggalBayar
    FROM db_5214.tg521420102 tg521420102 INNER JOIN kamus.tagihan tagihan
            ON (tg521420102.kd_tagih = tagihan.Kd_tagih)
   WHERE (tg521420102.kd_tagih != tg521420102.tgl_bayar)
         AND(tg521420102.tgl_bayar IS NOT NULL)
GROUP BY tg521420102.kd_tagih
UNION ALL
SELECT tg521420101.kd_tagih as kodeTagih,
         tagihan.nama_tagih as namaTagih,
         SUM(tg521420101.jumlah) as realisasi,
         tg521420101.tgl_bayar as tanggalBayar
    FROM db_5214.tg521420101 tg521420101 INNER JOIN kamus.tagihan tagihan
            ON (tg521420101.kd_tagih = tagihan.Kd_tagih)
   WHERE (tg521420101.kd_tagih != tg521420101.tgl_bayar)
         AND(tg521420101.tgl_bayar IS NOT NULL)
GROUP BY tg521420101.kd_tagih;