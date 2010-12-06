/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.cutinonakademis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class CutiNonAkademisDAOImpl implements CutiNonAkademisDAO {

    public CutiNonAkademisDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<CutiNonAkademis> gets(String kodeUnitKerja) throws Exception {
        String whereUnitKerja = "";
        if (kodeUnitKerja.isEmpty()) {
            whereUnitKerja = "";
        } else {
            whereUnitKerja = "AND kamus.unkerja.`kd_unit_kerja` = " + kodeUnitKerja;

        }

        Calendar cal = Calendar.getInstance();
        String tahun = cal.get(Calendar.YEAR) + "";

//        String tahun = "2008";
//        String sql = "SELECT "
//                + " cuti" + tahun + ".kdPegawai AS kodePegawai,  "
//                + " pegawai.Nama_peg AS namaPegawai,  "
//                + " kamus.unkerja.nama_unit_kerja AS unitKerja, "
//                + " SUM(kamus.jns_cuti.jatah_cuti - `cuti" + tahun + "`.`lama_cuti`) AS sisaCuti, "
//                + " unit_peg.kd_unit, "
//                + " `cuti" + tahun + "`.`lama_cuti`, "
//                + " kamus.jns_cuti.nama_cuti "
//                + " FROM "
//                + " personalia.cuti" + tahun + " "
//                + " LEFT OUTER JOIN personalia.pegawai ON (cuti" + tahun + ".kdPegawai = pegawai.kdPegawai) "
//                + " LEFT OUTER JOIN personalia.unit_peg ON (cuti" + tahun + ".kdPegawai = unit_peg.kdPegawai) "
//                + " INNER JOIN kamus.jns_cuti ON (`cuti" + tahun + "`.`Jenis_cuti`=kamus.jns_cuti.Kd_cuti) "
//                + " INNER JOIN kamus.unkerja ON (`unit_peg`.`kd_unit`=kamus.unkerja.`kd_unit_kerja`) "
//                + " WHERE "
//                + " `pegawai`.`AdmEdu`LIKE 1 AND `cuti" + tahun + "`.`Jenis_cuti` LIKE 1 " + whereUnitKerja
//                + " GROUP BY "
//                + " cuti" + tahun + ".kdPegawai";
//
        String sql =  "SELECT cutipegawai.NPP AS kodePegawai,"
         + " pegawai.Nama_peg AS namaPegawai,"
         + " jns_cuti.Nama_cuti AS nama_cuti,"
         + " kamus.unkerja.nama_unit_kerja AS unitKerja,"
         + " jns_cuti.jatah_cuti - SUM(cutipegawai.lama_cuti) AS sisaCuti,"
          + " unit_peg.kd_unit,"
         + " SUM(cutipegawai.lama_cuti) AS lama_cuti"
           + " FROM personalia.cutiPegawai cutipegawai "
         + " INNER JOIN personalia.pegawai pegawai ON (cutipegawai.NPP = pegawai.NPP)"
         + " INNER JOIN kamus.jns_cuti jns_cuti  ON (jns_cuti.Kd_cuti = cutipegawai.Jenis_cuti)"
         + " INNER JOIN personalia.unit_peg ON (cutipegawai.NPP = unit_peg.NPP)"
         + " INNER JOIN kamus.unkerja  ON (unit_peg.kd_unit = kamus.unkerja.kd_unit_kerja)"
         + " WHERE YEAR(cutipegawai.Tgl_Akhir_cuti) LIKE "+tahun+"" + whereUnitKerja
         + " AND cutipegawai.Jenis_cuti LIKE 1 "
         + " AND pegawai.AdmEdu LIKE 1 "
            + " GROUP BY pegawai.Nama_peg";
      

        List<CutiNonAkademis> data = new ArrayList<CutiNonAkademis>();
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            CutiNonAkademis o = new CutiNonAkademis();
            o.setKodePegawai(ClassAntiNull.AntiNullString(m.get("kodePegawai")));
            o.setNamaPegawai(ClassAntiNull.AntiNullString(m.get("namaPegawai")));
            o.setUnitKerja(ClassAntiNull.AntiNullString(m.get("unitKerja")));
            o.setSisaCuti(ClassAntiNull.AntiNullInt(m.get("sisaCuti")));
            data.add(o);
        }

        return data;
    }
}
