/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pendidikankaryawan;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class PendidikanKaryawanDAOImpl implements PendidikanKaryawanDAO {

    public PendidikanKaryawanDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getUnitKerja() {
        String sql = "SELECT ku.Kd_unit_kerja,ku.Nama_unit_kerja FROM kamus.unkerja ku";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getPendidikanKaryawan(String unitKerja) {
        String sql = "SELECT pg.Nama_peg,  jenjang.Nm_jenjang, ku.Nama_unit_kerja FROM personalia.pegawai pg  " +
                " INNER JOIN personalia.pendidikan ps ON pg.NPP = ps.NPP  " +
                " INNER JOIN kamus.jenjang jenjang ON jenjang.Kd_jenjang = ps.Jenjang  " +
                " INNER JOIN personalia.unit_peg pu ON pu.npp = pg.NPP " +
                " INNER JOIN kamus.unkerja ku ON ku.Kd_unit_kerja = pu.kd_unit " +
                " WHERE pg.stat_peg = '1' OR pg.stat_peg = '2' AND ku.Kd_unit_kerja = " + unitKerja +
                " ORDER BY ps.Jenjang";
        return ClassConnection.getJdbc().queryForList(sql);

    }
}
