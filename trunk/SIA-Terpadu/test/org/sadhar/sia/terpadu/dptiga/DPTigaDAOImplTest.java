/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.dptiga;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jasoet
 */
public class DPTigaDAOImplTest {

    public DPTigaDAOImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getByKodeUnit method, of class DPTigaDAOImpl.
     */
    @Test
    public void testGetByKodeUnit() throws Exception {
        System.out.println("getByKodeUnit");
        String kodeUnit = "1608";
        DPTigaDAOImpl instance = new DPTigaDAOImpl();
        List<DPTiga> result = instance.getByKodeUnit(kodeUnit);
        for (DPTiga dpt : result) {
            System.out.println(dpt.getKodePegawai());
            System.out.println(dpt.getKodeUnit());
            System.out.println(dpt.getNamaPegawai());
            System.out.println(dpt.getNamaUnit());
            System.out.println(dpt.getNilaiDP3());
            System.out.println(dpt.getTahunPenilaian());
        }
    }
}
