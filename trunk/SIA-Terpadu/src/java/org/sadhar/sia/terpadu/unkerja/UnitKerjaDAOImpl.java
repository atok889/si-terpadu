/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.unkerja;

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
public class UnitKerjaDAOImpl implements UnitKerjaDAO{

    public UnitKerja get(String kode) throws Exception {
        String sql = "SELECT Kd_unit_kerja,Nama_unit_kerja FROM kamus.unkerja " +
                "WHERE Kd_unit_kerja=?";
        return (UnitKerja) ClassConnection.getJdbc().queryForObject(sql, new Object[]{kode}, new RowMapper(){

            public Object mapRow(ResultSet rs, int i) throws SQLException {
                UnitKerja unKerja = new UnitKerja();
                unKerja.setKode(rs.getString("Kd_unit_kerja"));
                unKerja.setNama(rs.getString("Nama_unit_kerja"));
                return unKerja;
            }

        });
    }

    public List<UnitKerja> gets() throws Exception {
        List<UnitKerja> units = new ArrayList<UnitKerja>();
        String sql = "SELECT Kd_unit_kerja,Nama_unit_kerja FROM kamus.unkerja ORDER BY " +
                "Kd_unit_kerja";
        List<Map> rows  = ClassConnection.getJdbc().queryForList(sql);
        for(Map m:rows){
            UnitKerja uk = new UnitKerja();
            uk.setKode(m.get("Kd_unit_kerja").toString());
            uk.setNama(m.get("Nama_unit_kerja").toString());
            units.add(uk);
        }
        return units;
    }

}
