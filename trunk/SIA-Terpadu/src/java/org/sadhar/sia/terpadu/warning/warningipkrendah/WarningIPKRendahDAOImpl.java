/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningipkrendah;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class WarningIPKRendahDAOImpl implements WarningIPKRendahDAO {

    public WarningIPKRendahDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std ORDER BY Nama_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getAngkatan() {
        String sql = " select distinct if(left(nomor_mhs,1)='9',concat('19',left(nomor_mhs,2)), "
                + " if(left(nomor_mhs,1)='8',concat('19',left(nomor_mhs,2)), "
                + " concat('20',left(nomor_mhs,2)))) as angkatan "
                + " from db_1114.mhs1114   ORDER BY angkatan ";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getWarningIPKRendah(String kodeProdi) {
        List<Map> tahuns = this.getAngkatan();
        List<Map> datas = new ArrayList<Map>();
        List<Map> allData = new ArrayList<Map>();
        for (Map data : tahuns) {
            if (isTabelKHExist(kodeProdi, (String) data.get("angkatan"))) {
                String sql = "SELECT detail_mhs.Nomor_mhs, mhs.nama_mhs, prodi.Nama_prg, fakultas.Nama_fak, "
                        + " SUM(SKS * angka)/SUM(SKS) as ipk, "
                        + " IF(LEFT( mhs.nomor_mhs, 1)='9', CONCAT('19', LEFT( mhs.nomor_mhs, 2)),"
                        + " IF(LEFT( mhs.nomor_mhs, 1)='8', CONCAT('19', LEFT( mhs.nomor_mhs,2)), CONCAT('20', LEFT( mhs.nomor_mhs, 2)))) AS angkatan "
                        + " FROM db_" + kodeProdi + ".kh" + kodeProdi + data.get("angkatan") + " AS detail_mhs "
                        + " INNER JOIN db_" + kodeProdi + ".mhs" + kodeProdi + " mhs ON (mhs.nomor_mhs = detail_mhs.Nomor_mhs) "
                        + " INNER JOIN kamus.prg_std prodi ON (prodi.Kd_prg = '" + kodeProdi + "')  "
                        + " INNER JOIN kamus.nilai nilai ON (nilai.huruf = detail_mhs.Nilai) "
                        + " INNER JOIN kamus.fakultas fakultas ON (prodi.Kd_fak = fakultas.Kd_fakultas)"
                        + " GROUP BY detail_mhs.Nomor_mhs, mhs.nama_mhs";
                datas = ClassConnection.getJdbc().queryForList(sql);
                allData.addAll(datas);
            }

        }
        return allData;
    }

    public List<Map> getWarningIPKRendah() {
        List<Map> prodis = this.getProdi();
        List<Map> tahuns = this.getAngkatan();
        List<Map> datas = new ArrayList<Map>();
        List<Map> allData = new ArrayList<Map>();
        for (Map prodi : prodis) {
            for (Map data : tahuns) {
                if (isTabelKHExist(prodi.get("Kd_prg").toString(), (String) data.get("angkatan"))) {
                    String sql = "SELECT detail_mhs.Nomor_mhs, mhs.nama_mhs, prodi.Nama_prg, fakultas.Nama_fak, "
                            + " SUM(SKS * angka)/SUM(SKS) as ipk, "
                            + " IF(LEFT( mhs.nomor_mhs, 1)='9', CONCAT('19', LEFT( mhs.nomor_mhs, 2)),"
                            + " IF(LEFT( mhs.nomor_mhs, 1)='8', CONCAT('19', LEFT( mhs.nomor_mhs,2)), CONCAT('20', LEFT( mhs.nomor_mhs, 2)))) AS angkatan "
                            + " FROM db_" + prodi.get("Kd_prg").toString() + ".kh" + prodi.get("Kd_prg").toString() + data.get("angkatan") + " AS detail_mhs "
                            + " INNER JOIN db_" + prodi.get("Kd_prg").toString() + ".mhs" + prodi.get("Kd_prg").toString() + " mhs ON (mhs.nomor_mhs = detail_mhs.Nomor_mhs) "
                            + " INNER JOIN kamus.prg_std prodi ON (prodi.Kd_prg = '" + prodi.get("Kd_prg").toString() + "')  "
                            + " INNER JOIN kamus.nilai nilai ON (nilai.huruf = detail_mhs.Nilai) "
                            + " INNER JOIN kamus.fakultas fakultas ON (prodi.Kd_fak = fakultas.Kd_fakultas)"
                            + " GROUP BY detail_mhs.Nomor_mhs, mhs.nama_mhs";
                    datas = ClassConnection.getJdbc().queryForList(sql);
                    allData.addAll(datas);
                }

            }
        }
        return allData;
    }

    public boolean isTabelKHExist(String kodeProdi, String tahun) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'kh" + kodeProdi + tahun + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }
}
