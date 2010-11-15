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
        String sql = "select distinct(pp.`Nama_peg`),ku.`Kd_unit_kerja`,kj.Kd_jenjang, ku.`Nama_unit_kerja`,kj.`Nm_jenjang`,ps.Univ FROM "
                + " personalia.studi ps "
                + " INNER JOIN personalia.unit_peg pu ON ps.`NPP` = pu.npp "
                + " INNER JOIN kamus.unkerja ku ON ku.`Kd_unit_kerja` = pu.kd_unit "
                + " INNER JOIN personalia.pegawai pp ON pp.`NPP` = ps.`NPP` "
                + " INNER JOIN kamus.jenjang kj ON kj.`Kd_jenjang` = ps.`Jenjang` "
                + " WHERE ps.`statusLulus` like '%N%'  AND (ps.Tgl_selesai_studi is not null or ps.Tgl_selesai_studi like '%00-00-000%')  AND (pp.Status_keluar like '1' or pp.Status_keluar like '6' or pp.Status_keluar like '7')";
        if (progdi != null && jenjangStudi != null) {
            sql += " AND ku.`Kd_unit_kerja` = '" + progdi.getKodeUnitKerja() + "' and kj.Kd_jenjang = " + jenjangStudi.getKode() + "";
        } else if (progdi != null && jenjangStudi == null) {
            sql += " AND ku.`Kd_unit_kerja` = '" + progdi.getKodeUnitKerja() + "'";
        } else if (progdi == null && jenjangStudi != null) {
            sql += " AND  kj.Kd_jenjang = " + jenjangStudi.getKode() + "";
        } else {
            sql += "";
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
