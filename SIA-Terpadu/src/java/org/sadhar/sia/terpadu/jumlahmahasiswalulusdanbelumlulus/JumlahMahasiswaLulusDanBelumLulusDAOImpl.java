/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswalulusdanbelumlulus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class JumlahMahasiswaLulusDanBelumLulusDAOImpl implements JumlahMahasiswaLulusDanBelumLulusDAO {

    public JumlahMahasiswaLulusDanBelumLulusDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getJumlahMahasiswa(String kodeProdi) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableMhsExist(kodeProdi)) {
                    String sql = "SELECT DISTINCT  'totalMahasiswa', IF(LEFT(nomor_mhs,1)='9', CONCAT('19', LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan, " +
                            " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                            " FROM db_" + kodeProdi + ".mhs" + kodeProdi + "   GROUP BY angkatan" +
                            " UNION " +
                            " SELECT DISTINCT  'totalMahasiswa', IF(LEFT(nomor_mhs,1)='9', CONCAT('19', LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan, " +
                            " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                            " FROM db_" + kodeProdi + ".ll" + kodeProdi + "   GROUP BY angkatan" +
                            " UNION " +
                            " SELECT DISTINCT  'totalMahasiswa', IF(LEFT(nomor_mhs,1)='9', CONCAT('19', LEFT(nomor_mhs,2))," +
                            " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan, " +
                            " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                            " FROM db_" + kodeProdi + ".do" + kodeProdi + "   GROUP BY angkatan";
                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            if (isTableMhsExist(kodeProdi)) {
                String sql = "SELECT DISTINCT  'totalMahasiswa', IF(LEFT(nomor_mhs,1)='9', CONCAT('19', LEFT(nomor_mhs,2))," +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan, " +
                        " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                        " FROM db_" + kodeProdi + ".mhs" + kodeProdi + "   GROUP BY angkatan" +
                        " UNION " +
                        " SELECT DISTINCT  'totalMahasiswa', IF(LEFT(nomor_mhs,1)='9', CONCAT('19', LEFT(nomor_mhs,2))," +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan, " +
                        " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                        " FROM db_" + kodeProdi + ".ll" + kodeProdi + "   GROUP BY angkatan" +
                        " UNION " +
                        " SELECT DISTINCT  'totalMahasiswa', IF(LEFT(nomor_mhs,1)='9', CONCAT('19', LEFT(nomor_mhs,2))," +
                        " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan, " +
                        " COUNT(LEFT(nomor_mhs,2)) AS jumlah " +
                        " FROM db_" + kodeProdi + ".do" + kodeProdi + "   GROUP BY angkatan";
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getJumlahMahasiswaLulus(String kodeProdi) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTableLulusExist(kodeProdi)) {
                    String sql = "SELECT DISTINCT  'totalMahasiswaLulus', IF(LEFT(nomor_mhs, 1)='9', CONCAT('19', LEFT(nomor_mhs, 2))," +
                            " IF(LEFT(nomor_mhs, 1)='8', CONCAT('19', LEFT(nomor_mhs,2)), CONCAT('20', LEFT(nomor_mhs, 2)))) AS angkatan, " +
                            " COUNT(LEFT(nomor_mhs, 2)) AS jumlah " +
                            " FROM db_" + kodeProdi + ".ll" + kodeProdi + "  GROUP BY angkatan";
                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            if (isTableLulusExist(kodeProdi)) {
                String sql = "SELECT DISTINCT  'totalMahasiswaLulus', IF(LEFT(nomor_mhs, 1)='9', CONCAT('19', LEFT(nomor_mhs, 2))," +
                        " IF(LEFT(nomor_mhs, 1)='8', CONCAT('19', LEFT(nomor_mhs,2)), CONCAT('20', LEFT(nomor_mhs, 2)))) AS angkatan, " +
                        " COUNT(LEFT(nomor_mhs, 2)) AS jumlah " +
                        " FROM db_" + kodeProdi + ".ll" + kodeProdi + "  GROUP BY angkatan";
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
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

    public boolean isTableMhsExist(String kodeProdi) {
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
}
