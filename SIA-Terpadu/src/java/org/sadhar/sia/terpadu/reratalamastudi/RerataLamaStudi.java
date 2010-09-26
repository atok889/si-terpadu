/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.reratalamastudi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;

/**
 *
 * @author jasoet
 */
public class RerataLamaStudi {

    private List<String> tahunAngkatanList;
    

    public RerataLamaStudi() {
        tahunAngkatanList = new ArrayList<String>();
        tahunAngkatanList.add("90");
        tahunAngkatanList.add("91");
        tahunAngkatanList.add("92");
        tahunAngkatanList.add("93");
        tahunAngkatanList.add("94");
        tahunAngkatanList.add("95");
        tahunAngkatanList.add("96");
        tahunAngkatanList.add("97");
        tahunAngkatanList.add("98");
        tahunAngkatanList.add("99");
        tahunAngkatanList.add("00");

        tahunAngkatanList.add("01");
        tahunAngkatanList.add("02");
        tahunAngkatanList.add("03");
        tahunAngkatanList.add("04");
        tahunAngkatanList.add("05");
        tahunAngkatanList.add("06");
        tahunAngkatanList.add("07");
        tahunAngkatanList.add("08");
        tahunAngkatanList.add("09");
        tahunAngkatanList.add("10");

        DateTime dt = new DateTime(new Date());
        int yearNow = dt.getYear();
        String subYearString = Integer.toString(yearNow).substring(2);

        for (int i = 10; i < Integer.parseInt(subYearString); i++) {
            tahunAngkatanList.add("" + i);
        }
    }

    public List<String> getTahunAngkatanList() {
        return tahunAngkatanList;
    }

    public void setTahunAngkatanList(List<String> tahunAngkatanList) {
        this.tahunAngkatanList = tahunAngkatanList;
    }
}
