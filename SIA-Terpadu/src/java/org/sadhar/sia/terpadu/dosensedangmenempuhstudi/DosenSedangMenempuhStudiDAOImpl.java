/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.dosensedangmenempuhstudi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.jenjangstudi.JenjangStudi;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudi;

/**
 *
 * @author jasoet
 */
public class DosenSedangMenempuhStudiDAOImpl implements DosenSedangMenempuhStudiDAO {

    public List<DosenSedangMenempuhStudi> getData(UKProgramStudi progdi, JenjangStudi jenjangStudi) throws Exception {
        List<DosenSedangMenempuhStudi> data = new ArrayList<DosenSedangMenempuhStudi>();
//        String sql = "select distinct(pp.`Nama_peg`),ku.`Kd_unit_kerja`,kj.Kd_jenjang, ku.`Nama_unit_kerja`,kj.`Nm_jenjang`,ps.Univ FROM "
//                + " personalia.studi ps "
//                + " INNER JOIN personalia.unit_peg pu ON ps.`NPP` = pu.npp "
//                + " INNER JOIN kamus.unkerja ku ON ku.`Kd_unit_kerja` = pu.kd_unit "
//                + " INNER JOIN personalia.pegawai pp ON pp.`NPP` = ps.`NPP` "
//                + " INNER JOIN kamus.jenjang kj ON kj.`Kd_jenjang` = ps.`Jenjang` "
//                + " WHERE ps.`statusLulus` like '%N%'  AND (ps.Tgl_selesai_studi is not null or ps.Tgl_selesai_studi like '%00-00-000%')  AND (pp.Status_keluar like '1' or pp.Status_keluar like '6' or pp.Status_keluar like '7')";
//
        String sqlCreateView = " CREATE OR REPLACE VIEW "
                + " tempo.dosenStudi(kdPegawai,Nama_peg,Kd_unit_kerja,Kd_jenjang,Nama_unit_kerja,Univ)AS"
                + " SELECT ps.kdPegawai AS kdPegawai," 
                + " pp.Nama_peg AS Nama_peg,"
         + " pu.kd_unit AS Kd_unit_kerja,"
         + " MAX(ps.Jenjang) AS Kd_jenjang,"
         + " ku.Nama_unit_kerja AS Nama_unit_kerja,"
         + " IF(ps.Univ<=>NULL,'DATA BELUM DI ISI ',ps.Univ) AS Univ"
            + " FROM (((personalia.unit_peg pu "
                + " INNER JOIN kamus.unkerja ku ON (pu.kd_unit = ku.Kd_unit_kerja))"
                + " INNER JOIN personalia.pegawai pp ON (pp.kdPegawai = pu.kdPegawai))"
                + " INNER JOIN personalia.studi ps ON (ps.kdPegawai = pp.kdPegawai))"
                + " WHERE ps.`statusLulus`='N' "
                + " AND (pp.Status_keluar like '1' or pp.Status_keluar like '6' or pp.Status_keluar like '7')"
                + " GROUP BY ps.kdPegawai";
        ClassConnection.getJdbc().execute(sqlCreateView);

        String sql ="SELECT ds.kdPegawai,ds.Nama_peg,ds.Kd_unit_kerja,MAX(ds.Kd_jenjang),"
                +" ds.Nama_unit_kerja, kj.Nm_jenjang,ds.Univ"
                +" FROM tempo.dosenStudi ds"
                +" INNER JOIN kamus.jenjang kj ON ( kj.Kd_jenjang = ds.Kd_Jenjang)";

        if (progdi != null && jenjangStudi != null) {
        sql +=  " WHERE "+" ds.`Kd_unit_kerja` = '" + progdi.getKodeUnitKerja() + "' AND ds.Kd_jenjang = " + jenjangStudi.getKode() + " GROUP BY ds.kdPegawai " +"";
        } else if (progdi != null && jenjangStudi == null) {
            sql += " WHERE "+" ds.`Kd_unit_kerja` = '" + progdi.getKodeUnitKerja() + "'"+" GROUP BY ds.kdPegawai "+"";
        } else if (progdi == null && jenjangStudi != null) {
            sql += " WHERE "+" ds.Kd_jenjang = " + jenjangStudi.getKode() +" GROUP BY ds.kdPegawai "+ "";
        } else {
            sql += " GROUP BY ds.kdPegawai "+"";
        }

        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            DosenSedangMenempuhStudi o = new DosenSedangMenempuhStudi();
            o.setNama(ClassAntiNull.AntiNullString(m.get("Nama_peg")));
            o.setJenjangStudi(ClassAntiNull.AntiNullString(m.get("Nm_jenjang")));
            o.setProdi(ClassAntiNull.AntiNullString(m.get("Nama_unit_kerja")));
            o.setTempat(ClassAntiNull.AntiNullString(m.get("Univ")));
            data.add(o);

        }
        return data;
    }

    public CategoryDataset getDataset(UKProgramStudi progdi, JenjangStudi jenjangStudi) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
