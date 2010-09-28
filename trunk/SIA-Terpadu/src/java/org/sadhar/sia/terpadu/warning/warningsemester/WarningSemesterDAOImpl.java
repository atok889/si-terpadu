/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningsemester;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

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

    public List<Map> getSemesterMahasiswa(String kodeProdi, String semester) {
        int angka = 0;
        if (semester.equals("1")) {
            angka = 1;
        } else {
            angka = 0;
        }
        String sql = "SELECT IF(LEFT(mhs.nomor_mhs,1)='9', CONCAT('19', LEFT(mhs.nomor_mhs,2)), " +
                " IF(LEFT(mhs.nomor_mhs,1)='8',CONCAT('19',LEFT(mhs.nomor_mhs,2)),CONCAT('20',LEFT(mhs.nomor_mhs,2)))) AS angkatan, " +
                " (year(now()) - (IF(LEFT(mhs.nomor_mhs,1)='9', CONCAT('19', LEFT(mhs.nomor_mhs,2))," +
                " IF(LEFT(mhs.nomor_mhs,1)='8',CONCAT('19',LEFT(mhs.nomor_mhs,2)),CONCAT('20',LEFT(mhs.nomor_mhs,2))))))*2 +" + angka + " AS semester," +
                " mhs.nama_mhs,prodi.Nama_prg,fak.Nama_fak FROM db_" + kodeProdi + ".mhs" + kodeProdi + " mhs " +
                " INNER JOIN kamus.prg_std prodi ON SUBSTRING(mhs.nomor_mhs,3,4) = prodi.Kd_prg " +
                " INNER JOIN kamus.fakultas fak ON prodi.Kd_fak = fak.Kd_fakultas " +
                " ORDER BY angkatan";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getWarningSemester(String kodeProdi, String semester) {
        List<Map> mahasiswaSemester = new ArrayList<Map>();
        for (Map map : getSemesterMahasiswa(kodeProdi, semester)) {
            if (Double.valueOf(map.get("semester").toString()) >= 13.0) {
                mahasiswaSemester.add(map);
            }
        }
        return mahasiswaSemester;
    }

    public List<Map> getWarningSemester(String semester) {
        List<Map> mahasiswaSemester = new ArrayList<Map>();
        for (Map prodi : this.getProdi()) {
            if (isTableMhsExist(prodi.get("Kd_prg").toString())) {
                for (Map map : getSemesterMahasiswa(prodi.get("Kd_prg").toString(), semester)) {
                    if (Double.valueOf(map.get("semester").toString()) >= 13.0) {
                        mahasiswaSemester.add(map);
                    }
                }
            }
        }
        return mahasiswaSemester;
    }

    public boolean isTableMhsExist(String kodeProdi) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'mhs" + kodeProdi + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }
}
