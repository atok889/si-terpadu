/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.fakultas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class FakultasDAOImpl implements FakultasDAO {

    public FakultasDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Fakultas> gets() throws Exception {
        List<Fakultas> list = new ArrayList<Fakultas>();
        String sql = "SELECT SUBSTRING_INDEX(ku.Kd_unit_kerja,'0',2) as prefix,ku.Kd_unit_kerja as kodeUnitKerja,ku.Nama_unit_kerja as nama from kamus.unkerja ku where ku.Nama_unit_kerja like 'FAKULTAS%'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            Fakultas o = new Fakultas();
            o.setKodeUnitKerja(m.get("kodeUnitKerja").toString());
            o.setPrefix(m.get("prefix").toString());
            o.setNama(m.get("nama").toString());
            list.add(o);
        }
        return list;
    }

    public Fakultas getByPrefix(String prefix) throws Exception {
        String sql = "SELECT SUBSTRING_INDEX(ku.Kd_unit_kerja,'0',2) as prefix,ku.Kd_unit_kerja as kodeUnitKerja,ku.Nama_unit_kerja as nama from kamus.unkerja ku where ku.Nama_unit_kerja like 'FAKULTAS%' and SUBSTRING_INDEX(ku.Kd_unit_kerja,'0',2)  = '" + prefix + "'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        Fakultas o = null;
        for (Map m : rows) {
            o = new Fakultas();
            o.setKodeUnitKerja(m.get("kodeUnitKerja").toString());
            o.setPrefix(m.get("prefix").toString());
            o.setNama(m.get("nama").toString());
        }
        return o;
    }

    public Fakultas get(String kodeUnitKerja) throws Exception {
        String sql = "SELECT SUBSTRING_INDEX(ku.Kd_unit_kerja,'0',2) as prefix,ku.Kd_unit_kerja as kodeUnitKerja,ku.Nama_unit_kerja as nama from kamus.unkerja ku where ku.Nama_unit_kerja like 'FAKULTAS%' and ku.Kd_unit_kerja  like '%" + kodeUnitKerja + "%'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        Fakultas o = null;
        for (Map m : rows) {
            o = new Fakultas();
            o.setKodeUnitKerja(m.get("kodeUnitKerja").toString());
            o.setPrefix(m.get("prefix").toString());
            o.setNama(m.get("nama").toString());
        }
        return o;
    }

    public List<Fakultas> getsByName(String nama) throws Exception {
        List<Fakultas> list = new ArrayList<Fakultas>();
        String sql = "SELECT SUBSTRING_INDEX(ku.Kd_unit_kerja,'0',2) as prefix,ku.Kd_unit_kerja as kodeUnitKerja,ku.Nama_unit_kerja as nama from kamus.unkerja ku where ku.Nama_unit_kerja like 'FAKULTAS%' and ku.Nama_unit_kerja like '%"+nama+"'%";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            Fakultas o = new Fakultas();
            o.setKodeUnitKerja(m.get("kodeUnitKerja").toString());
            o.setPrefix(m.get("prefix").toString());
            o.setNama(m.get("nama").toString());
            list.add(o);
        }
        return list;
    }
}
