/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.sadhar.sia.terpadu.warning.warningipkrendah.WarningIPKRendahDAO;
import org.sadhar.sia.terpadu.warning.warningipkrendah.WarningIPKRendahDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class WarningIPKRendahTest {

    private WarningIPKRendahDAO warningIPKRendahDAO;

    public WarningIPKRendahTest() {
        DBConnection.initDataSource();
        warningIPKRendahDAO = new WarningIPKRendahDAOImpl();
    }

    //@Test
    public void getAllWarning() {
        List<Map> datas = warningIPKRendahDAO.getWarningIPKRendah();
        for (Map data : datas) {
            System.out.print("---" + data.get("Nomor_mhs"));
            System.out.print("---" + data.get("nama_mhs"));
            System.out.print("---" + data.get("Nama_fak"));
            System.out.print("---" + data.get("ipk"));
            System.out.println("---" + data.get("angkatan"));
        }
    }

    @Test
    public void getWarningByProdi() {
        List<Map> datas = warningIPKRendahDAO.getWarningIPKRendah("1114");
        for (Map data : datas) {
            System.out.print("---" + data.get("Nomor_mhs"));
            System.out.print("---" + data.get("nama_mhs"));
            System.out.print("---" + data.get("Nama_fak"));
            System.out.print("---" + data.get("ipk"));
            System.out.println("---" + data.get("angkatan"));
        }
    }
}
