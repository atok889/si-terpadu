/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.datapegawai.demografi;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author Hendro Steven
 */
public class DemografiPegawaiDAOImpl implements DemografiPegawaiDAO {

    public DemografiPegawaiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public CategoryDataset getBarDemografiBySex() throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "SELECT unkerja.Nama_unit_kerja AS unit, "
                + "sex.Nm_sex AS sex, "
                + "COUNT(pegawai.kdPegawai) AS jumlah "
                + "FROM ((personalia.pegawai pegawai "
                + "INNER JOIN personalia.unit_peg unit_peg "
                + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                + "INNER JOIN kamus.sex sex "
                + "ON (sex.Kd_sex = pegawai.Jns_klm)) "
                + "INNER JOIN kamus.unkerja unkerja "
                + "ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit) "
                + "WHERE (pegawai.Status_keluar = '1'  or pegawai.Status_keluar = '7') "
                + "GROUP BY unkerja.Nama_unit_kerja, sex.Nm_sex "
                + "ORDER BY unkerja.Nama_unit_kerja ASC, sex.Nm_sex ASC";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("sex").toString(), m.get("unit").toString());
        }
        return dataset;
    }

    public CategoryDataset getBarDemografiByAgama() throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "SELECT unkerja.Nama_unit_kerja AS unit, "
                + "agama.Nm_agama AS agama, "
                + "COUNT(DISTINCT pegawai.kdPegawai) AS jumlah "
                + "FROM ((personalia.pegawai pegawai "
                + "INNER JOIN personalia.unit_peg unit_peg "
                + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                + "RIGHT OUTER JOIN kamus.agama agama "
                + "ON (agama.Kd_agama = pegawai.Agm)) "
                + "INNER JOIN kamus.unkerja unkerja "
                + "ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit) "
                + "WHERE (pegawai.Status_keluar = '1'  or pegawai.Status_keluar = '7') "
                + "GROUP BY unkerja.Nama_unit_kerja, agama.Nm_agama "
                + "ORDER BY unkerja.Nama_unit_kerja ASC, agama.Nm_agama ASC";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("agama").toString(), m.get("unit").toString());
        }
        return dataset;
    }

    public CategoryDataset getBarDemografiByPendidikan() throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "SELECT unkerja.Nama_unit_kerja AS unit, "
                + "jenjang.Nm_jenjang AS pendidikan, "
                + "COUNT(DISTINCT pegawai.kdPegawai) AS jumlah "
                + "FROM (((personalia.pegawai pegawai "
                + "INNER JOIN personalia.unit_peg unit_peg "
                + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                + "INNER JOIN personalia.pendidikan pendidikan "
                + "ON (pegawai.kdPegawai = pendidikan.kdPegawai)) "
                + "INNER JOIN kamus.jenjang jenjang "
                + "ON (jenjang.Kd_jenjang = pendidikan.Jenjang)) "
                + "INNER JOIN kamus.unkerja unkerja "
                + "ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit) "
                + "WHERE (pegawai.Status_keluar = '1'  or pegawai.Status_keluar = '7') "
                + "GROUP BY unkerja.Nama_unit_kerja, jenjang.Nm_jenjang "
                + "ORDER BY pendidikan.Tgl_ijasah DESC, "
                + "unkerja.Nama_unit_kerja ASC, "
                + "jenjang.Nm_jenjang ASC";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("pendidikan").toString(), m.get("unit").toString());
        }
        return dataset;
    }

    public PieDataset getPieDemografiBySex() throws Exception {
        DefaultPieDataset dataset = new DefaultPieDataset();
        String sql = "SELECT sex.Nm_sex AS sex, COUNT(pegawai.kdPegawai) AS jumlah "
                + "FROM kamus.sex sex INNER JOIN personalia.pegawai pegawai "
                + "ON (sex.Kd_sex = pegawai.Jns_klm) "
                + "WHERE (pegawai.Status_keluar = '1'  or pegawai.Status_keluar = '7') "
                + "GROUP BY sex.Nm_sex ORDER BY sex.Nm_sex ASC";

        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        DecimalFormat df = new DecimalFormat("#.##");
        double total = 0;
        double laki = 0;
        double perem = 0;
        for (Map m : rows) {
            total += Double.valueOf(m.get("jumlah").toString());
            if (m.get("sex").toString().equalsIgnoreCase("laki-laki")) {
                laki += Double.valueOf(m.get("jumlah").toString());
            } else {
                perem += Double.valueOf(m.get("jumlah").toString());
            }
        }
        dataset.setValue("Laki-laki: " + df.format((laki / total) * 100) + " %", (laki / total) * 100);
        dataset.setValue("Perempuan: " + df.format((perem / total) * 100) + " %", (perem / total) * 100);
        return dataset;
    }

    public PieDataset getPieDemografiByAgama() throws Exception {
        DefaultPieDataset dataset = new DefaultPieDataset();
        String sql = "SELECT agama.Nm_agama as agama, COUNT(pegawai.kdPegawai) AS jumlah "
                + "FROM kamus.agama agama LEFT OUTER JOIN personalia.pegawai pegawai "
                + "ON (agama.Kd_agama = pegawai.Agm) "
                + "WHERE (pegawai.Status_keluar = '1'  or pegawai.Status_keluar = '7') "
                + "GROUP BY agama.Nm_agama";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        DecimalFormat df = new DecimalFormat("#.##");
        double budha = 0;
        double hindu = 0;
        double islam = 0;
        double katholik = 0;
        double kepercayaan = 0;
        double khong = 0;
        double kristen = 0;
        double lain = 0;
        double total = 0;
        for (Map m : rows) {
            total += Double.valueOf(m.get("jumlah").toString());
            if (m.get("agama").toString().equalsIgnoreCase("budha")) {
                budha += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("agama").toString().equalsIgnoreCase("hindu")) {
                hindu += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("agama").toString().equalsIgnoreCase("islam")) {
                islam += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("agama").toString().equalsIgnoreCase("katholik")) {
                katholik += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("agama").toString().equalsIgnoreCase("kepercayaan")) {
                kepercayaan += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("agama").toString().equalsIgnoreCase("khong hu chu")) {
                khong += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("agama").toString().equalsIgnoreCase("kristen")) {
                kristen += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("agama").toString().equalsIgnoreCase("lain")) {
                lain += Double.valueOf(m.get("jumlah").toString());
            }
        }
        dataset.setValue("Budha: " + df.format((budha / total) * 100) + " %", (budha / total) * 100);
        dataset.setValue("Hindu: " + df.format((hindu / total) * 100) + " %", (hindu / total) * 100);
        dataset.setValue("Islam: " + df.format((islam / total) * 100) + " %", (islam / total) * 100);
        dataset.setValue("Katholik: " + df.format((katholik / total) * 100) + " %", (katholik / total) * 100);
        dataset.setValue("Kepercayaan: " + df.format((kepercayaan / total) * 100) + " %", (kepercayaan / total) * 100);
        dataset.setValue("Khong Hu Chu: " + df.format((khong / total) * 100) + " %", (khong / total) * 100);
        dataset.setValue("Kristen: " + df.format((kristen / total) * 100) + " %", (kristen / total) * 100);
        dataset.setValue("Lainnya: " + df.format((lain / total) * 100) + " %", (lain / total) * 100);
        return dataset;
    }

    public PieDataset getPieDemografiByPendidikan() throws Exception {
        DefaultPieDataset dataset = new DefaultPieDataset();
        String sql = "SELECT jenjang.Nm_jenjang AS pendidikan, "
                + "COUNT(DISTINCT pegawai.kdPegawai) AS jumlah "
                + "FROM (personalia.pegawai pegawai "
                + "INNER JOIN personalia.pendidikan pendidikan "
                + "ON (pegawai.kdPegawai = pendidikan.kdPegawai)) "
                + "INNER JOIN kamus.jenjang jenjang "
                + "ON (jenjang.Kd_jenjang = pendidikan.Jenjang) "
                + "WHERE (pegawai.Status_keluar = '1'  or pegawai.Status_keluar = '7') "
                + "GROUP BY jenjang.Nm_jenjang "
                + "ORDER BY pendidikan.Tgl_ijasah DESC,jenjang.Nm_jenjang ASC";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        DecimalFormat df = new DecimalFormat("#.##");
        double d2 = 0;
        double d3 = 0;
        double lain = 0;
        double profesi = 0;
        double s1 = 0;
        double s2 = 0;
        double s3 = 0;
        double sd = 0;
        double slta = 0;
        double sltp = 0;
        double total = 0;
        for (Map m : rows) {
            total += Double.valueOf(m.get("jumlah").toString());
            if (m.get("pendidikan").toString().equalsIgnoreCase("D II")) {
                d2 += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("pendidikan").toString().equalsIgnoreCase("D III")) {
                d3 += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("pendidikan").toString().equalsIgnoreCase("lain")) {
                lain += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("pendidikan").toString().equalsIgnoreCase("profesi")) {
                profesi += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("pendidikan").toString().equalsIgnoreCase("S1")) {
                s1 += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("pendidikan").toString().equalsIgnoreCase("s2")) {
                s2 += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("pendidikan").toString().equalsIgnoreCase("s3")) {
                s3 += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("pendidikan").toString().equalsIgnoreCase("sd")) {
                sd += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("pendidikan").toString().equalsIgnoreCase("slta")) {
                slta += Double.valueOf(m.get("jumlah").toString());
            } else if (m.get("pendidikan").toString().equalsIgnoreCase("sltp")) {
                sltp += Double.valueOf(m.get("jumlah").toString());
            }
        }
        dataset.setValue("D II: " + df.format((d2 / total) * 100) + " %", (d2 / total) * 100);
        dataset.setValue("D III: " + df.format((d3 / total) * 100) + " %", (d3 / total) * 100);
        dataset.setValue("Lain: " + df.format((lain / total) * 100) + " %", (lain / total) * 100);
        dataset.setValue("Profesi: " + df.format((profesi / total) * 100) + " %", (profesi / total) * 100);
        dataset.setValue("S1: " + df.format((s1 / total) * 100) + " %", (s1 / total) * 100);
        dataset.setValue("S2: " + df.format((s2 / total) * 100) + " %", (s2 / total) * 100);
        dataset.setValue("S3: " + df.format((s3 / total) * 100) + " %", (s3 / total) * 100);
        dataset.setValue("SD: " + df.format((sd / total) * 100) + " %", (sd / total) * 100);
        dataset.setValue("SLTA: " + df.format((slta / total) * 100) + " %", (slta / total) * 100);
        dataset.setValue("SLTP: " + df.format((sltp / total) * 100) + " %", (sltp / total) * 100);
        return dataset;
    }
}
