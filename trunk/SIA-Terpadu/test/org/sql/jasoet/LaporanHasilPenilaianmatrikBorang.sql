--Perbandingan pra dan pasca monev
--parameter unit kerja atau prodi..
SELECT imovrka.kodeUnit,
       imovrka.noIsian,
       imovrka.isianMonevinRKA as NamaKomponen,
       imovrka.skorPraMonev,
       imovrka.skorPascaMonev
  FROM mutu.isianMonevInRKA imovrka
 WHERE imovrka.kodeUnit LIKE '%1605%'

--Perbandingan total  skor dari tahun ke tahun dg batas minimum 2005, 
--parameter prodi saja..
SELECT imovrka.tahun,
      imovrka.kodeUnit,
       imovrka.noIsian,
       imovrka.isianMonevinRKA,
       imovrka.skorPascaMonev
  FROM mutu.isianMonevInRKA imovrka

WHERE imovrka.tahun BETWEEN '2009' AND '2010'

--Perbandingan total skor antar unit kerja dan prodi pada tahun terakhir
SELECT imovrka.kodeUnit,
       imovrka.tahun
       imovrka.noIsian,
       imovrka.isianMonevinRKA,
       imovrka.skorPascaMonev
  FROM mutu.isianMonevInRKA imovrka
 WHERE imovrka.kodeUnit LIKE '%1605%' AND imovrka.tahun=2010
