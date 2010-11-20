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
import org.joda.time.DateTime;
import org.junit.Test;
import org.sadhar.sia.terpadu.statistikmahasiswa.StatistikMahasiswaDAO;
import org.sadhar.sia.terpadu.statistikmahasiswa.StatistikMahasiswaDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class StatistikMahasiswaTest {

    private StatistikMahasiswaDAO statistikMahasiswaDAO;
    private String kodeProdi = null;

    public StatistikMahasiswaTest() {
        DBConnection.initDataSource();
        statistikMahasiswaDAO = new StatistikMahasiswaDAOImpl();
    }

    @Test
    public void getDetailMhs() {
        List<Map> results = new ArrayList<Map>();
        int no = 1;

        for (Map map : statistikMahasiswaDAO.getDetailAllMahasiswaLulus(null, "1998")) {
            System.out.println(no + "." + map.get("nama_mhs").toString());
            no++;
        }
    }

    //@Test
    public void getMahasiswa() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> mahasiswas = new ArrayList<Map>();

        List<Map> mahasiswaDO = statistikMahasiswaDAO.getMahasiswaLulus(null);
        System.out.println(mahasiswaDO.size());

        for (int i = 1998; i <= new DateTime().getYear(); i++) {
            Integer jumlah = 0;
            Integer angkatan = 0;
            String status = null;
            Map mahasiswa = new HashMap();


            if (mahasiswaDO.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0);
                mahasiswa.put("tahun", i);
                mahasiswa.put("status", "Mahasiswa Lulus");
            } else {
                mahasiswa = new HashMap();
                jumlah = 0;
                for (Map map : mahasiswaDO) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        status = map.get("status").toString();
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("status", status);
                        } else {
                            jumlah += Integer.parseInt(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("status", status);
                        }
                    }
                }
            }
            mahasiswas.add(mahasiswa);
        }

        for (Map map : mahasiswas) {
            System.out.println(map.get("tahun") + "-------" + map.get("jumlah") + "--------------" + map.get("status"));
            Integer jumlah = 0;
            try {
                jumlah = Integer.parseInt(map.get("jumlah").toString());
            } catch (NullPointerException npe) {
                jumlah = 0;
            }
            dataset.addValue(jumlah, map.get("tahun").toString(), map.get("status").toString());
        }

        for (Object row : dataset.getRowKeys()) {
            System.out.print(row.toString() + "--");
        }

        System.out.println("");

        for (Object column : dataset.getColumnKeys()) {
            System.out.print(column.toString() + "--");
            int i = 1;
            for (Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                System.out.print("|" + number.intValue());
                i++;
            }
            System.out.println("");
        }
    }
}
