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
import org.sadhar.sia.terpadu.ipkdanips.rerataips.RerataIpsDAO;
import org.sadhar.sia.terpadu.ipkdanips.rerataips.RerataIpsDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class RerataIpsTest {

    private RerataIpsDAO rerataIpsDAO;
    private String kodeProdi = "1114";
    private String tahunAngkatan = "2000";

    public RerataIpsTest() {
        DBConnection.initDataSource();
        rerataIpsDAO = new RerataIpsDAOImpl();
    }

    //@Test
    public void getAllData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int currentYear = new DateTime().getYear();
        List<Map> datas = new ArrayList<Map>();
        List<Map> prodis = rerataIpsDAO.getProdi();
        List<Map> result = rerataIpsDAO.getRerataIps(null, "2005");

        for (Map prodi : prodis) {
            //Tahun
            for (int i = currentYear - 5; i <= currentYear; i++) {
                //Semester
                for (int j = 1; j <= 2; j++) {
                    int count = 0;
                    Map map = new HashMap();
                    Double ips = null;
                    double ipsTotal = 0d;
                    //Data
                    for (Map data : result) {
                        if (prodi.get("Nama_prg").toString().equalsIgnoreCase(data.get("Nama_prg").toString())) {
                            if (data.get("ips") == null || data.get("ips") == "") {
                                ips = 0d;
                            } else {
                                ips = Double.parseDouble(data.get("ips").toString().substring(0, 4));
                            }
                            if (Integer.valueOf(data.get("tahun").toString()) == i && Integer.valueOf(data.get("semester").toString()) == j) {
                                ipsTotal += ips;
                                count++;
                            }

                            if (ipsTotal > 0) {
                                map.put("ips", new DecimalFormat("# 0.00").format(ipsTotal / count));
                            } else {
                                map.put("ips", ipsTotal);
                            }

                            map.put("angkatan", i);
                            map.put("Nama_prg", prodi.get("Nama_prg"));
                            map.put("tahun", i);

                        }
                    }
                    if (map.get("tahun") != null) {
                        map.put("semester", j);
                        dataset.addValue(Double.valueOf(map.get("ips").toString()), map.get("tahun") + "::" + map.get("semester").toString() + "::", map.get("Nama_prg").toString());
                        datas.add(map);
                    }
                }
            }
        }
        for (Map data : datas) {

            System.out.print("::" + data.get("angkatan"));
            System.out.print("::" + data.get("tahun"));
            System.out.print("::" + data.get("semester"));
            System.out.print("::" + data.get("ips"));
            System.out.print("::" + data.get("Nama_prg"));
            System.out.println("");
        }

        int total = 0;
        for (Object column : dataset.getColumnKeys()) {
            total++;
        }

        for (Object row : dataset.getRowKeys()) {
            System.out.print(row.toString());
        }

        System.out.println("");
        double[] ips = new double[dataset.getRowKeys().size()];

        for (Object column : dataset.getColumnKeys()) {
            int index = 0;
            System.out.print(column.toString() + "--");

            for (Object row : dataset.getRowKeys()) {
                Number nbr = dataset.getValue((Comparable) row, (Comparable) column);
                Double data = null;
                if (nbr != null) {
                    data = nbr.doubleValue();
                } else {
                    data = 0d;
                }
                ips[index] += data / total;
                System.out.print(data + "|");
                index++;
            }

            System.out.println("");
        }

        for (double j : ips) {
            System.out.print(j + "|");
        }
        System.out.println("");
    }

    //@Test
    public void getRerataIps() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> datas = new ArrayList<Map>();
        List<Map> fakultas = new ArrayList<Map>();
        List<Map> prodis = rerataIpsDAO.getProdi();
        List<Map> result = rerataIpsDAO.getRerataIps("1114", null);

        for (int i = 1998; i <= 2010; i++) {
            //Semester
            for (int j = 1; j <= 2; j++) {
                int count = 0;
                Map map = new HashMap();
                Double ips = null;
                double ipsTotal = 0d;
                //Data
                for (Map data : result) {

                    if (data.get("ips") == null || data.get("ips") == "") {
                        ips = 0d;
                    } else {
                        ips = Double.parseDouble(data.get("ips").toString().substring(0, 4));
                    }

                    if (Integer.valueOf(data.get("tahun").toString()) == i && Integer.valueOf(data.get("semester").toString()) == j) {
                        ipsTotal += ips;
                        count++;
                    }

                    if (ipsTotal > 0) {
                        map.put("ips", new DecimalFormat("# 0.00").format(ipsTotal / count));
                    } else {
                        map.put("ips", String.valueOf(ipsTotal));
                    }
                    map.put("fakultas", data.get("Nama_prg"));
                    map.put("tahun", String.valueOf(i));


                }
                if (map.get("tahun") != null) {
                    map.put("semester", j);
                    map.put("angkatan", String.valueOf(2010));
                    //dataset.addValue(Double.valueOf(map.get("ips").toString()), map.get("tahun") + "-" + map.get("semester").toString(), map.get("fakultas").toString());
                    map.put("tahun", map.get("tahun") + "-" + map.get("semester").toString());

                }
            }


            //##################################################################################################################

            int total = 0;
            for (Object column : dataset.getColumnKeys()) {
                total++;
            }

            for (Object row : dataset.getRowKeys()) {
                System.out.print(row.toString());
            }

            System.out.println("");
            double[] ips = new double[dataset.getRowKeys().size()];

            for (Object column : dataset.getColumnKeys()) {
                int index = 0;
                System.out.print(column.toString() + "--");

                for (Object row : dataset.getRowKeys()) {
                    Number nbr = dataset.getValue((Comparable) row, (Comparable) column);
                    Double data = null;
                    if (nbr != null) {
                        data = nbr.doubleValue();
                    } else {
                        data = 0d;
                    }
                    ips[index] += data / total;
                    System.out.print(data + "|");
                    index++;
                }

                System.out.println("");
            }

            for (double j : ips) {
                System.out.print(j + "|");
            }
            System.out.println("");
        }
    }

    @Test
    public void getRerataIpsAll() {
        List<Map> maps = new ArrayList<Map>();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        //for (int tahun = 2000; tahun <= 2002; tahun++) {
        List<Map> datas = rerataIpsDAO.getRerataIps("1114", null);
        maps.addAll(datas);
        // }

        List<Map> results = new ArrayList<Map>();

        for (Map map : maps) {
            //System.out.println(map.get("angkatan") + "=>" + map.get("tahun") + "=>" + map.get("semester") + "=>" + map.get("ips"));
            for (int i = 2000; i <= 2010; i++) {
                Map m = new HashMap();
                for (int j = 1; j <= 2; j++) {
                    if (j == Integer.valueOf(map.get("semester").toString())) {
                        if (j == 1) {
                            m.put("semester", j + 1);
                            m.put("tahun", i);
                        } else {
                            m.put("semester", j - 1);
                            m.put("tahun", i + 1);
                        }
                        m.put("ips", map.get("ips"));
                    }
                }
                if (i == Integer.valueOf(map.get("tahun").toString())) {
                    m.put("angkatan", i);
                    results.add(m);
                }
            }
        }

        System.out.println("########################################################################################");
        for (Map map : maps) {
            System.out.println(map.get("angkatan") + "=>" + map.get("tahun") + "=>" + map.get("semester") + "=>" + map.get("ips"));
        }



//        for (int tahun = 2000; tahun <= 2010; tahun++) {
//            List<Map> datas = rerataIpsDAO.getRerataIps("1122", String.valueOf(tahun));
//            for (int i = 2000; i <= 2010; i++) {
//                Map map = new HashMap();
//                for (int j = 1; j <= 2; j++) {
//                    double ipsTotal = 0d;
//                    int count = 0;
//                    for (Map data : datas) {
//                        double ips = 0d;
//                        if (data.get("ips") == null || data.get("ips") == "") {
//                            ips = 0d;
//                        } else {
//                            ips = Double.parseDouble(data.get("ips").toString().substring(0, 4));
//                        }
//                        if (data.get("tahun").toString().equals(String.valueOf(i)) && data.get("semester").toString().equals(String.valueOf(j))) {
//                            count++;
//                            ipsTotal += ips;
//                        }
//
//                        if (ipsTotal > 0) {
//                            map.put("ips", new DecimalFormat("# 0.00").format(ipsTotal / count));
//                        } else {
//                            map.put("ips", "0.0");
//                        }
//
//                        map.put("angkatan", data.get("angkatan"));
//                        map.put("tahun", String.valueOf(i));
//                        map.put("fakultas", data.get("Nama_prg"));
//
//                    }
//
//                    if (map.get("tahun") != null) {
//                        dataset.addValue(Double.valueOf(map.get("ips").toString()),
//                                map.get("tahun").toString() + "-" + j,
//                                map.get("fakultas").toString() + "-" + map.get("angkatan"));
//                        map.put("tahun", String.valueOf(i) + "-" + String.valueOf(j));
//                        Map m = new HashMap();
//                        m.put("ips", map.get("ips"));
//                        m.put("angkatan", map.get("angkatan"));
//                        m.put("tahun", map.get("tahun"));
//                        m.put("fakultas", map.get("fakultas"));
//                        maps.add(m);
//                    }
//                }
//            }
//        }
//
//        for (Map map : maps) {
//            System.out.println(map.get("fakultas") + "=>" + map.get("tahun") + "=>" + map.get("semester") + "=>" + map.get("ips"));
//        }
//
//        for (Object row : dataset.getRowKeys()) {
//            System.out.print(row.toString());
//        }
//        System.out.println("");
//        for (Object column : dataset.getColumnKeys()) {
//            System.out.print(column.toString() + "--");
//
//            for (Object row : dataset.getRowKeys()) {
//                Number nbr = dataset.getValue((Comparable) row, (Comparable) column);
//                Double data = null;
//                if (nbr != null) {
//                    data = nbr.doubleValue();
//                } else {
//                    data = 0d;
//                }
//            }
//
//            System.out.println("");
//        }

    }
}
