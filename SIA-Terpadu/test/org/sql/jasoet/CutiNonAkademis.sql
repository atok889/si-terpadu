SELECT
cutiPegawai.kdPegawai,
pegawai.Nama_peg,
kamus.unkerja.nama_unit_kerja,
SUM(kamus.jns_cuti.jatah_cuti - `cutiPegawai`.`lama_cuti`) AS SISA_CUTI_TAHUNAN,
unit_peg.kd_unit,
`cutiPegawai`.`lama_cuti`,
kamus.jns_cuti.nama_cuti
FROM
personalia.cutiPegawai
LEFT OUTER JOIN personalia.pegawai ON (cutiPegawai.kdPegawai = pegawai.kdPegawai)
LEFT OUTER JOIN personalia.unit_peg ON (cutiPegawai.kdPegawai = unit_peg.kdPegawai)
INNER JOIN kamus.jns_cuti ON (`cutiPegawai`.`Jenis_cuti`=kamus.jns_cuti.Kd_cuti)
INNER JOIN kamus.unkerja ON (`unit_peg`.`kd_unit`=kamus.unkerja.`kd_unit_kerja`)
WHERE
`pegawai`.`AdmEdu`LIKE 1 AND `cutiPegawai`.`Jenis_cuti` LIKE 1
GROUP BY
cutiPegawai.kdPegawai;


SELECT
cutiPegawai.kdPegawai,
pegawai.Nama_peg,
kamus.unkerja.nama_unit_kerja,
SUM(kamus.jns_cuti.jatah_cuti - `cutiPegawai`.`lama_cuti`) AS SISA_CUTI_TAHUNAN,
unit_peg.kd_unit,
`cutiPegawai`.`lama_cuti`,
kamus.jns_cuti.nama_cuti
FROM
personalia.cutiPegawai
LEFT OUTER JOIN personalia.pegawai ON (cutiPegawai.kdPegawai = pegawai.kdPegawai)
LEFT OUTER JOIN personalia.unit_peg ON (cutiPegawai.kdPegawai = unit_peg.kdPegawai)
INNER JOIN kamus.jns_cuti ON (`cutiPegawai`.`Jenis_cuti`=kamus.jns_cuti.Kd_cuti)
INNER JOIN kamus.unkerja ON (`unit_peg`.`kd_unit`=kamus.unkerja.`kd_unit_kerja`)
WHERE
`pegawai`.`AdmEdu`LIKE 1 AND `cutiPegawai`.`Jenis_cuti` LIKE 1 AND kamus.unkerja.`kd_unit_kerja` LIKE 12000000
GROUP BY
cutiPegawai.kdPegawai;
