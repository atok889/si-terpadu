/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.junit.Test;
import org.sadhar.sia.terpadu.jumlahpenelitian.jumlahdandaftarpenelitianperdosen.JumlahDanDaftarPenelitianPerDosenDAO;
import org.sadhar.sia.terpadu.jumlahpenelitian.jumlahdandaftarpenelitianperdosen.JumlahDanDaftarPenelitianPerDosenDAOImpl;
import org.sadhar.sia.terpadu.jumlahpenelitian.jumlahpenelitianperprodi.JumlahPenelitianPerProdiDAO;
import org.sadhar.sia.terpadu.jumlahpenelitian.jumlahpenelitianperprodi.JumlahPenelitianPerProdiDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class JumlahPenelitianPerProdiTest {

    //private JumlahPenelitianPerProdiDAO dao;
    private JumlahDanDaftarPenelitianPerDosenDAO dao;

    public JumlahPenelitianPerProdiTest() {
        DBConnection.initDataSource();
        // dao = new JumlahPenelitianPerProdiDAOImpl();
        dao = new JumlahDanDaftarPenelitianPerDosenDAOImpl();
    }

   // @Test
    public void get() {
        //  List<Map> maps = dao.getJumlahPenelitianPerProdi();5314
        List<Map> results = dao.getJumlahDanDaftarPenelitianPerDosen("5314");
        //Ambil data keselurhan
        for (Map map : results) {
            System.out.println(map.get("Nama_unit_kerja") + "=>" + map.get("jumlah") + "=>" + map.get("tahunPenilaian") + "-" + map.get("waktu").toString().substring(1, 3) + "=>" + map.get("Nama_peg"));
        }

        System.out.println("=========================================");
        List<String> temp = new ArrayList<String>();
        List<Map> temps = new ArrayList<Map>();
        //Buang nama yanng sama
        for (Map map : results) {
            int count = 0;
            Map m = new HashMap();
            if (!temp.contains(map.get("Nama_peg").toString())) {
                m.put("nama", map.get("Nama_peg"));
                count += Integer.valueOf(map.get("jumlah").toString());
                m.put("jumlah", count);
            }
            temp.add(map.get("Nama_peg").toString());
            temps.add(m);
        }


        List<Map> datas = new ArrayList<Map>();
        //Tambahakan jumlah pada nama yang sama
        for (Map nama : temps) {
            int jumlah = 0;
            Map map = new HashMap();
            // System.out.println(nama.get("nama") + "=>" + nama.get("jumlah"));
            if (nama.get("nama") != null) {
                for (Map data : results) {
                    if (nama.get("nama").toString().equals(data.get("Nama_peg").toString())) {
                        jumlah += Integer.valueOf(data.get("jumlah").toString());
                    }
                }
            }
            map.put("jumlah", jumlah);
            map.put("nama", nama.get("nama"));
            datas.add(map);
        }

        for (Map map : datas) {
            System.out.println(map.get("nama") + "=>" + map.get("jumlah"));
        }
    }

    @Test
    public void getDetail() {
        List<Map> results = dao.getDetailJumlahDanDaftarPenelitianPerDosen("00866");
        for (Map map : results) {
            System.out.println(map.get("judul") + "=>" + map.get("jumlah") + "=>" + map.get("tahunPenilaian") + "=>" + map.get("Nama_peg"));
        }

        List<Map> datas = new ArrayList<Map>();
        for (int i = new DateTime().getYear() - 5; i <= new DateTime().getYear(); i++) {
            Map map = new HashMap();
            String tahun = null;
            String nama = null;
            String jumlah = null;
            for (Map data : results) {
                if (Integer.valueOf(data.get("tahunPenilaian").toString()) == i) {
                    tahun = data.get("tahunPenilaian").toString();
                    nama = data.get("Nama_peg").toString();
                    jumlah = data.get("jumlah").toString();
                    break;
                } else {
                    tahun = String.valueOf(i);
                    nama = "jkjk";
                    jumlah = "0";
                }
            }
            map.put("tahun", tahun);
            map.put("nama", nama);
            map.put("jumlah", jumlah);
            datas.add(map);
        }

        for (Map data : datas) {
            System.out.println(data.get("tahun") + "=>" + data.get("nama") + "=>" + data.get("jumlah"));
        }
    }
}
