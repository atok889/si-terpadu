/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import org.junit.Test;
import org.sadhar.sia.terpadu.daftarkaryawandandosenyangakanpensiun.DaftarKaryawanDanDosenYangAkanPensiunDAO;
import org.sadhar.sia.terpadu.daftarkaryawandandosenyangakanpensiun.DaftarKaryawanDanDosenYangAkanPensiunDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class DaftarKaryawanDanDosenYangAkanPensiunTest {

    private DaftarKaryawanDanDosenYangAkanPensiunDAO dao;

    public DaftarKaryawanDanDosenYangAkanPensiunTest() {
        DBConnection.initDataSource();
        dao = new DaftarKaryawanDanDosenYangAkanPensiunDAOImpl();
    }

    @Test
    public void get() {
    }
}
