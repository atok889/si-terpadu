/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.sadhar.sia.terpadu.ipkdanips.ipkdanipssetiapmahasiswa.IpkdanIpsSetiapMahasiswaDAO;
import org.sadhar.sia.terpadu.ipkdanips.ipkdanipssetiapmahasiswa.IpkdanIpsSetiapMahasiswaDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class IpkDanIpsSetiapMahasiswaTest {

    private IpkdanIpsSetiapMahasiswaDAO dao;

    public IpkDanIpsSetiapMahasiswaTest() {
        DBConnection.initDataSource();
        dao = new IpkdanIpsSetiapMahasiswaDAOImpl();
    }

    @Test
    public void getData() {
        List<Map> results = dao.getIpkDanIpsSetiapMahasiswa("5314", "2009", "2009", "1", null);
        List<Map> datas = new ArrayList<Map>();

        for (Map result : results) {
            System.out.println(result.get("nama_mhs") + "=>" + result.get("tahun") + "=>" + result.get("angkatan") + "=>" + result.get("semester") + "=>" + result.get("ipk") + "=>" + result.get("ips"));
        }

    }
}
