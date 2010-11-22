/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.datapegawai.jumlahpegawai;

import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
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
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CategoryDataset getJumlahPegawaiByGolongan(UnitKerja unKerja) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CategoryDataset getJumlahPegawaiByJabatanAkademik(UnitKerja unKerja) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (unKerja == null) {
            sql = "SELECT jab_akad.Nama_jab_akad AS jabatan, COUNT(pegawai.kdPegawai) AS jumlah, "
                    + "YEAR(jab_akad_pegawai.Tgl_SK_Jabak) AS tahun "
                    + "FROM (personalia.pegawai pegawai "
                    + "INNER JOIN personalia.jab_akad_pegawai jab_akad_pegawai "
                    + "ON (pegawai.kdPegawai = jab_akad_pegawai.kdPegawai)) "
                    + "INNER JOIN kamus.jab_akad jab_akad "
                    + "ON (jab_akad.Kd_jab_akad = jab_akad_pegawai.Kd_Jabak) "
                    + "WHERE (pegawai.Status_keluar = '1' OR pegawai.Status_keluar='7') AND "
                    + "YEAR(jab_akad_pegawai.Tgl_SK_Jabak) BETWEEN '2000' AND YEAR(CURDATE()) "
                    + "GROUP BY jab_akad.Nama_jab_akad,YEAR(jab_akad_pegawai.Tgl_SK_Jabak) "
                    + "ORDER BY YEAR(jab_akad_pegawai.Tgl_SK_Jabak)";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("jabatan").toString(), m.get("tahun").toString());
            }
        } else {
            sql = "SELECT jab_akad.Nama_jab_akad AS jabatan, COUNT(pegawai.kdPegawai) AS jumlah, "
                    + "YEAR(jab_akad_pegawai.Tgl_SK_Jabak) AS tahun "
                    + "FROM ((personalia.pegawai pegawai "
                    + "INNER JOIN "
                    + "personalia.jab_akad_pegawai jab_akad_pegawai "
                    + "ON (pegawai.kdPegawai = jab_akad_pegawai.kdPegawai)) "
                    + "INNER JOIN "
                    + "personalia.unit_peg unit_peg "
                    + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                    + "INNER JOIN "
                    + "kamus.jab_akad jab_akad "
                    + "ON (jab_akad.Kd_jab_akad = jab_akad_pegawai.Kd_Jabak) "
                    + "WHERE (pegawai.Status_keluar = '1' OR pegawai.Status_keluar='7') AND "
                    + "YEAR(jab_akad_pegawai.Tgl_SK_Jabak) BETWEEN '2000' AND YEAR(CURDATE())  "
                    + "AND (unit_peg.kd_unit = ?) "
                    + "GROUP BY jab_akad.Nama_jab_akad,YEAR(jab_akad_pegawai.Tgl_SK_Jabak) "
                    + "ORDER BY YEAR(jab_akad_pegawai.Tgl_SK_Jabak)";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[]{unKerja.getKode()});
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("jabatan").toString(), m.get("tahun").toString());
            }
        }
        return dataset;
    }
}
