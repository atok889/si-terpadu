/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.gelombang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author Hendro Steven
 */
public class GelombangDAOImpl implements GelombangDAO {

    public Gelombang get(int kode) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Gelombang> gets() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Gelombang> gets(String except) throws Exception {
        List<Gelombang> gels = new ArrayList<Gelombang>();
        String sql = "SELECT kd_gelombang, nm_gelombang FROM kamus.gelombang WHERE kd_gelombang<>'"+except+"'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for(Map m : rows){
            Gelombang gel = new Gelombang();
            gel.setKode(m.get("kd_gelombang").toString());
            gel.setNama(m.get("nm_gelombang").toString());
            gels.add(gel);
        }
        return gels;
    }

}
