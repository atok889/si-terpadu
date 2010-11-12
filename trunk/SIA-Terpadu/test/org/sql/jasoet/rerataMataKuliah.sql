--PAKE yg INI aja soalanya matakuliah bisa keluar semua, kalo inner join aja gak bisa--
SELECT mtk5314.kd_mtk,
         mtk5314.nama_mtk,
         mtk5314.SKS,
         SUM(nilai.angka),
         COUNT(DISTINCT kr531420091.nomor_mhs),
         SUM(nilai.angka)/
         COUNT(DISTINCT kr531420091.nomor_mhs) as rataNilaiMK
    FROM (db_5314.kr531420091 kr531420091
          RIGHT OUTER JOIN db_5314.mtk5314 mtk5314
             ON (kr531420091.kd_mtk = mtk5314.kd_mtk))
         LEFT OUTER JOIN kamus.nilai nilai
            ON (nilai.huruf = kr531420091.nilai)
GROUP BY mtk5314.kd_mtk
------------------------------------------------------------------------------------

SELECT mtk5314.kd_mtk,
         mtk5314.nama_mtk,
         mtk5314.SKS,
         SUM(nilai.angka),
         COUNT(DISTINCT kr531420091.nomor_mhs),
         SUM(nilai.angka)/
         COUNT(DISTINCT kr531420091.nomor_mhs) as rataNilaiMK
    FROM (kamus.nilai nilai
          INNER JOIN db_5314.kr531420091 kr531420091
             ON (nilai.huruf = kr531420091.nilai))
         RIGHT OUTER JOIN db_5314.mtk5314 mtk5314
            ON (kr531420091.kd_mtk = mtk5314.kd_mtk)
GROUP BY mtk5314.kd_mtk

--tes query
SELECT mtk5314.nama_mtk,
         mtk5314.SKS,
         kr531420091.kd_mtk,
         SUM(nilai.angka),
         COUNT(DISTINCT kr531420091.nomor_mhs),
         SUM(nilai.angka)/COUNT(DISTINCT kr531420091.nomor_mhs) as hasil
    FROM (db_5314.kr531420091 kr531420091
          INNER JOIN db_5314.mtk5314 mtk5314
             ON (kr531420091.kd_mtk = mtk5314.kd_mtk))
         INNER JOIN kamus.nilai nilai
            ON (nilai.huruf = kr531420091.nilai)
GROUP BY mtk5314.kd_mtk

select count(mtk.kd_mtk) from db_5314.mtk5314 mtk
