/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistikpendidikandosen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class StatistikPendidikanDAOImpl implements StatistikPendidikanDosenDAO {

    public StatistikPendidikanDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getStatistikPendidikan() {
        List<Map> results = new ArrayList<Map>();
        for (Map prodi : this.getProdi()) {
            String kodeProdi = prodi.get("Kd_prg").toString();
            if (isTabelDosendExist(kodeProdi)) {
                String sql = "SELECT dosen.npp,dosen.nama_peg, jenjang.Nm_jenjang, prg.Nama_prg FROM db_" + kodeProdi + ".dosen" + kodeProdi + " dosen " +
                        " INNER JOIN personalia.pendidikan ps ON dosen.npp = ps.kdPegawai " +
                        " INNER JOIN kamus.jenjang jenjang ON jenjang.Kd_jenjang = ps.Jenjang" +
                        " INNER JOIN kamus.prg_std prg ON prg.Kd_prg = '" + kodeProdi + "'";

                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public boolean isTabelDosendExist(String kodeProdi) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'dosen" + kodeProdi + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }
}
