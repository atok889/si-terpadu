/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistiklamastudi;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class StatistikLamaStudiDAOImpl implements StatistikLamaStudiDAO {

    public StatistikLamaStudiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std ORDER BY Nama_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getStatistikStudi(String kodeProdi, String semester) {
        //Data mahasiswa pada db_mhs114 belum beres sama sekali
        String sql = "select yud.nomor_mhs, mhs.nama_mhs, kamus.Nama_prg, yud.tgl_yudisium, "
                + " SUBSTRING(yud.tgl_yudisium,6,2) AS bulan,SUBSTRING(yud.tgl_yudisium,1,4) AS tahun, "
                + " SUBSTRING(tgl_yudisium,1,4) - IF(left(yud.nomor_mhs,1)='9',concat('19',left(yud.nomor_mhs,2)), "
                + " IF(LEFT(yud.nomor_mhs,1)='8',CONCAT('19',LEFT(yud.nomor_mhs,2)),CONCAT('20',LEFT(yud.nomor_mhs,2)))) as lama "
                + " FROM db_1114.yud1114 as yud "
                + " LEFT JOIN db_1114.mhs_1114a mhs ON mhs.nomor_mhs = yud.nomor_mhs "
                + " INNER JOIN kamus.prg_std kamus ON kamus.Kd_prg = '1114'";

        return ClassConnection.getJdbc().queryForList(sql);
    }
}
