/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.dosen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author Hendro Steven
 */
public class DosenDAOImpl implements DosenDAO {

    public DosenDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Dosen> gets() throws Exception {
        List<Dosen> list = new ArrayList<Dosen>();
        String sql = "SELECT kdPegawai,Nama_peg FROM personalia.pegawai";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for(Map m :rows){
            Dosen dosen = new Dosen();
            dosen.setKdPegawai(m.get("kdPegawai").toString());
            dosen.setNama(m.get("Nama_peg").toString());
            list.add(dosen);
        }
        return list;
    }

    public Dosen get(String kdPegawai) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Dosen> getsByName(String nama) throws Exception {
        List<Dosen> list = new ArrayList<Dosen>();
        String sql = "SELECT kdPegawai,Nama_peg FROM personalia.pegawai WHERE Nama_peg LIKE '%"+nama+"%'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for(Map m :rows){
            Dosen dosen = new Dosen();
            dosen.setKdPegawai(m.get("kdPegawai").toString());
            dosen.setNama(m.get("Nama_peg").toString());
            list.add(dosen);
        }
        return list;
    }
}
