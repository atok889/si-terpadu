SELECT
cuti2008.kdPegawai,
pegawai.Nama_peg,
kamus.unkerja.nama_unit_kerja,
SUM(kamus.jns_cuti.jatah_cuti - `cuti2008`.`lama_cuti`) AS SISA_CUTI_TAHUNAN,
unit_peg.kd_unit,
`cuti2008`.`lama_cuti`,
kamus.jns_cuti.nama_cuti
FROM
personalia.cuti2008
LEFT OUTER JOIN personalia.pegawai ON (cuti2008.kdPegawai = pegawai.kdPegawai)
LEFT OUTER JOIN personalia.unit_peg ON (cuti2008.kdPegawai = unit_peg.kdPegawai)
INNER JOIN kamus.jns_cuti ON (`cuti2008`.`Jenis_cuti`=kamus.jns_cuti.Kd_cuti)
INNER JOIN kamus.unkerja ON (`unit_peg`.`kd_unit`=kamus.unkerja.`kd_unit_kerja`)
WHERE
`pegawai`.`AdmEdu`LIKE 1 AND `cuti2008`.`Jenis_cuti` LIKE 1
GROUP BY
cuti2008.kdPegawai;


SELECT
cuti2008.kdPegawai,
pegawai.Nama_peg,
kamus.unkerja.nama_unit_kerja,
SUM(kamus.jns_cuti.jatah_cuti - `cuti2008`.`lama_cuti`) AS SISA_CUTI_TAHUNAN,
unit_peg.kd_unit,
`cuti2008`.`lama_cuti`,
kamus.jns_cuti.nama_cuti
FROM
personalia.cuti2008
LEFT OUTER JOIN personalia.pegawai ON (cuti2008.kdPegawai = pegawai.kdPegawai)
LEFT OUTER JOIN personalia.unit_peg ON (cuti2008.kdPegawai = unit_peg.kdPegawai)
INNER JOIN kamus.jns_cuti ON (`cuti2008`.`Jenis_cuti`=kamus.jns_cuti.Kd_cuti)
INNER JOIN kamus.unkerja ON (`unit_peg`.`kd_unit`=kamus.unkerja.`kd_unit_kerja`)
WHERE
`pegawai`.`AdmEdu`LIKE 1 AND `cuti2008`.`Jenis_cuti` LIKE 1 AND kamus.unkerja.`kd_unit_kerja` LIKE 12000000
GROUP BY
cuti2008.kdPegawai;
