/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.bebansksdosen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author Hendro Steven
 */
public class BebanSKSDosenDAOImpl implements BebanSKSDosenDAO{

    public List<BebanSKSDosen> getBebanSKSDosen(ProgramStudi ps, String tahun, int semester) throws Exception {
        List<BebanSKSDosen> bebans = new ArrayList<BebanSKSDosen>();
        String db = "db_"+ps.getKode();
        String tbl = "rekaploaddsn"+ps.getKode()+tahun+semester;
        String db_tbl = db+"."+tbl;
        String sql = "SELECT NmDosen, TotSks FROM "+db_tbl+"";
        System.out.println(sql);
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for(Map m : rows){
            BebanSKSDosen bsd = new BebanSKSDosen();
            bsd.setNama(m.get("NmDosen").toString());
            bsd.setSks(Integer.valueOf(m.get("TotSks").toString()));
            bebans.add(bsd);
        }
        return bebans;
    }

}
