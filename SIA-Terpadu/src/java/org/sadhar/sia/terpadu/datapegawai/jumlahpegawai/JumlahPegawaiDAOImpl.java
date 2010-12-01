/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.datapegawai.jumlahpegawai;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.unkerja.UnitKerja;

/**
 *
 * @author Hendro Steven
 */
public class JumlahPegawaiDAOImpl implements JumlahPegawaiDAO {

    public JumlahPegawaiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public CategoryDataset getJumlahPegawaiByTotal(UnitKerja unKerja) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (unKerja == null) {
            sql = "SELECT  COUNT(pegawai.kdPegawai) AS jumlah "
                    + ",year(dt_status_peg.tgl_sk_status) as tahun "
                    + "FROM "
                    + "personalia.pegawai pegawai "
                    + "INNER JOIN "
                    + "personalia.dt_status_peg dt_status_peg "
                    + "ON (pegawai.kdPegawai = dt_status_peg.kdPegawai) "
                    + "WHERE (pegawai.Status_keluar = '1'  or pegawai.Status_keluar = '7') and "
                    + "year(dt_status_peg.tgl_sk_status) BETWEEN '2000' AND YEAR(CURDATE()) "
                    + "GROUP BY year(dt_status_peg.tgl_sk_status)";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), "Jumlah", m.get("tahun").toString());
            }
        } else {
            sql = "SELECT  COUNT(pegawai.kdPegawai) AS jumlah "
                    + ", YEAR(dt_status_peg.tgl_sk_status) AS tahun "
                    + "FROM  "
                    + "(personalia.pegawai pegawai "
                    + "INNER JOIN "
                    + "personalia.unit_peg unit_peg "
                    + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                    + "INNER JOIN "
                    + "personalia.dt_status_peg dt_status_peg "
                    + "ON (pegawai.kdPegawai = dt_status_peg.kdPegawai) "
                    + "WHERE (pegawai.Status_keluar = '1'  OR pegawai.Status_keluar = '7') AND "
                    + "YEAR(dt_status_peg.tgl_sk_status) BETWEEN '2000' AND YEAR(CURDATE()) "
                    + "AND unit_peg.kd_unit=? "
                    + "GROUP BY dt_status_peg.tgl_sk_status";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[]{unKerja.getKode()});
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), "Jumlah", m.get("tahun").toString());
            }
        }

        return dataset;
    }

    public CategoryDataset getJumlahPegawaiByStatus(UnitKerja unKerja) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (unKerja == null) {
            sql = "SELECT  stat_peg.St_peg AS `status` "
                    + ", COUNT(pegawai.kdPegawai) AS jumlah "
                    + ",YEAR(dt_status_peg.tgl_sk_status) AS tahun "
                    + "FROM "
                    + "(personalia.pegawai pegawai "
                    + "INNER JOIN "
                    + "personalia.dt_status_peg dt_status_peg "
                    + "ON (pegawai.kdPegawai = dt_status_peg.kdPegawai)) "
                    + "INNER JOIN "
                    + "kamus.stat_peg stat_peg "
                    + "ON (stat_peg.Kd_stat_peg = pegawai.stat_peg) "
                    + "WHERE (pegawai.Status_keluar = '1' OR pegawai.Status_keluar='7') AND "
                    + "YEAR(dt_status_peg.tgl_sk_status) BETWEEN '2000' AND YEAR(CURDATE()) "
                    + "GROUP BY stat_peg.St_peg,YEAR(dt_status_peg.tgl_sk_status) "
                    + "ORDER BY YEAR(dt_status_peg.tgl_sk_status)";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("status").toString(), m.get("tahun").toString());
            }
        } else {
            sql = "SELECT  stat_peg.St_peg AS `status` "
                    + ", COUNT(pegawai.kdPegawai) AS jumlah "
                    + ",YEAR(dt_status_peg.tgl_sk_status) AS tahun "
                    + "FROM "
                    + "((personalia.pegawai pegawai "
                    + "INNER JOIN  "
                    + "personalia.unit_peg unit_peg "
                    + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                    + "INNER JOIN  "
                    + "personalia.dt_status_peg dt_status_peg "
                    + "ON (pegawai.kdPegawai = dt_status_peg.kdPegawai)) "
                    + "INNER JOIN "
                    + "kamus.stat_peg stat_peg "
                    + "ON (stat_peg.Kd_stat_peg = pegawai.stat_peg) "
                    + "WHERE (pegawai.Status_keluar = '1' OR pegawai.Status_keluar='7') AND "
                    + "YEAR(dt_status_peg.tgl_sk_status) BETWEEN '2000' AND YEAR(CURDATE()) "
                    + "AND (unit_peg.kd_unit = ?) "
                    + "GROUP BY stat_peg.St_peg,YEAR(dt_status_peg.tgl_sk_status) "
                    + "ORDER BY YEAR(dt_status_peg.tgl_sk_status)";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[]{unKerja.getKode()});
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("status").toString(), m.get("tahun").toString());
            }
        }
        return dataset;
    }

    public CategoryDataset getJumlahPegawaiByPangkat(UnitKerja unKerja) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        DateTime dt = new DateTime(new Date());
        updateJumlahPegawaiByPangkat();
        if (unKerja == null) {
            sql = "SELECT COUNT(jmlpegpangkat.kode) AS jumlah, jmlpegpangkat.pangkat AS pangkat "
                    + "FROM tempo.jmlpegpangkat jmlpegpangkat  "
                    + "WHERE (YEAR(jmlpegpangkat.tanggal_mulai) >= ?) "
                    + "OR (YEAR(jmlpegpangkat.tanggal_selesai) <= ?)  "
                    + "GROUP BY jmlpegpangkat.pangkat";
            for (int thn = 2000; thn <= dt.getYear(); thn++) {
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[]{
                            String.valueOf(thn), String.valueOf(thn)
                        });
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("pangkat").toString(), String.valueOf(thn));
                }
            }
        } else {
            sql = "SELECT COUNT(jmlpegpangkat.kode) AS jumlah,  "
                    + "jmlpegpangkat.pangkat AS pangkat  "
                    + "FROM tempo.jmlpegpangkat jmlpegpangkat INNER JOIN personalia.unit_peg unit_peg  "
                    + "ON (jmlpegpangkat.kode = unit_peg.kdPegawai)  "
                    + "WHERE (YEAR(jmlpegpangkat.tanggal_mulai) >= ?)  "
                    + "OR(YEAR(jmlpegpangkat.tanggal_selesai) <= ?)  "
                    + "AND(unit_peg.kd_unit = ?)  "
                    + "GROUP BY jmlpegpangkat.pangkat";
            for (int thn = 2000; thn <= dt.getYear(); thn++) {
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[]{String.valueOf(thn), String.valueOf(thn), unKerja.getKode()});
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("pangkat").toString(), String.valueOf(thn));
                }
            }
        }
        return dataset;
    }

    public CategoryDataset getJumlahPegawaiByGolongan(UnitKerja unKerja) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        DateTime dt = new DateTime(new Date());
        updateJumlahPegawaiByGolongan();
        if (unKerja == null) {
            sql = "SELECT COUNT(jmlpeggol.kode) AS jumlah, jmlpeggol.golongan as golongan "
                    + "FROM tempo.jmlpeggol jmlpeggol "
                    + "WHERE (YEAR(jmlpeggol.tanggal_mulai) >= ?) "
                    + "OR (YEAR(jmlpeggol.tanggal_selesai) <= ?) "
                    + "GROUP BY jmlpeggol.golongan";
            for (int thn = 2000; thn <= dt.getYear(); thn++) {
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[]{
                            String.valueOf(thn), String.valueOf(thn)
                        });
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("golongan").toString(), String.valueOf(thn));
                }
            }
        } else {
            sql = "SELECT COUNT(jmlpeggol.kode) AS jumlah,  "
                    + "jmlpeggol.golongan "
                    + "FROM tempo.jmlpeggol jmlpeggol INNER JOIN personalia.unit_peg unit_peg "
                    + "ON (jmlpeggol.kode = unit_peg.kdPegawai) "
                    + "WHERE (YEAR(jmlpeggol.tanggal_mulai) >= ?) "
                    + "OR(YEAR(jmlpeggol.tanggal_selesai) <= ?) "
                    + "AND(unit_peg.kd_unit = ?) "
                    + "GROUP BY jmlpeggol.golongan";
            for (int thn = 2000; thn <= dt.getYear(); thn++) {
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[]{String.valueOf(thn), String.valueOf(thn), unKerja.getKode()});
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("golongan").toString(), String.valueOf(thn));
                }
            }
        }
        return dataset;
    }

    public CategoryDataset getJumlahPegawaiByJabatanAkademik(UnitKerja unKerja) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        DateTime dt = new DateTime(new Date());
        updateJumlahPegawaiByJabatanAkademik();
        if (unKerja == null) {
            sql = "SELECT COUNT(jmlpegakad.kode) AS jumlah, jmlpegakad.jabatan as jabatan "
                    + "FROM tempo.jmlpegakad jmlpegakad "
                    + "WHERE (YEAR(jmlpegakad.tanggal_mulai) >= ?) "
                    + "OR (YEAR(jmlpegakad.tanggal_selesai) <= ?) "
                    + "GROUP BY jmlpegakad.jabatan";
            for (int thn = 2000; thn <= dt.getYear(); thn++) {
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[]{
                            String.valueOf(thn), String.valueOf(thn)
                        });
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("jabatan").toString(), String.valueOf(thn));
                }
            }
        } else {
            sql = "SELECT COUNT(jmlpegakad.kode) AS jumlah,  "
                    + "jmlpegakad.jabatan "
                    + "FROM tempo.jmlpegakad jmlpegakad INNER JOIN personalia.unit_peg unit_peg "
                    + "ON (jmlpegakad.kode = unit_peg.kdPegawai) "
                    + "WHERE (YEAR(jmlpegakad.tanggal_mulai) >= ?) "
                    + "OR(YEAR(jmlpegakad.tanggal_selesai) <= ?) "
                    + "AND(unit_peg.kd_unit = ?) "
                    + "GROUP BY jmlpegakad.jabatan";
            for (int thn = 2000; thn <= dt.getYear(); thn++) {
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[]{String.valueOf(thn), String.valueOf(thn), unKerja.getKode()});
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("jabatan").toString(), String.valueOf(thn));
                }
            }
        }
        return dataset;
    }

    private void updateJumlahPegawaiByPangkat() throws Exception {
        String sqlDelete = "DELETE FROM tempo.jmlpegpangkat";
        String sqlInsert = "INSERT INTO tempo.jmlpegpangkat(kode,tanggal_mulai,tanggal_selesai,pangkat) VALUES(?,?,?,?)";
        String sql = "SELECT pegawai.kdPegawai as kode,"
                + "pegawai.Nama_peg,"
                + "pangkat_peg.Tmt_pangkat as tanggal_mulai,"
                + "pangkat.Nama_pang as pangkat "
                + "FROM (kamus.pangkat pangkat "
                + "INNER JOIN personalia.pangkat_peg pangkat_peg "
                + "ON (pangkat.Kd_pang = pangkat_peg.Kd_pang)) "
                + "INNER JOIN personalia.pegawai pegawai "
                + "ON (pegawai.kdPegawai = pangkat_peg.kdPegawai) "
                + "WHERE ((pangkat_peg.Tmt_pangkat IS NOT NULL) AND (pangkat_peg.Tmt_pangkat<>'0000-00-00')) "
                + "ORDER BY pegawai.kdPegawai ASC, pangkat_peg.Tmt_pangkat ASC";
        ClassConnection.getJdbc().update(sqlDelete);
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (int x = 0; x < rows.size(); x++) {
            Map mp = (Map) rows.get(x);
            JumlahPegawaiPangkat peg = new JumlahPegawaiPangkat();
            peg.setKode(mp.get("kode").toString());
            DateTime dt = new DateTime(mp.get("tanggal_mulai").toString());
            //System.out.println(peg.getKode() + " " + dt.toString("yyyy-MM-dd"));
            peg.setTanggal_mulai(dt.toDate());
            if ((x + 1) < rows.size()) {
                Map temp = rows.get(x + 1);
                if (mp.get("kode").toString().equals(temp.get("kode").toString())) {
                    DateTime dtTemp = new DateTime(temp.get("tanggal_mulai").toString());
                    peg.setTanggal_selesai(dtTemp.toDate());
                } else {
                    peg.setTanggal_selesai(new Date());
                }
            } else {
                peg.setTanggal_selesai(new Date());
            }
            peg.setPangkat(mp.get("pangkat").toString());
            ClassConnection.getJdbc().update(sqlInsert, new Object[]{peg.getKode(), peg.getTanggal_mulai(), peg.getTanggal_selesai(),
                        peg.getPangkat()});

        }
    }

    private void updateJumlahPegawaiByGolongan() throws Exception {
        String sqlDelete = "DELETE FROM tempo.jmlpeggol";
        String sqlInsert = "INSERT INTO tempo.jmlpeggol(kode,tanggal_mulai,tanggal_selesai,golongan) VALUES(?,?,?,?)";
        String sql = "SELECT pegawai.kdPegawai AS kode,"
                + "pegawai.Nama_peg AS nama,"
                + "golongan_peg.tgl_sk_gol AS tanggal_mulai,"
                + "golgaji.Nama_gol AS golongan "
                + "FROM (personalia.pegawai pegawai "
                + "INNER JOIN personalia.golongan_peg golongan_peg "
                + "ON (pegawai.kdPegawai = golongan_peg.kdPegawai)) "
                + "INNER JOIN kamus.golgaji golgaji "
                + "ON (golgaji.Kd_gol = golongan_peg.Kd_gol) "
                + "WHERE ((golongan_peg.tgl_sk_gol IS NOT NULL) "
                + "AND (golongan_peg.tgl_sk_gol <> '0000-00-00')) "
                + "AND(pegawai.stat_peg = '1') "
                + "ORDER BY pegawai.kdPegawai ASC, golongan_peg.tgl_sk_gol ASC";
        ClassConnection.getJdbc().update(sqlDelete);
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (int x = 0; x < rows.size(); x++) {
            Map mp = (Map) rows.get(x);
            JumlahPegawaiGolongan peg = new JumlahPegawaiGolongan();
            peg.setKode(mp.get("kode").toString());
            DateTime dt = new DateTime(mp.get("tanggal_mulai").toString());
            //System.out.println(peg.getKode() + " " + dt.toString("yyyy-MM-dd"));
            peg.setTanggal_mulai(dt.toDate());
            if ((x + 1) < rows.size()) {
                Map temp = rows.get(x + 1);
                if (mp.get("kode").toString().equals(temp.get("kode").toString())) {
                    DateTime dtTemp = new DateTime(temp.get("tanggal_mulai").toString());
                    peg.setTanggal_selesai(dtTemp.toDate());
                } else {
                    peg.setTanggal_selesai(new Date());
                }
            } else {
                peg.setTanggal_selesai(new Date());
            }
            peg.setGolongan(mp.get("golongan").toString());
            ClassConnection.getJdbc().update(sqlInsert, new Object[]{peg.getKode(), peg.getTanggal_mulai(), peg.getTanggal_selesai(),
                        peg.getGolongan()});

        }
    }

    private void updateJumlahPegawaiByJabatanAkademik() throws Exception {
        String sqlDelete = "DELETE FROM tempo.jmlpegakad";
        String sqlInsert = "INSERT INTO tempo.jmlpegakad(kode,tanggal_mulai,tanggal_selesai,jabatan) VALUES(?,?,?,?)";
        String sql = "SELECT pegawai.kdPegawai AS kode,"
                + "pegawai.Nama_peg AS nama,"
                + "MIN(jab_akad_pegawai.Tgl_SK_Jabak) AS tanggal_mulai,"
                + "jab_akad.Nama_jab_akad AS jabatan "
                + "FROM (personalia.pegawai pegawai "
                + "LEFT OUTER JOIN personalia.jab_akad_pegawai jab_akad_pegawai "
                + "ON (pegawai.kdPegawai = jab_akad_pegawai.kdPegawai)) "
                + "INNER JOIN kamus.jab_akad jab_akad "
                + "ON (jab_akad.Kd_jab_akad = jab_akad_pegawai.Kd_Jabak) "
                + "WHERE (jab_akad_pegawai.Tgl_SK_Jabak IS NOT NULL) AND (jab_akad_pegawai.Tgl_SK_Jabak <> '0000-00-00')"
                + "AND(pegawai.AdmEdu = '2') "
                + "AND(pegawai.stat_peg = '1')  "
                + "GROUP BY pegawai.kdPegawai, jab_akad.Nama_jab_akad "
                + "ORDER BY pegawai.kdPegawai ASC, 3 ASC";
        ClassConnection.getJdbc().update(sqlDelete);
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (int x = 0; x < rows.size(); x++) {
            Map mp = (Map) rows.get(x);
            JumlahPegawaiJabAkad peg = new JumlahPegawaiJabAkad();
            peg.setKode(mp.get("kode").toString());
            DateTime dt = new DateTime(mp.get("tanggal_mulai").toString());
            //System.out.println(peg.getKode() + " " + dt.toString("yyyy-MM-dd"));
            peg.setTanggal_mulai(dt.toDate());
            if ((x + 1) < rows.size()) {
                Map temp = rows.get(x + 1);
                if (mp.get("kode").toString().equals(temp.get("kode").toString())) {
                    DateTime dtTemp = new DateTime(temp.get("tanggal_mulai").toString());
                    peg.setTanggal_selesai(dtTemp.toDate());
                } else {
                    peg.setTanggal_selesai(new Date());
                }
            } else {
                peg.setTanggal_selesai(new Date());
            }
            peg.setJabatan(mp.get("jabatan").toString());
            ClassConnection.getJdbc().update(sqlInsert, new Object[]{peg.getKode(), peg.getTanggal_mulai(), peg.getTanggal_selesai(),
                        peg.getJabatan()});

        }
    }
}
