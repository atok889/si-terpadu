/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.sadhar.sia.terpadu.daftargedungtanahdankendaraan.DaftarGedungTanahDanKendaraanDAO;
import org.sadhar.sia.terpadu.daftargedungtanahdankendaraan.DaftarGedungTanahDanKendaraanDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class DaftarGedungTanahDanKendaraanTest {

    private DaftarGedungTanahDanKendaraanDAO dao;

    public DaftarGedungTanahDanKendaraanTest() {
        DBConnection.initDataSource();
        dao = new DaftarGedungTanahDanKendaraanDAOImpl();
    }

    @Test
    public void getData() {
        List<Map> datas = new ArrayList<Map>();
        String currentJenis = "";
        Integer no = 0;
        for (Map data : dao.getDaftarGedungTanahDanKendaraan()) {
            Map map = new HashMap();

            if (currentJenis.equals(data.get("jenis"))) {
                map.put("jenis", "");
                map.put("no", "");
            } else {
                currentJenis = data.get("jenis").toString();
                no++;
                map.put("no", no+"");
                map.put("jenis", currentJenis);
            }

            map.put("nama", data.get("nama"));
            map.put("keterangan", data.get("keterangan"));
            datas.add(map);
        }

        for (Map data : datas) {
            System.out.println(data.get("no") + "=>" + data.get("jenis") + "=>" + data.get("nama") + "=>" + data.get("keterangan"));

        }
    }
}
