/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataipstotal;

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
public class RerataIpsTotalDAOImpl implements RerataIpsTotalDAO {

    public RerataIpsTotalDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getRerataIpsTotal(String kodeProdi) {
        List<Map> results = new ArrayList<Map>();

        for (int i = 1; i <= 2; i++) {
            for (int j = 2008; j <= new DateTime().getYear(); j++) {
                if (isTabelIqExist(kodeProdi, String.valueOf(j), String.valueOf(i))) {
                    String sql = null;
                    if (i == 1) {
                        sql = "SELECT iq.nomor_mhs, IF(LEFT(nomor_mhs, 1)='9', CONCAT('19', LEFT(nomor_mhs, 2)), IF(LEFT(nomor_mhs, 1)='8'," +
                                " CONCAT('19', LEFT(nomor_mhs,2)), CONCAT('20', LEFT(nomor_mhs, 2)))) AS angkatan, " +
                                " CAST(((" + j + " - IF(LEFT(nomor_mhs, 1)='9', CONCAT('19', LEFT(nomor_mhs, 2)), IF(LEFT(nomor_mhs, 1)='8', " +
                                " CONCAT('19', LEFT(nomor_mhs,2)), CONCAT('20', LEFT(nomor_mhs, 2))))) *2) AS UNSIGNED) AS semester,iq.ips, iq.ipk " +
                                " FROM db_" + kodeProdi + ".iq" + kodeProdi + j + i + " iq WHERE ips > 0";

                    } else {
                        sql = "SELECT iq.nomor_mhs, IF(LEFT(nomor_mhs, 1)='9', CONCAT('19', LEFT(nomor_mhs, 2)), IF(LEFT(nomor_mhs, 1)='8'," +
                                " CONCAT('19', LEFT(nomor_mhs,2)), CONCAT('20', LEFT(nomor_mhs, 2)))) AS angkatan, " +
                                " CAST(((" + j + "  - IF(LEFT(nomor_mhs, 1)='9', CONCAT('19', LEFT(nomor_mhs, 2)), IF(LEFT(nomor_mhs, 1)='8', " +
                                " CONCAT('19', LEFT(nomor_mhs,2)), CONCAT('20', LEFT(nomor_mhs, 2))))) *2+1) AS UNSIGNED)AS semester,iq.ips, iq.ipk " +
                                " FROM db_" + kodeProdi + ".iq" + kodeProdi + j + i + " iq WHERE ips > 0";

                    }
                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        }
        return results;
    }

    public boolean isTabelIqExist(String kodeProdi, String tahun, String semester) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'iq" + kodeProdi + tahun + semester + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }
}
