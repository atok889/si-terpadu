/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.assetbaranginvestasi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class AssetBarangInvestasiDAOImpl implements AssetBarangInvestasiDAO {

    public AssetBarangInvestasiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<AssetBarangInvestasi> gets() throws Exception {
        List<AssetBarangInvestasi> listAsset = new ArrayList<AssetBarangInvestasi>();
        String sql = "SELECT  baranginvestasi.kodeBarangInvestasi as kode "
                + "  , baranginvestasi.nomorInventaris as nomor "
                + "  , baranginvestasi.kodeSubKelompokBarangInvestasi as kodekelompok "
                + "  , subkelompokbaranginvestasi.subKelompokBarangInvestasi as nama,"
                + " COUNT(baranginvestasi.`kodeSubKelompokBarangInvestasi`)as jumlah "
                + " FROM "
                + "  asset.barangInvestasi baranginvestasi "
                + "  INNER JOIN "
                + "  kamus.subKelompokBarangInvestasi subkelompokbaranginvestasi "
                + "  ON (subkelompokbaranginvestasi.kodeSubKelompokBarangInvestasi = baranginvestasi.kodeSubKelompokBarangInvestasi) "
                + " GROUP BY baranginvestasi.`kodeSubKelompokBarangInvestasi` "
                + " HAVING COUNT(baranginvestasi.`kodeSubKelompokBarangInvestasi`) >= 1 ORDER BY subkelompokbaranginvestasi.subKelompokBarangInvestasi ASC";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        int i = 1;
        for (Map m : rows) {
            AssetBarangInvestasi abi = new AssetBarangInvestasi();
            abi.setIndex((i++));
            abi.setKode(ClassAntiNull.AntiNullString(m.get("kode")));
            abi.setNama(ClassAntiNull.AntiNullString(m.get("nama")));
            abi.setNomor(ClassAntiNull.AntiNullString(m.get("nomor")));
            abi.setKodeKelompok(ClassAntiNull.AntiNullString(m.get("kodekelompok")));
            abi.setJumlah(ClassAntiNull.AntiNullInt(m.get("jumlah")));

            listAsset.add(abi);
        }
        return listAsset;
    }
}
