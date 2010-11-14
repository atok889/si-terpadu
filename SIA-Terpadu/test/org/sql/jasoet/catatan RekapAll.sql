--  Pada query ini tiga hal yg penting yaitu anggaran, pencairan dana , pengajuan atau LPJ
--  Dalam query ini bagian pencairan dan pengajuan saling berkaitan
-- yg ditampilkan pada adalah


SELECT *
    FROM (SELECT anggaran.no AS no1,
                 anggaran.nm AS nm1,
                 anggaran.kodepos AS kodepos1,
                 anggaran.namaPosBiaya AS namaPosBiaya1,
                 anggaran.anggaran AS anggaran1,
                 pencairan.no AS no2,
                 pencairan.nm AS nm2,
                 pencairan.kodepos AS kodepos2,
                 pencairan.namaPosBiaya AS namaPosBiaya2,
                 pencairan.pengajuan AS pengajuan2,
                 pencairan.cair AS cair2,
                 lapPJ.no AS no3,
                 lapPJ.nm AS nm3,
                 lapPJ.kodepos AS kodepos3,
                 lapPJ.namaPosBiaya AS namaPosBiaya3,
                 lapPJ.pengajuan AS pengajuan3,
                 lapPJ.LPJ AS LPJ3
            FROM (  SELECT '1' AS no,
                           'RAB Non Rapat' AS nm,
                           rab.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           sum(harga * kuantitas) AS anggaran
                      FROM    ppmk.anggaranRABNonRapatKerja AS rab
                           INNER JOIN
                              ppmk.posBiaya AS pos
                           ON rab.kodePosBiaya = pos.kodePosBiaya
                     WHERE     rab.kodeUnit = '16053140'
                           AND tahun = '2010'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab.kodePosBiaya
                  UNION ALL
                    SELECT '2' AS no,
                           'RAB Rapat Kerja' AS nm,
                           rab_rapat.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           if(jumlahHari = 0,
                              sum(harga * kuantitas),
                              sum(harga * kuantitas * jumlahHari))
                              AS anggaran
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.rapatKerjaUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.idRapatKerjaUnit
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'Y'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab_rapat.kodePosBiaya
                  UNION ALL
                    SELECT '3' AS no,
                           pos2.namaPosBiaya AS nm,
                           rab_rapat.kodePosBiaya AS kodepos,
                           pos.namaPosBiaya,
                           sum(if(jumlahHari = 0,
                                  harga * kuantitas,
                                  harga * kuantitas * jumlahHari))
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.kegiatanUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.kodeKegiatanUnit
                           INNER JOIN ppmk.kelompokKegiatan AS klpk
                              ON rpt.kodeKelompokKegiatan = klpk.kodeKelompok
                           INNER JOIN ppmk.kelompokRKA AS kplkRKA
                              ON klpk.kodeKelompok = kplkRKA.kodeKelompokKegiatan
                           INNER JOIN ppmk.posBiaya AS pos2
                              ON kplkRKA.kodePosBiaya = pos2.kodePosBiaya
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'N'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rpt.kodeKelompokKegiatan, rab_rapat.kodePosBiaya)
                 AS anggaran
                 INNER JOIN (  SELECT '1' AS no,
                                      'RAB Non Rapat' AS nm,
                                      dp.kodePosBiaya AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(harga * kuantitasPengajuan)
                                         AS pengajuan,
                                      sum(harga * kuantitasDisetujui) AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodePosBiaya = pos.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya <> 5
                             GROUP BY dp.kodePosBiaya
                             UNION ALL
                               SELECT '2' AS no,
                                      'RAB Rapat Kerja' AS nm,
                                      dp.kodeSub AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS pengajuan,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSub = pos.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya = 5
                             GROUP BY dp.kodePosBiaya, dp.kodeSub
                             UNION ALL
                               SELECT '3' AS no,
                                      pos2.namaPosBiaya,
                                      dp.kodeSub AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS cair,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSub = pos.kodePosbiaya
                                      INNER JOIN ppmk.posBiaya AS pos2
                                         ON dp.kodePosBiaya = pos2.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '2'
                             GROUP BY dp.kodePosBiaya, dp.kodeSub) AS pencairan
                    ON anggaran.no = pencairan.no
                       AND anggaran.kodepos = pencairan.kodepos
                 INNER JOIN (  SELECT '1' AS no,
                                      'RAB Non Rapat' AS nm,
                                      dp.kodePosBiaya AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(harga * kuantitasPengajuan)
                                         AS pengajuan,
                                      sum(harga * kuantitasDisetujui) AS LPJ
                                 FROM ppmk.lpj AS pp
                                      INNER JOIN ppmk.detailPosLPJ AS dp
                                         ON pp.idLPJ = dp.idLPJ
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodePosBiaya = pos.kodePosbiaya
                                WHERE     pp.tahun = '2010'
                                      AND pp.unit = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya <> 5
                             GROUP BY dp.kodePosBiaya
                             UNION ALL
                               SELECT '2' AS no,
                                      'RAB Rapat Kerja' AS nm,
                                      dp.kodeSubPos AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS pengajuan,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.lpj AS pp
                                      INNER JOIN ppmk.detailPosLPJ AS dp
                                         ON pp.idLPJ = dp.idLPJ
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSubPos = pos.kodePosbiaya
                                WHERE     pp.tahun = '2010'
                                      AND pp.unit = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya = 5
                             GROUP BY dp.kodePosBiaya, dp.kodeSubPos
                             UNION ALL
                               SELECT '3' AS no,
                                      pos2.namaPosBiaya AS nm,
                                      dp.kodeSubPos AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS cair,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.lpj AS pp
                                      INNER JOIN ppmk.detailPosLPJ AS dp
                                         ON pp.idLPJ = dp.idLPJ
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSubPos = pos.kodePosbiaya
                                      INNER JOIN ppmk.posBiaya AS pos2
                                         ON dp.kodePosBiaya = pos2.kodePosbiaya
                                WHERE     pp.tahun = '2010'
                                      AND pp.unit = '16053140'
                                      AND dp.kodeJenisAnggaran = '2'
                             GROUP BY dp.kodePosBiaya, dp.kodeSubPos) lapPJ
                    ON pencairan.no = lapPJ.no
                       AND pencairan.kodepos = lapPJ.kodepos
          UNION ALL
--  Bagian Pencairan dan Pengajuan
          SELECT anggaran.no,
                 anggaran.nm,
                 anggaran.kodepos,
                 anggaran.namaPosBiaya,
                 anggaran.anggaran,
                 IF(pencairan.no<=>NULL,'',pencairan.no),
                 IF(pencairan.nm <=> NULL,'',pencairan.nm),
                 IF(pencairan.kodepos <=> NULL,'',pencairan.kodepos),
                 IF(pencairan.namaPosBiaya<=>NULL,'',pencairan.namaPosBiaya),
                 IF(pencairan.pengajuan<=>NULL,'',pencairan.pengajuan),
                 IF(pencairan.cair<=>NULL,'',pencairan.cair),
                 IF(lapPJ.no<=>NULL,'',lapPJ.no),
                 IF(lapPJ.nm<=>NULL,'',lapPJ.nm),
                 IF(lapPJ.kodepos<=>NULL,'',lapPJ.kodepos),
                 IF(lapPJ.namaPosBiaya<=>NULL,'',lapPJ.namaPosBiaya),
                 IF(lapPJ.pengajuan<=>NULL,'',lapPJ.pengajuan),
                 IF(lapPJ.LPJ<=>NULL,'',lapPJ.LPJ)
            FROM (  SELECT '1' AS no,
                           'RAB Non Rapat' AS nm,
                           rab.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           sum(harga * kuantitas) AS anggaran
                      FROM    ppmk.anggaranRABNonRapatKerja AS rab
                           INNER JOIN
                              ppmk.posBiaya AS pos
                           ON rab.kodePosBiaya = pos.kodePosBiaya
                     WHERE     rab.kodeUnit = '16053140'
                           AND tahun = '2010'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab.kodePosBiaya
                  UNION ALL
                    SELECT '2' AS no,
                           'RAB Rapat Kerja' AS nm,
                           rab_rapat.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           if(jumlahHari = 0,
                              sum(harga * kuantitas),
                              sum(harga * kuantitas * jumlahHari))
                              AS anggaran
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.rapatKerjaUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.idRapatKerjaUnit
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'Y'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab_rapat.kodePosBiaya
                  UNION ALL
                    SELECT '3' AS no,
                           pos2.namaPosBiaya,
                           rab_rapat.kodePosBiaya AS kodepos,
                           pos.namaPosBiaya,
                           sum(if(jumlahHari = 0,
                                  harga * kuantitas,
                                  harga * kuantitas * jumlahHari))
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.kegiatanUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.kodeKegiatanUnit
                           INNER JOIN ppmk.kelompokKegiatan AS klpk
                              ON rpt.kodeKelompokKegiatan = klpk.kodeKelompok
                           INNER JOIN ppmk.kelompokRKA AS kplkRKA
                              ON klpk.kodeKelompok = kplkRKA.kodeKelompokKegiatan
                           INNER JOIN ppmk.posBiaya AS pos2
                              ON kplkRKA.kodePosBiaya = pos2.kodePosBiaya
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'N'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rpt.kodeKelompokKegiatan, rab_rapat.kodePosBiaya)
                 AS anggaran
                 LEFT JOIN (  SELECT '1' AS no,
                                     'RAB Non Rapat' AS nm,
                                     dp.kodePosBiaya AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(harga * kuantitasPengajuan) AS pengajuan,
                                     sum(harga * kuantitasDisetujui) AS cair
                                FROM ppmk.pengajuanPengambilan AS pp
                                     INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                        ON pp.idPengajuan = dp.idPengajuan
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodePosBiaya = pos.kodePosbiaya
                               WHERE     pp.tahunAnggaran = '2010'
                                     AND pp.kodeUnitPengaju = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya <> 5
                            GROUP BY dp.kodePosBiaya
                            UNION ALL
                              SELECT '2' AS no,
                                     'RAB Rapat Kerja' AS nm,
                                     dp.kodeSub AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS pengajuan,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.pengajuanPengambilan AS pp
                                     INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                        ON pp.idPengajuan = dp.idPengajuan
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSub = pos.kodePosbiaya
                               WHERE     pp.tahunAnggaran = '2010'
                                     AND pp.kodeUnitPengaju = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya = 5
                            GROUP BY dp.kodePosBiaya, dp.kodeSub
                            UNION ALL
                              SELECT '3' AS no,
                                     pos2.namaPosBiaya AS nm,
                                     dp.kodeSub AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS cair,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.pengajuanPengambilan AS pp
                                     INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                        ON pp.idPengajuan = dp.idPengajuan
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSub = pos.kodePosbiaya
                                     INNER JOIN ppmk.posBiaya AS pos2
                                        ON dp.kodePosBiaya = pos2.kodePosbiaya
                               WHERE     pp.tahunAnggaran = '2010'
                                     AND pp.kodeUnitPengaju = '16053140'
                                     AND dp.kodeJenisAnggaran = '2'
                            GROUP BY dp.kodePosBiaya, dp.kodeSub) AS pencairan
                    ON anggaran.no = pencairan.no
                       AND anggaran.kodepos = pencairan.kodepos
                 LEFT JOIN (  SELECT '1' AS no,
                                     'RAB Non Rapat' AS nm,
                                     dp.kodePosBiaya AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(harga * kuantitasPengajuan) AS pengajuan,
                                     sum(harga * kuantitasDisetujui) AS LPJ
                                FROM ppmk.lpj AS pp
                                     INNER JOIN ppmk.detailPosLPJ AS dp
                                        ON pp.idLPJ = dp.idLPJ
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodePosBiaya = pos.kodePosbiaya
                               WHERE     pp.tahun = '2010'
                                     AND pp.unit = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya <> 5
                            GROUP BY dp.kodePosBiaya
                            UNION ALL
                              SELECT '2' AS no,
                                     'RAB Rapat Kerja' AS nm,
                                     dp.kodeSubPos AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS pengajuan,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.lpj AS pp
                                     INNER JOIN ppmk.detailPosLPJ AS dp
                                        ON pp.idLPJ = dp.idLPJ
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSubPos = pos.kodePosbiaya
                               WHERE     pp.tahun = '2010'
                                     AND pp.unit = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya = 5
                            GROUP BY dp.kodePosBiaya, dp.kodeSubPos
                            UNION ALL
                              SELECT '3' AS no,
                                     pos2.namaPosBiaya AS nm,
                                     dp.kodeSubPos AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS cair,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.lpj AS pp
                                     INNER JOIN ppmk.detailPosLPJ AS dp
                                        ON pp.idLPJ = dp.idLPJ
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSubPos = pos.kodePosbiaya
                                     INNER JOIN ppmk.posBiaya AS pos2
                                        ON dp.kodePosBiaya = pos2.kodePosbiaya
                               WHERE     pp.tahun = '2010'
                                     AND pp.unit = '16053140'
                                     AND dp.kodeJenisAnggaran = '2'
                            GROUP BY dp.kodePosBiaya, dp.kodeSubPos) lapPJ
                    ON anggaran.no = lapPJ.no
                       AND anggaran.kodepos = lapPJ.kodepos
           WHERE lapPJ.no IS NULL AND pencairan.no IS NULL
          UNION ALL
          SELECT anggaran.no,
                 anggaran.nm,
                 anggaran.kodepos,
                 anggaran.namaPosBiaya,
                 anggaran.anggaran ,
                 pencairan.no,
                 pencairan.nm,
                 pencairan.kodepos,
                 pencairan.namaPosBiaya,
                 pencairan.pengajuan,
                 pencairan.cair,
                 lapPJ.no,
                 lapPJ.nm,
                 lapPJ.kodepos,
                 lapPJ.namaPosBiaya,
                 lapPJ.pengajuan,
                 lapPJ.LPJ
            FROM (  SELECT '1' AS no,
                           'RAB Non Rapat' AS nm,
                           rab.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           sum(harga * kuantitas) AS anggaran
                      FROM    ppmk.anggaranRABNonRapatKerja AS rab
                           INNER JOIN
                              ppmk.posBiaya AS pos
                           ON rab.kodePosBiaya = pos.kodePosBiaya
                     WHERE     rab.kodeUnit = '16053140'
                           AND tahun = '2010'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab.kodePosBiaya
                  UNION ALL
                    SELECT '2' AS no,
                           'RAB Rapat Kerja' AS nm,
                           rab_rapat.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           if(jumlahHari = 0,
                              sum(harga * kuantitas),
                              sum(harga * kuantitas * jumlahHari))
                              AS anggaran
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.rapatKerjaUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.idRapatKerjaUnit
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'Y'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab_rapat.kodePosBiaya
                  UNION ALL
                    SELECT '3' AS no,
                           pos2.namaPosBiaya AS nm,
                           rab_rapat.kodePosBiaya AS kodepos,
                           pos.namaPosBiaya,
                           sum(if(jumlahHari = 0,
                                  harga * kuantitas,
                                  harga * kuantitas * jumlahHari))
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.kegiatanUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.kodeKegiatanUnit
                           INNER JOIN ppmk.kelompokKegiatan AS klpk
                              ON rpt.kodeKelompokKegiatan = klpk.kodeKelompok
                           INNER JOIN ppmk.kelompokRKA AS kplkRKA
                              ON klpk.kodeKelompok = kplkRKA.kodeKelompokKegiatan
                           INNER JOIN ppmk.posBiaya AS pos2
                              ON kplkRKA.kodePosBiaya = pos2.kodePosBiaya
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'N'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rpt.kodeKelompokKegiatan, rab_rapat.kodePosBiaya)
                 AS anggaran
                 RIGHT JOIN (  SELECT '1' AS no,
                                      'RAB Non Rapat' AS nm,
                                      dp.kodePosBiaya AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(harga * kuantitasPengajuan)
                                         AS pengajuan,
                                      sum(harga * kuantitasDisetujui) AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodePosBiaya = pos.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya <> 5
                             GROUP BY dp.kodePosBiaya
                             UNION ALL
                               SELECT '2' AS no,
                                      'RAB Rapat Kerja' AS nm,
                                      dp.kodeSub AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS pengajuan,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSub = pos.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya = 5
                             GROUP BY dp.kodePosBiaya, dp.kodeSub
                             UNION ALL
                               SELECT '3' AS no,
                                      pos2.namaPosBiaya AS nm,
                                      dp.kodeSub AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS cair,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSub = pos.kodePosbiaya
                                      INNER JOIN ppmk.posBiaya AS pos2
                                         ON dp.kodePosBiaya = pos2.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '2'
                             GROUP BY dp.kodePosBiaya, dp.kodeSub) AS pencairan
                    ON anggaran.no = pencairan.no
                       AND anggaran.kodepos = pencairan.kodepos
                 LEFT JOIN (  SELECT '1' AS no,
                                     'RAB Non Rapat' AS nm,
                                     dp.kodePosBiaya AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(harga * kuantitasPengajuan) AS pengajuan,
                                     sum(harga * kuantitasDisetujui) AS LPJ
                                FROM ppmk.lpj AS pp
                                     INNER JOIN ppmk.detailPosLPJ AS dp
                                        ON pp.idLPJ = dp.idLPJ
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodePosBiaya = pos.kodePosbiaya
                               WHERE     pp.tahun = '2010'
                                     AND pp.unit = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya <> 5
                            GROUP BY dp.kodePosBiaya
                            UNION ALL
                              SELECT '2' AS no,
                                     'RAB Rapat Kerja' AS nm,
                                     dp.kodeSubPos AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS pengajuan,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.lpj AS pp
                                     INNER JOIN ppmk.detailPosLPJ AS dp
                                        ON pp.idLPJ = dp.idLPJ
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSubPos = pos.kodePosbiaya
                               WHERE     pp.tahun = '2010'
                                     AND pp.unit = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya = 5
                            GROUP BY dp.kodePosBiaya, dp.kodeSubPos
                            UNION ALL
                              SELECT '3' AS no,
                                     pos2.namaPosBiaya AS nm,
                                     dp.kodeSubPos AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS cair,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.lpj AS pp
                                     INNER JOIN ppmk.detailPosLPJ AS dp
                                        ON pp.idLPJ = dp.idLPJ
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSubPos = pos.kodePosbiaya
                                     INNER JOIN ppmk.posBiaya AS pos2
                                        ON dp.kodePosBiaya = pos2.kodePosbiaya
                               WHERE     pp.tahun = '2010'
                                     AND pp.unit = '16053140'
                                     AND dp.kodeJenisAnggaran = '2'
                            GROUP BY dp.kodePosBiaya, dp.kodeSubPos) lapPJ
                    ON pencairan.no = lapPJ.no
                       AND pencairan.kodepos = lapPJ.kodepos
           WHERE lapPJ.no IS NULL AND anggaran.no IS NULL
          UNION ALL
          SELECT anggaran.no,
                 anggaran.nm,
                 anggaran.kodepos,
                 anggaran.namaPosBiaya,
                 anggaran.anggaran,
                 pencairan.no,
                 pencairan.nm,
                 pencairan.kodepos,
                 pencairan.namaPosBiaya,
                 pencairan.pengajuan,
                 pencairan.cair,
                 lapPJ.no,
                 lapPJ.nm,
                 lapPJ.kodepos,
                 IF(lapPJ.namaPosBiaya<=>NULL,'',lapPJ.namaPosBiaya),
                 lapPJ.pengajuan,
                 lapPJ.LPJ
            FROM (  SELECT '1' AS no,
                           'RAB Non Rapat' AS nm,
                           dp.kodePosBiaya AS kodepos,
                           pos.namaPosBiaya,
                           sum(harga * kuantitasPengajuan) AS pengajuan,
                           sum(harga * kuantitasDisetujui) AS LPJ
                      FROM ppmk.lpj AS pp
                           INNER JOIN ppmk.detailPosLPJ AS dp
                              ON pp.idLPJ = dp.idLPJ
                           INNER JOIN ppmk.posBiaya AS pos
                              ON dp.kodePosBiaya = pos.kodePosbiaya
                     WHERE     pp.tahun = '2010'
                           AND pp.unit = '16053140'
                           AND dp.kodeJenisAnggaran = '1'
                           AND dp.kodePosBiaya <> 5
                  GROUP BY dp.kodePosBiaya
                  UNION ALL
                    SELECT '2' AS no,
                           'RAB Rapat Kerja' AS nm,
                           dp.kodeSubPos AS kodepos,
                           pos.namaPosBiaya,
                           sum(if(
                                  jumlahHariPengajuan = 0,
                                  (harga * kuantitasPengajuan),
                                  (  harga
                                   * kuantitasPengajuan
                                   * jumlahHariPengajuan)))
                              AS pengajuan,
                           sum(if(
                                  jumlahHariDisetujui = 0,
                                  (harga * kuantitasDisetujui),
                                  (  harga
                                   * kuantitasDisetujui
                                   * jumlahHariDisetujui)))
                              AS cair
                      FROM ppmk.lpj AS pp
                           INNER JOIN ppmk.detailPosLPJ AS dp
                              ON pp.idLPJ = dp.idLPJ
                           INNER JOIN ppmk.posBiaya AS pos
                              ON dp.kodeSubPos = pos.kodePosbiaya
                     WHERE     pp.tahun = '2010'
                           AND pp.unit = '16053140'
                           AND dp.kodeJenisAnggaran = '1'
                           AND dp.kodePosBiaya = 5
                  GROUP BY dp.kodePosBiaya, dp.kodeSubPos
                  UNION ALL
                    SELECT '3' AS no,
                           pos2.namaPosBiaya AS nm,
                           dp.kodeSubPos AS kodepos,
                           pos.namaPosBiaya,
                           sum(if(
                                  jumlahHariPengajuan = 0,
                                  (harga * kuantitasPengajuan),
                                  (  harga
                                   * kuantitasPengajuan
                                   * jumlahHariPengajuan)))
                              AS cair,
                           sum(if(
                                  jumlahHariDisetujui = 0,
                                  (harga * kuantitasDisetujui),
                                  (  harga
                                   * kuantitasDisetujui
                                   * jumlahHariDisetujui)))
                              AS cair
                      FROM ppmk.lpj AS pp
                           INNER JOIN ppmk.detailPosLPJ AS dp
                              ON pp.idLPJ = dp.idLPJ
                           INNER JOIN ppmk.posBiaya AS pos
                              ON dp.kodeSubPos = pos.kodePosbiaya
                           INNER JOIN ppmk.posBiaya AS pos2
                              ON dp.kodePosBiaya = pos2.kodePosbiaya
                     WHERE     pp.tahun = '2010'
                           AND pp.unit = '16053140'
                           AND dp.kodeJenisAnggaran = '2'
                  GROUP BY dp.kodePosBiaya, dp.kodeSubPos) lapPJ
                 LEFT JOIN (  SELECT '1' AS no,
                                     'RAB Non Rapat' AS nm,
                                     rab.kodePosBiaya AS kodepos,
                                     namaPosBiaya,
                                     sum(harga * kuantitas) AS anggaran
                                FROM    ppmk.anggaranRABNonRapatKerja AS rab
                                     INNER JOIN
                                        ppmk.posBiaya AS pos
                                     ON rab.kodePosBiaya = pos.kodePosBiaya
                               WHERE     rab.kodeUnit = '16053140'
                                     AND tahun = '2010'
                                     AND isRevisi = 'Y'
                                     AND isSudahDiajukan = 'Y'
                            GROUP BY rab.kodePosBiaya
                            UNION ALL
                              SELECT '2' AS no,
                                     'RAB Rapat Kerja' AS nm,
                                     rab_rapat.kodePosBiaya AS kodepos,
                                     namaPosBiaya,
                                     if(jumlahHari = 0,
                                        sum(harga * kuantitas),
                                        sum(harga * kuantitas * jumlahHari))
                                        AS anggaran
                                FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON rab_rapat.kodePosBiaya =
                                              pos.kodePosBiaya
                                     INNER JOIN ppmk.rapatKerjaUnit AS rpt
                                        ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                              rpt.idRapatKerjaUnit
                               WHERE     rpt.kodeUnit = '16053140'
                                     AND rpt.tahun = '2010'
                                     AND isRapat = 'Y'
                                     AND isRevisi = 'Y'
                                     AND isSudahDiajukan = 'Y'
                            GROUP BY rab_rapat.kodePosBiaya
                            UNION ALL
                              SELECT '3' AS no,
                                     pos2.namaPosBiaya AS nm,
                                     rab_rapat.kodePosBiaya AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(jumlahHari = 0,
                                            harga * kuantitas,
                                            harga * kuantitas * jumlahHari))
                                FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON rab_rapat.kodePosBiaya =
                                              pos.kodePosBiaya
                                     INNER JOIN ppmk.kegiatanUnit AS rpt
                                        ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                              rpt.kodeKegiatanUnit
                                     INNER JOIN ppmk.kelompokKegiatan AS klpk
                                        ON rpt.kodeKelompokKegiatan =
                                              klpk.kodeKelompok
                                     INNER JOIN ppmk.kelompokRKA AS kplkRKA
                                        ON klpk.kodeKelompok =
                                              kplkRKA.kodeKelompokKegiatan
                                     INNER JOIN ppmk.posBiaya AS pos2
                                        ON kplkRKA.kodePosBiaya =
                                              pos2.kodePosBiaya
                               WHERE     rpt.kodeUnit = '16053140'
                                     AND rpt.tahun = '2010'
                                     AND isRapat = 'N'
                                     AND isRevisi = 'Y'
                                     AND isSudahDiajukan = 'Y'
                            GROUP BY rpt.kodeKelompokKegiatan,
                                     rab_rapat.kodePosBiaya) AS anggaran
                    ON lapPJ.no = anggaran.no
                       AND lapPJ.kodepos = anggaran.kodepos
                 LEFT JOIN (  SELECT '1' AS no,
                                     'RAB Non Rapat' AS nm,
                                     dp.kodePosBiaya AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(harga * kuantitasPengajuan) AS pengajuan,
                                     sum(harga * kuantitasDisetujui) AS cair
                                FROM ppmk.pengajuanPengambilan AS pp
                                     INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                        ON pp.idPengajuan = dp.idPengajuan
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodePosBiaya = pos.kodePosbiaya
                               WHERE     pp.tahunAnggaran = '2010'
                                     AND pp.kodeUnitPengaju = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya <> 5
                            GROUP BY dp.kodePosBiaya
                            UNION ALL
                              SELECT '2' AS no,
                                     'RAB Rapat Kerja' AS nm,
                                     dp.kodeSub AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS pengajuan,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.pengajuanPengambilan AS pp
                                     INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                        ON pp.idPengajuan = dp.idPengajuan
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSub = pos.kodePosbiaya
                               WHERE     pp.tahunAnggaran = '2010'
                                     AND pp.kodeUnitPengaju = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya = 5
                            GROUP BY dp.kodePosBiaya, dp.kodeSub
                            UNION ALL
                              SELECT '3' AS no,
                                     pos2.namaPosBiaya AS nm,
                                     dp.kodeSub AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS cair,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.pengajuanPengambilan AS pp
                                     INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                        ON pp.idPengajuan = dp.idPengajuan
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSub = pos.kodePosbiaya
                                     INNER JOIN ppmk.posBiaya AS pos2
                                        ON dp.kodePosBiaya = pos2.kodePosbiaya
                               WHERE     pp.tahunAnggaran = '2010'
                                     AND pp.kodeUnitPengaju = '16053140'
                                     AND dp.kodeJenisAnggaran = '2'
                            GROUP BY dp.kodePosBiaya, dp.kodeSub) AS pencairan
                    ON lapPJ.no = pencairan.no
                       AND lapPJ.kodepos = pencairan.kodepos
           WHERE pencairan.no IS NULL AND anggaran.no IS NULL
          UNION ALL
          SELECT anggaran.no,
                 anggaran.nm,
                 anggaran.kodepos,
                 anggaran.namaPosBiaya,
                 anggaran.anggaran,
                 pencairan.no,
                 pencairan.nm,
                 pencairan.kodepos,
                 pencairan.namaPosBiaya,
                 pencairan.pengajuan,
                 pencairan.cair,
                 lapPJ.no,
                 lapPJ.nm,
                 lapPJ.kodepos,
                 lapPJ.namaPosBiaya,
                 lapPJ.pengajuan,
                 lapPJ.LPJ
            FROM (  SELECT '1' AS no,
                           'RAB Non Rapat' AS nm,
                           rab.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           sum(harga * kuantitas) AS anggaran
                      FROM    ppmk.anggaranRABNonRapatKerja AS rab
                           INNER JOIN
                              ppmk.posBiaya AS pos
                           ON rab.kodePosBiaya = pos.kodePosBiaya
                     WHERE     rab.kodeUnit = '16053140'
                           AND tahun = '2010'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab.kodePosBiaya
                  UNION ALL
                    SELECT '2' AS no,
                           'RAB Rapat Kerja' AS nm,
                           rab_rapat.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           if(jumlahHari = 0,
                              sum(harga * kuantitas),
                              sum(harga * kuantitas * jumlahHari))
                              AS anggaran
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.rapatKerjaUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.idRapatKerjaUnit
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'Y'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab_rapat.kodePosBiaya
                  UNION ALL
                    SELECT '3' AS no,
                           pos2.namaPosBiaya,
                           rab_rapat.kodePosBiaya AS kodepos,
                           pos.namaPosBiaya,
                           sum(if(jumlahHari = 0,
                                  harga * kuantitas,
                                  harga * kuantitas * jumlahHari))
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.kegiatanUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.kodeKegiatanUnit
                           INNER JOIN ppmk.kelompokKegiatan AS klpk
                              ON rpt.kodeKelompokKegiatan = klpk.kodeKelompok
                           INNER JOIN ppmk.kelompokRKA AS kplkRKA
                              ON klpk.kodeKelompok = kplkRKA.kodeKelompokKegiatan
                           INNER JOIN ppmk.posBiaya AS pos2
                              ON kplkRKA.kodePosBiaya = pos2.kodePosBiaya
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'N'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rpt.kodeKelompokKegiatan, rab_rapat.kodePosBiaya)
                 AS anggaran
                 INNER JOIN (  SELECT '1' AS no,
                                      'RAB Non Rapat' AS nm,
                                      dp.kodePosBiaya AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(harga * kuantitasPengajuan)
                                         AS pengajuan,
                                      sum(harga * kuantitasDisetujui) AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodePosBiaya = pos.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya <> 5
                             GROUP BY dp.kodePosBiaya
                             UNION ALL
                               SELECT '2' AS no,
                                      'RAB Rapat Kerja' AS nm,
                                      dp.kodeSub AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS pengajuan,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSub = pos.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya = 5
                             GROUP BY dp.kodePosBiaya, dp.kodeSub
                             UNION ALL
                               SELECT '3' AS no,
                                      pos2.namaPosBiaya AS nm,
                                      dp.kodeSub AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS cair,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSub = pos.kodePosbiaya
                                      INNER JOIN ppmk.posBiaya AS pos2
                                         ON dp.kodePosBiaya = pos2.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '2'
                             GROUP BY dp.kodePosBiaya, dp.kodeSub) AS pencairan
                    ON anggaran.no = pencairan.no
                       AND anggaran.kodepos = pencairan.kodepos
                 LEFT JOIN (  SELECT '1' AS no,
                                     'RAB Non Rapat' AS nm,
                                     dp.kodePosBiaya AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(harga * kuantitasPengajuan) AS pengajuan,
                                     sum(harga * kuantitasDisetujui) AS LPJ
                                FROM ppmk.lpj AS pp
                                     INNER JOIN ppmk.detailPosLPJ AS dp
                                        ON pp.idLPJ = dp.idLPJ
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodePosBiaya = pos.kodePosbiaya
                               WHERE     pp.tahun = '2010'
                                     AND pp.unit = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya <> 5
                            GROUP BY dp.kodePosBiaya
                            UNION ALL
                              SELECT '2' AS no,
                                     'RAB Rapat Kerja' AS nm,
                                     dp.kodeSubPos AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS pengajuan,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.lpj AS pp
                                     INNER JOIN ppmk.detailPosLPJ AS dp
                                        ON pp.idLPJ = dp.idLPJ
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSubPos = pos.kodePosbiaya
                               WHERE     pp.tahun = '2010'
                                     AND pp.unit = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya = 5
                            GROUP BY dp.kodePosBiaya, dp.kodeSubPos
                            UNION ALL
                              SELECT '3' AS no,
                                     pos2.namaPosBiaya AS nm,
                                     dp.kodeSubPos AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS cair,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.lpj AS pp
                                     INNER JOIN ppmk.detailPosLPJ AS dp
                                        ON pp.idLPJ = dp.idLPJ
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSubPos = pos.kodePosbiaya
                                     INNER JOIN ppmk.posBiaya AS pos2
                                        ON dp.kodePosBiaya = pos2.kodePosbiaya
                               WHERE     pp.tahun = '2010'
                                     AND pp.unit = '16053140'
                                     AND dp.kodeJenisAnggaran = '2'
                            GROUP BY dp.kodePosBiaya, dp.kodeSubPos) lapPJ
                    ON anggaran.no = lapPJ.no
                       AND anggaran.kodepos = lapPJ.kodepos
           WHERE lapPJ.no IS NULL
          UNION ALL
