/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.rasiodosenmahasiswa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class RasioDosenMahasiswaDAOImpl implements RasioDosenMahasiswaDAO {

    public RasioDosenMahasiswaDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getRasioDosenMahasiswa(String kodeProdi, String tahun, String semester) {
        List<Map> results = new ArrayList<Map>();
        for (Map map : this.getProdi()) {
            String prodi = map.get("Kd_prg").toString();
            if (isTabelJwExist(prodi, tahun, semester) && isTabelKrExist(prodi, tahun, semester)) {
//            String sqlCreateView = "CREATE OR REPLACE VIEW kamus.rasioDosenMahasiswa(jumlah,kd_mtk,nama) AS " +
//                    "SELECT COUNT(nomor_mhs) AS jumlah, krs.kd_mtk, 0 AS Nama_peg " +
//                    " FROM db_" + kodeProdi + ".kr" + kodeProdi + tahun + semester + " krs  GROUP BY kd_mtk " +
//                    " UNION " +
//                    " SELECT 0 as jumlah, jw.kd_mtk, pp.Nama_peg FROM personalia.pegawai pp " +
//                    " INNER JOIN kamus.stat_edu ks ON pp.AdmEdu = ks.Kd_stat_edu " +
//                    " INNER JOIN db_" + kodeProdi + ".jw" + kodeProdi + tahun + semester + " jw ON pp.kdPegawai = jw.NPP " +
//                    " WHERE ks.Kd_stat_edu='2'";
//            ClassConnection.getJdbc().execute(sqlCreateView);

                String sql = "select jw.NPP, Nama_peg as nama, jw.kd_mtk, count(nomor_mhs) as jumlah" +
                        " from db_" + prodi + ".kr" + prodi + tahun + semester + " as krs  " +
                        " inner join db_" + prodi + ".jw" + prodi + tahun + semester + " as jw on krs.kd_mtk= jw.kd_mtk and krs.seksi=jw.seksi " +
                        " inner join personalia.pegawai as p on p.kdPegawai=jw.NPP " +
                        " group by jw.NPP";
                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        }
        return results;
    }

    public List<Map> getRasioDosenMahasiswa(String tahun, String semester) {
        List<Map> results = new ArrayList<Map>();
        for (Map prodi : this.getProdi()) {
            String kodeProdi = prodi.get("Kd_prg").toString();
            if (isTabelRgExist(kodeProdi, tahun, semester)) {
//                String sql1 = "SELECT COUNT(*) AS jml  from " +
//                        " (SELECT kdPegawai,npp,Nama_peg, " +
//                        " (SELECT kd_unit from personalia.unit_peg as un where kdPegawai=peg.kdPegawai order by tgl_mulai_unit desc limit 1) as kd_unit," +
//                        " (SELECT Nama_unit_kerja " +
//                        " FROM personalia.unit_peg as un " +
//                        " inner join kamus.unkerja as kms on kms.Kd_unit_kerja=un.kd_unit " +
//                        " WHERE kdPegawai=peg.kdPegawai order by tgl_mulai_unit desc limit 1) as unit " +
//                        " FROM personalia.pegawai as peg where admEdu='2' and Status_keluar='1' ) as sub where sub.kd_unit='160" + kodeProdi + "0' ";
                String sql1 = "SELECT " +
                        " COUNT(DISTINCT pegawai.kdPegawai) AS jml " +
                        " FROM (kamus.unkerja unkerja LEFT JOIN personalia.unit_peg unit_peg ON (unkerja.Kd_unit_kerja = unit_peg.kd_unit)) " +
                        " LEFT JOIN personalia.pegawai pegawai ON (pegawai.kdPegawai = unit_peg.kdPegawai) " +
                        " WHERE (pegawai.AdmEdu = '2') AND unkerja.Kd_unit_kerja='160" + kodeProdi + "0' " +
                        " GROUP BY unkerja.Kd_unit_kerja ORDER BY unkerja.Kd_unit_kerja ASC";
                String sql2 = " SELECT COUNT( * ) AS jml FROM db_" + kodeProdi + ".rg" + kodeProdi + tahun + semester + "  as mhs " +
                        " INNER JOIN kamus.unkerja prg ON prg.Kd_unit_kerja='160" + kodeProdi + "0' " +
                        " WHERE (st_mhs = '1')";

                double jumlahDosen = 0;
                double jumlahMahasiswa = 0;
                try {
                    jumlahDosen = ClassConnection.getJdbc().queryForInt(sql1);
                    jumlahMahasiswa = ClassConnection.getJdbc().queryForInt(sql2);
                } catch (Exception e) {
                    jumlahDosen = 0;
                    jumlahMahasiswa = 0;
                }

                Map m = new HashMap();
                if (jumlahDosen > 0) {
                    m.put("jumlah", jumlahMahasiswa / jumlahDosen);
                    m.put("prodi", prodi.get("Nama_prg").toString());
                    results.add(m);
                }


                System.out.println(sql1);
            }
        }
        return results;
    }

    public boolean isTabelJwExist(String kodeProdi, String tahun, String semester) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'jw" + kodeProdi + tahun + semester + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }

    public boolean isTabelKrExist(String kodeProdi, String tahun, String semester) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'kr" + kodeProdi + tahun + semester + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }

    public boolean isTabelRgExist(String kodeProdi, String tahun, String semester) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'rg" + kodeProdi + tahun + semester + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }

    public List<Map> getNamaDosen(String kodeProdi, String tahun, String semester) {
        List<Map> results = new ArrayList<Map>();
        if (isTabelJwExist(kodeProdi, tahun, semester) && isTabelKrExist(kodeProdi, tahun, semester)) {
            String sql = "SELECT DISTINCT(pp.Nama_peg) AS nama FROM personalia.pegawai pp " +
                    " INNER JOIN kamus.stat_edu ks ON pp.AdmEdu=ks.Kd_stat_edu " +
                    " INNER JOIN db_" + kodeProdi + ".jw" + kodeProdi + tahun + semester + " jw ON pp.kdPegawai = jw.NPP " +
                    " WHERE ks.Kd_stat_edu='2'";
            results.addAll(ClassConnection.getJdbc().queryForList(sql));
        }
        return results;
    }
}
