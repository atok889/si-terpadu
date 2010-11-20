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
import org.sadhar.sia.terpadu.ipkdanips.ipkdanipssetiapmahasiswa.IpkdanIpsSetiapMahasiswaDAO;
import org.sadhar.sia.terpadu.ipkdanips.ipkdanipssetiapmahasiswa.IpkdanIpsSetiapMahasiswaDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class RerataIpsTotalTest {

    private IpkdanIpsSetiapMahasiswaDAO dao;

    public RerataIpsTotalTest() {
        DBConnection.initDataSource();
        dao = new IpkdanIpsSetiapMahasiswaDAOImpl();
    }

    @Test
    public void getData() {
//        List<Map> results = dao.getIpkDanIpsSetiapMahasiswa("1114");
//        List<Map> datas = new ArrayList<Map>();
//        for (int i = 1; i <= 14; i++) {
//            Map map = new HashMap();
//            int count = 0;
//            double ips = 0;
//            for (Map result : results) {
//                if (Integer.valueOf(result.get("semester").toString()) == i) {
//                    count++;
//                    ips += Double.valueOf(result.get("ips").toString());
//                }
//            }
//            map.put("ips", ips / count);
//            map.put("semester", i);
//            datas.add(map);
//        }
//
//
//        for (Map data : datas) {
//            System.out.println(data.get("ips") + " => " + data.get("semester"));
//        }
    }
}
