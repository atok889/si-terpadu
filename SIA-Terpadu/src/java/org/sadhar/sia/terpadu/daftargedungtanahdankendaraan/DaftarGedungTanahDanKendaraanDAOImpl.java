/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftargedungtanahdankendaraan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class DaftarGedungTanahDanKendaraanDAOImpl implements DaftarGedungTanahDanKendaraanDAO {

    public DaftarGedungTanahDanKendaraanDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getDaftarGedungTanahDanKendaraan() {
        List<Map> results = new ArrayList<Map>();
        List<Map> datas = new ArrayList<Map>();
        String sql = "SELECT CONCAT(kendaraan.type ,' ',merk.`namaMerkBarangInvestasi`) AS nama,CONCAT_WS(' ','Tahun',kendaraan.tahunPembuatan) AS keterangan,COUNT(kendaraan.type) AS jumlah" +
                " FROM asset.kendaraan kendaraan " +
                " INNER JOIN asset.kepemilikan kepemilikan ON kepemilikan.`idKepemilikan` = '2' " +
                " INNER JOIN kamus.merkbaranginvestasi merk  ON kendaraan.`kodeMerkBarangInvestasi`= merk.`kodeMerkBarangInvestasi` " +
                " GROUP BY merk.namaMerkBarangInvestasi,kendaraan.type,kendaraan.`tahunPembuatan` " +
                " UNION " +
                " SELECT lokasi.namaLokasi AS nama,CONCAT_WS(' ','Luas',lokasi.luasLokasi,' m2') AS keterangan,COUNT(lokasi.namaLokasi) AS jumlah" +
                " FROM asset.lokasi lokasi " +
                " INNER JOIN asset.kepemilikan kepemilikan ON kepemilikan.`idKepemilikan` = '2' " +
                " GROUP BY lokasi.namaLokasi " +
                " UNION " +
                " SELECT tanah.namaTanah AS nama, CONCAT_WS(' ','Luas',CAST(tanah.luasTanah AS CHAR),'m2'), COUNT(tanah.namaTanah) AS jumlah " +
                " FROM asset.tanah tanah " +
                " INNER JOIN asset.kepemilikan kepemilikan ON kepemilikan.idKepemilikan = '2' " +
                " GROUP BY tanah.namaTanah";

        results = ClassConnection.getJdbc().queryForList(sql);
        for (Map result : results) {
            Map map = new HashMap();
            String keterangan = null;
            if (result.get("keterangan").toString().equals("Tahun 0")) {
                keterangan = "Tidak ada keterangan";
            } else {
                keterangan = result.get("keterangan").toString();
            }
            map.put("keterangan", keterangan);
            map.put("nama", result.get("nama"));
            map.put("jumlah", result.get("jumlah"));
            datas.add(map);
        }
        return datas;
    }
}
