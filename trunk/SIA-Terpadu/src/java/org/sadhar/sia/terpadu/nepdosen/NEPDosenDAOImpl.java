/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.nepdosen;

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
public class NEPDosenDAOImpl implements NEPDosenDAO {

    public NEPDosenDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<NEPDosen> getByKodeUnit(String kodeUnit) throws Exception {
       String tahun = "2008";
      //   String tahun = Calendar.getInstance().get(Calendar.YEAR) + "";
        String sql = "SELECT resume_kategori.npp as npp, "
                + "    pegawai.Nama_peg as namaPegawai, "
                + "     unit_peg.Kd_unit as kodeUnit, "
                + "    unkerja.Nama_unit_kerja as namaUnit, "
                + "    AVG(resume_kategori.skor) AS rataRata, "
                + "    resume_kategori.ta AS tahunAjaran "
                + "  FROM ((kamus.unkerja unkerja "
                + "        INNER JOIN kamus.unit_peg unit_peg "
                + "         ON (unkerja.Kd_unit_kerja = unit_peg.Kd_unit)) "
                + "   INNER JOIN personalia.pegawai pegawai "
                + "       ON (pegawai.NPP = unit_peg.NPP)) "
                + "    INNER JOIN evaluasi.resume_kategori resume_kategori "
                + "     ON (resume_kategori.npp = pegawai.NPP) "
                + "    WHERE resume_kategori.ta LIKE '"+tahun+"%' AND unit_peg.Kd_unit LIKE '%" + kodeUnit + "%' "
                + "  GROUP BY resume_kategori.npp";
        List<NEPDosen> list = new ArrayList<NEPDosen>();
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            NEPDosen nepdosen = new NEPDosen();
            nepdosen.setKodeUnit(ClassAntiNull.AntiNullString(m.get("kodeUnit")));
            nepdosen.setNamaPegawai(ClassAntiNull.AntiNullString(m.get("namaPegawai")));
            nepdosen.setNamaUnit(ClassAntiNull.AntiNullString(m.get("namaUnit")));
            nepdosen.setKodeUnit(ClassAntiNull.AntiNullString(m.get("kodeUnit")));
            nepdosen.setRataRata(ClassAntiNull.AntiNullDouble(m.get("rataRata")));
            nepdosen.setTahunAjaran(tahun);
            list.add(nepdosen);
        }
        return list;
    }

    public List<NEPDosen> getAll() throws Exception {
       String tahun = "2008";
       //  String tahun = Calendar.getInstance().get(Calendar.YEAR) + "";
        String sql = "SELECT resume_kategori.npp as npp, "
                + "    pegawai.Nama_peg as namaPegawai, "
                + "     unit_peg.Kd_unit as kodeUnit, "
                + "    unkerja.Nama_unit_kerja as namaUnit, "
                + "    AVG(resume_kategori.skor) AS rataRata, "
                + "    resume_kategori.ta AS tahunAjaran "
                + "  FROM ((kamus.unkerja unkerja "
                + "        INNER JOIN kamus.unit_peg unit_peg "
                + "         ON (unkerja.Kd_unit_kerja = unit_peg.Kd_unit)) "
                + "   INNER JOIN personalia.pegawai pegawai "
                + "       ON (pegawai.NPP = unit_peg.NPP)) "
                + "    INNER JOIN evaluasi.resume_kategori resume_kategori "
                + "     ON (resume_kategori.npp = pegawai.NPP) "
                + "    WHERE resume_kategori.ta LIKE '"+tahun+"%' "
                + "  GROUP BY resume_kategori.npp";
        List<NEPDosen> list = new ArrayList<NEPDosen>();
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            NEPDosen nepdosen = new NEPDosen();
            nepdosen.setKodeUnit(ClassAntiNull.AntiNullString(m.get("kodeUnit")));
            nepdosen.setNamaPegawai(ClassAntiNull.AntiNullString(m.get("namaPegawai")));
            nepdosen.setNamaUnit(ClassAntiNull.AntiNullString(m.get("namaUnit")));
            nepdosen.setKodeUnit(ClassAntiNull.AntiNullString(m.get("kodeUnit")));
            nepdosen.setRataRata(ClassAntiNull.AntiNullDouble(m.get("rataRata")));
            nepdosen.setTahunAjaran(tahun);
            list.add(nepdosen);
        }
        return list;
    }
}
