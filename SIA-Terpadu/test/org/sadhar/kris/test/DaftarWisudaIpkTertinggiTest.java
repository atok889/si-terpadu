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
import org.sadhar.sia.terpadu.ipkdanips.daftarwisudaipktertinggi.DaftarWisudaIpkTertinggiDAO;
import org.sadhar.sia.terpadu.ipkdanips.daftarwisudaipktertinggi.DaftarWisudaIpkTertinggiDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class DaftarWisudaIpkTertinggiTest {

    private DaftarWisudaIpkTertinggiDAO daftarWisudaIpkTertinggiDAO;

    public DaftarWisudaIpkTertinggiTest() {
        DBConnection.initDataSource();
        daftarWisudaIpkTertinggiDAO = new DaftarWisudaIpkTertinggiDAOImpl();
    }

    @Test
    public void generateTahun() {
        List<Map> datas = daftarWisudaIpkTertinggiDAO.getTanggalWisuda();
        for (Map map : datas) {
            System.out.println(map.get("tanggal"));
        }

    }

//@Test
    public void getData() {
        List<Map> results = daftarWisudaIpkTertinggiDAO.getDaftarWisudaIpkTertinggi("2005-11-19");
        List<Map> prodis = daftarWisudaIpkTertinggiDAO.getProdi();
        List<Map> datas = new ArrayList<Map>();

        for (Map prodi : prodis) {
            Map data = new HashMap();
            double ipk = 0;
            String nama = null;
            for (Map map : results) {
                if (map.get("Nama_prg").toString().equalsIgnoreCase(prodi.get("Nama_prg").toString())) {
                    //System.out.println(map.get("Nama_prg") + "::" + map.get("nama_mhs") + "::" + map.get("ipk"));
                    if (ipk < Double.valueOf(map.get("ipk").toString())) {
                        ipk = Double.valueOf(map.get("ipk").toString());
                        nama =
                                map.get("nama_mhs").toString();
                    }

                }
            }
            if (nama != null) {
                data.put("prodi", prodi.get("Nama_prg").toString());
                data.put("nama", nama);
                data.put("ipk", ipk);
                datas.add(data);
            }
        }

        for (Map map : datas) {
            System.out.println(map.get("prodi") + "::" + map.get("nama") + "::" + map.get("ipk"));
        }
    }
}
