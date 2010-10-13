/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahdosen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author Hendro Steven
 */
public class JumlahDosenDAOImpl implements JumlahDosenDAO {

    public List<JumlahFakultas> getJumlahDosen() throws Exception {
        List<JumlahFakultas> jmlDosen = new ArrayList<JumlahFakultas>();
        String sql1 = "SELECT SUBSTRING(unkerja.Kd_unit_kerja,1,4) AS kodeunit, "
                + "unkerja.Nama_unit_kerja AS unit,"
                + "COUNT(DISTINCT pegawai.kdPegawai) AS jumlah "
                + "FROM (kamus.unkerja unkerja "
                + "LEFT JOIN personalia.unit_peg unit_peg "
                + "ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit)) "
                + "LEFT JOIN personalia.pegawai pegawai "
                + "ON (pegawai.kdPegawai = unit_peg.kdPegawai) "
                + "WHERE (pegawai.AdmEdu = '2') AND unkerja.Nama_unit_kerja LIKE '%FAKULTAS%' "
                + "GROUP BY unkerja.Kd_unit_kerja "
                + "ORDER BY unkerja.Kd_unit_kerja ASC";
        List<Map> rows1 = ClassConnection.getJdbc().queryForList(sql1);
        for (Map m1 : rows1) {
            JumlahFakultas jf = new JumlahFakultas();
            jf.setNama(m1.get("unit").toString());
            int total = Integer.valueOf(m1.get("jumlah").toString());
            String sql2 = "SELECT unkerja.Kd_unit_kerja AS kode, "
                    + "unkerja.Nama_unit_kerja AS unit, "
                    + "COUNT(DISTINCT pegawai.kdPegawai) AS jumlah "
                    + "FROM (kamus.unkerja unkerja "
                    + "LEFT JOIN personalia.unit_peg unit_peg "
                    + "ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit)) "
                    + "LEFT JOIN personalia.pegawai pegawai "
                    + "ON (pegawai.kdPegawai = unit_peg.kdPegawai) "
                    + "WHERE (pegawai.AdmEdu = '2') AND (SUBSTRING(unkerja.Kd_unit_kerja,1,4)='" + m1.get("kodeunit").toString() + "') "
                    + "AND (SUBSTRING(unkerja.Kd_unit_kerja,5,4)<>'0000') "
                    + "GROUP BY unkerja.Kd_unit_kerja ORDER BY unkerja.Kd_unit_kerja ASC";
            List<Map> rows2 = ClassConnection.getJdbc().queryForList(sql2);
            for (Map m2 : rows2) {
                JumlahProdi jp = new JumlahProdi();
                jp.setNama(m2.get("unit").toString());
                jp.setJumlah(Integer.valueOf(m2.get("jumlah").toString()));
                total += jp.getJumlah();
                jf.getProdis().add(jp);
            }
            jf.setJumlah(total);
            jmlDosen.add(jf);
        }
        return jmlDosen;
    }
}
