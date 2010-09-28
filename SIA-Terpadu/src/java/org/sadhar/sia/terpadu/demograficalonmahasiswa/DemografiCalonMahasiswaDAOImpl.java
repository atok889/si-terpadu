/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.demograficalonmahasiswa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.kabkota.KabKota;
import org.sadhar.sia.terpadu.provinsi.Provinsi;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;
import org.sadhar.sia.terpadu.prodi.ProgramStudiDAOImpl;

/**
 *
 * @author Hendro Steven
 */
public class DemografiCalonMahasiswaDAOImpl implements DemografiCalonMahasiswaDAO {

    public DemografiCalonMahasiswaDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public CategoryDataset getJenisKelaminDataset(ProgramStudi progdi, String tahun) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (progdi != null && tahun.isEmpty()) {
            DateTime dt = new DateTime(new Date());
            int year = dt.getYear();
            int perem = 0;
            int laki = 0;
            for (int x = year; x > year - 5; x--) {
                sql = "SELECT kamus.sex.Nm_sex AS sex, COUNT(pmb.pdf" + x + ".nomor) AS jumlah "
                        + "FROM kamus.sex sex INNER JOIN pmb.pdf" + x + " pdf" + x + " "
                        + "ON (sex.Kd_sex = pdf" + x + ".kd_sex) WHERE pil1='" + progdi.getKode() + "' "
                        + "GROUP BY sex.Nm_sex";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    //dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("sex").toString(), x+"");
                    if (m.get("sex").toString().equalsIgnoreCase("Laki-Laki")) {
                        laki += Integer.valueOf(m.get("jumlah").toString());
                    } else {
                        perem += Integer.valueOf(m.get("jumlah").toString());
                    }
                }
            }
            dataset.addValue(laki, "Laki-laki", progdi.getNama());
            dataset.addValue(perem, "Perempuan", progdi.getNama());
        } else if (progdi != null && !tahun.isEmpty()) {
            sql = "SELECT kamus.sex.Nm_sex AS sex, COUNT(pmb.pdf" + tahun + ".nomor) AS jumlah "
                    + "FROM kamus.sex sex INNER JOIN pmb.pdf" + tahun + " pdf" + tahun + " "
                    + "ON (sex.Kd_sex = pdf" + tahun + ".kd_sex) WHERE pil1='" + progdi.getKode() + "' "
                    + "GROUP BY sex.Nm_sex";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("sex").toString(), progdi.getNama());
            }
        } else if (progdi == null && tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));

            for (ProgramStudi ps : progdis) {
                DateTime dt = new DateTime(new Date());
                int year = dt.getYear();
                int perem = 0;
                int laki = 0;
                for (int x = year; x > year - 5; x--) {
                    sql = "SELECT kamus.sex.Nm_sex AS sex, COUNT(pmb.pdf" + x + ".nomor) AS jumlah "
                            + "FROM kamus.sex sex INNER JOIN pmb.pdf" + x + " pdf" + x + " "
                            + "ON (sex.Kd_sex = pdf" + x + ".kd_sex) WHERE pil1='" + ps.getKode() + "' "
                            + "GROUP BY sex.Nm_sex";
                    List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                    for (Map m : rows) {
                        //dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("sex").toString(), x+"");
                        if (m.get("sex").toString().equalsIgnoreCase("Laki-Laki")) {
                            laki += Integer.valueOf(m.get("jumlah").toString());
                        } else {
                            perem += Integer.valueOf(m.get("jumlah").toString());
                        }
                    }
                }
                dataset.addValue(laki, "Laki-laki", ps.getNama());
                dataset.addValue(perem, "Perempuan", ps.getNama());
            }
        } else if (progdi == null && !tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));

            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.sex.Nm_sex AS sex, COUNT(pmb.pdf" + tahun + ".nomor) AS jumlah "
                        + "FROM kamus.sex sex INNER JOIN pmb.pdf" + tahun + " pdf" + tahun + " "
                        + "ON (sex.Kd_sex = pdf" + tahun + ".kd_sex) WHERE pil1='" + ps.getKode() + "' "
                        + "GROUP BY sex.Nm_sex";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("sex").toString(), ps.getNama());
                }
            }
        }
        return dataset;
    }

    public CategoryDataset getAgamaDataset(ProgramStudi progdi, String tahun) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (progdi != null && tahun.isEmpty()) {
            DateTime dt = new DateTime(new Date());
            int year = dt.getYear();
            int islam = 0;
            int khatolik = 0;
            int kristen = 0;
            int budha = 0;
            int hindu = 0;
            int khonghuchu = 0;
            int kepercayaan = 0;
            int lain = 0;
            for (int x = year; x > year - 5; x--) {
                sql = "SELECT kamus.agama.Nm_agama AS agama, COUNT(pmb.pdf" + x + ".nomor) AS jumlah "
                        + "FROM kamus.agama agama INNER JOIN pmb.pdf" + x + " pdf" + x + " "
                        + "ON (agama.Kd_agama = pdf" + x + ".kd_agama) WHERE pil1='" + progdi.getKode() + "' "
                        + "GROUP BY agama.Nm_agama";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    //dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("sex").toString(), x+"");
                    if (m.get("agama").toString().equalsIgnoreCase("Islam")) {
                        islam += Integer.valueOf(m.get("jumlah").toString());
                    } else if (m.get("agama").toString().equalsIgnoreCase("Katholik")) {
                        khatolik += Integer.valueOf(m.get("jumlah").toString());
                    } else if (m.get("agama").toString().equalsIgnoreCase("kristen")) {
                        kristen += Integer.valueOf(m.get("jumlah").toString());
                    } else if (m.get("agama").toString().equalsIgnoreCase("budha")) {
                        budha += Integer.valueOf(m.get("jumlah").toString());
                    } else if (m.get("agama").toString().equalsIgnoreCase("hindu")) {
                        hindu += Integer.valueOf(m.get("jumlah").toString());
                    } else if (m.get("agama").toString().equalsIgnoreCase("Khong Hu Chu")) {
                        khonghuchu += Integer.valueOf(m.get("jumlah").toString());
                    } else if (m.get("agama").toString().equalsIgnoreCase("Lain")) {
                        lain += Integer.valueOf(m.get("jumlah").toString());
                    }
                }
            }
            dataset.addValue(islam, "Islam", progdi.getNama());
            dataset.addValue(khatolik, "Khatolik", progdi.getNama());
            dataset.addValue(kristen, "Kristen", progdi.getNama());
            dataset.addValue(budha, "Budha", progdi.getNama());
            dataset.addValue(hindu, "Hindu", progdi.getNama());
            dataset.addValue(khonghuchu, "Khong Hu Chu", progdi.getNama());
            dataset.addValue(kepercayaan, "Kepercayaan", progdi.getNama());
            dataset.addValue(lain, "Lain", progdi.getNama());
        } else if (progdi != null && !tahun.isEmpty()) {
            sql = "SELECT kamus.agama.Nm_agama AS agama, COUNT(pmb.pdf" + tahun + ".nomor) AS jumlah "
                    + "FROM kamus.agama agama INNER JOIN pmb.pdf" + tahun + " pdf" + tahun + " "
                    + "ON (agama.Kd_agama = pdf" + tahun + ".kd_agama) WHERE pil1='" + progdi.getKode() + "' "
                    + "GROUP BY agama.Nm_agama";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("agama").toString(), progdi.getNama());
            }
        } else if (progdi == null && tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));

            for (ProgramStudi ps : progdis) {
                DateTime dt = new DateTime(new Date());
                int year = dt.getYear();
                int islam = 0;
                int khatolik = 0;
                int kristen = 0;
                int budha = 0;
                int hindu = 0;
                int khonghuchu = 0;
                int kepercayaan = 0;
                int lain = 0;
                for (int x = year; x > year - 5; x--) {
                    sql = "SELECT kamus.agama.Nm_agama AS agama, COUNT(pmb.pdf" + x + ".nomor) AS jumlah "
                            + "FROM kamus.agama agama INNER JOIN pmb.pdf" + x + " pdf" + x + " "
                            + "ON (agama.Kd_agama = pdf" + x + ".kd_agama) WHERE pil1='" + ps.getKode() + "' "
                            + "GROUP BY agama.Nm_agama";
                    List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                    for (Map m : rows) {
                        //dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("sex").toString(), x+"");
                        if (m.get("agama").toString().equalsIgnoreCase("Islam")) {
                            islam += Integer.valueOf(m.get("jumlah").toString());
                        } else if (m.get("agama").toString().equalsIgnoreCase("Khatolik")) {
                            khatolik += Integer.valueOf(m.get("jumlah").toString());
                        } else if (m.get("agama").toString().equalsIgnoreCase("kristen")) {
                            kristen += Integer.valueOf(m.get("jumlah").toString());
                        } else if (m.get("agama").toString().equalsIgnoreCase("budha")) {
                            budha += Integer.valueOf(m.get("jumlah").toString());
                        } else if (m.get("agama").toString().equalsIgnoreCase("hindu")) {
                            hindu += Integer.valueOf(m.get("jumlah").toString());
                        } else if (m.get("agama").toString().equalsIgnoreCase("Khong Hu Chu")) {
                            khonghuchu += Integer.valueOf(m.get("jumlah").toString());
                        } else if (m.get("agama").toString().equalsIgnoreCase("Lain")) {
                            lain += Integer.valueOf(m.get("jumlah").toString());
                        }
                    }
                }
                dataset.addValue(islam, "Islam", ps.getNama());
                dataset.addValue(khatolik, "Khatolik", ps.getNama());
                dataset.addValue(kristen, "Kristen", ps.getNama());
                dataset.addValue(budha, "Budha", ps.getNama());
                dataset.addValue(hindu, "Hindu", ps.getNama());
                dataset.addValue(khonghuchu, "Khong Hu Chu", ps.getNama());
                dataset.addValue(kepercayaan, "Kepercayaan", ps.getNama());
                dataset.addValue(lain, "Lain", ps.getNama());
            }
        } else if (progdi == null && !tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));

            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.agama.Nm_agama AS agama, COUNT(pmb.pdf" + tahun + ".nomor) AS jumlah "
                        + "FROM kamus.agama agama INNER JOIN pmb.pdf" + tahun + " pdf" + tahun + " "
                        + "ON (agama.Kd_agama = pdf" + tahun + ".kd_agama) WHERE pil1='" + ps.getKode() + "' "
                        + "GROUP BY agama.Nm_agama";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("agama").toString(), ps.getNama());
                }
            }
        }
        return dataset;
    }

    public CategoryDataset getAsalDaerahDataset(ProgramStudi progdi, String tahun, Provinsi prov, KabKota kabkota) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
