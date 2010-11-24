/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.reratanilaitespmb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.gelombang.Gelombang;
import org.sadhar.sia.terpadu.gelombang.GelombangDAOImpl;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;
import org.sadhar.sia.terpadu.prodi.ProgramStudiDAO;
import org.sadhar.sia.terpadu.prodi.ProgramStudiDAOImpl;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author Hendro Steven
 */
public class RerataNilaiTesPmbDAOImpl implements RerataNilaiTesPmbDAO {

    /**
     *
    SELECT SUM(nil11)/COUNT(nomor) AS pv,SUM(nil12)/COUNT(nomor) AS kn,SUM(nil13)/COUNT(nomor) AS pm,
    SUM(nil14)/COUNT(nomor) AS hr,SUM(nil15)/COUNT(nomor) AS bi,SUM(final)/COUNT(nomor) AS final FROM nf020065314
     */
    public RerataNilaiTesPmb getRerataNilaiTesPmb(String tahun, ProgramStudi prodi) throws Exception {
        RerataNilaiTesPmb hasil = null;
        List<Gelombang> gelombangs = new GelombangDAOImpl().gets("9");
        for (Gelombang gel : gelombangs) {
            if (isTableExist(tahun, prodi, gel)) {
                String sql = "SELECT SUM(nil11)/COUNT(nomor) AS pv,"
                        + "SUM(nil12)/COUNT(nomor) AS kn,"
                        + "SUM(nil13)/COUNT(nomor) AS pm,"
                        + "SUM(nil14)/COUNT(nomor) AS hr,"
                        + "SUM(nil15)/COUNT(nomor) AS bi,"
                        + "SUM(final)/COUNT(nomor) AS final "
                        + "FROM pmb.nf" + gel.getKode() + tahun + prodi.getKode() + "";
                hasil = (RerataNilaiTesPmb) ClassConnection.getJdbc().queryForObject(sql, new RowMapper() {

                    public Object mapRow(ResultSet rs, int i) throws SQLException {
                        RerataNilaiTesPmb tmp = new RerataNilaiTesPmb();                        
                        tmp.setPv(rs.getDouble(1));
                        tmp.setKn(rs.getDouble(2));
                        tmp.setPm(rs.getDouble(3));
                        tmp.setHr(rs.getDouble(4));
                        tmp.setBi(rs.getDouble(5));
                        tmp.setNilaiFinal(rs.getDouble(6));
                        return tmp;
                    }
                });
                hasil.setTahun(tahun);
                hasil.setProgdi(prodi.getNama());
            }
        }
        return hasil;
    }

    private boolean isTableExist(String tahun, ProgramStudi prodi, Gelombang gel) throws Exception {
        SqlRowSet rs = null;
        boolean result = false;
        String sql = "SHOW tables from pmb like 'nf" + gel.getKode() + tahun + prodi.getKode() + "'";
        try {
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
            result = rs.next();
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public List<RerataNilaiTesPmb> getAllRerataNilaiTesPmb(String tahun) throws Exception {
        List<RerataNilaiTesPmb> daftar = new ArrayList<RerataNilaiTesPmb>();
        ProgramStudiDAO dao = new ProgramStudiDAOImpl();
        List<ProgramStudi> prodi = dao.getProgramStudi();
        for (ProgramStudi p : prodi) {
            if (getRerataNilaiTesPmb(tahun, p) != null) {
                daftar.add(getRerataNilaiTesPmb(tahun, p));
            }
        }
        return daftar;
    }
}
