--View PANGKAT  DOSEN--
CREATE OR REPLACE VIEW tempo.pangkatdosen(npp,Nama_peg,umur,tgl_sk,kd_unit,Nama_unit_kerja,
kode_pang ) AS
SELECT up.npp,pp.Nama_peg,year(CURDATE()) - year(pp.Tgl_lahir) as umur,MAX(up.tgl_sk_unit)as tgl_sk,up.kd_unit,uk.Nama_unit_kerja,
MAX(cast(ppp.Kd_pang as UNSIGNED)) as kode_pang
from personalia.unit_peg up
LEFT OUTER JOIN kamus.unkerja uk ON (up.kd_unit = uk.Kd_unit_kerja )
INNER JOIN personalia.pegawai pp ON (up.NPP = pp.npp and pp.AdmEdu = '2')
INNer JOIN personalia.pangkat_peg ppp ON (up.npp=ppp.NPP )
INNER JOIN kamus.pangkat kp ON (kp.Kd_pang = ppp.Kd_pang);
group by up.npp  order by up.npp

--SELECT NAMA PANGKAT
select pd.npp,pd.Nama_peg,pd.umur,pd.tgl_sk,pd.kd_unit,pd.Nama_unit_kerja,
pd.kode_pang,  kp.Nama_pang
from tempo.pangkatdosen pd
inner join kamus.pangkat kp on (pd.kode_pang=kp.Kd_pang);

--VIEW JABATAN DOSEN
CREATE OR REPLACE VIEW tempo.jabatandosen(NPP,Nama_peg,umur,
`kd_unit`,`nama_unit_kerja`, Kd_jabak  ) AS
(select pjap.`NPP`, pp.`Nama_peg`, year(CURDATE()) - year(pp.Tgl_lahir) as umur,
ppu.`kd_unit`,ku.`nama_unit_kerja`,
 MAX(CAST(pjap.Kd_Jabak as UNSIGNED)) AS Kd_jabak
from personalia.jab_akad_pegawai pjap
INNER JOIN personalia.unit_peg ppu on pjap.`NPP`=ppu.`npp`
INNER JOIN personalia.pegawai pp on pjap.`NPP`=pp.`NPP`
LEFT OUTER JOIN kamus.unkerja ku on ku.`kd_unit_kerja`=ppu.`kd_unit`
INNER JOIN kamus.jab_akad kja on kja.`kd_jab_akad` =pjap.`Kd_jabak`
group by pjap.npp  order by pjap.npp );

--SELECT NAMA PANGKAT
select NPP as npp,Nama_peg as nama,umur as umur,
 `kd_unit` as kodeunitkerja,`nama_unit_kerja` as namaunitkerja,
Kd_jabak as kodejabatan , kja.Nama_jab_akad as namajabatan
 From tempo.jabatandosen tjd
 INNER JOIN kamus.jab_akad kja on (tjd.Kd_jabak=kja.Kd_jab_akad);

--SELECT and Count By KodeJabatan
select count(tjd.Kd_jabak) as jumlah ,NPP as npp,Nama_peg as nama,umur as umur,
 `kd_unit` as kodeunitkerja,`nama_unit_kerja` as namaunitkerja,
Kd_jabak as kodejabatan , kja.Nama_jab_akad as namajabatan
 From tempo.jabatandosen tjd
 INNER JOIN kamus.jab_akad kja on (tjd.Kd_jabak=kja.Kd_jab_akad)
group by tjd.Kd_jabak;

--SELECT and Count By KodeJabatan per Fakultas
select count(tjd.Kd_jabak) as jumlah ,NPP as npp,Nama_peg as nama,umur as umur,
 `kd_unit` as kodeunitkerja,`nama_unit_kerja` as namaunitkerja,
Kd_jabak as kodejabatan , kja.Nama_jab_akad as namajabatan
 From tempo.jabatandosen tjd
 INNER JOIN kamus.jab_akad kja on (tjd.Kd_jabak=kja.Kd_jab_akad)
where tjd.kd_unit LIKE '1601%'
group by tjd.Kd_jabak;

--Fakultas
SELECT SUBSTRING_INDEX(ku.Kd_unit_kerja,'0',2) as prefix,ku.Kd_unit_kerja as kodeUnitKerja,
ku.Nama_unit_kerja as nama from kamus.unkerja ku
where ku.Nama_unit_kerja like 'FAKULTAS%';

--Count Pangkat
select count(pd.kode_pang) as jumlah,pd.npp,pd.Nama_peg,pd.umur,pd.tgl_sk,pd.kd_unit,pd.Nama_unit_kerja,
pd.kode_pang,  kp.Nama_pang
from tempo.pangkatdosen pd
inner join kamus.pangkat kp on (pd.kode_pang=kp.Kd_pang)
group by pd.kode_pang;

--Count Pangkat By Fakultas
select count(pd.kode_pang) as jumlah,pd.npp,pd.Nama_peg,pd.umur,pd.tgl_sk,pd.kd_unit,pd.Nama_unit_kerja,
pd.kode_pang,  kp.Nama_pang
from tempo.pangkatdosen pd
inner join kamus.pangkat kp on (pd.kode_pang=kp.Kd_pang)
where pd.kd_unit LIKE '1601%'
group by pd.kode_pang;