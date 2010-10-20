select  up.npp,MAX(up.tgl_sk_unit),up.kd_unit,uk.Nama_unit_kerja,pp.Nama_peg,
MAX(cast(ppp.Kd_pang as UNSIGNED)) as kode_pang,ppp.NPP,kp.Nama_pang
from personalia.unit_peg up
LEFT OUTER JOIN kamus.unkerja uk ON (up.kd_unit = uk.Kd_unit_kerja )
INNER JOIN personalia.pegawai pp ON (up.NPP = pp.npp and pp.AdmEdu = '2')
INNer JOIN personalia.pangkat_peg ppp ON (up.npp=ppp.NPP )
INNER JOIN kamus.pangkat kp ON (kp.Kd_pang = ppp.Kd_pang)
group by up.npp  order by up.npp