/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.sadhar.sia.terpadu.warning.warningadministratif.WarningAdministratifDAO;
import org.sadhar.sia.terpadu.warning.warningadministratif.WarningAdministratifDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class WarningAdministratifTest {

    private WarningAdministratifDAO warningAdministratifDAO;
    private String kodeProdi = "1122";
    private String tahunAngkatan = "2009";

    public WarningAdministratifTest() {
        DBConnection.initDataSource();
        warningAdministratifDAO = new WarningAdministratifDAOImpl();
    }

    @Test
    public void getWarning() {
        List<Map> datas = warningAdministratifDAO.getWarningAdministratif(kodeProdi);

        List<Map> dataMahasiswa = new ArrayList<Map>();

        for (Map map : datas) {
            System.out.print(map.get("nama_mhs") + "::" + map.get("Nama_prg") + "::" + map.get("Nama_fak") + "::" + map.get("angkatan"));
            System.out.println("");
        }
    }
}
