/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.dosen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

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
        String sql = "SELECT kdPegawai,CONCAT(Gelar_depan,Nama_peg,Gelar_blk) as nama FROM personalia.pegawai AND AdmEdu=2";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            Dosen dosen = new Dosen();
            dosen.setKdPegawai(m.get("kdPegawai").toString());
            dosen.setNama(m.get("nama").toString());
            list.add(dosen);
        }
        return list;
    }

    public Dosen get(String kdPegawai) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Dosen> getsByName(String nama) throws Exception {
        List<Dosen> list = new ArrayList<Dosen>();
        String sql = "SELECT kdPegawai,Gelar_depan,Nama_peg,Gelar_blk FROM personalia.pegawai WHERE Nama_peg LIKE '%" + nama + "%' AND AdmEdu=2";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            Dosen dosen = new Dosen();
            dosen.setKdPegawai(m.get("kdPegawai").toString());
            dosen.setNama(m.get("nama").toString());
            list.add(dosen);
        }
        return list;
    }

    public List<Dosen> getsByName(String nama, ProgramStudi prodi) throws Exception {
        List<Dosen> list = new ArrayList<Dosen>();
        //String sql = "SELECT kdPegawai,Gelar_depan,Nama_peg,Gelar_blk FROM personalia.pegawai WHERE Nama_peg LIKE '%"+nama+"%' AND AdmEdu=2";
        String sql = "SELECT pegawai.kdPegawai,pegawai.Nama_peg, "
                + "pegawai.Gelar_depan,"
                + "pegawai.Gelar_blk,"
                + "unkerja.Nama_unit_kerja,"
                + "unkerja.Kd_unit_kerja "
                + "FROM (personalia.pegawai pegawai "
                + "INNER JOIN personalia.unit_peg unit_peg "
                + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                + "INNER JOIN kamus.unkerja unkerja "
                + "ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit) "
                + "WHERE (pegawai.Nama_peg LIKE '%" + nama + "%') AND (SUBSTRING(unkerja.Kd_unit_kerja,4,4)='" + prodi.getKode() + "') "
                + "AND (pegawai.AdmEdu = '2')";
        System.out.println(sql);
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            Dosen dosen = new Dosen();
            dosen.setKdPegawai(m.get("kdPegawai").toString());
            String temp = ClassAntiNull.AntiNullString(m.get("Gelar_depan")) + " " + ClassAntiNull.AntiNullString(m.get("Nama_peg")) + " "
                    + ClassAntiNull.AntiNullString(m.get("Gelar_blk"));
            dosen.setNama(temp);
            list.add(dosen);
        }
        return list;
    }

    public List<Dosen> getsByProdi(ProgramStudi prodi) throws Exception {
        List<Dosen> list = new ArrayList<Dosen>();
        String sql = "SELECT DISTINCT pegawai.kdPegawai, pegawai.Nama_peg, "
                + "pegawai.Gelar_depan,"
                + "pegawai.Gelar_blk,"
                + "unkerja.Nama_unit_kerja,"
                + "unkerja.Kd_unit_kerja "
                + "FROM (personalia.pegawai pegawai "
                + "INNER JOIN personalia.unit_peg unit_peg "
                + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                + "INNER JOIN kamus.unkerja unkerja "
                + "ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit) "
                + "WHERE (SUBSTRING(unkerja.Kd_unit_kerja,4,4)='" + prodi.getKode() + "') "
                + "AND (pegawai.AdmEdu = '2')";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        
        for (Map m : rows) {
            Dosen dosen = new Dosen();
            dosen.setKdPegawai(m.get("kdPegawai").toString());
            String temp = ClassAntiNull.AntiNullString(m.get("Gelar_depan")) + " " + ClassAntiNull.AntiNullString(m.get("Nama_peg")) + " "
                    + ClassAntiNull.AntiNullString(m.get("Gelar_blk"));
            dosen.setNama(temp);
            list.add(dosen);
        }
        return list;
    }
}
