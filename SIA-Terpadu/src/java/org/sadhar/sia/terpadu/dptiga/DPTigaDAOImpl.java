/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.dptiga;

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
public class DPTigaDAOImpl implements DPTigaDAO {

    public DPTigaDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<DPTiga> getByKodeUnit(String kodeUnit) throws Exception {
    String tahun = "2008";
        //String tahun = Calendar.getInstance().get(Calendar.YEAR) + "";
        String sql = "SELECT timPenilaiDP3.kdPegawaiYgDinilai as kodePegawai, "
                + "   pegawai.Nama_peg as namaPegawai, "
                + "   SUM(nilaiSubKomponenPegawai.Nilai)/count(nilaiSubKomponenPegawai.Nilai)as nilaiDP3, "
                + "  unit_peg.kd_unit as kodeUnit, "
                + "    unkerja.Nama_unit_kerja as namaUnit, "
                + "      timPenilaiDP3.tahunPenilaian as tahunPenilaian "
                + "   FROM (((personalia.pegawai pegawai "
                + "  INNER JOIN personalia.unit_peg unit_peg "
                + "   ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                + " INNER JOIN personalia.timPenilaiDP3 timPenilaiDP3 "
                + "     ON (timPenilaiDP3.kdPegawaiYgDinilai = pegawai.kdPegawai)) "
                + "   INNER JOIN personalia.nilaiSubKomponenPegawai nilaiSubKomponenPegawai "
                + "        ON (timPenilaiDP3.idTim = nilaiSubKomponenPegawai.idTim)) "
                + "      INNER JOIN kamus.unkerja unkerja "
                + "           ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit) "
                + " WHERE unit_peg.kd_unit LIKE '%" + kodeUnit + "%' and timPenilaiDP3.tahunPenilaian = " + tahun;
        List<DPTiga> list = new ArrayList<DPTiga>();
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            DPTiga dptiga = new DPTiga();
            dptiga.setKodeUnit(ClassAntiNull.AntiNullString(m.get("kodeUnit")));
            dptiga.setKodePegawai(ClassAntiNull.AntiNullString(m.get("kodePegawai")));
            dptiga.setNamaPegawai(ClassAntiNull.AntiNullString(m.get("namaPegawai")));
            dptiga.setNamaUnit(ClassAntiNull.AntiNullString(m.get("namaUnit")));
            dptiga.setNilaiDP3(ClassAntiNull.AntiNullDouble(m.get("nilaiDP3")));
            dptiga.setTahunPenilaian(tahun);
            list.add(dptiga);
        }
        return list;
    }

    public List<DPTiga> getAll() throws Exception {
              String tahun = "2008";
        //String tahun = Calendar.getInstance().get(Calendar.YEAR) + "";
        String sql = "SELECT timPenilaiDP3.kdPegawaiYgDinilai as kodePegawai, "
                + "   pegawai.Nama_peg as namaPegawai, "
                + "   SUM(nilaiSubKomponenPegawai.Nilai)/count(nilaiSubKomponenPegawai.Nilai)as nilaiDP3, "
                + "  unit_peg.kd_unit as kodeUnit, "
                + "    unkerja.Nama_unit_kerja as namaUnit, "
                + "      timPenilaiDP3.tahunPenilaian as tahunPenilaian "
                + "   FROM (((personalia.pegawai pegawai "
                + "  INNER JOIN personalia.unit_peg unit_peg "
                + "   ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                + " INNER JOIN personalia.timPenilaiDP3 timPenilaiDP3 "
                + "     ON (timPenilaiDP3.kdPegawaiYgDinilai = pegawai.kdPegawai)) "
                + "   INNER JOIN personalia.nilaiSubKomponenPegawai nilaiSubKomponenPegawai "
                + "        ON (timPenilaiDP3.idTim = nilaiSubKomponenPegawai.idTim)) "
                + "      INNER JOIN kamus.unkerja unkerja "
                + "           ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit) "
                + " WHERE   timPenilaiDP3.tahunPenilaian = " + tahun;
        List<DPTiga> list = new ArrayList<DPTiga>();
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            DPTiga dptiga = new DPTiga();
            dptiga.setKodeUnit(ClassAntiNull.AntiNullString(m.get("kodeUnit")));
            dptiga.setKodePegawai(ClassAntiNull.AntiNullString(m.get("kodePegawai")));
            dptiga.setNamaPegawai(ClassAntiNull.AntiNullString(m.get("namaPegawai")));
            dptiga.setNamaUnit(ClassAntiNull.AntiNullString(m.get("namaUnit")));
            dptiga.setNilaiDP3(ClassAntiNull.AntiNullDouble(m.get("nilaiDP3")));
            dptiga.setTahunPenilaian(tahun);
            list.add(dptiga);
        }
        return list;
    }
}
