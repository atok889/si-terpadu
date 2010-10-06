/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistikmahasiswa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class StatistikMahasiswaDAOImpl implements StatistikMahasiswaDAO {

    public StatistikMahasiswaDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getAngkatan() {
        int tahun = 1980;
        List<Map> tahuns = new ArrayList<Map>();
        for (int i = tahun; i <= new DateTime().getYear(); i++) {
            Map map = new HashMap();
            map.put("angkatan", i);
            tahuns.add(map);
        }
        return tahuns;
    }

    public List<Map> getMahasiswaLulus(String kodeProdi) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableLulusExist(kodeProdi)) {
                    String sql = "SELECT DISTINCT 'Mahasiswa Lulus' AS status,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan," +
                            " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                            " FROM db_" + kodeProdi + ".ll" + kodeProdi +
                            " GROUP BY angkatan ";
                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT DISTINCT 'Mahasiswa Lulus' AS status,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan," +
                    " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                    " FROM db_" + kodeProdi + ".ll" + kodeProdi +
                    " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) " +
                    " GROUP BY angkatan ";
            if (isTableLulusExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getMahasiswaDO(String kodeProdi) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableDOExist(kodeProdi)) {
                    String sql = "SELECT DISTINCT 'Mahasiswa DO' AS status,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan," +
                            " COUNT(LEFT(nomor_mhs,2)) AS jumlah,nomor_mhs " +
                            " FROM db_" + kodeProdi + ".do" + kodeProdi +
                            " GROUP BY angkatan ";
                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }

            }
        } else {
            String sql = "SELECT DISTINCT 'Mahasiswa DO' AS status,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan," +
                    " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                    " FROM db_" + kodeProdi + ".do" + kodeProdi +
                    " GROUP BY angkatan ";
            if (isTableDOExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getMahasiswaCuti(String kodeProdi) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT DISTINCT 'Mahasiswa Cuti' AS status,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan," +
                            " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " WHERE st_mhs = '3' " +
                            " GROUP BY angkatan ";
                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT DISTINCT 'Mahasiswa Cuti' AS status,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan," +
                    " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                    " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " WHERE st_mhs = '3' " +
                    " GROUP BY angkatan ";
            if (isTableMhsExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }

        }
        return results;
    }

    public List<Map> getMahasiswaRegistrasi(String kodeProdi) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT DISTINCT 'Mahasiswa Registrasi' AS status,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan," +
                            " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " WHERE st_mhs = '1' " +
                            " GROUP BY angkatan ";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT DISTINCT 'Mahasiswa Registrasi' AS status,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan," +
                    " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                    " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " WHERE st_mhs = '1' " +
                    " GROUP BY angkatan ";

            if (isTableMhsExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }

        }
        return results;
    }

    public List<Map> getMahasiswaTidakRegistrasi(String kodeProdi) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT DISTINCT 'Mahasiswa Tidak Registrasi' AS status,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan," +
                            " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " WHERE st_mhs = '2' " +
                            " GROUP BY angkatan ";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT DISTINCT 'Mahasiswa Tidak Registrasi' AS status,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan," +
                    " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                    " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " WHERE st_mhs = '2' " +
                    " GROUP BY angkatan ";

            if (isTableMhsExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getDetailMahasiswaDO(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableDOExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                            " FROM db_" + kodeProdi + ".do" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + " ORDER BY angkatan ";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                    " FROM db_" + kodeProdi + ".do" + kodeProdi + " mhs " +
                    " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                    " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + " ORDER BY angkatan ";

            if (isTableDOExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getDetailAllMahasiswaDO(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableDOExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                            " FROM db_" + kodeProdi + ".do" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + " ORDER BY angkatan";
                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                    " FROM db_" + kodeProdi + ".do" + kodeProdi + " mhs " +
                    " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                    " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + " ORDER BY angkatan";

            if (isTableDOExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getDetailMahasiswaRegistrasi(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + " AND mhs.st_mhs = '1' " +
                            " ORDER BY angkatan";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                    " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                    " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                    " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + " AND mhs.st_mhs = '1' " +
                    " ORDER BY angkatan";

            if (isTableMhsExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getDetailAllMahasiswaRegistrasi(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + "  AND mhs.st_mhs = '1' " +
                            " ORDER BY angkatan";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                    " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                    " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                    " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + "  AND mhs.st_mhs = '1'" +
                    " ORDER BY angkatan ";

            if (isTableMhsExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getDetailMahasiswaTidakRegistrasi(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + " AND mhs.st_mhs = '2'" +
                            " ORDER BY angkatan";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                    " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                    " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                    " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + " AND mhs.st_mhs = '2' " +
                    " ORDER BY angkatan";

            if (isTableMhsExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getDetailAllMahasiswaTidakRegistrasi(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + "  AND mhs.st_mhs = '2'" +
                            " ORDER BY angkatan ";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                    " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                    " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                    " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + "  AND mhs.st_mhs = '2' " +
                    " ORDER BY angkatan";

            if (isTableMhsExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getDetailMahasiswaCuti(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + " AND mhs.st_mhs = '3'" +
                            " ORDER BY angkatan";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                    " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                    " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                    " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + " AND mhs.st_mhs = '2'" +
                    " ORDER BY angkatan ";

            if (isTableMhsExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getDetailAllMahasiswaCuti(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + "  AND mhs.st_mhs = '3' " +
                            " ORDER BY angkatan";
                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                    " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                    " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                    " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + "  AND mhs.st_mhs = '3'" +
                    " ORDER BY angkatan ";

            if (isTableMhsExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getDetailMahasiswaLulus(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableLulusExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                            " FROM db_" + kodeProdi + ".ll" + kodeProdi + " mhs " +
                            " RIGHT JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + "" +
                            " ORDER BY angkatan";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                    " FROM db_" + kodeProdi + ".ll" + kodeProdi + " mhs " +
                    " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                    " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + "" +
                    " ORDER BY angkatan ";

            if (isTableLulusExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getDetailAllMahasiswaLulus(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableLulusExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                            " FROM db_" + kodeProdi + ".ll" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + " ORDER BY angkatan ";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan" +
                    " FROM db_" + kodeProdi + ".ll" + kodeProdi + " mhs " +
                    " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                    " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + " ORDER BY angkatan ";

            if (isTableLulusExist(kodeProdi)) {
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
            System.out.println(results.size());
        }
        return results;
    }

    public List<Map> getDetailMahasiswa(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs  " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + " " +
                            " AND (st_mhs = '1' OR st_mhs = '2' OR st_mhs = '3')";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
                if (isTableDOExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                            " FROM db_" + kodeProdi + ".do" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + "";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
                if (isTableLulusExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                            " FROM db_" + kodeProdi + ".ll" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + "";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            if (isTableMhsExist(kodeProdi)) {
                String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                        " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs  " +
                        " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                        " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + " " +
                        " AND (st_mhs = '1' OR st_mhs = '2' OR st_mhs = '3')";

                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
            if (isTableDOExist(kodeProdi)) {
                String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                        " FROM db_" + kodeProdi + ".do" + kodeProdi + " mhs " +
                        " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                        " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + "";

                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
            if (isTableLulusExist(kodeProdi)) {
                String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                        " FROM db_" + kodeProdi + ".ll" + kodeProdi + " mhs " +
                        " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                        " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) = " + tahun + "";

                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getDetailAllMahasiswa(String kodeProdi, String tahun) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs  " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + " " +
                            " AND (st_mhs = '1' OR st_mhs = '2' OR st_mhs = '3')";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
                if (isTableDOExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                            " FROM db_" + kodeProdi + ".do" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + "";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
                if (isTableLulusExist(kodeProdi)) {
                    String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                            " FROM db_" + kodeProdi + ".ll" + kodeProdi + " mhs " +
                            " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                            " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + "";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            if (isTableMhsExist(kodeProdi)) {
                String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                        " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs  " +
                        " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                        " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + " " +
                        " AND (st_mhs = '1' OR st_mhs = '2' OR st_mhs = '3')";

                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
            if (isTableDOExist(kodeProdi)) {
                String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                        " FROM db_" + kodeProdi + ".do" + kodeProdi + " mhs " +
                        " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                        " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + "";

                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
            if (isTableLulusExist(kodeProdi)) {
                String sql = "SELECT mhs.nama_mhs,prodi.Nama_prg,IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2))," +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan " +
                        " FROM db_" + kodeProdi + ".ll" + kodeProdi + " mhs " +
                        " INNER JOIN kamus.prg_std prodi ON prodi.Kd_prg = '" + kodeProdi + "' " +
                        " WHERE IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) >= " + tahun + "";

                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public boolean isTableMhsExist(String kodeProdi) {
        SqlRowSet rs = null;
        Boolean result = null;
        String sql = "Show tables from db_" + kodeProdi + " like 'mhs" + kodeProdi + "'";
        try {
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
            result = rs.next();
        } catch (Exception e) {
            System.out.println("Table mhs tidak ada");
            result = false;
        }
        return result;
    }

    public boolean isTableLulusExist(String kodeProdi) {
        SqlRowSet rs = null;
        Boolean result = null;
        String sql = "Show tables from db_" + kodeProdi + " like 'll" + kodeProdi + "'";
        try {
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
            result = rs.next();
        } catch (Exception e) {
            System.out.println("Table ll tidak ada");
            result = false;
        }
        return result;
    }

    public boolean isTableDOExist(String kodeProdi) {
        SqlRowSet rs = null;
        Boolean result = null;
        String sql = "Show tables from db_" + kodeProdi + " like 'do" + kodeProdi + "'";
        try {
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
            result = rs.next();
        } catch (Exception e) {
            System.out.println("Table do tidak ada");
            result = false;
        }
        return result;
    }
}
