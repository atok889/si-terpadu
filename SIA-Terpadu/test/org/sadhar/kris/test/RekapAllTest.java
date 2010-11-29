/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.sadhar.sia.terpadu.rekapAll.RekapAll;
import org.sadhar.sia.terpadu.rekapAll.RekapAllDAO;
import org.sadhar.sia.terpadu.rekapAll.RekapAllDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class RekapAllTest {

    private RekapAllDAO dao;

    public RekapAllTest() {
        DBConnection.initDataSource();
        dao = new RekapAllDAOImpl();
    }

    @Test
    public void get() throws Exception {
        List<RekapAll> datas = dao.getLpj("16053140", "2010");
        System.out.println(datas.size());
    }
}
