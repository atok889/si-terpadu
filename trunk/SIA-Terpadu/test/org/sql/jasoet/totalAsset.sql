SELECT  'Barang Investasi' AS jenis, subkelompokbaranginvestasi.subKelompokBarangInvestasi as nama,CONCAT(COUNT(baranginvestasi.`kodeSubKelompokBarangInvestasi`),' Unit') as keterangan
FROM
 asset.baranginvestasi baranginvestasi
 INNER JOIN
 kamus.subkelompokbaranginvestasi subkelompokbaranginvestasi
 ON (subkelompokbaranginvestasi.kodeSubKelompokBarangInvestasi = baranginvestasi.kodeSubKelompokBarangInvestasi)
GROUP BY baranginvestasi.`kodeSubKelompokBarangInvestasi`
HAVING COUNT(baranginvestasi.`kodeSubKelompokBarangInvestasi`) >= 1
UNION
SELECT 'Gedung' AS jenis,al.namaLokasi as nama,CONCAT(al.luasLokasi,' m2')  AS keterangan
                FROM asset.lokasi al
UNION
SELECT 'Kendaraan' AS jenis, ak.type as nama, CONCAT(CAST(COUNT(ak.type) AS SIGNED),' Unit') AS keterangan
FROM asset.kendaraan ak
GROUP BY ak.type
UNION
SELECT 'Tanah' AS jenis,ast.namaTanah as nama, CONCAT(ast.luasTanah, ' m2') as keterangan FROM asset.tanah ast

