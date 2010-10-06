/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.demograficalonmahasiswa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.kabkota.KabKota;
import org.sadhar.sia.terpadu.kabkota.KabKotaDAOImpl;
import org.sadhar.sia.terpadu.provinsi.Provinsi;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;
import org.sadhar.sia.terpadu.prodi.ProgramStudiDAOImpl;
import org.sadhar.sia.terpadu.provinsi.ProvinsiDAOImpl;

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
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (progdi == null && tahun.isEmpty() && prov == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();
            DateTime dt = new DateTime(new Date());
            int year = dt.getYear();
            for (ProgramStudi ps : progdis) {
                Map temp = new HashMap();
                List<Provinsi> provinsi = new ArrayList<Provinsi>();
                for (int x = year; x > year - 5; x--) {
                    sql = "SELECT SUBSTR(kamus.kabupaten.kd_kab,1,2) AS provinsi, COUNT(pmb.pdf" + x + ".nomor) AS jumlah "
                            + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + x + " pdf" + x + " "
                            + "ON (kabupaten.kd_kab = pdf" + x + ".kabupsek) "
                            + "GROUP BY SUBSTR(kabupaten.kd_kab,1,2)";
                    List<Map> rows = ClassConnection.getJdbc().queryForList(sql);

                    for (Map m : rows) {
                        //Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        String kdProv = m.get("provinsi").toString();
                        if (temp.get(kdProv) == null) {
                            provinsi.add(new ProvinsiDAOImpl().getProv(m.get("provinsi").toString()));
                            temp.put(kdProv, Integer.valueOf(m.get("jumlah").toString()));
                        } else {
                            int jml = Integer.valueOf(temp.get(kdProv).toString());
                            jml += Integer.valueOf(m.get("jumlah").toString());
                            temp.put(kdProv, jml);
                        }
                        //dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                    }
                }
                for (Provinsi p : provinsi) {
                    dataset.addValue(Integer.valueOf(temp.get(p.getKode()).toString()), p.getNama(), ps.getNama());
                }
            }
        } else if (progdi != null && tahun.isEmpty() && prov == null) {
            DateTime dt = new DateTime(new Date());
            int year = dt.getYear();
            for (int x = year; x > year - 5; x--) {
                sql = "SELECT SUBSTR(kabupaten.kd_kab,1,2) AS provinsi, COUNT(pdf" + x + ".nomor) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + x + " pdf" + x + " "
                        + "ON (kabupaten.kd_kab = pdf" + x + ".kabupsek) "
                        + "WHERE (pdf" + x + ".pil1 = " + progdi.getKode() + ") "
                        + "GROUP BY SUBSTR(kabupaten.kd_kab,1,2)";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                Map temp = new HashMap();
                List<Provinsi> provinsi = new ArrayList<Provinsi>();
                for (Map m : rows) {
                    String kdProv = m.get("provinsi").toString();
                    if (temp.get(kdProv) == null) {
                        provinsi.add(new ProvinsiDAOImpl().getProv(m.get("provinsi").toString()));
                        temp.put(kdProv, Integer.valueOf(m.get("jumlah").toString()));
                    } else {
                        int jml = Integer.valueOf(temp.get(kdProv).toString());
                        jml += Integer.valueOf(m.get("jumlah").toString());
                        temp.put(kdProv, jml);
                    }
                }
                for (Provinsi p : provinsi) {
                    dataset.addValue(Integer.valueOf(temp.get(p.getKode()).toString()), p.getNama(), progdi.getNama());
                }
            }
        } else if (progdi != null && !tahun.isEmpty() && prov == null) {
            sql = "SELECT SUBSTR(kabupaten.kd_kab,1,2) AS provinsi, COUNT(pdf" + tahun + ".nomor) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + tahun + " pdf" + tahun + " "
                    + "ON (kabupaten.kd_kab = pdf" + tahun + ".kabupsek) "
                    + "WHERE (pdf" + tahun + ".pil1 = " + progdi.getKode() + ") "
                    + "GROUP BY SUBSTR(kabupaten.kd_kab,1,2)";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), progdi.getNama());
            }
        } else if (progdi != null && !tahun.isEmpty() && prov != null && kabkota == null) {
            sql = "SELECT kabupaten.nama_kab AS kabupaten, COUNT(pdf" + tahun + ".nomor) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + tahun + " pdf" + tahun + " "
                    + "ON (kabupaten.kd_kab = pdf" + tahun + ".kabupsek) "
                    + "WHERE (pdf" + tahun + ".pil1 = " + progdi.getKode() + ") "
                    + "AND SUBSTR(kabupaten.kd_kab,1,2)=" + prov.getKode() + " "
                    + "GROUP BY kabupaten.kd_kab";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kabupaten").toString(), progdi.getNama());
            }
        } else if (progdi != null && !tahun.isEmpty() && prov != null && kabkota != null) {
            sql = "SELECT kabupaten.nama_kab AS kabupaten, COUNT(pdf" + tahun + ".nomor) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + tahun + " pdf" + tahun + " "
                    + "ON (kabupaten.kd_kab = pdf" + tahun + ".kabupsek) "
                    + "WHERE (pdf" + tahun + ".pil1 = " + progdi.getKode() + ") "
                    + "AND kabupaten.kd_kab=" + kabkota.getKode() + " "
                    + "GROUP BY kabupaten.kd_kab";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kabupaten").toString(), progdi.getNama());
            }
        } else if (progdi == null && !tahun.isEmpty() && prov == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();
            for (ProgramStudi ps : progdis) {
                sql = "SELECT SUBSTR(kamus.kabupaten.kd_kab,1,2) AS provinsi, COUNT(pmb.pdf" + tahun + ".nomor) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + tahun + " pdf" + tahun + " "
                        + "ON (kabupaten.kd_kab = pdf" + tahun + ".kabupsek) "
                        + "GROUP BY SUBSTR(kabupaten.kd_kab,1,2)";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                }
            }
        } else if (progdi == null && !tahun.isEmpty() && prov != null && kabkota == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();
            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.kabupaten.nama_kab AS kabupaten, COUNT(pmb.pdf" + tahun + ".nomor) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + tahun + " pdf" + tahun + " "
                        + "ON (kabupaten.kd_kab = pdf" + tahun + ".kabupsek) "
                        + "WHERE SUBSTR(kamus.kabupaten.kd_kab,1,2)=" + prov.getKode() + " "
                        + "GROUP BY kabupaten.kd_kab";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    //Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kabupaten").toString(), ps.getNama());
                }
            }
        } else if (progdi == null && !tahun.isEmpty() && prov != null && kabkota != null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();
            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.kabupaten.nama_kab AS kabupaten, COUNT(pmb.pdf" + tahun + ".nomor) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + tahun + " pdf" + tahun + " "
                        + "ON (kabupaten.kd_kab = pdf" + tahun + ".kabupsek) "
                        + "WHERE kamus.kabupaten.kd_kab)=" + kabkota.getKode() + " "
                        + "GROUP BY kabupaten.kd_kab";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    //Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kabupaten").toString(), ps.getNama());
                }
            }
        } else if (progdi == null && tahun.isEmpty() && prov != null && kabkota == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();
            DateTime dt = new DateTime(new Date());
            int year = dt.getYear();
            for (ProgramStudi ps : progdis) {
                Map temp = new HashMap();
                List<String> kabupaten = new ArrayList<String>();
                for (int x = year; x > year - 5; x--) {
                    sql = "SELECT kamus.kabupaten.nama_kab AS kabupaten, COUNT(pmb.pdf" + x + ".nomor) AS jumlah "
                            + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + x + " pdf" + x + " "
                            + "ON (kabupaten.kd_kab = pdf" + x + ".kabupsek) "
                            + "WHERE SUBSTR(kamus.kabupaten.kd_kab,1,2)=" + prov.getKode() + " "
                            + "GROUP BY kabupaten.nama_kab";
                    List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                    for (Map m : rows) {
                        //Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        String nmKab = m.get("kabupaten").toString();
                        if (temp.get(nmKab) == null) {
                            //kabupaten.add(new KabKotaDAOImpl().getKabKota(m.get("kabupaten").toString()));
                            kabupaten.add(nmKab);
                            temp.put(nmKab, Integer.valueOf(m.get("jumlah").toString()));
                        } else {
                            int jml = Integer.valueOf(temp.get(nmKab).toString());
                            jml += Integer.valueOf(m.get("jumlah").toString());
                            temp.put(nmKab, jml);
                        }
                        //dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                    }
                }
                for (String p : kabupaten) {
                    dataset.addValue(Integer.valueOf(temp.get(p).toString()), p, ps.getNama());
                }
            }
        } else if (progdi == null && tahun.isEmpty() && prov != null && kabkota != null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();
            DateTime dt = new DateTime(new Date());
            int year = dt.getYear();
            for (ProgramStudi ps : progdis) {
                Map temp = new HashMap();
                List<String> kabupaten = new ArrayList<String>();
                for (int x = year; x > year - 5; x--) {
                    sql = "SELECT kamus.kabupaten.nama_kab AS kabupaten, COUNT(pmb.pdf" + x + ".nomor) AS jumlah "
                            + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + x + " pdf" + x + " "
                            + "ON (kabupaten.kd_kab = pdf" + x + ".kabupsek) "
                            + "WHERE kamus.kabupaten.kd_kab=" + kabkota.getKode() + " "
                            + "GROUP BY kabupaten.nama_kab";
                    List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                    for (Map m : rows) {
                        //Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        String nmKab = m.get("kabupaten").toString();
                        if (temp.get(nmKab) == null) {
                            //kabupaten.add(new KabKotaDAOImpl().getKabKota(m.get("kabupaten").toString()));
                            kabupaten.add(nmKab);
                            temp.put(nmKab, Integer.valueOf(m.get("jumlah").toString()));
                        } else {
                            int jml = Integer.valueOf(temp.get(nmKab).toString());
                            jml += Integer.valueOf(m.get("jumlah").toString());
                            temp.put(nmKab, jml);
                        }
                        //dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                    }
                }
                for (String p : kabupaten) {
                    dataset.addValue(Integer.valueOf(temp.get(p).toString()), p, ps.getNama());
                }
            }
        } else if (progdi != null && tahun.isEmpty() && prov != null && kabkota == null) {
            DateTime dt = new DateTime(new Date());
            int year = dt.getYear();
            Map temp = new HashMap();
            List<String> kabupaten = new ArrayList<String>();
            for (int x = year; x > year - 5; x--) {
                sql = "SELECT kamus.kabupaten.nama_kab AS kabupaten, COUNT(pmb.pdf" + x + ".nomor) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + x + " pdf" + x + " "
                        + "ON (kabupaten.kd_kab = pdf" + x + ".kabupsek) "
                        + "WHERE SUBSTR(kamus.kabupaten.kd_kab,1,2)=" + prov.getKode() + " "
                        + "AND pmb.pdf" + x + ".pil1=" + progdi.getKode() + " "
                        + "GROUP BY kabupaten.kd_kab";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    //Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                    String nmKab = m.get("kabupaten").toString();
                    if (temp.get(nmKab) == null) {
                        //kabupaten.add(new KabKotaDAOImpl().getKabKota(m.get("kabupaten").toString()));
                        kabupaten.add(nmKab);
                        temp.put(nmKab, Integer.valueOf(m.get("jumlah").toString()));
                    } else {
                        int jml = Integer.valueOf(temp.get(nmKab).toString());
                        jml += Integer.valueOf(m.get("jumlah").toString());
                        temp.put(nmKab, jml);
                    }
                    //dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                }
            }
            for (String p : kabupaten) {
                dataset.addValue(Integer.valueOf(temp.get(p).toString()), p, progdi.getNama());
            }

        } else if (progdi != null && tahun.isEmpty() && prov != null && kabkota != null) {
            DateTime dt = new DateTime(new Date());
            int year = dt.getYear();
            Map temp = new HashMap();
            List<String> kabupaten = new ArrayList<String>();
            for (int x = year; x > year - 5; x--) {
                sql = "SELECT kamus.kabupaten.nama_kab AS kabupaten, COUNT(pmb.pdf" + x + ".nomor) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN pmb.pdf" + x + " pdf" + x + " "
                        + "ON (kabupaten.kd_kab = pdf" + x + ".kabupsek) "
                        + "WHERE kamus.kabupaten.kd_kab=" + kabkota.getKode() + " "
                        + "AND pmb.pdf" + x + ".pil1=" + progdi.getKode() + " "
                        + "GROUP BY kabupaten.kd_kab";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    //Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                    String nmKab = m.get("kabupaten").toString();
                    if (temp.get(nmKab) == null) {
                        //kabupaten.add(new KabKotaDAOImpl().getKabKota(m.get("kabupaten").toString()));
                        kabupaten.add(nmKab);
                        temp.put(nmKab, Integer.valueOf(m.get("jumlah").toString()));
                    } else {
                        int jml = Integer.valueOf(temp.get(nmKab).toString());
                        jml += Integer.valueOf(m.get("jumlah").toString());
                        temp.put(nmKab, jml);
                    }
                    //dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                }
            }
            for (String p : kabupaten) {
                dataset.addValue(Integer.valueOf(temp.get(p).toString()), p, progdi.getNama());
            }
        }
        return dataset;
    }
}
