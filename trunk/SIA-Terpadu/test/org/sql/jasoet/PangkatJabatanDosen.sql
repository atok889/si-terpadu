--View PANGKAT DAN JABATAN DOSEN--
CREATE OR REPLACE VIEW tempo.pangkatjabatandosen(npp,tgl_sk,kd_unit,Nama_unit_kerja,Nama_peg,
kode_pang) AS
SELECT up.npp,MAX(up.tgl_sk_unit)as tgl_sk,up.kd_unit,uk.Nama_unit_kerja,pp.Nama_peg,
MAX(cast(ppp.Kd_pang as UNSIGNED)) as kode_pang
from personalia.unit_peg up
LEFT OUTER JOIN kamus.unkerja uk ON (up.kd_unit = uk.Kd_unit_kerja )
INNER JOIN personalia.pegawai pp ON (up.NPP = pp.npp and pp.AdmEdu = '2')
INNer JOIN personalia.pangkat_peg ppp ON (up.npp=ppp.NPP )
INNER JOIN kamus.pangkat kp ON (kp.Kd_pang = ppp.Kd_pang)
group by up.npp  order by up.npp

--SELECT NAMA PANGKAT
select npp,tgl_sk,kd_unit,Nama_unit_kerja,Nama_peg,
kode_pang, kp.Nama_pang from tempo.pangkatjabatandosen, kamus.pangkat kp
where tempo.pangkatjabatandosen.kode_pang=kp.Kd_pang