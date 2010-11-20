/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.Map;
import org.junit.Test;
import org.sadhar.sia.terpadu.daftarmatakuliahyangpalingseringdiulang.DaftarMataKuliahYangPalingSeringDiulangDAO;
import org.sadhar.sia.terpadu.daftarmatakuliahyangpalingseringdiulang.DaftarMataKuliahYangPalingSeringDiulangDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class DaftarMataKuliahYangPalingSeringDiulangTest {

    private DaftarMataKuliahYangPalingSeringDiulangDAO dao;

    public DaftarMataKuliahYangPalingSeringDiulangTest() {
        DBConnection.initDataSource();
        dao = new DaftarMataKuliahYangPalingSeringDiulangDAOImpl();
    }

    @Test
    public void getData() {
        for (Map map : dao.getDaftarMataKuliahYangPalingSeringDiulang("1114", "20002")) {
            System.out.println(map.get("mataKuliah") + "=>" + map.get("jumlah") + "=>" + map.get("dosen"));
        }
    }
}
