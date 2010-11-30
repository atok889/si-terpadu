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
//                String sql = "SELECT dosen.npp,dosen.nama_peg, MAX(jenjang.Nm_jenjang) as Nm_jenjang ,prg.Nama_prg FROM db_" + kodeProdi + ".dosen" + kodeProdi + " dosen " +
//                        " INNER JOIN personalia.pendidikan ps ON dosen.npp = ps.kdPegawai " +
//                        " INNER JOIN kamus.jenjang jenjang ON jenjang.Kd_jenjang = ps.Jenjang" +
//                        " INNER JOIN kamus.prg_std prg ON prg.Kd_prg = '" + kodeProdi + "' " +
//                        " GROUP BY dosen.nama_peg";

                String sql = "SELECT pegawai.npp,pegawai.nama_peg,MAX(jenjang.Nm_jenjang) as Nm_jenjang , unkerja.`Nama_unit_kerja` as Nama_prg,unkerja.`Kd_unit_kerja` as kodeUnit " +
                        " FROM (kamus.unkerja unkerja " +
                        " LEFT JOIN personalia.unit_peg unit_peg ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit)) " +
                        " LEFT JOIN personalia.pegawai pegawai ON (pegawai.kdPegawai = unit_peg.kdPegawai) " +
                        " INNER JOIN personalia.pendidikan ps ON pegawai.`kdPegawai` = ps.kdPegawai " +
                        " INNER JOIN kamus.jenjang jenjang ON jenjang.Kd_jenjang = ps.Jenjang " +
                        " WHERE (pegawai.AdmEdu = '2') AND unkerja.Kd_unit_kerja='160" + kodeProdi + "0' " +
                        " GROUP BY pegawai.nama_peg ORDER BY unkerja.Kd_unit_kerja ";

                results.addAll(ClassConnection.getJdbc().queryForList(sql));
                System.out.println(sql);
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
