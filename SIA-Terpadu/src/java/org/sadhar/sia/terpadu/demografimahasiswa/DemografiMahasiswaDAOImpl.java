/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.demografimahasiswa;

import org.sadhar.sia.terpadu.kabkota.KabKota;
import org.sadhar.sia.terpadu.provinsi.Provinsi;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;
import org.sadhar.sia.terpadu.prodi.ProgramStudiDAOImpl;
import org.sadhar.sia.terpadu.provinsi.ProvinsiDAOImpl;

/**
 *
 * @author Hendro Steven
 */
public class DemografiMahasiswaDAOImpl implements DemografiMahasiswaDAO {

    public DemografiMahasiswaDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
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
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

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
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

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
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

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
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

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
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

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
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

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
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

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
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

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

    public CategoryDataset getAsalDaerahDataset(ProgramStudi progdi, String tahun, Provinsi prov, KabKota kabkota, int jumlah) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (progdi != null && tahun.isEmpty() && prov == null) {
            sql = "SELECT SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2) AS provinsi, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "GROUP BY SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2)";

            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            if (jumlah == 0) {
                for (Map m : rows) {
                    Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                }
            } else if (jumlah == 1) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml < 50) {
                        Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                    }
                }
            } else if (jumlah == 2) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 50 && jml < 100) {
                        Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                    }
                }
            } else if (jumlah == 3) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 100 && jml < 250) {
                        Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                    }
                }
            } else if (jumlah == 4) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 250 && jml < 500) {
                        Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                    }
                }
            } else if (jumlah == 5) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 500) {
                        Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                    }
                }
            }
        } else if (progdi != null && !tahun.isEmpty() && prov == null) {
            sql = "SELECT SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2) AS provinsi, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "WHERE YEAR(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".tglmskusd)='" + tahun + "' "
                    + "GROUP BY SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2)";

            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            if (jumlah == 0) {
                for (Map m : rows) {
                    Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                }
            } else if (jumlah == 1) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml < 50) {
                        Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                    }
                }
            } else if (jumlah == 2) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 50 && jml < 100) {
                        Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                    }
                }
            } else if (jumlah == 3) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 100 && jml < 250) {
                        Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                    }
                }
            } else if (jumlah == 4) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 250 && jml < 500) {
                        Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                    }
                }
            } else if (jumlah == 5) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 500) {
                        Provinsi p = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), p.getNama(), progdi.getNama());
                    }
                }
            }
        } else if (progdi != null && !tahun.isEmpty() && prov != null && kabkota == null) {

            sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "WHERE YEAR(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".tglmskusd)='" + tahun + "' "
                    + "AND SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2)='" + prov.getKode() + "' "
                    + "GROUP BY db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal";

            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            if (jumlah == 0) {
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                }
            } else if (jumlah == 1) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml < 50) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 2) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 50 && jml < 100) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 3) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 100 && jml < 250) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 4) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 250 && jml < 500) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 5) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 500) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            }
        } else if (progdi != null && !tahun.isEmpty() && prov != null && kabkota != null) {
            sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "WHERE YEAR(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".tglmskusd)='" + tahun + "' "
                    + "AND db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal='" + kabkota.getKode() + "' "
                    + " GROUP BY mhs" + progdi.getKode() + ".kd_kab_asal";

            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            if (jumlah == 0) {
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                }
            } else if (jumlah == 1) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml < 50) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 2) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 50 && jml < 100) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 3) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 100 && jml < 250) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 4) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 250 && jml < 500) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 5) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 500) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            }
        } else if (progdi != null && tahun.isEmpty() && prov != null && kabkota == null) {

            sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "WHERE SUBSTRING(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal,1,2)='" + prov.getKode() + "'  "
                    + "GROUP BY db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal";


            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            if (jumlah == 0) {
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                }
            } else if (jumlah == 1) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml < 50) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 2) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 50 && jml < 100) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 3) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 100 && jml < 250) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 4) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 250 && jml < 500) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 5) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 500) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            }
        } else if (progdi != null && tahun.isEmpty() && prov != null && kabkota != null) {

            sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".nomor_mhs) AS jumlah "
                    + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " mhs" + progdi.getKode() + " "
                    + "ON (kabupaten.kd_kab = mhs" + progdi.getKode() + ".kd_kab_asal) "
                    + "WHERE db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal='" + kabkota.getKode() + "' "
                    + "GROUP BY db_" + progdi.getKode() + ".mhs" + progdi.getKode() + ".kd_kab_asal";


            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            if (jumlah == 0) {
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                }
            } else if (jumlah == 1) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml < 50) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 2) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 50 && jml < 100) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 3) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 100 && jml < 250) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 4) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 250 && jml < 500) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            } else if (jumlah == 5) {
                for (Map m : rows) {
                    int jml = Integer.valueOf(m.get("jumlah").toString());
                    if (jml >= 500) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), progdi.getNama());
                    }
                }
            }
        } //progdi  kosong
        else if (progdi == null && tahun.isEmpty() && prov == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {

                sql = "SELECT SUBSTRING(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal,1,2) AS provinsi, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (kabupaten.kd_kab = mhs" + ps.getKode() + ".kd_kab_asal) "
                        + "GROUP BY SUBSTRING(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal,1,2)";

                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                if (jumlah == 0) {
                    for (Map m : rows) {
                        Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                    }
                } else if (jumlah == 1) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml < 50) {
                            Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                        }
                    }
                } else if (jumlah == 2) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 50 && jml < 100) {
                            Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                        }
                    }
                } else if (jumlah == 3) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 100 && jml < 250) {
                            Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                        }
                    }
                } else if (jumlah == 4) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 250 && jml < 500) {
                            Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                        }
                    }
                } else if (jumlah == 5) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 500) {
                            Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                        }
                    }
                }
            }
        } else if (progdi == null && !tahun.isEmpty() && prov == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {

                sql = "SELECT SUBSTRING(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal,1,2) AS provinsi, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (kabupaten.kd_kab = mhs" + ps.getKode() + ".kd_kab_asal) "
                        + "WHERE YEAR(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".tglmskusd)='" + tahun + "' "
                        + "GROUP BY SUBSTRING(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal,1,2)";

                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                if (jumlah == 0) {
                    for (Map m : rows) {
                        Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                    }
                } else if (jumlah == 1) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml < 50) {
                            Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                        }
                    }
                } else if (jumlah == 2) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 50 && jml < 100) {
                            Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                        }
                    }
                } else if (jumlah == 3) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 100 && jml < 250) {
                            Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                        }
                    }
                } else if (jumlah == 4) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 250 && jml < 500) {
                            Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                        }
                    }
                } else if (jumlah == 5) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 500) {
                            Provinsi provinsi = new ProvinsiDAOImpl().getProv(m.get("provinsi").toString());
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), provinsi.getNama(), ps.getNama());
                        }
                    }
                }
            }
        } else if (progdi == null && !tahun.isEmpty() && prov != null && kabkota == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

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
                if (jumlah == 0) {
                    for (Map m : rows) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                    }
                } else if (jumlah == 1) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml < 50) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 2) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 50 && jml < 100) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 3) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 100 && jml < 250) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 4) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 250 && jml < 500) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 5) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 500) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                }
            }
        } else if (progdi == null && !tahun.isEmpty() && prov != null && kabkota != null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

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
                if (jumlah == 0) {
                    for (Map m : rows) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                    }
                } else if (jumlah == 1) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml < 50) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 2) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 50 && jml < 100) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 3) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 100 && jml < 250) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 4) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 250 && jml < 500) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 5) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 500) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                }
            }
        } else if (progdi == null && tahun.isEmpty() && prov != null && kabkota == null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {

                sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (kabupaten.kd_kab = mhs" + ps.getKode() + ".kd_kab_asal) "
                        + "WHERE SUBSTRING(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal,1,2)='" + prov.getKode() + "'  "
                        + "GROUP BY db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal";


                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                if (jumlah == 0) {
                    for (Map m : rows) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                    }
                } else if (jumlah == 1) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml < 50) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 2) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 50 && jml < 100) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 3) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 100 && jml < 250) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 4) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 250 && jml < 500) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                } else if (jumlah == 5) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 500) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                }
            }
        } else if (progdi == null && tahun.isEmpty() && prov != null && kabkota != null) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = new ProgramStudiDAOImpl().getProgramStudi();

