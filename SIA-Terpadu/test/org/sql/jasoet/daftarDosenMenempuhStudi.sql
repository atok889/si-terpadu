CREATE OR REPLACE VIEW tempo.studidosen (Nama_peg,Kd_unit_kerja,Kd_jenjang,Nama_unit_kerja,Nm_jenjang,Univ) AS 
select distinct(pp.`Nama_peg`),
ku.`Kd_unit_kerja`,kj.Kd_jenjang,
ku.`Nama_unit_kerja`, kj.`Nm_jenjang`,ps.Univ FROM
 personalia.studi ps
INNER JOIN personalia.unit_peg pu ON ps.`NPP` = pu.npp
INNER JOIN kamus.unkerja ku ON ku.`Kd_unit_kerja` = pu.kd_unit
INNER JOIN personalia.pegawai pp ON pp.`NPP` = ps.`NPP`
INNER JOIN kamus.jenjang kj ON kj.`Kd_jenjang` = ps.`Jenjang`
WHERE ps.`statusLulus` like '%N%'
AND (ps.Tgl_selesai_studi is not null or ps.Tgl_selesai_studi like '%00%')
AND (pp.Status_keluar like '1' or pp.Status_keluar like '6' or pp.Status_keluar like '7')
order by pp.Nama_peg


select * from tempo.studidosen sd
where sd.Kd_jenjang in (select max(sd.Kd_jenjang) from tempo.studidosen )
Group by sd.Nama_peg





