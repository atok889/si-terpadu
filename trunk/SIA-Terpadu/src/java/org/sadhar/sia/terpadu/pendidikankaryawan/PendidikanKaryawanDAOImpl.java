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
        String sql = "SELECT ku.Kd_unit_kerja,ku.Nama_unit_kerja FROM kamus.unkerja ku ORDER BY  ku.Kd_unit_kerja";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getPendidikanKaryawan(String unitKerja) {
//        String sql = "SELECT concat_ws(' ',Gelar_depan,`Nama_peg`,`Gelar_blk`) as Nama_peg,  jenjang.Nm_jenjang, ku.Nama_unit_kerja, MAX(pu.tgl_sk_unit) FROM personalia.pegawai pg  " +
//                " INNER JOIN personalia.pendidikan ps ON pg.NPP = ps.NPP  " +
//                " INNER JOIN kamus.jenjang jenjang ON jenjang.Kd_jenjang = ps.Jenjang  " +
//                " INNER JOIN personalia.unit_peg pu ON pu.npp = pg.NPP " +
//                " INNER JOIN kamus.unkerja ku ON ku.Kd_unit_kerja = pu.kd_unit " +
//                " WHERE pg.stat_peg BETWEEN '1' AND '2' AND ku.Kd_unit_kerja = " + unitKerja + " AND pg.AdmEdu = '1'" +
//                "  AND ps.Jenjang = (SELECT MAX(ps.Jenjang) FROM personalia.pegawai pg  " +
//                " INNER JOIN personalia.pendidikan ps ON pg.NPP = ps.NPP" +
//                " INNER JOIN kamus.jenjang jenjang ON jenjang.Kd_jenjang = ps.Jenjang " +
//                " INNER JOIN personalia.unit_peg pu ON pu.npp = pg.NPP " +
//                " INNER JOIN kamus.unkerja ku ON ku.Kd_unit_kerja = pu.kd_unit " +
//                " WHERE pg.stat_peg BETWEEN '1' AND '2' AND ku.Kd_unit_kerja = " + unitKerja + " AND pg.AdmEdu = '1' " +
//                " ORDER BY ps.Jenjang DESC)";
        String createView = "create or replace view tempo.pendidikan_karyawan_vi as (" +
                " SELECT concat_ws(' ',Gelar_depan,`Nama_peg`,`Gelar_blk`) as Nama_peg, max(jenjang.Kd_jenjang) as Kd_jenjang , jenjang.Nm_jenjang, ku.Nama_unit_kerja " +
                " FROM  personalia.pegawai pg " +
                " INNER JOIN personalia.pendidikan ps ON pg.NPP = ps.NPP " +
                " INNER JOIN kamus.jenjang jenjang ON jenjang.Kd_jenjang = ps.Jenjang " +
                " INNER JOIN personalia.unit_peg pu ON pu.npp = pg.NPP " +
                " INNER JOIN kamus.unkerja ku ON ku.Kd_unit_kerja = pu.kd_unit " +
                " WHERE pg.stat_peg BETWEEN '1' AND '2' AND ku.Kd_unit_kerja = " + unitKerja + " AND pg.AdmEdu = '1' group by Nama_peg)";
        ClassConnection.getJdbc().execute(createView);

        String sql = "select kj.Kd_jenjang, Nama_peg, kj.Nm_jenjang, Nama_unit_kerja" +
                " from tempo.pendidikan_karyawan_vi tv , kamus.jenjang kj where kj.Kd_jenjang = tv.Kd_jenjang ORDER BY Nama_peg";

        return ClassConnection.getJdbc().queryForList(sql);

    }
}
