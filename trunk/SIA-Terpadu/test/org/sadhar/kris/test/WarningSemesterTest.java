/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.sadhar.sia.terpadu.warning.warningsemester.WarningSemesterDAO;
import org.sadhar.sia.terpadu.warning.warningsemester.WarningSemesterDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class WarningSemesterTest {

    private String kodeProdi = "8114";
    private String tahunAngkatan = "1996";
    private WarningSemesterDAO warningSemesterDAO;
    public WarningSemesterTest() {
        DBConnection.initDataSource();
        warningSemesterDAO = new WarningSemesterDAOImpl();
    }

    @Test
    public void getData(){
        List<Map> data = warningSemesterDAO.getWarningSemester("2");
        for(Map map : data){
            System.out.print("--"+map.get("semester"));
            System.out.println("--"+map.get("nomor_mhs"));
        }
    }
}
