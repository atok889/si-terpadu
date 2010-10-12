/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.ipkdanipssetiapmahasiswa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class IpkdanIpsSetiapMahasiswaDAOImpl implements IpkdanIpsSetiapMahasiswaDAO {

    public IpkdanIpsSetiapMahasiswaDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getIpkDanIpsSetiapMahasiswa(String kodeProdi, String angkatan, String tahun, String semester, String param1, String param2, String param3) {
        List<Map> results = new ArrayList<Map>();
        String sqlCreateView = "create or replace view  kamus.ipkDanIpsSetiapMahasiswa(" +
                "  nomor_mhs, nama_mhs, angkatan, tahun, semester, ipk, ips, Nama_prg, fakultas ) as" +
                " SELECT  mhs.nomor_mhs,mhs.nama_mhs,IF(LEFT(kt.nomor_mhs, 1)='9', CONCAT('19', LEFT(kt.nomor_mhs, 2)), IF(LEFT(kt.nomor_mhs, 1)='8', " +
                " CONCAT('19', LEFT(kt.nomor_mhs,2)), CONCAT('20', LEFT(kt.nomor_mhs, 2)))) AS angkatan, " +
                " SUBSTRING(kt.ambil,1,4) as tahun, SUBSTRING(kt.ambil,5,1) AS semester, 0 AS ipk, " +
                " SUM(kt.sks * nilai.angka)/SUM(kt.sks) as ips, prg.Nama_prg, fak.Nama_fak as fakultas " +
                " FROM db_" + kodeProdi + ".kt" + kodeProdi + angkatan + " as kt " +
                " INNER JOIN kamus.nilai nilai ON (nilai.huruf = kt.Nilai) " +
                " INNER JOIN kamus.prg_std prg ON (prg.Kd_prg = '" + kodeProdi + "') " +
                " INNER JOIN db_" + kodeProdi + ".mhs" + kodeProdi + " mhs ON (mhs.nomor_mhs = kt.nomor_mhs) " +
                " INNER JOIN kamus.fakultas fak ON (fak.Kd_fakultas = prg.Kd_fak) " +
                " WHERE SUBSTRING(kt.ambil,5,1) = 1 OR SUBSTRING(kt.ambil,5,1) = 2 " +
                " GROUP BY nomor_mhs, semester  " +
                " UNION " +
                " SELECT  mhs.nomor_mhs,mhs.nama_mhs,IF(LEFT(kh.nomor_mhs, 1)='9', CONCAT('19', LEFT(kh.nomor_mhs, 2)), IF(LEFT(kh.nomor_mhs, 1)='8', CONCAT('19', LEFT(kh.nomor_mhs,2)), CONCAT('20', LEFT(kh.nomor_mhs, 2)))) AS angkatan, " +
                " SUBSTRING(kh.ambil,1,4) as tahun, SUBSTRING(kh.ambil,5,1) AS semester, " +
                " SUM(kh.sks * nilai.angka)/SUM(kh.sks) as ipk, 0 AS ips,prg.Nama_prg, fak.Nama_fak as fakultas " +
                " FROM db_" + kodeProdi + ".kh" + kodeProdi + angkatan + " as kh " +
                " INNER JOIN kamus.nilai nilai ON (nilai.huruf = kh.Nilai) " +
                " INNER JOIN kamus.prg_std prg ON (prg.Kd_prg = '" + kodeProdi + "')" +
                " INNER JOIN db_" + kodeProdi + ".mhs" + kodeProdi + " mhs ON (mhs.nomor_mhs = kh.nomor_mhs) " +
                " INNER JOIN kamus.fakultas fak ON (fak.Kd_fakultas = prg.Kd_fak) " +
                " WHERE SUBSTRING(kh.ambil,5,1) = 1 OR SUBSTRING(kh.ambil,5,1) = 2" +
                " GROUP BY nomor_mhs, semester";


        try {
            if (isTabelKhExist(kodeProdi, tahun) && isTabelKtExist(kodeProdi, tahun)) {
                ClassConnection.getJdbc().execute(sqlCreateView);
                System.out.println("view " + sqlCreateView);
                String param = "ORDER BY ";

                if (param1 != null) {
                    param += " ipk,";
                }
                if (param2 != null) {
                    param += " ips,";
                }
                if (param3 != null) {
                    param += " nama_mhs,";
                }
                param += " nomor_mhs";

                String sql = "select  nomor_mhs, nama_mhs, angkatan, tahun, semester, SUM(ipk) ipk, SUM(ips) ips, Nama_prg, fakultas " +
                        " FROM kamus.ipkDanIpsSetiapMahasiswa " +
                        "  WHERE tahun = '" + tahun + "' and semester = '" + semester + "'" +
                        " GROUP BY nomor_mhs, semester " + param;
                System.out.println(sql);
                results = ClassConnection.getJdbc().queryForList(sql);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return results;
    }

    public boolean isTabelKtExist(String kodeProdi, String tahun) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'kt" + kodeProdi + tahun + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }

    public boolean isTabelKhExist(String kodeProdi, String tahun) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'kh" + kodeProdi + tahun + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }
}
