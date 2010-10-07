/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataipklulusan;

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
public class RerataIpkLulusanDAOImpl implements RerataIpkLulusanDAO {

    public RerataIpkLulusanDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Nama_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getRerataIPKLulusan() {
        List<Map> results = new ArrayList<Map>();
        for (Map prodi : getProdi()) {
            String kodeProdi = prodi.get("Kd_prg").toString();
            if (isTabelLLExist(kodeProdi)) {
                for (int i = new DateTime().getYear()-5 ; i <= new DateTime().getYear(); i++) {

                    if (isTabelTrExist(kodeProdi, String.valueOf(i))) {
                        String sql = "SELECT  IF(LEFT(ll.nomor_mhs,1)='9', CONCAT('19', LEFT(ll.nomor_mhs,2))," +
                                " IF(LEFT(ll.nomor_mhs,1)='8',CONCAT('19',LEFT(ll.nomor_mhs,2)),CONCAT('20',LEFT(ll.nomor_mhs,2)))) AS angkatan," +
                                " ll.nomor_mhs,ll.nama_mhs, SUM(sks * angka)/SUM(sks) as ipk, prg.Nama_prg,prg.Kd_prg " +
                                " FROM db_" + kodeProdi + ".ll" + kodeProdi + " as ll " +
                                " INNER JOIN db_" + kodeProdi + ".tr" + kodeProdi + i + " tr ON ll.nomor_mhs = tr.nomor_mhs " +
                                " INNER JOIN kamus.nilai nilai ON nilai.huruf = tr.Nilai " +
                                " INNER JOIN kamus.prg_std prg ON prg.Kd_prg = '" + kodeProdi + "'" +
                                " GROUP BY ll.nomor_mhs ORDER by angkatan";
                        results.addAll(ClassConnection.getJdbc().queryForList(sql));
                    }
                }
            }
        }
        return results;
    }

    public boolean isTabelLLExist(String kodeProdi) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'll" + kodeProdi + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }

    public boolean isTabelTrExist(String kodeProdi, String tahun) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'tr" + kodeProdi + tahun + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }
  
}
