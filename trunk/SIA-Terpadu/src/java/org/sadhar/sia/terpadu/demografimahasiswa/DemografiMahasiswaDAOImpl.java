/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.demografimahasiswa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.jumlahmahasiswa.ProgramStudi;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Hendro Steven
 */
public class DemografiMahasiswaDAOImpl implements DemografiMahasiswaDAO {

    public DemografiMahasiswaDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<ProgramStudi> getProgramStudi() throws Exception {
        List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
        String sql = "SELECT Kd_prg,Nama_prg FROM kamus.prg_std ORDER BY Kd_prg";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            ProgramStudi ps = new ProgramStudi();
            ps.setKode(m.get("Kd_prg").toString());
            ps.setNama(m.get("Nama_prg").toString());
            progdis.add(ps);
        }
        return progdis;
    }

    public CategoryDataset getJenisKelaminDataset(ProgramStudi progdi, String tahun) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (progdi != null && tahun.isEmpty()) {
            sql = "SELECT kamus.sex.Nm_sex AS sex, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.sex sex INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (sex.Kd_sex = mhs" + progdi.getKode() + ".kd_sex) GROUP BY sex.Nm_sex";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("sex").toString(), progdi.getNama());
            }
        } else if (progdi != null && !tahun.isEmpty()) {
            sql = "SELECT kamus.sex.Nm_sex AS sex, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.sex sex INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (sex.Kd_sex = mhs" + progdi.getKode() + ".kd_sex) WHERE YEAR(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".tglmskusd)='" + tahun + "' GROUP BY sex.Nm_sex";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("sex").toString(), progdi.getNama());
            }
        } else if (progdi == null && tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.sex.Nm_sex AS sex, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.sex sex INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (sex.Kd_sex = mhs" + ps.getKode() + ".kd_sex) GROUP BY sex.Nm_sex";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("sex").toString(), ps.getNama());
                }
            }
        } else if (progdi == null && !tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.sex.Nm_sex AS sex, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.sex sex INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (sex.Kd_sex = mhs" + ps.getKode() + ".kd_sex) WHERE YEAR(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".tglmskusd)='" + tahun + "' GROUP BY sex.Nm_sex";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("sex").toString(), ps.getNama());
                }
            }
        }
        return dataset;
    }

    public CategoryDataset getAgamaDataset(ProgramStudi progdi, String tahun) throws Exception {
        String sql = "";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (progdi != null && tahun.isEmpty()) {
            sql = "SELECT kamus.agama.Nm_agama as agama, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.agama agama INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (agama.Kd_agama = mhs" + progdi.getKode() + ".kd_agama) GROUP BY agama.Nm_agama";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("agama").toString(), progdi.getNama());
            }
        } else if (progdi != null && !tahun.isEmpty()) {
            sql = "SELECT kamus.agama.Nm_agama as agama, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.agama agama INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (agama.Kd_agama = mhs" + progdi.getKode() + ".kd_agama) WHERE YEAR(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".tglmskusd)='" + tahun + "' GROUP BY agama.Nm_agama";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("agama").toString(), progdi.getNama());
            }
        } else if (progdi == null && tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));

            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.agama.Nm_agama as agama, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.agama agama INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (agama.Kd_agama = mhs" + ps.getKode() + ".kd_agama) GROUP BY agama.Nm_agama";

                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("agama").toString(), ps.getNama());
                }
            }
        } else if (progdi == null && !tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.agama.Nm_agama as agama, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.agama agama INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (agama.Kd_agama = mhs" + ps.getKode() + ".kd_agama) WHERE YEAR(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".tglmskusd)='" + tahun + "' GROUP BY agama.Nm_agama";

                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("agama").toString(), ps.getNama());
                }
            }
        }
        return dataset;
    }

    public CategoryDataset getEkonomiOrtuDataset(ProgramStudi progdi, String tahun) throws Exception {
        String sql = "";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (progdi != null && tahun.isEmpty()) {
            sql = "SELECT kamus.penghasilan.nm_hasil as penghasilan, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.penghasilan penghasilan INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (penghasilan.kd_hasil = mhs" + progdi.getKode() + ".hasil_ortu) "
                    + "GROUP BY penghasilan.nm_hasil";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("penghasilan").toString(), progdi.getNama());
            }
        } else if (progdi != null && !tahun.isEmpty()) {
            sql = "SELECT kamus.penghasilan.nm_hasil as penghasilan, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.penghasilan penghasilan INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (penghasilan.kd_hasil = mhs" + progdi.getKode() + ".hasil_ortu) "
                    + "WHERE YEAR(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".tglmskusd)='" + tahun + "' GROUP BY penghasilan.nm_hasil";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("penghasilan").toString(), progdi.getNama());
            }
        } else if (progdi == null && tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));

            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.penghasilan.nm_hasil as penghasilan, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.penghasilan penghasilan INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (penghasilan.kd_hasil = mhs" + ps.getKode() + ".hasil_ortu) "
                        + "GROUP BY penghasilan.nm_hasil";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("penghasilan").toString(), ps.getNama());
                }
            }
        } else if (progdi == null && !tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.penghasilan.nm_hasil as penghasilan, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.penghasilan penghasilan INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (penghasilan.kd_hasil = mhs" + ps.getKode() + ".hasil_ortu) "
                        + "WHERE YEAR(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".tglmskusd)='" + tahun + "' GROUP BY penghasilan.nm_hasil";

                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("penghasilan").toString(), ps.getNama());
                }
            }
        }
        return dataset;
    }

    public CategoryDataset getPekerjaanOrtuDataset(ProgramStudi progdi, String tahun) throws Exception {
        String sql = "";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (progdi != null && tahun.isEmpty()) {
            sql = "SELECT kamus.pekerjaan.Nm_kerja as pekerjaan, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.pekerjaan pekerjaan INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (pekerjaan.Kd_kerja = mhs" + progdi.getKode() + ".kerja_bpk) "
                    + "GROUP BY pekerjaan.Nm_kerja";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("pekerjaan").toString(), progdi.getNama());
            }
        } else if (progdi != null && !tahun.isEmpty()) {
            sql = "SELECT kamus.pekerjaan.Nm_kerja as pekerjaan, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.pekerjaan pekerjaan INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (pekerjaan.Kd_kerja = mhs" + progdi.getKode() + ".kerja_bpk) "
                    + "WHERE YEAR(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".tglmskusd)='" + tahun + "' GROUP BY pekerjaan.Nm_kerja";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("pekerjaan").toString(), progdi.getNama());
            }
        } else if (progdi == null && tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));

            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.pekerjaan.Nm_kerja as pekerjaan, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.pekerjaan pekerjaan INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (pekerjaan.Kd_kerja = mhs" + ps.getKode() + ".kerja_bpk) "
                        + "GROUP BY pekerjaan.Nm_kerja";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("pekerjaan").toString(), ps.getNama());
                }
            }
        } else if (progdi == null && !tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.pekerjaan.Nm_kerja as pekerjaan, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.pekerjaan pekerjaan INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (pekerjaan.Kd_kerja = mhs" + ps.getKode() + ".kerja_bpk) "
                        + "WHERE YEAR(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".tglmskusd)='" + tahun + "' GROUP BY pekerjaan.Nm_kerja";

                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("penghasilan").toString(), ps.getNama());
                }
            }
        }
        return dataset;
    }

    public CategoryDataset getAsalDaerahDataset(ProgramStudi progdi, String tahun, Provinsi prov, KabKota kabkota) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (progdi != null && tahun.isEmpty() && prov == null) {
            sql = "SELECT SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2) AS provinsi, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "GROUP BY SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2)";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                Provinsi p = getProv(m.get("provinsi").toString());
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
            }
        } else if (progdi != null && !tahun.isEmpty() && prov == null) {
            sql = "SELECT SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2) AS provinsi, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "WHERE YEAR(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".tglmskusd)='" + tahun + "' "
                    + "GROUP BY SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2)";

            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                Provinsi p = getProv(m.get("provinsi").toString());
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
            }
        } else if (progdi != null && !tahun.isEmpty() && prov != null && kabkota == null) {
            sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "WHERE YEAR(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".tglmskusd)='" + tahun + "' "
                    + "AND SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2)='" + prov.getKode() + "' "
                    + "GROUP BY db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal";

            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
            }
        } else if (progdi != null && !tahun.isEmpty() && prov != null && kabkota != null) {
            sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "WHERE YEAR(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".tglmskusd)='" + tahun + "' "
                    + "AND db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal='" + kabkota.getKode() + "' "
                    + " GROUP BY mhs" + progdi.getKode() + ".kd_kab_asal";
            System.out.println(sql);
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
            }
        } else if (progdi != null && tahun.isEmpty() && prov != null && kabkota == null) {
            sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "WHERE SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2)='" + prov.getKode() + "'  "
                    + "GROUP BY db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal";

            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
            }
        } else if (progdi != null && tahun.isEmpty() && prov != null && kabkota != null) {
            sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "WHERE db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal='" + kabkota.getKode() + "' "
                    + "GROUP BY db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal";

            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
            }
        } //progdi tdk kosong
        else if (progdi == null && tahun.isEmpty() && prov == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {
                sql = "SELECT SUBSTRING(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal,1,2) AS provinsi, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (kabupaten.kd_kab = mhs" + ps.getKode() + ".kd_kab_asal) "
                        + "GROUP BY SUBSTRING(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal,1,2)";

                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    Provinsi provinsi = getProv(m.get("provinsi").toString());
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                }
            }
        } else if (progdi == null && !tahun.isEmpty() && prov == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {
                sql = "SELECT SUBSTRING(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal,1,2) AS provinsi, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (kabupaten.kd_kab = mhs" + ps.getKode() + ".kd_kab_asal) "
                        + "WHERE YEAR(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".tglmskusd)='" + tahun + "' "
                        + "GROUP BY SUBSTRING(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal,1,2)";

                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    Provinsi provinsi = getProv(m.get("provinsi").toString());
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                }
            }
        } else if (progdi == null && !tahun.isEmpty() && prov != null && kabkota == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (kabupaten.kd_kab = mhs" + ps.getKode() + ".kd_kab_asal) "
                        + "WHERE YEAR(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".tglmskusd)='" + tahun + "' "
                        + "AND SUBSTRING(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal,1,2)='" + prov.getKode() + "' "
                        + "GROUP BY db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal";

                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                }
            }
        } else if (progdi == null && !tahun.isEmpty() && prov != null && kabkota != null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (kabupaten.kd_kab = mhs" + ps.getKode() + ".kd_kab_asal) "
                        + "WHERE YEAR(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".tglmskusd)='" + tahun + "' "
                        + "AND db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal='" + kabkota.getKode() + "' "
                        + " GROUP BY mhs" + ps.getKode() + ".kd_kab_asal";

                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                }
            }
        } else if (progdi == null && tahun.isEmpty() && prov != null && kabkota == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (kabupaten.kd_kab = mhs" + ps.getKode() + ".kd_kab_asal) "
                        + "WHERE SUBSTRING(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal,1,2)='" + prov.getKode() + "'  "
                        + "GROUP BY db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal";

                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                }
            }
        } else if (progdi == null && tahun.isEmpty() && prov != null && kabkota != null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {
                sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (kabupaten.kd_kab = mhs" + ps.getKode() + ".kd_kab_asal) "
                        + "WHERE db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal='" + kabkota.getKode() + "' "
                        + "GROUP BY db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal";


                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                }
            }
        }
        return dataset;
    }

    public List<Provinsi> getProvinsi() throws Exception {
        List<Provinsi> prov = new ArrayList<Provinsi>();
        String sql = "SELECT kd_prop,nama_prop FROM kamus.propinsi ORDER BY kd_prop";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            Provinsi p = new Provinsi();
            p.setKode(m.get("kd_prop").toString());
            p.setNama(m.get("nama_prop").toString());
            prov.add(p);
        }
        return prov;
    }

    public List<KabKota> getKabKota(Provinsi provinsi) throws Exception {
        List<KabKota> kk = new ArrayList<KabKota>();
        String sql = "SELECT kd_kab,nama_kab FROM kamus.kabupaten WHERE SUBSTRING(kd_kab,1,2)='" + provinsi.getKode() + "'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            KabKota kab = new KabKota();
            kab.setKode(m.get("kd_kab").toString());
            kab.setNama(m.get("nama_kab").toString());
            kk.add(kab);
        }
        return kk;
    }

    public Provinsi getProv(String kode) throws Exception {
        String sql = "SELECT kd_prop,nama_prop FROM kamus.propinsi WHERE kd_prop=?";
        return (Provinsi) ClassConnection.getJdbc().queryForObject(sql, new Object[]{kode},
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        return new Provinsi(
                                rs.getString("kd_prop"),
                                rs.getString("nama_prop"));
                    }
                });
    }
}
