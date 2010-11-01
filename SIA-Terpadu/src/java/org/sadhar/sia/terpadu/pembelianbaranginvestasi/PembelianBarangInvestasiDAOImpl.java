/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pembelianbaranginvestasi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author Hendro Steven
 */
public class PembelianBarangInvestasiDAOImpl implements PembelianBarangInvestasiDAO {

    public List<PembelianBarangInvestasi> loadData() throws Exception {
        List<PembelianBarangInvestasi> data = new ArrayList<PembelianBarangInvestasi>();
        String sql = "SELECT  subKelompokBarangInvestasi.subKelompokBarangInvestasi AS nama, "
                + "COUNT(barangInvestasi.kodeBarangInvestasi) AS jumlah, "
                + "pembelianBarangInvestasi.harga AS harga, "
                + "(COUNT(barangInvestasi.kodeBarangInvestasi)* pembelianBarangInvestasi.harga) AS subtotal "
                + "FROM (asset.barangInvestasi barangInvestasi "
                + "INNER JOIN asset.pembelianBarangInvestasi pembelianBarangInvestasi "
                + "ON (barangInvestasi.kodeBarangInvestasi = pembelianBarangInvestasi.kodeBarangInvestasi)) "
                + "INNER JOIN kamus.subKelompokBarangInvestasi subKelompokBarangInvestasi "
                + "ON (subKelompokBarangInvestasi.kodeSubKelompokBarangInvestasi = "
                + "barangInvestasi.kodeSubKelompokBarangInvestasi) "
                + "WHERE (barangInvestasi.tglHapus IS NULL) GROUP BY barangInvestasi.kodeSubKelompokBarangInvestasi";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            PembelianBarangInvestasi pbi = new PembelianBarangInvestasi();
            pbi.setNama(m.get("nama").toString());
            pbi.setNilai(Double.valueOf(m.get("harga").toString()));
            pbi.setJumlah(Integer.valueOf(m.get("jumlah").toString()));
            pbi.setSubtotal(Double.parseDouble(m.get("subtotal").toString()));
            data.add(pbi);
        }
        return data;
    }
}
