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
import org.sadhar.sia.terpadu.distribusiumurdosendankaryawan.DistribusiUmurDosenDanKaryawanDAO;
import org.sadhar.sia.terpadu.distribusiumurdosendankaryawan.DistribusiUmurDosenDanKaryawanDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class DistribusiUmurDosenTest {

    private DistribusiUmurDosenDanKaryawanDAO dao;

    public DistribusiUmurDosenTest() {
        DBConnection.initDataSource();
        dao = new DistribusiUmurDosenDanKaryawanDAOImpl();
    }

    @Test
    public void get() {
        List<Map> results = dao.getAll("16011140","31","40");
        int rangeA = 0;
        int rangeB = 0;
        int rangeC = 0;
        int rangeD = 0;
        int rangeE = 0;

        for (Map result : results) {
            //System.out.println(result.get("nama") + "=>" + result.get("golongan") + "=>" + result.get("jabatan") + "=>" + result.get("umur"));
            int umur = Integer.valueOf(result.get("umur").toString());
            if (umur >= 21 && umur <= 30) {
                rangeA++;
            } else if (umur >= 31 && umur <= 40) {
                rangeB++;
            } else if (umur >= 41 && umur <= 50) {
                rangeC++;
            } else if (umur >= 51 && umur <= 60) {
                rangeD++;
            } else if (umur >= 61 && umur <= 70) {
                rangeE++;
            }
        }

        List<Map> datas = new ArrayList<Map>();

        for (Map result : results) {
            Map map = new HashMap();
//            map.put("nama", result.get("nama"));
//            map.put("golongan", result.get("golongan"));
//            map.put("jabatan", result.get("jabatan"));
//            map.put("umur", result.get("umur"));
//            map.put("status", result.get("status"));
            int umur = Integer.valueOf(result.get("umur").toString());
            if (umur >= 21 && umur <= 30) {
                map.put("jumlah", rangeA);
                map.put("range", "A");
            } else if (umur >= 31 && umur <= 40) {
                map.put("jumlah", rangeB);
                map.put("range", "B");
            } else if (umur >= 41 && umur <= 50) {
                map.put("jumlah", rangeC);
                map.put("range", "C");
            } else if (umur >= 51 && umur <= 60) {
                map.put("jumlah", rangeD);
                map.put("range", "D");
            } else if (umur >= 61 && umur <= 70) {
                map.put("jumlah", rangeE);
                map.put("range", "E");
            }
            datas.add(map);
        }

        for (Map result : datas) {
            System.out.println(result.get("nama") + "=>" + result.get("golongan") + "=>" + result.get("range") + "=>" + result.get("jumlah"));
        }


    }
}
