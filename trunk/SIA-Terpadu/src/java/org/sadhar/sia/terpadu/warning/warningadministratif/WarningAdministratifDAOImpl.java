/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningadministratif;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class WarningAdministratifDAOImpl implements WarningAdministratifDAO {

    public WarningAdministratifDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getSemesterMahasiswa(String kodeProdi, String akademik, String semester) {
        String sql = "SELECT IF(LEFT(rg.nomor_mhs,1)='9', CONCAT('19', LEFT(rg.nomor_mhs,2)), " +
                " IF(LEFT(rg.nomor_mhs,1)='8',CONCAT('19',LEFT(rg.nomor_mhs,2)),CONCAT('20',LEFT(rg.nomor_mhs,2)))) AS angkatan,  " +
                " ((year(now())- 10) - (IF(LEFT(rg.nomor_mhs,1)='9', CONCAT('19', LEFT(rg.nomor_mhs,2)),  " +
                " IF(LEFT(rg.nomor_mhs,1)='8',CONCAT('19',LEFT(rg.nomor_mhs,2)),CONCAT('20',LEFT(rg.nomor_mhs,2))))))*2 +1   AS semester,  " +
                " masalah_keuangan.* FROM db_" + kodeProdi + ".rg" + kodeProdi + akademik + semester + " rg" + " INNER JOIN db_" + kodeProdi +
                ".masalahkeuangan" + kodeProdi + " masalah_keuangan ON rg.nomor_mhs = masalah_keuangan.nomor_mhs";

        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getWarningAdministratif(String kodeProdi) {
        List<Map> datas = new ArrayList<Map>();
        if (kodeProdi == null) {
            for (Map prodi : getProdi()) {
                kodeProdi = prodi.get("Kd_prg").toString();
                for (int i = new DateTime().getYear() - 15; i <= new DateTime().getYear(); i++) {
                    int x = 1;
                    int y = 2;
                    if (isTabelRgExist(kodeProdi, String.valueOf(i), String.valueOf(x)) && isTabelRgExist(kodeProdi, String.valueOf(i), String.valueOf(y))) {
                        String sql = "SELECT IF(LEFT(rg1.nomor_mhs, 1)='9', CONCAT('19', LEFT(rg1.nomor_mhs, 2)), " +
                                " IF(LEFT(rg1.nomor_mhs, 1)='8', CONCAT('19', LEFT(rg1.nomor_mhs,2)), CONCAT('20', LEFT(rg1.nomor_mhs, 2)))) AS angkatan, " +
                                " mhs.nama_mhs,prg.Nama_prg,fak.Nama_fak " +
                                " FROM db_" + kodeProdi + ".rg" + kodeProdi + i + x + " rg1 " +
                                " INNER JOIN db_" + kodeProdi + ".rg" + kodeProdi + i + y + " rg2 ON rg1.nomor_mhs=rg2.nomor_mhs " +
                                " INNER JOIN db_" + kodeProdi + ".mhs" + kodeProdi + " mhs ON rg1.nomor_mhs = mhs.nomor_mhs " +
                                " INNER JOIN kamus.prg_std prg ON prg.Kd_prg = '" + kodeProdi + "' " +
                                " INNER JOIN kamus.fakultas fak ON fak.Kd_fakultas = prg.Kd_fak " +
                                " WHERE rg1.st_mhs = '2'";                      
                        datas.addAll(ClassConnection.getJdbc().queryForList(sql));
                    }
                }
            }
        } else {
            for (int i = new DateTime().getYear() - 15; i <= new DateTime().getYear(); i++) {
                int x = 1;
                int y = 2;
                if (isTabelRgExist(kodeProdi, String.valueOf(i), String.valueOf(x)) && isTabelRgExist(kodeProdi, String.valueOf(i), String.valueOf(y))) {
                    String sql = "SELECT IF(LEFT(rg1.nomor_mhs, 1)='9', CONCAT('19', LEFT(rg1.nomor_mhs, 2)), " +
                            " IF(LEFT(rg1.nomor_mhs, 1)='8', CONCAT('19', LEFT(rg1.nomor_mhs,2)), CONCAT('20', LEFT(rg1.nomor_mhs, 2)))) AS angkatan, " +
                            " mhs.nama_mhs,prg.Nama_prg,fak.Nama_fak " +
                            " FROM db_" + kodeProdi + ".rg" + kodeProdi + i + x + " rg1 " +
                            " INNER JOIN db_" + kodeProdi + ".rg" + kodeProdi + i + y + " rg2 ON rg1.nomor_mhs=rg2.nomor_mhs " +
                            " INNER JOIN db_" + kodeProdi + ".mhs" + kodeProdi + " mhs ON rg1.nomor_mhs = mhs.nomor_mhs " +
                            " INNER JOIN kamus.prg_std prg ON prg.Kd_prg = '" + kodeProdi + "' " +
                            " INNER JOIN kamus.fakultas fak ON fak.Kd_fakultas = prg.Kd_fak " +
                            " WHERE rg1.st_mhs = '2'";                   
                    datas.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        }
        return datas;
    }

    public boolean isTabelRgExist(String kodeProdi, String tahun, String semester) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'rg" + kodeProdi + tahun + semester + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }
}
