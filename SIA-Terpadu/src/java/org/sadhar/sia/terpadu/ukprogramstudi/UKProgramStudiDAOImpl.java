/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ukprogramstudi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class UKProgramStudiDAOImpl implements UKProgramStudiDAO {

    public UKProgramStudiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<UKProgramStudi> gets() throws Exception {
        List<UKProgramStudi> list = new ArrayList<UKProgramStudi>();
        String sql = "SELECT SUBSTRING(ku.Kd_unit_kerja,4,4) as shortKode, ku.Kd_unit_kerja as kodeUnitKerja,ku.Nama_unit_kerja as nama from kamus.unkerja ku where ku.Nama_unit_kerja like 'PROGRAM STUDI%'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            UKProgramStudi o = new UKProgramStudi();
            o.setShortKode(m.get("shortKode").toString());
            o.setKodeUnitKerja(m.get("kodeUnitKerja").toString());
            o.setNama(m.get("nama").toString());
            list.add(o);
        }
        return list;
    }

    public UKProgramStudi get(String kodeUnitKerja) throws Exception {
        String sql = "SELECT SUBSTRING(ku.Kd_unit_kerja,4,4) as shortKode, ku.Kd_unit_kerja as kodeUnitKerja,ku.Nama_unit_kerja as nama from kamus.unkerja ku where ku.Nama_unit_kerja like 'PROGRAM STUDI%' and ku.Kd_unit_kerja = '" + kodeUnitKerja + "'";

        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        UKProgramStudi o=null;
        for (Map m : rows) {
            o = new UKProgramStudi();
            o.setShortKode(m.get("shortKode").toString());
            o.setKodeUnitKerja(m.get("kodeUnitKerja").toString());
            o.setNama(m.get("nama").toString());
        }
        return o;
    }

    public List<UKProgramStudi> getsByName(String nama) throws Exception {
         List<UKProgramStudi> list = new ArrayList<UKProgramStudi>();
        String sql = "SELECT SUBSTRING(ku.Kd_unit_kerja,4,4) as shortKode, ku.Kd_unit_kerja as kodeUnitKerja,ku.Nama_unit_kerja as nama from kamus.unkerja ku where ku.Nama_unit_kerja like 'PROGRAM STUDI%' and ku.Nama_unit_kerja like '%"+nama+"%'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            UKProgramStudi o = new UKProgramStudi();
            o.setShortKode(m.get("shortKode").toString());
            o.setKodeUnitKerja(m.get("kodeUnitKerja").toString());
            o.setNama(m.get("nama").toString());
            list.add(o);
        }
        return list;
    }
}
