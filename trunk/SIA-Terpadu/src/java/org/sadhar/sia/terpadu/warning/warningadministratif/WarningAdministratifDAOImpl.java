/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningadministratif;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class WarningAdministratifDAOImpl implements WarningAdministratifDAO {

    public WarningAdministratifDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std ORDER BY Nama_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getSemesterMahasiswa(String kodeProdi, String tahun, String semester) {
        String sql = "SELECT IF(LEFT(rg.nomor_mhs,1)='9', CONCAT('19', LEFT(rg.nomor_mhs,2)), "
                + " IF(LEFT(rg.nomor_mhs,1)='8',CONCAT('19',LEFT(rg.nomor_mhs,2)),CONCAT('20',LEFT(rg.nomor_mhs,2)))) AS angkatan,  "
                + " ((year(now())- 10) - (IF(LEFT(rg.nomor_mhs,1)='9', CONCAT('19', LEFT(rg.nomor_mhs,2)),  "
                + " IF(LEFT(rg.nomor_mhs,1)='8',CONCAT('19',LEFT(rg.nomor_mhs,2)),CONCAT('20',LEFT(rg.nomor_mhs,2))))))*2 +1   AS semester,  "
                + " masalah_keuangan.* FROM db_" + kodeProdi + ".rg" + kodeProdi + tahun + semester + " rg"
                + " INNER JOIN db_" + kodeProdi + ".masalahkeuangan" + kodeProdi + " masalah_keuangan ON rg.nomor_mhs = masalah_keuangan.nomor_mhs";
        return ClassConnection.getJdbc().queryForList(sql);

       
    }
}
