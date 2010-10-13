/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jenjangstudi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class JenjangStudiDAOImpl implements JenjangStudiDAO {

    public JenjangStudiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<JenjangStudi> gets() throws Exception {
        List<JenjangStudi> list = new ArrayList<JenjangStudi>();
        String sql = "Select kj.Kd_jenjang as kode,kj.Nm_jenjang as nama,kj.jenjang_ing as namaEng from kamus.jenjang kj";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            JenjangStudi o = new JenjangStudi();
            o.setKode(m.get("kode").toString());
            o.setNama(m.get("nama").toString());
            o.setNamaEng(m.get("namaEng").toString());
            list.add(o);
        }
        return list;
    }

    public JenjangStudi get(String kode) throws Exception {
        String sql = "Select kj.Kd_jenjang as kode,kj.Nm_jenjang as nama,kj.jenjang_ing as namaEng from kamus.jenjang kj where kj.Kd_jenjang = '" + kode + "'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        JenjangStudi o = null;
        for (Map m : rows) {
            o = new JenjangStudi();
            o.setKode(m.get("kode").toString());
            o.setNama(m.get("nama").toString());
            o.setNamaEng(m.get("namaEng").toString());
        }
        return o;
    }

    public List<JenjangStudi> getsByName(String nama) throws Exception {
        List<JenjangStudi> list = new ArrayList<JenjangStudi>();
        String sql = "Select kj.Kd_jenjang as kode,kj.Nm_jenjang as nama,kj.jenjang_ing as namaEng from kamus.jenjang kj WHERE kj.Nm_jenjang LIKE '%" + nama + "%'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            JenjangStudi o = new JenjangStudi();
            o.setKode(m.get("kode").toString());
            o.setNama(m.get("nama").toString());
            o.setNamaEng(m.get("namaEng").toString());
            list.add(o);
        }
        return list;
    }
}
