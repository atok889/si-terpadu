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
    /*
     * create view personalia.pangkatView as (select ppp.`NPP` as npp,max(cast(ppp.kd_pang as unsigned) )as kodePangkat, pp.`Nama_peg` as nama, pp.`Tgl_lahir` as tanggalLahir,ppu.`kd_unit` as kodeUnit,
    ku.`nama_unit_kerja` as namaUnit,   year(CURDATE()) - year(pp.Tgl_lahir) as umur from personalia.pangkat_peg ppp
    INNER JOIN personalia.unit_peg ppu on ppp.`NPP`=ppu.`npp`
    INNER JOIN personalia.pegawai pp on (ppp.`NPP`=pp.`NPP` and pp.AdmEdu like '2')
    INNER JOIN kamus.unkerja ku on ku.`kd_unit_kerja`=ppu.`kd_unit` group by ppp.NPP);
     */

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
