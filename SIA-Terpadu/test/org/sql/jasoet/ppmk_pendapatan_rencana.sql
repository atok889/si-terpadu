SELECT anggaranpendapatankemahasiswaan.idPosAnggaranPendapatanUnit AS idPosAnggaranPendapatanUnit,
         pospendapatan.posPendapatan AS jenisPendapatan,
         SUM(anggaranpendapatankemahasiswaan.tarif)
            *SUM(anggaranpendapatankemahasiswaan.prediksiJmlMhsBaru)AS Rencana,
          posanggaranpendapatanunit.tahunAnggaran AS tahun
    FROM (ppmk.posPendapatan pospendapatan
          INNER JOIN ppmk.posAnggaranPendapatanUnit posanggaranpendapatanunit
             ON (posPendapatan.idPosPendapatan =
                    posanggaranpendapatanunit.idPosPendapatan))
         INNER JOIN ppmk.anggaranPendapatanKemahasiswaan anggaranpendapatankemahasiswaan
            ON (anggaranpendapatankemahasiswaan.idPosAnggaranPendapatanUnit =
                   posanggaranpendapatanunit.idPosAnggaranPendapatanUnit)
WHERE posanggaranpendapatanunit.tahunAnggaran='2010'
GROUP BY anggaranpendapatankemahasiswaan.idPosAnggaranPendapatanUnit
UNION ALL
SELECT anggaranpendapatanujianakhirkkn.idPosAnggaranPendapatanUnit AS idPosAnggaranPendapatanUnit,
         pospendapatan.posPendapatan AS jenisPendapatan,
         SUM(anggaranpendapatanujianakhirkkn.prediksiJmlMhsYgUjian)*
         SUM(anggaranpendapatanujianakhirkkn.tarif)AS Rencana,
            posanggaranpendapatanunit.tahunAnggaran AS Tahun
    FROM (ppmk.anggaranPendapatanUjianAkhirKKN anggaranpendapatanujianakhirkkn
          INNER JOIN ppmk.posAnggaranPendapatanUnit posanggaranpendapatanunit
             ON (anggaranpendapatanujianakhirkkn.idPosAnggaranPendapatanUnit =
                    posanggaranpendapatanunit.idPosAnggaranPendapatanUnit))
         INNER JOIN ppmk.posPendapatan pospendapatan
            ON (pospendapatan.idPosPendapatan =
                   posanggaranpendapatanunit.idPosPendapatan)
WHERE  posanggaranpendapatanunit.tahunAnggaran='2010'
GROUP BY anggaranpendapatanujianakhirkkn.idPosAnggaranPendapatanUnit
UNION ALL
SELECT anggaranpendapatanunituktsks.idPosAnggaranPendapatanUnit AS idPosAnggaranPendapatanUnit,
         pospendapatan.posPendapatan AS jenisPendapatan,
         SUM(anggaranpendapatanunituktsks.jmlSKS)*SUM(anggaranpendapatanunituktsks.tarif) AS RENCANA,
        posanggaranpendapatanunit.tahunAnggaran AS Tahun
    FROM (ppmk.posAnggaranPendapatanUnit posanggaranpendapatanunit
          INNER JOIN ppmk.posPendapatan pospendapatan
             ON (posanggaranpendapatanunit.idPosPendapatan =
                    pospendapatan.idPosPendapatan))
         INNER JOIN ppmk.anggaranPendapatanUnitUKTSKS anggaranpendapatanunituktsks
            ON (anggaranpendapatanunituktsks.idPosAnggaranPendapatanUnit =
                   posanggaranpendapatanunit.idPosAnggaranPendapatanUnit)
WHERE posanggaranpendapatanunit.tahunAnggaran='2010'
GROUP BY anggaranpendapatanunituktsks.idPosAnggaranPendapatanUnit

