/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;
import org.sadhar.sia.terpadu.statistikpendidikandosen.StatistikPendidikanDAOImpl;
import org.sadhar.sia.terpadu.statistikpendidikandosen.StatistikPendidikanDosenDAO;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class StatistikPendidikanTest {

    private StatistikPendidikanDosenDAO dao;

    public StatistikPendidikanTest() {
        DBConnection.initDataSource();
        dao = new StatistikPendidikanDAOImpl();
    }

    @Test
    public void getData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> datas = new ArrayList<Map>();
        List<Map> results = dao.getStatistikPendidikan();
        String[] jenjangs = {"S1", "S2", "S3"};


        for (Map prodi : dao.getProdi()) {
            for (String jenjang : jenjangs) {
                Map map = new HashMap();
                int pendidikan = 0;
                for (Map data : results) {
                    if (prodi.get("Nama_prg").toString().equalsIgnoreCase(data.get("Nama_prg").toString())) {
                        if (data.get("Nm_jenjang").toString().equals(jenjang.toString())) {                         
                            pendidikan++;
                        }
                    }
                }
                map.put("prodi", prodi.get("Nama_prg"));
                map.put("pendidikan", jenjang.toString());
                map.put("jumlah", pendidikan);
                dataset.addValue(Integer.valueOf(map.get("jumlah").toString()), map.get("prodi").toString(), map.get("pendidikan").toString());
                datas.add(map);
            }
        }

        for (Map data : datas) {
            System.out.println(data.get("prodi").toString() + "=>" + data.get("jumlah") + "=>" + data.get("pendidikan"));
        }
    }
}
