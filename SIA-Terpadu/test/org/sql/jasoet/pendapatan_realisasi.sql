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