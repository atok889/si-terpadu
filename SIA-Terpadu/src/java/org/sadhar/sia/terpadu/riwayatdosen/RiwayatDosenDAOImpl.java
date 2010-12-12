/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.riwayatdosen;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class RiwayatDosenDAOImpl implements RiwayatDosenDAO {

    static {
        String sql = "CREATE OR REPLACE VIEW tempo.riwayatDosen(riwayat,kodePegawai,namaPegawai,tahun,keterangan) AS "
                + "  SELECT 'STUDI LANJUT' AS riwayat, "
                + "          studi.kdPegawai AS kodePegawai, "
                + "         LTRIM(CONCAT(pegawai.Gelar_depan,' ',pegawai.Nama_peg,' ',pegawai.Gelar_blk)) AS namaPegawai, "
                + "        IF(YEAR(studi.Tgl_selesai_studi) <=> NULL,'',YEAR(studi.Tgl_selesai_studi)) AS tahun , "
                + "       CONCAT(IF(jenjang.Nm_jenjang<=>NULL,' ',jenjang.Nm_jenjang),' ',IF(studi.Univ<=>NULL,' ',studi.Univ)) as keterangan "
                + "  FROM (kamus.jenjang jenjang "
                + "       INNER JOIN personalia.studi studi "
                + "       ON (jenjang.Kd_jenjang = studi.Jenjang)) "
                + "     INNER JOIN personalia.pegawai pegawai "
                + "        ON (studi.kdPegawai = pegawai.kdPegawai) "
                + "  WHERE pegawai.Status_keluar='1' OR pegawai.Status_keluar='6' OR  pegawai.Status_keluar='7' "
                + "  AND studi.Tgl_selesai_studi AND pegawai.AdmEdu='2' "
                + "  UNION ALL "
                + "  SELECT 'PENDIDIKAN' AS riwayat, "
                + "      pendidikan.kdPegawai AS kodePegawai, "
                + "   LTRIM(CONCAT(pegawai.Gelar_depan,' ',pegawai.Nama_peg,' ',pegawai.Gelar_blk)) AS namaPegawai, "
                + "  YEAR(pendidikan.Tgl_ijasah) as tahun, "
                + "  CONCAT(jenjang.Nm_jenjang,'  ', "
                + "  pendidikan.Prodi) as keterangan "
                + "  FROM (personalia.pendidikan pendidikan "
                + "  INNER JOIN kamus.jenjang jenjang "
                + "             ON (pendidikan.Jenjang = jenjang.Kd_jenjang)) "
                + "         INNER JOIN personalia.pegawai pegawai "
                + "            ON (pendidikan.kdPegawai = pegawai.kdPegawai) "
                + "  WHERE  pegawai.Status_keluar='1' OR pegawai.Status_keluar='6' OR  pegawai.Status_keluar='7' AND pegawai.AdmEdu='2' "
                + "  UNION ALL "
                + "  SELECT 'JABATAN' as riwayat, "
                + "          pejabat.kdPegawai AS kodePegawai, "
                + "         LTRIM(CONCAT(pegawai.Gelar_depan,' ',pegawai.Nama_peg,' ',pegawai.Gelar_blk)) AS namaPegawai, "
                + "         YEAR(pejabat.tgl_sk_angkat_jabat)as tahun , "
                + "         IF(jabatan.Nama_jab<=>NULL,'',jabatan.Nama_jab)as keterangan "
                + "  FROM (personalia.pejabat pejabat "
                + "          INNER JOIN kamus.jabatan jabatan "
                + "             ON (pejabat.kode_jab = jabatan.Kd_jab)) "
                + "         INNER JOIN personalia.pegawai pegawai "
                + "            ON (pejabat.kdPegawai = pegawai.kdPegawai) "
                + "  WHERE pegawai.Status_keluar='1' OR pegawai.Status_keluar='6' OR  pegawai.Status_keluar='7' "
                + "      AND YEAR(pejabat.tgl_sk_angkat_jabat) AND pegawai.AdmEdu='2'";

        try {
            ClassConnection.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public RiwayatDosenDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<DataDosen> getDataDosenByUnitKerja(String unitKerja) {
        String sql = "SELECT riwayatDosen.kodePegawai as kode,"
                + "  riwayatDosen.namaPegawai as nama,"
                + "  IF(MAX(unitPegawai.tgl_sk_unit)<=>NULL,'',MAX(unitPegawai.tgl_sk_unit))as tahun_sk,"
                + "  unkerja.Nama_unit_kerja"
                + "  FROM tempo.riwayatDosen riwayatDosen"
                + "  INNER JOIN personalia.pegawai pegawai ON (riwayatDosen.kodePegawai= pegawai.kdPegawai)"
                + "  INNER JOIN personalia.unit_peg unitPegawai ON (pegawai.kdPegawai=unitPegawai.kdPegawai)"
                + "  INNER JOIN kamus.unkerja unkerja ON (unitPegawai.kd_unit=unkerja.kd_unit_kerja)"
                + "  WHERE unkerja.Kd_unit_kerja='" + unitKerja + "'"
                + "  GROUP BY riwayatDosen.kodePegawai";

        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);

        List<DataDosen> data = new ArrayList<DataDosen>();
        for (Map m : rows) {
            DataDosen dd = new DataDosen();
            dd.setKode(ClassAntiNull.AntiNullString(m.get("kode")));
            dd.setNama(ClassAntiNull.AntiNullString(m.get("nama")));
            data.add(dd);
        }

        return data;
    }

    public List<RiwayatDosen> getRiwayatDosenByKodeDosen(String kodePegawai, String unitKerja) {
        String sql = "SELECT riwayatDosen.riwayat as riwayat, "
                + "   riwayatDosen.namaPegawai as nama, "
                + "      CAST(riwayatDosen.tahun AS CHAR) as tahun, "
                + "      riwayatDosen.keterangan as keterangan, "
                + "      pegawai.Alamat as alamat"
                + "  FROM tempo.riwayatDosen riwayatDosen "
                + "  INNER JOIN personalia.pegawai pegawai ON (riwayatDosen.kodePegawai= pegawai.kdPegawai) "
                + "  INNER JOIN personalia.unit_peg unitPegawai ON (pegawai.kdPegawai=unitPegawai.kdPegawai) "
                + "  INNER JOIN kamus.unkerja unkerja ON (unitPegawai.kd_unit=unkerja.kd_unit_kerja) "
                + "  WHERE unkerja.Kd_unit_kerja='" + unitKerja + "' AND riwayatDosen.kodePegawai='" + kodePegawai + "'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);

        List<RiwayatDosen> data = new ArrayList<RiwayatDosen>();
        for (Map m : rows) {
            RiwayatDosen rd  = new RiwayatDosen();
            rd.setRiwayat(ClassAntiNull.AntiNullString(m.get("riwayat")));
            rd.setAlamat(ClassAntiNull.AntiNullString(m.get("alamat")));
            rd.setKeterangan(ClassAntiNull.AntiNullString(m.get("keterangan")));
            rd.setNama(ClassAntiNull.AntiNullString(m.get("nama")));
            rd.setTahun(""+m.get("tahun"));
            data.add(rd);
        }
        return data;
    }
}