--BAGIAN ANGGARAN
          SELECT IF(anggaran.no<=>NULL,'',anggaran.no),
                 IF(anggaran.nm<=>NULL,'',anggaran.nm),
                 IF(anggaran.kodepos<=>NULL,'',anggaran.kodepos),
                 IF(anggaran.namaPosBiaya<=>NULL,'',anggaran.namaPosBiaya),
                 IF(anggaran.anggaran<=>NULL,'0',anggaran.anggaran),
                 pencairan.no,
                 pencairan.nm,
                 pencairan.kodepos,
                 pencairan.namaPosBiaya,
                 pencairan.pengajuan,
                 pencairan.cair,
                 lapPJ.no,
                 lapPJ.nm,
                 lapPJ.kodepos,
                 lapPJ.namaPosBiaya,
                 lapPJ.pengajuan,
                 lapPJ.LPJ
            FROM (  SELECT '1' AS no,
                           'RAB Non Rapat' AS nm,
                           rab.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           sum(harga * kuantitas) AS anggaran
                      FROM    ppmk.anggaranRABNonRapatKerja AS rab
                           INNER JOIN
                              ppmk.posBiaya AS pos
                           ON rab.kodePosBiaya = pos.kodePosBiaya
                     WHERE     rab.kodeUnit = '16053140'
                           AND tahun = '2010'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab.kodePosBiaya
                  UNION ALL
                    SELECT '2' AS no,
                           'RAB Rapat Kerja' AS nm,
                           rab_rapat.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           if(jumlahHari = 0,
                              sum(harga * kuantitas),
                              sum(harga * kuantitas * jumlahHari))
                              AS anggaran
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.rapatKerjaUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.idRapatKerjaUnit
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'Y'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab_rapat.kodePosBiaya
                  UNION ALL
                    SELECT '3' AS no,
                           pos2.namaPosBiaya,
                           rab_rapat.kodePosBiaya AS kodepos,
                           pos.namaPosBiaya,
                           sum(if(jumlahHari = 0,
                                  harga * kuantitas,
                                  harga * kuantitas * jumlahHari))
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.kegiatanUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.kodeKegiatanUnit
                           INNER JOIN ppmk.kelompokKegiatan AS klpk
                              ON rpt.kodeKelompokKegiatan = klpk.kodeKelompok
                           INNER JOIN ppmk.kelompokRKA AS kplkRKA
                              ON klpk.kodeKelompok = kplkRKA.kodeKelompokKegiatan
                           INNER JOIN ppmk.posBiaya AS pos2
                              ON kplkRKA.kodePosBiaya = pos2.kodePosBiaya
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'N'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rpt.kodeKelompokKegiatan, rab_rapat.kodePosBiaya)
                 AS anggaran
                 RIGHT JOIN (  SELECT '1' AS no,
                                      'RAB Non Rapat' AS nm,
                                      dp.kodePosBiaya AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(harga * kuantitasPengajuan)
                                         AS pengajuan,
                                      sum(harga * kuantitasDisetujui) AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodePosBiaya = pos.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya <> 5
                             GROUP BY dp.kodePosBiaya
                             UNION ALL
                               SELECT '2' AS no,
                                      'RAB Rapat Kerja' AS nm,
                                      dp.kodeSub AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS pengajuan,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSub = pos.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya = 5
                             GROUP BY dp.kodePosBiaya, dp.kodeSub
                             UNION ALL
                               SELECT '3' AS no,
                                      pos2.namaPosBiaya AS nm,
                                      dp.kodeSub AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS cair,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.pengajuanPengambilan AS pp
                                      INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                         ON pp.idPengajuan = dp.idPengajuan
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSub = pos.kodePosbiaya
                                      INNER JOIN ppmk.posBiaya AS pos2
                                         ON dp.kodePosBiaya = pos2.kodePosbiaya
                                WHERE     pp.tahunAnggaran = '2010'
                                      AND pp.kodeUnitPengaju = '16053140'
                                      AND dp.kodeJenisAnggaran = '2'
                             GROUP BY dp.kodePosBiaya, dp.kodeSub) AS pencairan
                    ON anggaran.no = pencairan.no
                       AND anggaran.kodepos = pencairan.kodepos
                 INNER JOIN (  SELECT '1' AS no,
                                      'RAB Non Rapat' AS nm,
                                      dp.kodePosBiaya AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(harga * kuantitasPengajuan)
                                         AS pengajuan,
                                      sum(harga * kuantitasDisetujui) AS LPJ
                                 FROM ppmk.lpj AS pp
                                      INNER JOIN ppmk.detailPosLPJ AS dp
                                         ON pp.idLPJ = dp.idLPJ
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodePosBiaya = pos.kodePosbiaya
                                WHERE     pp.tahun = '2010'
                                      AND pp.unit = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya <> 5
                             GROUP BY dp.kodePosBiaya
                             UNION ALL
                               SELECT '2' AS no,
                                      'RAB Rapat Kerja' AS nm,
                                      dp.kodeSubPos AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS pengajuan,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.lpj AS pp
                                      INNER JOIN ppmk.detailPosLPJ AS dp
                                         ON pp.idLPJ = dp.idLPJ
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSubPos = pos.kodePosbiaya
                                WHERE     pp.tahun = '2010'
                                      AND pp.unit = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya = 5
                             GROUP BY dp.kodePosBiaya, dp.kodeSubPos
                             UNION ALL
                               SELECT '3' AS no,
                                      pos2.namaPosBiaya AS nm,
                                      dp.kodeSubPos AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS cair,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.lpj AS pp
                                      INNER JOIN ppmk.detailPosLPJ AS dp
                                         ON pp.idLPJ = dp.idLPJ
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSubPos = pos.kodePosbiaya
                                      INNER JOIN ppmk.posBiaya AS pos2
                                         ON dp.kodePosBiaya = pos2.kodePosbiaya
                                WHERE     pp.tahun = '2010'
                                      AND pp.unit = '16053140'
                                      AND dp.kodeJenisAnggaran = '2'
                             GROUP BY dp.kodePosBiaya, dp.kodeSubPos) lapPJ
                    ON pencairan.no = lapPJ.no
                       AND pencairan.kodepos = lapPJ.kodepos
           WHERE anggaran.no IS NULL
          UNION ALL
          SELECT anggaran.no,
                 anggaran.nm,
                 anggaran.kodepos,
                 anggaran.namaPosBiaya,
                 anggaran.anggaran,
                 pencairan.no,
                 pencairan.nm,
                 pencairan.kodepos,
                 pencairan.namaPosBiaya,
                 pencairan.pengajuan,
                 pencairan.cair,
                 lapPJ.no,
                 lapPJ.nm,
                 lapPJ.kodepos,
                 lapPJ.namaPosBiaya,
                 lapPJ.pengajuan,
                 lapPJ.LPJ
            FROM (  SELECT '1' AS no,
                           'RAB Non Rapat' AS nm,
                           rab.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           sum(harga * kuantitas) AS anggaran
                      FROM    ppmk.anggaranRABNonRapatKerja AS rab
                           INNER JOIN
                              ppmk.posBiaya AS pos
                           ON rab.kodePosBiaya = pos.kodePosBiaya
                     WHERE     rab.kodeUnit = '16053140'
                           AND tahun = '2010'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab.kodePosBiaya
                  UNION ALL
                    SELECT '2' AS no,
                           'RAB Rapat Kerja' AS nm,
                           rab_rapat.kodePosBiaya AS kodepos,
                           namaPosBiaya,
                           if(jumlahHari = 0,
                              sum(harga * kuantitas),
                              sum(harga * kuantitas * jumlahHari))
                              AS anggaran
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.rapatKerjaUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.idRapatKerjaUnit
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'Y'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rab_rapat.kodePosBiaya
                  UNION ALL
                    SELECT '3' AS no,
                           pos2.namaPosBiaya,
                           rab_rapat.kodePosBiaya AS kodepos,
                           pos.namaPosBiaya,
                           sum(if(jumlahHari = 0,
                                  harga * kuantitas,
                                  harga * kuantitas * jumlahHari))
                      FROM ppmk.anggaranBiayaRapatKerjaDanKegiatan AS rab_rapat
                           INNER JOIN ppmk.posBiaya AS pos
                              ON rab_rapat.kodePosBiaya = pos.kodePosBiaya
                           INNER JOIN ppmk.kegiatanUnit AS rpt
                              ON rab_rapat.kodeRapatKerjaSubKegiatan =
                                    rpt.kodeKegiatanUnit
                           INNER JOIN ppmk.kelompokKegiatan AS klpk
                              ON rpt.kodeKelompokKegiatan = klpk.kodeKelompok
                           INNER JOIN ppmk.kelompokRKA AS kplkRKA
                              ON klpk.kodeKelompok = kplkRKA.kodeKelompokKegiatan
                           INNER JOIN ppmk.posBiaya AS pos2
                              ON kplkRKA.kodePosBiaya = pos2.kodePosBiaya
                     WHERE     rpt.kodeUnit = '16053140'
                           AND rpt.tahun = '2010'
                           AND isRapat = 'N'
                           AND isRevisi = 'Y'
                           AND isSudahDiajukan = 'Y'
                  GROUP BY rpt.kodeKelompokKegiatan, rab_rapat.kodePosBiaya)
                 AS anggaran
                 INNER JOIN (  SELECT '1' AS no,
                                      'RAB Non Rapat' AS nm,
                                      dp.kodePosBiaya AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(harga * kuantitasPengajuan)
                                         AS pengajuan,
                                      sum(harga * kuantitasDisetujui) AS LPJ
                                 FROM ppmk.lpj AS pp
                                      INNER JOIN ppmk.detailPosLPJ AS dp
                                         ON pp.idLPJ = dp.idLPJ
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodePosBiaya = pos.kodePosbiaya
                                WHERE     pp.tahun = '2010'
                                      AND pp.unit = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya <> 5
                             GROUP BY dp.kodePosBiaya
                             UNION ALL
                               SELECT '2' AS no,
                                      'RAB Rapat Kerja' AS nm,
                                      dp.kodeSubPos AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS pengajuan,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.lpj AS pp
                                      INNER JOIN ppmk.detailPosLPJ AS dp
                                         ON pp.idLPJ = dp.idLPJ
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSubPos = pos.kodePosbiaya
                                WHERE     pp.tahun = '2010'
                                      AND pp.unit = '16053140'
                                      AND dp.kodeJenisAnggaran = '1'
                                      AND dp.kodePosBiaya = 5
                             GROUP BY dp.kodePosBiaya, dp.kodeSubPos
                             UNION ALL
                               SELECT '3' AS no,
                                      pos2.namaPosBiaya AS nm,
                                      dp.kodeSubPos AS kodepos,
                                      pos.namaPosBiaya,
                                      sum(if(
                                             jumlahHariPengajuan = 0,
                                             (harga * kuantitasPengajuan),
                                             (  harga
                                              * kuantitasPengajuan
                                              * jumlahHariPengajuan)))
                                         AS cair,
                                      sum(if(
                                             jumlahHariDisetujui = 0,
                                             (harga * kuantitasDisetujui),
                                             (  harga
                                              * kuantitasDisetujui
                                              * jumlahHariDisetujui)))
                                         AS cair
                                 FROM ppmk.lpj AS pp
                                      INNER JOIN ppmk.detailPosLPJ AS dp
                                         ON pp.idLPJ = dp.idLPJ
                                      INNER JOIN ppmk.posBiaya AS pos
                                         ON dp.kodeSubPos = pos.kodePosbiaya
                                      INNER JOIN ppmk.posBiaya AS pos2
                                         ON dp.kodePosBiaya = pos2.kodePosbiaya
                                WHERE     pp.tahun = '2010'
                                      AND pp.unit = '16053140'
                                      AND dp.kodeJenisAnggaran = '2'
                             GROUP BY dp.kodePosBiaya, dp.kodeSubPos) lapPJ
                    ON anggaran.no = lapPJ.no
                       AND anggaran.kodepos = lapPJ.kodepos
                 LEFT JOIN (  SELECT '1' AS no,
                                     'RAB Non Rapat' AS nm,
                                     dp.kodePosBiaya AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(harga * kuantitasPengajuan) AS pengajuan,
                                     sum(harga * kuantitasDisetujui) AS cair
                                FROM ppmk.pengajuanPengambilan AS pp
                                     INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                        ON pp.idPengajuan = dp.idPengajuan
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodePosBiaya = pos.kodePosbiaya
                               WHERE     pp.tahunAnggaran = '2010'
                                     AND pp.kodeUnitPengaju = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya <> 5
                            GROUP BY dp.kodePosBiaya
                            UNION ALL
                              SELECT '2' AS no,
                                     'RAB Rapat Kerja' AS nm,
                                     dp.kodeSub AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS pengajuan,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.pengajuanPengambilan AS pp
                                     INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                        ON pp.idPengajuan = dp.idPengajuan
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSub = pos.kodePosbiaya
                               WHERE     pp.tahunAnggaran = '2010'
                                     AND pp.kodeUnitPengaju = '16053140'
                                     AND dp.kodeJenisAnggaran = '1'
                                     AND dp.kodePosBiaya = 5
                            GROUP BY dp.kodePosBiaya, dp.kodeSub
                            UNION ALL
                              SELECT '3' AS no,
                                     pos2.namaPosBiaya AS nm,
                                     dp.kodeSub AS kodepos,
                                     pos.namaPosBiaya,
                                     sum(if(
                                            jumlahHariPengajuan = 0,
                                            (harga * kuantitasPengajuan),
                                            (  harga
                                             * kuantitasPengajuan
                                             * jumlahHariPengajuan)))
                                        AS cair,
                                     sum(if(
                                            jumlahHariDisetujui = 0,
                                            (harga * kuantitasDisetujui),
                                            (  harga
                                             * kuantitasDisetujui
                                             * jumlahHariDisetujui)))
                                        AS cair
                                FROM ppmk.pengajuanPengambilan AS pp
                                     INNER JOIN ppmk.detailPengajuanPengambilan AS dp
                                        ON pp.idPengajuan = dp.idPengajuan
                                     INNER JOIN ppmk.posBiaya AS pos
                                        ON dp.kodeSub = pos.kodePosbiaya
                                     INNER JOIN ppmk.posBiaya AS pos2
                                        ON dp.kodePosBiaya = pos2.kodePosbiaya
                               WHERE     pp.tahunAnggaran = '2010'
                                     AND pp.kodeUnitPengaju = '16053140'
                                     AND dp.kodeJenisAnggaran = '2'
                            GROUP BY dp.kodePosBiaya, dp.kodeSub) AS pencairan
                    ON anggaran.no = pencairan.no
                       AND anggaran.kodepos = pencairan.kodepos
           WHERE pencairan.no IS NULL) AS subRekap
ORDER BY no1,namaPosBiaya1