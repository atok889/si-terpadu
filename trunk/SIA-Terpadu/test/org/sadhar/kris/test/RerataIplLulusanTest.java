/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;
import org.sadhar.sia.terpadu.ipkdanips.rerataipklulusan.RerataIpkLulusanDAO;
import org.sadhar.sia.terpadu.ipkdanips.rerataipklulusan.RerataIpkLulusanDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class RerataIplLulusanTest {

    private RerataIpkLulusanDAO rerataIpkLulusanDAO;

    public RerataIplLulusanTest() {
        DBConnection.initDataSource();
        rerataIpkLulusanDAO = new RerataIpkLulusanDAOImpl();
    }

    @Test
    public void getData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> datas = rerataIpkLulusanDAO.getRerataIPKLulusan();
        List<Map> prodis = rerataIpkLulusanDAO.getProdi();
        List<Map> rerata = new ArrayList<Map>();
        for (Map prodi : prodis) {
            for (int i = 2000; i <= 2010; i++) {
                Map map = new HashMap();
                int count = 0;
                double totalIpk = 0d;
                for (Map data : datas) {
                    if (i == Integer.valueOf(data.get("angkatan").toString()) && data.get("Kd_prg").toString().equalsIgnoreCase(prodi.get("Kd_prg").toString())) {
                        totalIpk += Double.valueOf(data.get("ipk").toString());
                        count += 1;
                    }
                }
                map.put("angkatan", i);
                if (totalIpk == 0d) {
                    map.put("ipk", 0d);
                } else {
                    map.put("ipk", totalIpk / count);
                }
                map.put("prodi", prodi.get("Nama_prg"));
                rerata.add(map);
            }
        }

        for (Map map : rerata) {
            System.out.println(map.get("prodi") + "::" + map.get("angkatan") + "::" + map.get("ipk"));
            dataset.addValue(Double.valueOf(map.get("ipk").toString()), map.get("angkatan").toString(), map.get("prodi").toString());
        }


        for (Object row : dataset.getRowKeys()) {
            System.out.print(row.toString() + "--");
        }

        System.out.println("");

        for (Object column : dataset.getColumnKeys()) {
            System.out.print(column.toString() + "--");

            for (Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                System.out.print("|" + number.doubleValue());
            }

            System.out.println("");

        }

    }
}
