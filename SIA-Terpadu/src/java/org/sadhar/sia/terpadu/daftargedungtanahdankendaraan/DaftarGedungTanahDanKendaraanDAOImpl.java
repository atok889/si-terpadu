/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftargedungtanahdankendaraan;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sql = " SELECT 'Gedung',al.namaLokasi as nama,al.luasLokasi AS keterangan " +
                " FROM asset.lokasi al " +
                " UNION" +
                " SELECT 'Kendaraan', ak.type as nama, CAST(COUNT(ak.type) AS SIGNED) AS keterangan FROM asset.kendaraan ak " +
                " GROUP BY ak.type " +
                " UNION " +
                " SELECT 'Tanah',ast.namaTanah as nama, ast.luasTanah as keterangan FROM asset.tanah ast";

        int count = 0;
        try {
            PreparedStatement ps = ClassConnection.getConnection().prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {

                String jenis = result.getString("gedung");
                String ukuran = null;
                if (jenis.equalsIgnoreCase("kendaraan")) {
                    ukuran = "unit";
                } else if (jenis.equalsIgnoreCase("gedung")) {
                    ukuran = "m2";
                } else if (jenis.equalsIgnoreCase("tanah")) {
                    ukuran = "m2";
                }

                if (result.getString("nama").toString().startsWith("K.")) {
                    Map map = new HashMap();
                    map.put("jenis", result.getString("gedung"));
                    map.put("nama", result.getString("nama"));
                    count += result.getInt("keterangan");
                    map.put("keterangan", count + " " + ukuran);
                    datas.add(map);
                } else {
                    Map map = new HashMap();
                    map.put("jenis", result.getString("gedung"));
                    map.put("nama", result.getString("nama"));
                    map.put("keterangan", result.getString("keterangan") + " " + ukuran);
                    results.add(map);
                }
            }
        } catch (SQLException ex) {
        }

        //Special case for K.**
        Map result = new HashMap();
        for (Map data : datas) {            
            result.put("jenis", "Gedung");
            result.put("nama", "Gedung K");
            result.put("keterangan", data.get("keterangan"));
        }
        datas.clear();
        datas.add(result);
        datas.addAll(results);

        return datas;
    }
}
