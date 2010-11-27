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
import org.sadhar.sia.terpadu.statistiklamastudi.StatistikLamaStudi;
import org.sadhar.sia.terpadu.statistiklamastudi.StatistikLamaStudiDAO;
import org.sadhar.sia.terpadu.statistiklamastudi.StatistikLamaStudiDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class StatistikLamaStudiTest {

    private String kodeProdi = "1114";
    private String tahunAngkatan = "1996";
    private StatistikLamaStudiDAO statistikLamaStudiDAO;

    public StatistikLamaStudiTest() {
        DBConnection.initDataSource();
        statistikLamaStudiDAO = new StatistikLamaStudiDAOImpl();
    }

    @Test
    public void getData() throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<Map> results = statistikLamaStudiDAO.getStatistikLamaStudi("1124", "");
        List<Map> datas = new ArrayList<Map>();
        for (int i = 1; i <= 16; i++) {
            Map map = new HashMap();
            for (Map result : results) {
                //System.out.println(result.get("prodi") + "=>" + result.get("semester") + "=>" + result.get("lama"));
                if (Integer.valueOf(result.get("semester").toString()) == i) {
                    map.put("lama", result.get("lama"));
                    map.put("semester", i);
                } else {
                    map.put("semester", i);
                }
                map.put("prodi", result.get("prodi"));
            }
            datas.add(map);
        }

//        for (Map prodi : statistikLamaStudiDAO.getProdi()) {
//            Map map = new HashMap();
//            for (Map result : results) {
//                if (result.get("prodi").toString().equals(prodi.get("Nama_prg").toString())) {
//                    map.put("lama", result.get("lama"));
//                    map.put("semester", result.get("semester"));
//                } else {
//                    map.put("semester", "10");
//                }
//                map.put("prodi", prodi.get("Nama_prg"));
//            }
//            datas.add(map);
//        }



        for (Map data : datas) {
            System.out.println(data.get("prodi") + "=>" + data.get("semester") + "=>" + data.get("lama"));
            int lama = 0;
            if (data.get("lama") != null) {
                lama = Integer.valueOf(data.get("lama").toString());
            }
            dataset.addValue(lama, "Smt " + data.get("semester"), data.get("prodi").toString());
        }



        for (Object column : dataset.getColumnKeys()) {
            //System.out.println(column.toString());
            int i = 1;
            for (Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                System.out.print(column.toString() + "=>" + number.intValue());
                System.out.println("");
                i++;
            }
        }
    }

    //@Test
    public void getListData() {
//        List<StatistikLamaStudi> statistikLamaStudis = statistikLamaStudiDAO.getStatistikLamaStudi();
//        List<Map> maps = new ArrayList<Map>();
//        for (StatistikLamaStudi statistikLamaStudi : statistikLamaStudis) {
//
//            for (int i = 16; i >= 1; i--) {
//                Map map = new HashMap();
//                map.put("prodi", statistikLamaStudi.getProdi());
//                map.put("semester", "Smt " + i);
//                map.put("jumlah", statistikLamaStudi.getSemesterValue(i));
//                maps.add(map);
//            }
//        }
//
//        for (Map map : maps) {
//            System.out.println(map.get("prodi") + "--" + map.get("semester") + "--" + map.get("jumlah"));
//        }
    }
}
