SELECT  'Barang Investasi', subkelompokbaranginvestasi.subKelompokBarangInvestasi as nama, COUNT(baranginvestasi.`kodeSubKelompokBarangInvestasi`)as keterangan
FROM
 asset.baranginvestasi baranginvestasi
 INNER JOIN
 kamus.subkelompokbaranginvestasi subkelompokbaranginvestasi
 ON (subkelompokbaranginvestasi.kodeSubKelompokBarangInvestasi = baranginvestasi.kodeSubKelompokBarangInvestasi)
GROUP BY baranginvestasi.`kodeSubKelompokBarangInvestasi`
HAVING COUNT(baranginvestasi.`kodeSubKelompokBarangInvestasi`) >= 1
UNION
SELECT 'Gedung',al.namaLokasi as nama,al.luasLokasi AS keterangan
                FROM asset.lokasi al
UNION
SELECT 'Kendaraan', ak.type as nama, CAST(COUNT(ak.type) AS SIGNED) AS keterangan
FROM asset.kendaraan ak
GROUP BY ak.type
UNION
SELECT 'Tanah',ast.namaTanah as nama, ast.luasTanah as keterangan FROM asset.tanah ast

