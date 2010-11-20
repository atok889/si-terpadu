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
import org.sadhar.sia.terpadu.ipkdanips.sebaranipkmahasiswa.SebaranIpkMahasiswaDAO;
import org.sadhar.sia.terpadu.ipkdanips.sebaranipkmahasiswa.SebaranIpkMahasiswaDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class SebaranIpkLulusanTest {

    private SebaranIpkMahasiswaDAO sebaranIpkLulusanDAO;

    public SebaranIpkLulusanTest() {
        DBConnection.initDataSource();
        sebaranIpkLulusanDAO = new SebaranIpkMahasiswaDAOImpl();
    }

    @Test
    public void getData() {
        List<Map> datas = sebaranIpkLulusanDAO.getSebaranIpkLulusan();
        List<Map> prodis = sebaranIpkLulusanDAO.getProdi();
        List<Map> rerata = new ArrayList<Map>();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();


        for (Map prodi : prodis) {
            int countE = 0;
            int countD = 0;
            int countC = 0;
            int countB = 0;
            int countA = 0;
            int totalMahasiswa = 0;
            Map map = new HashMap();
            for (Map data : datas) {

                //System.out.println(Double.parseDouble(map.get("ipk").toString()) + "::" + map.get("Nama_prg").toString());
                if (prodi.get("Kd_prg").toString().equalsIgnoreCase(data.get("Kd_prg").toString())) {
                    double ipk = Double.parseDouble(data.get("ipk").toString());
                    if (ipk >= 0 && ipk <= 2) {
                        countE += 1;
                    } else if (ipk > 2 && ipk <= 2.5) {
                        countD += 1;
                    } else if (ipk > 2.5 && ipk <= 3) {
                        countC += 1;
                    } else if (ipk > 3 && ipk <= 3.5) {
                        countB += 1;
                    } else if (ipk > 3 && ipk <= 4) {
                        countA += 1;
                    }
                    map.put("ipkA", countA);
                    map.put("ipkB", countB);
                    map.put("ipkC", countC);
                    map.put("ipkD", countD);
                    map.put("ipkE", countE);
                    totalMahasiswa++;
                } else {
                    map.put("ipkA", countA);
                    map.put("ipkB", countB);
                    map.put("ipkC", countC);
                    map.put("ipkD", countD);
                    map.put("ipkE", countE);
                }
            }
            map.put("total", totalMahasiswa);
            map.put("prodi", prodi.get("Nama_prg").toString());
            //Hitung prosentase
            int ipkA = Integer.parseInt(map.get("ipkA").toString());
            int ipkB = Integer.parseInt(map.get("ipkB").toString());
            int ipkC = Integer.parseInt(map.get("ipkC").toString());
            int ipkD = Integer.parseInt(map.get("ipkD").toString());
            int ipkE = Integer.parseInt(map.get("ipkE").toString());
            double prosentaseA = 0;
            double prosentaseB = 0;
            double prosentaseC = 0;
            double prosentaseD = 0;
            double prosentaseE = 0;
            if (totalMahasiswa != 0) {
                prosentaseA = ((double) ipkA / totalMahasiswa) * 100;
                prosentaseB = ((double) ipkB / totalMahasiswa) * 100;
                prosentaseC = ((double) ipkC / totalMahasiswa) * 100;
                prosentaseD = ((double) ipkD / totalMahasiswa) * 100;
                prosentaseE = ((double) ipkE / totalMahasiswa) * 100;
            }
           
            map.put("prosentaseA", prosentaseA);
            map.put("prosentaseB", prosentaseB);
            map.put("prosentaseC", prosentaseC);
            map.put("prosentaseD", prosentaseD);
            map.put("prosentaseE", prosentaseE);
            rerata.add(map);
            System.out.println(prodi.get("Kd_prg").toString() + "::" + countE + "::" + countD + "::" + countC + "::" + countB + "::" + countA);

        }

        for (Map map : rerata) {
            System.out.print(map.get("prosentaseA") + "::" + map.get("prosentaseB") + "::" + map.get("prosentaseC") + "::" + map.get("prosentaseD") + "::" + map.get("prosentaseE"));
            System.out.println("");
            System.out.print(map.get("prodi") + "::" + map.get("ipkA") + "::" + map.get("ipkB") + "::" + map.get("ipkC") + "::" + map.get("ipkD") + "::" + map.get("ipkE") + "::" + map.get("total"));
            System.out.println("");
        }



        for (Object row : dataset.getRowKeys()) {
            System.out.print(row.toString() + "::");
        }

        System.out.println("");
        for (Object column : dataset.getColumnKeys()) {
            System.out.print(column.toString() + "::");
            int i = 1;
            for (Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                if (number == null) {
                    number = 0d;
                } else {
                    System.out.print("|" + number.doubleValue());
                }
                i++;
            }
            System.out.println("");
        }
    }
}
