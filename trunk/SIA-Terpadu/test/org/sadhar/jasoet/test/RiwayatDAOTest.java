/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.jasoet.test;

import java.util.List;
import org.junit.Test;
import org.sadhar.sia.terpadu.riwayatdosen.DataDosen;
import org.sadhar.sia.terpadu.riwayatdosen.RiwayatDosen;
import org.sadhar.sia.terpadu.riwayatdosen.RiwayatDosenDAO;
import org.sadhar.sia.terpadu.riwayatdosen.RiwayatDosenDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class RiwayatDAOTest {

    private RiwayatDosenDAO dao;

    public RiwayatDAOTest() {
        DBConnection.initDataSource();
        dao = new RiwayatDosenDAOImpl();
    }

    @Test
    public void getData() {
        List<DataDosen> ldata = dao.getDataDosenByUnitKerja("16053140");

        for (DataDosen dd : ldata) {
            System.out.println(dd.getKode() + "-" + dd.getNama());
        }

        System.out.println("================+");

        List<RiwayatDosen> lrd = dao.getRiwayatDosenByKodeDosen("00961", "16053140");

        for (RiwayatDosen rd : lrd) {
            System.out.println(rd.getRiwayat() + "-" + rd.getNama() + "-" + rd.getTahun() + "-" + rd.getKeterangan());
        }
    }
}
