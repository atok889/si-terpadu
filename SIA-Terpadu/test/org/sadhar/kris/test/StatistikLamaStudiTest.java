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

        StatistikLamaStudi statistikLamaStudis = statistikLamaStudiDAO.getStatistikLamaStudi("5314");
        List<Map> datas = new ArrayList<Map>();

        for (int i = 1; i <= 16; i++) {
            Map map = new HashMap();
            dataset.addValue(statistikLamaStudis.getSemesterValue(i), "Smt " + i, statistikLamaStudis.getProdi());
            map.put("jumlah", statistikLamaStudis.getSemesterValue(i));
            map.put("semester", i);
            datas.add(map);
        }

        for (Map map : datas) {
            System.out.println(map.get("semester") + "=>" + map.get("jumlah"));
        }



        for (Object column : dataset.getColumnKeys()) {
            System.out.println(column.toString());
            int i = 1;
            for (Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                System.out.print(i + "=>" + number.intValue());
                //System.out.println("");
                i++;
            }
        }
    }

    //@Test
    public void getListData() {
        List<StatistikLamaStudi> statistikLamaStudis = statistikLamaStudiDAO.getStatistikLamaStudi();
        List<Map> maps = new ArrayList<Map>();
        for (StatistikLamaStudi statistikLamaStudi : statistikLamaStudis) {

            for (int i = 16; i >= 1; i--) {
                Map map = new HashMap();
                map.put("prodi", statistikLamaStudi.getProdi());
                map.put("semester", "Smt " + i);
                map.put("jumlah", statistikLamaStudi.getSemesterValue(i));
                maps.add(map);
            }
        }

        for (Map map : maps) {
            System.out.println(map.get("prodi") + "--" + map.get("semester") + "--" + map.get("jumlah"));
        }
    }
}
