/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pendapatan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class PendapatanDAOImpl implements PendapatanDAO {

    public PendapatanDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<PendapatanRencana> getAllRencana(String tahun) {
        String sql = "SELECT anggaranpendapatankemahasiswaan.idPosAnggaranPendapatanUnit AS idPosAnggaranPendapatanUnit, "
                + "  pospendapatan.posPendapatan AS jenisPendapatan, "
                + "  SUM(anggaranpendapatankemahasiswaan.tarif) "
                + "  *SUM(anggaranpendapatankemahasiswaan.prediksiJmlMhsBaru)AS rencana, "
                + "  posanggaranpendapatanunit.tahunAnggaran AS tahun "
                + "  FROM (ppmk.posPendapatan pospendapatan "
                + "            INNER JOIN ppmk.posAnggaranPendapatanUnit posanggaranpendapatanunit "
                + "            ON (pospendapatan.idPosPendapatan = "
                + "                posanggaranpendapatanunit.idPosPendapatan)) "
                + "  INNER JOIN ppmk.anggaranPendapatanKemahasiswaan anggaranpendapatankemahasiswaan "
                + "  ON (anggaranpendapatankemahasiswaan.idPosAnggaranPendapatanUnit = "
                + "      posanggaranpendapatanunit.idPosAnggaranPendapatanUnit) "
                + "  WHERE posanggaranpendapatanunit.tahunAnggaran='" + tahun + "' "
                + "  GROUP BY anggaranpendapatankemahasiswaan.idPosAnggaranPendapatanUnit "
                + "  UNION ALL "
                + "  SELECT anggaranpendapatanujianakhirkkn.idPosAnggaranPendapatanUnit AS idPosAnggaranPendapatanUnit, "
                + "           pospendapatan.posPendapatan AS jenisPendapatan, "
                + "        SUM(anggaranpendapatanujianakhirkkn.prediksiJmlMhsYgUjian)* "
                + "     SUM(anggaranpendapatanujianakhirkkn.tarif)AS rencana, "
                + "     posanggaranpendapatanunit.tahunAnggaran AS Tahun "
                + "  FROM (ppmk.anggaranPendapatanUjianAkhirKKN anggaranpendapatanujianakhirkkn "
                + "     INNER JOIN ppmk.posAnggaranPendapatanUnit posanggaranpendapatanunit "
                + "     ON (anggaranpendapatanujianakhirkkn.idPosAnggaranPendapatanUnit = "
                + "         posanggaranpendapatanunit.idPosAnggaranPendapatanUnit)) "
                + "  INNER JOIN ppmk.posPendapatan pospendapatan "
                + "  ON (pospendapatan.idPosPendapatan = "
                + "      posanggaranpendapatanunit.idPosPendapatan) "
                + "  WHERE  posanggaranpendapatanunit.tahunAnggaran='" + tahun + "' "
                + "  GROUP BY anggaranpendapatanujianakhirkkn.idPosAnggaranPendapatanUnit "
                + "  UNION ALL "
                + "  SELECT anggaranpendapatanunituktsks.idPosAnggaranPendapatanUnit AS idPosAnggaranPendapatanUnit, "
                + "        pospendapatan.posPendapatan AS jenisPendapatan, "
                + "     SUM(anggaranpendapatanunituktsks.jmlSKS)*SUM(anggaranpendapatanunituktsks.tarif) AS rencana, "
                + "  posanggaranpendapatanunit.tahunAnggaran AS Tahun "
                + "  FROM (ppmk.posAnggaranPendapatanUnit posanggaranpendapatanunit "
                + "     INNER JOIN ppmk.posPendapatan pospendapatan "
                + "     ON (posanggaranpendapatanunit.idPosPendapatan = "
                + "         pospendapatan.idPosPendapatan)) "
                + "  INNER JOIN ppmk.anggaranPendapatanUnitUKTSKS anggaranpendapatanunituktsks "
                + "  ON (anggaranpendapatanunituktsks.idPosAnggaranPendapatanUnit = "
                + "      posanggaranpendapatanunit.idPosAnggaranPendapatanUnit) "
                + "  WHERE posanggaranpendapatanunit.tahunAnggaran='" + tahun + "' "
                + "  GROUP BY anggaranpendapatanunituktsks.idPosAnggaranPendapatanUnit";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);

        List<PendapatanRencana> result = new ArrayList<PendapatanRencana>();

        for (Map m : rows) {

            PendapatanRencana pr = new PendapatanRencana();
            pr.setIdPosAnggaranPendapatanUnit(ClassAntiNull.AntiNullString(m.get("idPosAnggaranPendapatanUnit")));
            pr.setJenisPendapatan(ClassAntiNull.AntiNullString(m.get("jenisPendapatan")));
            pr.setRencana(ClassAntiNull.AntiNullDouble(m.get("rencana")));
            pr.setTahun(ClassAntiNull.AntiNullString(m.get("tahun")));
            result.add(pr);
        }

        return result;
    }

    public List<PendapatanRealisasi> getAllRealisasi(String kode, String tahun) {
        String kodeTahun = kode + tahun;
        String sql = "SELECT tg" + kodeTahun + "2.kd_tagih as kodeTagih, "
                + "          tagihan.nama_tagih as namaTagih,"
                + "          SUM(tg" + kodeTahun + "2.jumlah) as realisasi,"
                + "          tg" + kodeTahun + "2.tgl_bayar as tanggalBayar"
                + "     FROM db_" + kode + ".tg" + kodeTahun + "2 tg" + kodeTahun + "2 INNER JOIN kamus.tagihan tagihan"
                + "             ON (tg" + kodeTahun + "2.kd_tagih = tagihan.Kd_tagih)"
                + "    WHERE (tg" + kodeTahun + "2.kd_tagih != tg" + kodeTahun + "2.tgl_bayar)"
                + "          AND(tg" + kodeTahun + "2.tgl_bayar IS NOT NULL)"
                + " GROUP BY tg" + kodeTahun + "2.kd_tagih"
                + " UNION ALL"
                + " SELECT tg" + kodeTahun + "1.kd_tagih as kodeTagih,"
                + "          tagihan.nama_tagih as namaTagih,"
                + "          SUM(tg" + kodeTahun + "1.jumlah) as realisasi,"
                + "          tg" + kodeTahun + "1.tgl_bayar as tanggalBayar"
                + " FROM db_" + kode + ".tg" + kodeTahun + "1 tg" + kodeTahun + "1 INNER JOIN kamus.tagihan tagihan"
                + "        ON (tg" + kodeTahun + "1.kd_tagih = tagihan.Kd_tagih)"
                + " WHERE (tg" + kodeTahun + "1.kd_tagih != tg" + kodeTahun + "1.tgl_bayar)"
                + "        AND(tg" + kodeTahun + "1.tgl_bayar IS NOT NULL)"
                + " GROUP BY tg" + kodeTahun + "1.kd_tagih";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);

        List<PendapatanRealisasi> result = new ArrayList<PendapatanRealisasi>();

        for (Map m : rows) {
            PendapatanRealisasi pr = new PendapatanRealisasi();
            pr.setKodeTagih(ClassAntiNull.AntiNullString(m.get("kodeTagih")));
            pr.setNamaTagih(ClassAntiNull.AntiNullString(m.get("namaTagih")));
            pr.setRealisasi(ClassAntiNull.AntiNullDouble(m.get("realisasi")));
            pr.setTanggalBayar(ClassAntiNull.AntiNullDate(m.get("tanggalBayar")));
            result.add(pr);
        }

        return result;

    }
}
