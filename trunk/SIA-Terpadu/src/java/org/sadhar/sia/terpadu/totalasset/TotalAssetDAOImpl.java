/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.totalasset;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class TotalAssetDAOImpl implements TotalAssetDAO {

    public TotalAssetDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<TotalAsset> gets() throws Exception {
        String sql = "SELECT  'Barang Investasi' AS jenis, subkelompokbaranginvestasi.subKelompokBarangInvestasi as nama,CONCAT(COUNT(baranginvestasi.`kodeSubKelompokBarangInvestasi`),' Unit') as keterangan "
                + " FROM "
                + " asset.baranginvestasi baranginvestasi "
                + " INNER JOIN "
                + " kamus.subkelompokbaranginvestasi subkelompokbaranginvestasi "
                + " ON (subkelompokbaranginvestasi.kodeSubKelompokBarangInvestasi = baranginvestasi.kodeSubKelompokBarangInvestasi) "
                + " GROUP BY baranginvestasi.`kodeSubKelompokBarangInvestasi` "
                + " HAVING COUNT(baranginvestasi.`kodeSubKelompokBarangInvestasi`) >= 1 "
                + " UNION "
                + " SELECT 'Gedung' AS jenis,al.namaLokasi as nama,CONCAT(al.luasLokasi,' m2')  AS keterangan "
                + "                 FROM asset.lokasi al "
                + " UNION "
                + " SELECT 'Kendaraan' AS jenis, ak.type as nama, CONCAT(CAST(COUNT(ak.type) AS SIGNED),' Unit') AS keterangan "
                + " FROM asset.kendaraan ak "
                + " GROUP BY ak.type "
                + " UNION "
                + " SELECT 'Tanah' AS jenis,ast.namaTanah as nama, CONCAT(ast.luasTanah, ' m2') as keterangan FROM asset.tanah ast ";

        List<TotalAsset> data = new ArrayList<TotalAsset>();
        PreparedStatement ps = ClassConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            TotalAsset ta = new TotalAsset();
            ta.setJenis(ClassAntiNull.AntiNullString(rs.getString("jenis")));
            ta.setNama(ClassAntiNull.AntiNullString(rs.getString("nama")));
            ta.setKeterangan(ClassAntiNull.AntiNullString(rs.getString("keterangan")));
            data.add(ta);
        }


        return data;
    }
}
