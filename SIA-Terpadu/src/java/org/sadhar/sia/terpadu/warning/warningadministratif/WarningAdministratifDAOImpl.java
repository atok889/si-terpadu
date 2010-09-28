/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningadministratif;

import java.util.ArrayList;
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
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getSemesterMahasiswa(String kodeProdi, String akademik, String semester) {
        String sql = "SELECT IF(LEFT(rg.nomor_mhs,1)='9', CONCAT('19', LEFT(rg.nomor_mhs,2)), "
                + " IF(LEFT(rg.nomor_mhs,1)='8',CONCAT('19',LEFT(rg.nomor_mhs,2)),CONCAT('20',LEFT(rg.nomor_mhs,2)))) AS angkatan,  "
                + " ((year(now())- 10) - (IF(LEFT(rg.nomor_mhs,1)='9', CONCAT('19', LEFT(rg.nomor_mhs,2)),  "
                + " IF(LEFT(rg.nomor_mhs,1)='8',CONCAT('19',LEFT(rg.nomor_mhs,2)),CONCAT('20',LEFT(rg.nomor_mhs,2))))))*2 +1   AS semester,  "
                + " masalah_keuangan.* FROM db_" + kodeProdi + ".rg" + kodeProdi + akademik + semester + " rg"
                + " INNER JOIN db_" + kodeProdi + ".masalahkeuangan" + kodeProdi + " masalah_keuangan ON rg.nomor_mhs = masalah_keuangan.nomor_mhs";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getWarningAdministratif(String kodeProdi, String akademik, String semester) {

        List<Map> mahasiswaSemester = new ArrayList<Map>();
        int no = 1;
        for (Map map : getSemesterMahasiswa(kodeProdi, akademik, semester)) {
            System.out.print("---" + no);
            System.out.print("---" + map.get("nomor_mhs"));
            System.out.println("--" + map.get("semester"));
            no++;
        }

        no = 1;
        for (Map map : getSemesterMahasiswa(kodeProdi, akademik, semester)) {
            double semesterAkhir = Double.parseDouble(map.get("semester").toString());
            String sqlSemesterAkhir = "SELECT nomor_mhs, sem" + (int) semesterAkhir + " as semester FROM db_" + kodeProdi + ".masalahkeuangan" + kodeProdi + " masalah_keuangan "
                    + " WHERE masalah_keuangan.nomor_mhs = " + map.get("nomor_mhs");
            System.out.println("--" + sqlSemesterAkhir);
            Map mahasiswaSemesterAkhir = ClassConnection.getJdbc().queryForMap(sqlSemesterAkhir);
            System.out.print("---" + no);
            System.out.println("Semester Akhir " + mahasiswaSemesterAkhir.get("nomor_mhs"));
            System.out.println("Semester Akhir " + mahasiswaSemesterAkhir.get("semester"));
            no++;
            if (mahasiswaSemesterAkhir.get("semester").toString().equalsIgnoreCase("T")) {
                double semesterSebelumnya = Double.parseDouble(map.get("semester").toString()) - 1;
                String sqlSemesterSebelumnya = "SELECT nomor_mhs, sem" + (int) semesterSebelumnya + " as semester  FROM db_" + kodeProdi + ".masalahkeuangan" + kodeProdi + " masalah_keuangan "
                        + " WHERE masalah_keuangan.nomor_mhs = " + map.get("nomor_mhs");
                Map mahasiswaSemesterSebelumnya = ClassConnection.getJdbc().queryForMap(sqlSemesterSebelumnya);
                System.out.println("Semester Sebelumnya " + mahasiswaSemesterSebelumnya.get("nomor_mhs"));
                System.out.println("Semester Sebelumnya " + mahasiswaSemesterSebelumnya.get("semester"));
                mahasiswaSemester.add(mahasiswaSemesterSebelumnya);
            }
        }
        return mahasiswaSemester;
    }
}
