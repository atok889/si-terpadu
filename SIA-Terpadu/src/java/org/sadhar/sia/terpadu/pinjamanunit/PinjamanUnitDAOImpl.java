/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pinjamanunit;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class PinjamanUnitDAOImpl implements PinjamanUnitDAO {

    public PinjamanUnitDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getPinjamanUnit() {
        String sql = "SELECT ku.Nama_unit_kerja AS nama,SUM(pd.kuantitasDisetujui * pd.harga) AS jumlah " +
                " FROM ppmk.pengajuanPengambilan pp " +
                " INNER JOIN ppmk.detailPengajuanPengambilan pd ON pp.idPengajuan = pd.idPengajuan " +
                " INNER JOIN kamus.unkerja ku ON pp.kodeUnitPengaju = ku.Kd_unit_kerja " +
                " GROUP BY ku.Kd_unit_kerja";

        return ClassConnection.getJdbc().queryForList(sql);
    }
}
