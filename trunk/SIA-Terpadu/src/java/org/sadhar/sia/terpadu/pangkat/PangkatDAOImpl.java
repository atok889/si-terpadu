/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pangkat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class PangkatDAOImpl implements PangkatDAO {

    public PangkatDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Pangkat> gets() throws Exception {
        List<Pangkat> list = new ArrayList<Pangkat>();
        String sql = "Select Kd_pang as kode,Nama_pang as nama from kamus.pangkat";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            Pangkat o = new Pangkat();
            o.setKode(m.get("kode").toString());
            o.setNama(m.get("nama").toString());
            list.add(o);
        }
        return list;
    }

    public Pangkat get(String kode) throws Exception {
        String sql = "Select Kd_pang as kode,Nama_pang as nama from kamus.pangkat WHERE Kd_pang = '" + kode + "' ";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        Pangkat o = null;
        for (Map m : rows) {
            o = new Pangkat();
            o.setKode(m.get("kode").toString());
            o.setNama(m.get("nama").toString());
        }
        return o;
    }

    public List<Pangkat> getsByName(String nama) throws Exception {
        List<Pangkat> list = new ArrayList<Pangkat>();
        String sql = "Select Kd_pang as kode,Nama_pang as nama from kamus.pangkat WHERE Nama_pang LIKE '%" + nama + "%'  ";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            Pangkat o = new Pangkat();
            o.setKode(m.get("kode").toString());
            o.setNama(m.get("nama").toString());
            list.add(o);
        }
        return list;
    }
}
