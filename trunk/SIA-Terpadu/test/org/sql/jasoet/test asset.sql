select abi.`kodeBarangInvestasi`, abi.`kodeKepemilikan`, abi.`nomorInventaris`, abi.`kodeSubKelompokBarangInvestasi`,
abi.`kodeMerkBarangInvestasi`,ksubi.`subKelompokBarangInvestasi`,ksubi.`kodeKelompokBarangInvestasi`,kbi.`kelompokBarangInvestasi`,
kmbi.`namaMerkBarangInvestasi`,am.kepemilikan,pbi.`idPenggunaanRuang`,ku.`Nama_unit_kerja`
from asset.barangInvestasi abi
JOIN kamus.subKelompokBarangInvestasi ksubi on (abi.kodeSubKelompokBarangInvestasi=ksubi.kodeSubKelompokBarangInvestasi)
LEFT JOIN kamus.kelompokBarangInvestasi kbi on (ksubi.kodeKelompokBarangInvestasi=kbi.kodeKelompokBarangInvestasi)
left JOIN kamus.MerkBarangInvestasi kmbi on (abi.kodeMerkBarangInvestasi=kmbi.kodeMerkBarangInvestasi)
LEFT JOIN asset.kepemilikan am on abi.kodeKepemilikan=am.kodeKepemilikanUser
LEFT JOIN asset.penempatanBarangInvestasi pbi on abi.kodeBarangInvestasi=pbi.kodeBarangInvestasi
LEFT JOIN kamus.unkerja ku on pbi.idPenggunaanRuang=ku.idUnitKerja
order by abi.`kodeBarangInvestasi`,abi.kodeSubKelompokBarangInvestasi