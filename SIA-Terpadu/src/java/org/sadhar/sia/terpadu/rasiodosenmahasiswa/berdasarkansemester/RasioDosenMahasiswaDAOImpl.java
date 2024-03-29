/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.rasiodosenmahasiswa.berdasarkansemester;

import java.util.ArrayList;
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
        if (isTabelJwExist(kodeProdi, tahun, semester) && isTabelKrExist(kodeProdi, tahun, semester)) {
            String sqlCreateView = "CREATE OR REPLACE VIEW tempo.rasioDosenMahasiswa(jumlah,kd_mtk,nama) AS " +
                    "SELECT COUNT(nomor_mhs) AS jumlah, krs.kd_mtk, 0 AS Nama_peg " +
                    " FROM db_" + kodeProdi + ".kr" + kodeProdi + tahun + semester + " krs  GROUP BY kd_mtk " +
                    " UNION " +
                    " SELECT 0 as jumlah, jw.kd_mtk,  LTRIM(concat_ws(' ',pp.Gelar_depan,pp.Nama_peg,pp.Gelar_blk)) as Nama_peg FROM personalia.pegawai pp " +
                    " INNER JOIN kamus.stat_edu ks ON pp.AdmEdu = ks.Kd_stat_edu " +
                    " INNER JOIN db_" + kodeProdi + ".jw" + kodeProdi + tahun + semester + " jw ON pp.kdPegawai = jw.NPP " +
                    " WHERE ks.Kd_stat_edu='2'";

            ClassConnection.getJdbc().execute(sqlCreateView);

            String sql = " SELECT kd_mtk,max(jumlah) AS jumlah, max(nama) AS nama " +
                    " FROM tempo.rasioDosenMahasiswa GROUP BY kd_mtk";

            results.addAll(ClassConnection.getJdbc().queryForList(sql));
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

    public List<Map> getNamaDosen(String kodeProdi, String tahun, String semester) {
        List<Map> results = new ArrayList<Map>();
        if (isTabelJwExist(kodeProdi, tahun, semester) && isTabelKrExist(kodeProdi, tahun, semester)) {
            String sql = "SELECT DISTINCT LTRIM(concat_ws(' ',pp.Gelar_depan,pp.Nama_peg,pp.Gelar_blk)) AS nama FROM personalia.pegawai pp " +
                    " INNER JOIN kamus.stat_edu ks ON pp.AdmEdu=ks.Kd_stat_edu " +
                    " INNER JOIN db_" + kodeProdi + ".jw" + kodeProdi + tahun + semester + " jw ON pp.kdPegawai = jw.NPP " +
                    " WHERE ks.Kd_stat_edu='2'";
            results.addAll(ClassConnection.getJdbc().queryForList(sql));
        }
        return results;
    }
}
