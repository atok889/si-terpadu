--Perbandingan pra dan pasca monev
--parameter unit kerja atau prodi..
SELECT
        imovrka.tahun,
        imovrka.kodeUnit,
        imovrka.noIsian,
        imovrka.isianMonevinRKA,
        IF(imovrka.skorPascaMonev <=> null,0.0,imovrka.skorPascaMonev) AS skorPascaMonev,
        IF(imovrka.skorPraMonev <=> null,0.0,imovrka.skorPraMonev) AS skorPraMonev
FROM mutu.isianmonevinrka imovrka 
WHERE imovrka.kodeUnit LIKE '%1605%';

--Perbandingan total  skor dari tahun ke tahun dg batas minimum 2005, 
--parameter prodi saja..
SELECT
        imovrka.tahun,
        imovrka.kodeUnit,
        SUM(IF(imovrka.skorPascaMonev <=> null,0.0,imovrka.skorPascaMonev)) AS skor 
FROM mutu.isianmonevinrka imovrka
WHERE imovrka.tahun BETWEEN (YEAR(NOW())-5) AND YEAR(NOW()) AND imovrka.kodeUnit LIKE '%5214%'
group by imovrka.tahun;

--Perbandingan total skor antar unit kerja dan prodi pada tahun terakhir
SELECT
        imovrka.kodeUnit,
        unkerja.Nama_unit_kerja as nama,
        SUM(IF(imovrka.skorPascaMonev <=> null,0.0,imovrka.skorPascaMonev)) AS skor
FROM mutu.isianmonevinrka imovrka
INNER JOIN kamus.unkerja unkerja on (unkerja.Kd_unit_kerja = imovrka.kodeUnit)
WHERE  imovrka.tahun=YEAR(NOW()) group by imovrka.kodeUnit;
