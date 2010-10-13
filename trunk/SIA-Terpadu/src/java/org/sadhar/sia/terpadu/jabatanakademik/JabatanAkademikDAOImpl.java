/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jabatanakademik;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class JabatanAkademikDAOImpl implements JabatanAkademikDAO {

    public JabatanAkademikDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<JabatanAkademik> gets() throws Exception {
        List<JabatanAkademik> list = new ArrayList<JabatanAkademik>();
        String sql = "select Kd_jab_akad as kode,Nama_jab_akad as nama, Urut as urut from kamus.jab_akad order by urut";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            JabatanAkademik o = new JabatanAkademik();
            o.setKode(m.get("kode").toString());
            o.setNama(m.get("nama").toString());
            o.setUrut(m.get("urut").toString());
            list.add(o);
        }
        return list;
    }

    public JabatanAkademik get(String kode) throws Exception {
        String sql = "select Kd_jab_akad as kode,Nama_jab_akad as nama, Urut as urut from kamus.jab_akad WHERE Kd_jab_akad = '" + kode + "' order by urut";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        JabatanAkademik o = null;
        for (Map m : rows) {
            o = new JabatanAkademik();
            o.setKode(m.get("kode").toString());
            o.setNama(m.get("nama").toString());
            o.setUrut(m.get("urut").toString());
        }
        return o;
    }

    public List<JabatanAkademik> getsByName(String nama) throws Exception {
        List<JabatanAkademik> list = new ArrayList<JabatanAkademik>();
        String sql = "select Kd_jab_akad as kode,Nama_jab_akad as nama, Urut as urut from kamus.jab_akad WHERE Nama_jab_akad LIKE '%" + nama + "%' order by urut ";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            JabatanAkademik o = new JabatanAkademik();
            o.setKode(m.get("kode").toString());
            o.setNama(m.get("nama").toString());
            o.setUrut(m.get("urut").toString());
            list.add(o);
        }
        return list;
    }
}
