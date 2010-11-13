/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.laporankinerjapegawai;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class LaporanKinerjaPegawaiDAOImpl implements LaporanKinerjaPegawaiDAO {

    public LaporanKinerjaPegawaiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getUnitKerja() {
        String sql = "select ku.`Nama_unit_kerja` as nama from kamus.unkerja ku " +
                " inner join personalia.unit_peg pu ON ku.`Kd_unit_kerja` = pu.kd_unit" +
                " inner join personalia.pegawai pp ON pp.`NPP`=pu.npp" +
                " WHERE pp.`AdmEdu`='2' AND ku.`Nama_unit_kerja` LIKE '%PROGRAM%'" +
                " GROUP by pu.kd_unit";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getLaporanKinerjaPegawaiAdministratif() {
        List<Map> results = new ArrayList<Map>();
        for (int i = new DateTime().getYear() - 5; i <= new DateTime().getYear(); i++) {
            String sqlCreateView = "CREATE OR REPLACE VIEW kamus.kinerja_pegawai(kode,nama,rerata,tahun,semester) " +
                    " AS " +
                    " SELECT unit_peg.kd_unit as kodeUnit, unkerja.Nama_unit_kerja as namaUnit," +
                    " SUM(nilaisubkomponenpegawai.Nilai)/count(nilaisubkomponenpegawai.Nilai)as rataRata, " +
                    " timpenilaidp3.tahunPenilaian as tahun," +
                    " 0 as semester " +
                    " FROM (((personalia.pegawai pegawai INNER JOIN personalia.unit_peg unit_peg" +
                    " ON (pegawai.kdPegawai = unit_peg.kdPegawai)) " +
                    " INNER JOIN personalia.timpenilaidp3 timpenilaidp3 " +
                    " ON (timpenilaidp3.kdPegawaiYgDinilai = pegawai.kdPegawai)) " +
                    " INNER JOIN personalia.nilaisubkomponenpegawai nilaisubkomponenpegawai " +
                    " ON (timpenilaidp3.idTim = nilaisubkomponenpegawai.idTim)) " +
                    " INNER JOIN kamus.unkerja unkerja " +
                    " ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit) " +
                    " WHERE timpenilaidp3.tahunPenilaian='" + i + "'  AND personalia.pegawai.AdmEdu = '2' " +
                    " UNION " +
                    " SELECT unit_peg.Kd_unit as kodeUnit, " +
                    " unkerja.Nama_unit_kerja as namaUnit,AVG(resume_kategori.skor) AS rataRata," +
                    " SUBSTRING(resume_kategori.ta,1,4) AS tahun," +
                    " SUBSTRING(resume_kategori.ta,5,1) AS semester " +
                    " FROM ((kamus.unkerja unkerja INNER JOIN kamus.unit_peg unit_peg" +
                    " ON (unkerja.Kd_unit_kerja = unit_peg.Kd_unit))" +
                    " INNER JOIN personalia.pegawai pegawai" +
                    " ON (pegawai.NPP = unit_peg.NPP))" +
                    " INNER JOIN evaluasi.resume_kategori resume_kategori" +
                    " ON (resume_kategori.npp = pegawai.NPP)" +
                    " WHERE resume_kategori.ta LIKE '" + i + "%' GROUP BY resume_kategori.npp";

            ClassConnection.getJdbc().execute(sqlCreateView);
            String sql = " select AVG(rerata) as rerata, kp. kode, kp.nama, kp.tahun, kp.semester from kamus.kinerja_pegawai kp GROUP BY kode";

            try {
                PreparedStatement ps = ClassConnection.getConnection().prepareStatement(sql);
                ResultSet result = ps.executeQuery();
                while (result.next()) {
                    Map map = new HashMap();
                    map.put("kode", result.getString("kode"));
                    map.put("nama", result.getString("nama"));
                    map.put("tahun", result.getString("tahun"));
                    map.put("rerata", result.getString("rerata"));
                    map.put("semester", result.getString("semester"));
                    results.add(map);

                }
            } catch (SQLException ex) {
            }
        }
        return results;
    }
}
