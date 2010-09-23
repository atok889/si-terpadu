/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningsemester;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class WarningSemesterDAOImpl implements WarningSemesterDAO {

    public WarningSemesterDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std ORDER BY Nama_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getSemesterMahasiswa(String kodeProdi, String akademik, String semester) {
        String sql = "SELECT IF(LEFT(rg.nomor_mhs,1)='9', CONCAT('19', LEFT(rg.nomor_mhs,2)), "
                + " IF(LEFT(rg.nomor_mhs,1)='8',CONCAT('19',LEFT(rg.nomor_mhs,2)),CONCAT('20',LEFT(rg.nomor_mhs,2)))) AS angkatan,  "
                + " (year(now()) - (IF(LEFT(rg.nomor_mhs,1)='9', CONCAT('19', LEFT(rg.nomor_mhs,2)),  "
                + " IF(LEFT(rg.nomor_mhs,1)='8',CONCAT('19',LEFT(rg.nomor_mhs,2)),CONCAT('20',LEFT(rg.nomor_mhs,2))))))*2 +1   AS semester,  "
                + " masalah_keuangan.* FROM db_" + kodeProdi + ".rg" + kodeProdi + akademik + semester + " rg"
                + " INNER JOIN db_" + kodeProdi + ".masalahkeuangan" + kodeProdi + " masalah_keuangan ON rg.nomor_mhs = masalah_keuangan.nomor_mhs";
        System.out.println(sql);
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getWarningSemester(String kodeProdi, String akademik, String semester) {
        // Belum bisa dikerjakan, data harus direfrence ke db_mhsXXXX, tpi db_mhs gak jelas....
        List<Map> mahasiswaSemester = new ArrayList<Map>();
        for (Map map : getSemesterMahasiswa(kodeProdi, akademik, semester)) {
            if (Double.valueOf(map.get("semester").toString()) >= 13.0) {
                String sql = "SELECT *FROM ";
                System.out.println("Warning" + map.get("semester"));
            }
        }
        return mahasiswaSemester;
    }
}
