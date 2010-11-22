/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.daftarwisudaipktertinggi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class DaftarWisudaIpkTertinggiDAOImpl implements DaftarWisudaIpkTertinggiDAO {

    public DaftarWisudaIpkTertinggiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg !='0000' ORDER BY Nama_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getDaftarWisudaIpkTertinggi(String tanggalWisuda) {
        List<Map> results = new ArrayList<Map>();
        for (Map prodi : getProdi()) {
            String kodeProdi = prodi.get("Kd_prg").toString();
            if (isTabelLLExist(kodeProdi) && isTabelUrutExist(kodeProdi)) {
                for (int i = new DateTime().getYear() - 40; i <= new DateTime().getYear(); i++) {
                    if (isTabelTrExist(kodeProdi, String.valueOf(i))) {
                        String sql = "SELECT urut.TglWsd, ll.nomor_mhs,ll.nama_mhs,prg.Nama_prg, " +
                                " SUM(sks * angka)/SUM(sks) as ipk " +
                                " FROM db_" + kodeProdi + ".ll" + kodeProdi + " as ll " +
                                " INNER JOIN db_" + kodeProdi + ".tr" + kodeProdi + i + " tr ON ll.nomor_mhs = tr.nomor_mhs " +
                                " INNER JOIN kamus.nilai nilai ON (nilai.huruf = tr.Nilai) " +
                                " INNER JOIN db_" + kodeProdi + ".urutwsd" + kodeProdi + " urut ON(urut.NoMhs = tr.nomor_mhs) " +
                                " INNER JOIN kamus.prg_std prg ON ( prg.Kd_prg='" + kodeProdi + "') " +
                                " WHERE urut.TglWsd = '" + tanggalWisuda + "' GROUP BY ll.nomor_mhs;";
                        results.addAll(ClassConnection.getJdbc().queryForList(sql));
                    }
                }
            }
        }
        return results;
    }

    public List<Map> getTanggalWisuda() {
        String sql = "SELECT x.TglWsd as tanggal FROM db_1114.urutwsd1114 x GROUP BY tanggal ";
        for (Map prodi : getProdi()) {
            if (isTabelUrutExist(prodi.get("Kd_prg").toString())) {
                sql += " UNION SELECT x.TglWsd as tanggal FROM db_" + prodi.get("Kd_prg").toString() + ".urutwsd" + prodi.get("Kd_prg").toString() + " x GROUP BY tanggal";
            }
        }
        sql += " ORDER BY tanggal ";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public boolean isTabelTrExist(String kodeProdi, String tahun) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'tr" + kodeProdi + tahun + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }

    public boolean isTabelLLExist(String kodeProdi) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'll" + kodeProdi + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }

    public boolean isTabelUrutExist(String kodeProdi) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'urutwsd" + kodeProdi + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }
}
