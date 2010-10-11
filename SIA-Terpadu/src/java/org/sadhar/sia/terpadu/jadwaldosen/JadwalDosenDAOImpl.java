/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jadwaldosen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.dosen.Dosen;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author Hendro Steven
 */
public class JadwalDosenDAOImpl implements JadwalDosenDAO {

    public List<JadwalDosen> getJadwalDosen(Dosen dosen, ProgramStudi progdi, String tahun, int semester) throws Exception {
        List<JadwalDosen> jadwalDosen = new ArrayList<JadwalDosen>();
        String db = "db_" + progdi.getKode();
        String tabel = "jw" + progdi.getKode() + "" + tahun + "" + semester;
        String sql = "SELECT " + tabel + ".jam1 as jam," + tabel + ".hari1 as hari," + tabel + ".kd_mtk,pegawai.kdPegawai,pegawai.Nama_peg "
                + "FROM personalia.pegawai pegawai INNER JOIN " + db + "." + tabel + " " + tabel + " "
                + "ON (pegawai.kdPegawai = " + tabel + ".NPP) "
                + "WHERE (pegawai.kdPegawai = '" + dosen.getKdPegawai() + "') "
                + "ORDER BY " + tabel + ".jam1 ASC";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            JadwalDosen jd = new JadwalDosen();
            if (m.get("jam").toString().trim().length() > 0) {
                jd.setJam(m.get("jam").toString());
                jd.setSenin("");
                jd.setSelasa("");
                jd.setRabu("");
                jd.setKamis("");
                jd.setJumat("");
                jd.setSabtu("");
                if (m.get("hari").toString().equalsIgnoreCase("Senin")) {
                    jd.setSenin(m.get("kd_mtk").toString());
                }
                if (m.get("hari").toString().equalsIgnoreCase("Selasa")) {
                    jd.setSelasa(m.get("kd_mtk").toString());
                }
                if (m.get("hari").toString().equalsIgnoreCase("Rabu")) {
                    jd.setRabu(m.get("kd_mtk").toString());
                }
                if (m.get("hari").toString().equalsIgnoreCase("Kamis")) {
                    jd.setKamis(m.get("kd_mtk").toString());
                }
                if (m.get("hari").toString().equalsIgnoreCase("Jumat")) {
                    jd.setJumat(m.get("kd_mtk").toString());
                }
                if (m.get("hari").toString().equalsIgnoreCase("Sabtu")) {
                    jd.setSabtu(m.get("kd_mtk").toString());
                }
                jadwalDosen.add(jd);
            }
        }
        return jadwalDosen;
    }
}
