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
        imovrka.noIsian,
        imovrka.isianMonevinRKA,
        SUM(IF(imovrka.skorPascaMonev <=> null,0.0,imovrka.skorPascaMonev)) AS skorPascaMonev,
        SUM(IF(imovrka.skorPraMonev <=> null,0.0,imovrka.skorPraMonev)) AS skorPraMonev
FROM mutu.isianmonevinrka imovrka
WHERE imovrka.tahun BETWEEN '2009' AND '2010' group by imovrka.tahun;

--Perbandingan total skor antar unit kerja dan prodi pada tahun terakhir
SELECT
        imovrka.tahun,
        imovrka.kodeUnit,
        imovrka.noIsian,
        imovrka.isianMonevinRKA,
        SUM(IF(imovrka.skorPascaMonev <=> null,0.0,imovrka.skorPascaMonev)) AS skorPascaMonev,
        SUM(IF(imovrka.skorPraMonev <=> null,0.0,imovrka.skorPraMonev)) AS skorPraMonev
FROM mutu.isianmonevinrka imovrka
WHERE  imovrka.tahun='2010' group by imovrka.kodeUnit;
