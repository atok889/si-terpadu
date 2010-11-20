/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.Test;
import org.sadhar.sia.common.ClassUtility;
import org.sadhar.sia.terpadu.pinjamanunit.PinjamanUnitDAO;
import org.sadhar.sia.terpadu.pinjamanunit.PinjamanUnitDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class PinjamanUnitTest {

    private PinjamanUnitDAO dao;

    public PinjamanUnitTest() {
        DBConnection.initDataSource();
        dao = new PinjamanUnitDAOImpl();
    }

    @Test
    public void getData() {
        List<Map> results = dao.getPinjamanUnit();        
        DecimalFormat df=new DecimalFormat("#,##0.00");
        DecimalFormatSymbols idSymbols=df.getDecimalFormatSymbols();
        idSymbols.setGroupingSeparator('.');
        idSymbols.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(idSymbols);
        for (Map result : results) {
            System.out.println(result.get("nama") + "=>" + df.format(Double.valueOf(result.get("jumlah").toString())));
        }
    }
}
