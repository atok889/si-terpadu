/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.Map;
import org.junit.Test;
import org.sadhar.sia.terpadu.penghapusanbaranginvestasi.PenghapusanBarangInvestasiDAO;
import org.sadhar.sia.terpadu.penghapusanbaranginvestasi.PenghapusanBarangInvestasiDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class PenghapusanBarangInvestasiTest {

    private PenghapusanBarangInvestasiDAO dao;

    public PenghapusanBarangInvestasiTest() {
        DBConnection.initDataSource();
        dao = new PenghapusanBarangInvestasiDAOImpl();
    }

    @Test
    public void getData() {
        for (Map data : dao.getModelPenghapusanBarang()) {
            System.out.println(data.get("kodeModelPenghapusanBarang") + "=>" + data.get("modelPenghapusanBarang"));
        }
    }
}
