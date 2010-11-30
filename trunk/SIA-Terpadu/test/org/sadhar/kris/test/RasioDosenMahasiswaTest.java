/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.sadhar.sia.terpadu.rasiodosenmahasiswa.RasioDosenMahasiswaDAO;
import org.sadhar.sia.terpadu.rasiodosenmahasiswa.RasioDosenMahasiswaDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class RasioDosenMahasiswaTest {

    private RasioDosenMahasiswaDAO dao;

    public RasioDosenMahasiswaTest() {
        DBConnection.initDataSource();
        dao = new RasioDosenMahasiswaDAOImpl();
    }

    //@Test
    public void getData() {
        List<Map> results = dao.getRasioDosenMahasiswa("1114", "2005", "1");
        List<Map> dosens = dao.getNamaDosen("1114", "2005", "1");
        List<Map> datas = new ArrayList<Map>();
        for (Map dosen : dosens) {
            //System.out.println(result.get("jumlah") + "=>" + result.get("nama"));
            Map map = new HashMap();
            int jumlah = 0;
            for (Map result : results) {
                if (result.get("nama").toString().equals(dosen.get("nama"))) {
                    jumlah += Integer.valueOf(result.get("jumlah").toString());
                }
            }
            if (jumlah != 0) {
                map.put("nama", dosen.get("nama"));
                map.put("jumlah", jumlah);
                datas.add(map);
            }
        }

        for (Map data : datas) {
            System.out.println(data.get("jumlah") + "=>" + data.get("nama"));
        }
    }

    @Test
    public void get() {
//        List<Map> results = dao.getRasioDosenMahasiswa("2000","1");
//        for (Map result : results) {
//            Map map = new HashMap();
//            System.out.println(result.get("prodi") + "=>" + result.get("jumlah") + "=>" + result.get("status"));
//        }
        String tahun="20051";
        System.out.println(tahun.substring(0, 4));
    }
}
