/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.junit.Test;
import org.sadhar.sia.terpadu.laporankinerjapegawai.LaporanKinerjaPegawaiDAO;
import org.sadhar.sia.terpadu.laporankinerjapegawai.LaporanKinerjaPegawaiDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class LaporanKinerjaPegawaiTest {

    private LaporanKinerjaPegawaiDAO laporanKinerjaPegawaiDAO;

    public LaporanKinerjaPegawaiTest() {
        DBConnection.initDataSource();
        laporanKinerjaPegawaiDAO = new LaporanKinerjaPegawaiDAOImpl();
    }

    @Test
    public void get() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> results = laporanKinerjaPegawaiDAO.getLaporanKinerjaPegawaiAdministratif();
        List<Map> unitKerjas = laporanKinerjaPegawaiDAO.getUnitKerja();
        List<Map> datas = new ArrayList<Map>();

        for (int i = new DateTime().getYear() - 5; i <= new DateTime().getYear(); i++) {
            for (int j = 1; j <= 3; j++) {
                for (Map unitKerja : unitKerjas) {
                    Map map = new HashMap();
                    String nama = "";
                    String tahun = "";
                    String rerata = "0.00";
                    String semester = "";
                    for (Map result : results) {
                        if (result.get("nama") != null && result.get("tahun").toString().equals(String.valueOf(i)) &&
                                result.get("semester").toString().equals(String.valueOf(j))) {
                            if (unitKerja.get("nama").toString().equalsIgnoreCase(result.get("nama").toString())) {
                                nama = result.get("nama").toString();
                                tahun = result.get("tahun").toString();
                                rerata = result.get("rerata").toString();
                                semester = result.get("semester").toString();
                            } else {
                                nama = unitKerja.get("nama").toString();
                                tahun = String.valueOf(i);
                                semester = String.valueOf(i);
                            }
                        } else {
                            nama = unitKerja.get("nama").toString();
                            tahun = String.valueOf(i);
                            semester = String.valueOf(j);
                        }
                    }
                    map.put("nama", nama);
                    map.put("tahun", tahun);
                    map.put("rerata", new DecimalFormat(" #0.00").format(Double.valueOf(rerata)));
                    map.put("semester", semester);
                    datas.add(map);
                }
            }
        }

        List<Map> report = new ArrayList<Map>();
        for (Map data : datas) {
            //System.out.println(map.get("tahun") + "=>" + map.get("semester") + "=>" + map.get("nama") + "=>" + map.get("rerata"));
            if (data.get("semester").toString().equals("3")) {
                dataset.addValue(Double.valueOf(data.get("rerata").toString()), data.get("tahun").toString() + "|", data.get("nama").toString());
            } else {
                dataset.addValue(Double.valueOf(data.get("rerata").toString()), data.get("tahun").toString() + "-" + data.get("semester") + "|", data.get("nama").toString());
            }

            Map map = new HashMap();
            map.put("nama", data.get("data"));
            map.put("tahun", data.get("tahun").toString());
            map.put("kategori", "NEP");
            map.put("semester", data.get("semester"));
            if (data.get("semester").toString().equals("3")) {
                map.put("kategori", "DP3");
                map.put("semester", "");
            }
            map.put("rerata", data.get("rerata"));
            report.add(map);
        }
        for (Map map : report) {
            System.out.println(map.get("kategori") + "=>" + map.get("tahun") + "=>" + map.get("semester") + "=>" + map.get("nama") + "=>" + map.get("rerata"));
        }
    }

    //        for (Object row : dataset.getRowKeys()) {
    //            System.out.print(row.toString());
    //        }
    //
    //        System.out.println("");
    //
    //        for (Object column : dataset.getColumnKeys()) {
    //            System.out.println(column.toString());
    //            int i = 1;
    //            for (Object row : dataset.getRowKeys()) {
    //                Number number = dataset.getValue((Comparable) row, (Comparable) column);
    //                System.out.print("| " + number.doubleValue());
    //                //System.out.println("");
    //                i++;
    //            }
    //        }
    {
    }
}
