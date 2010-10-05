/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.sebaranipklulusan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class SebaranIpkLulusanDAOImpl implements SebaranIpkLulusanDAO {

    public SebaranIpkLulusanDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std ORDER BY Nama_prg WHERE Kd_prg != '0000' ORDER BY Nama_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getSebaranIpkLulusan() {
        List<Map> results = new ArrayList<Map>();
        for (Map prodi : getProdi()) {
            String kodeProdi = prodi.get("Kd_prg").toString();
            for (int i = new DateTime().getYear() - 5; i <= new DateTime().getYear(); i++) {
                if (isTabelKhExist(kodeProdi, String.valueOf(i))) {
                    String sql = "SELECT kh.Nomor_mhs, COUNT(Nomor_mhs) AS jumlah,SUM(SKS * angka)/SUM(SKS) as ipk,prg.Nama_prg " +
                            " FROM db_" + kodeProdi + ".kh" + kodeProdi + i + " kh " +
                            " INNER JOIN kamus.nilai nilai ON (nilai.huruf = kh.Nilai) " +
                            " INNER JOIN kamus.prg_std prg  ON (prg.Kd_prg='" + kodeProdi + "') GROUP BY kh.Nomor_mhs";
                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        }
        return results;
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