//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));


            for (ProgramStudi ps : progdis) {

                sql = "SELECT kamus.kabupaten.nama_kab AS kab, COUNT(db_" + ps.getKode() + ".mhs" + ps.getKode() + ".nomor_mhs) AS jumlah "
                        + "FROM kamus.kabupaten kabupaten INNER JOIN db_" + ps.getKode() + ".mhs" + ps.getKode() + " mhs" + ps.getKode() + " "
                        + "ON (kabupaten.kd_kab = mhs" + ps.getKode() + ".kd_kab_asal) "
                        + "WHERE db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal='" + kabkota.getKode() + "' "
                        + "GROUP BY db_" + ps.getKode() + ".mhs" + ps.getKode() + ".kd_kab_asal";


                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                if (jumlah == 0) {
                    for (Map m : rows) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                    }
                } else if (jumlah == 1) {
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml < 50) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                }else if(jumlah==2){
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 50 && jml<100) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                }else if(jumlah==3){
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 100 && jml<250) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                }else if(jumlah==4){
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 250 && jml<500) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                }else if(jumlah==5){
                    for (Map m : rows) {
                        int jml = Integer.valueOf(m.get("jumlah").toString());
                        if (jml >= 500) {
                            dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("kab").toString(), ps.getNama());
                        }
                    }
                }
            }
        }
        return dataset;
    }
}
