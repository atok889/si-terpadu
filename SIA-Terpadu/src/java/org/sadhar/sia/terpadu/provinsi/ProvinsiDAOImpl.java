/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.provinsi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Hendro Steven
 */
public class ProvinsiDAOImpl implements ProvinsiDAO{
 public Provinsi getProv(String kode) throws Exception {
        String sql = "SELECT kd_prop,nama_prop FROM kamus.propinsi WHERE kd_prop=?";
        return (Provinsi) ClassConnection.getJdbc().queryForObject(sql, new Object[]{kode},
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        return new Provinsi(
                                rs.getString("kd_prop"),
                                rs.getString("nama_prop"));
                    }
                });
    }
 public List<Provinsi> getProvinsi() throws Exception {
        List<Provinsi> prov = new ArrayList<Provinsi>();
        String sql = "SELECT kd_prop,nama_prop FROM kamus.propinsi ORDER BY kd_prop";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            Provinsi p = new Provinsi();
            p.setKode(m.get("kd_prop").toString());
            p.setNama(m.get("nama_prop").toString());
            prov.add(p);
        }
        return prov;
    }
}
