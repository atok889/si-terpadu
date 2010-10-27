SELECT  baranginvestasi.kodeBarangInvestasi
 , baranginvestasi.nomorInventaris
 , baranginvestasi.kodeSubKelompokBarangInvestasi
 , subkelompokbaranginvestasi.subKelompokBarangInvestasi, COUNT(baranginvestasi.`kodeSubKelompokBarangInvestasi`)as Jumlah_barang
FROM
 asset.baranginvestasi baranginvestasi
 INNER JOIN
 kamus.subkelompokbaranginvestasi subkelompokbaranginvestasi
 ON (subkelompokbaranginvestasi.kodeSubKelompokBarangInvestasi = baranginvestasi.kodeSubKelompokBarangInvestasi)
GROUP BY baranginvestasi.`kodeSubKelompokBarangInvestasi`
HAVING COUNT(baranginvestasi.`kodeSubKelompokBarangInvestasi`) >= 1