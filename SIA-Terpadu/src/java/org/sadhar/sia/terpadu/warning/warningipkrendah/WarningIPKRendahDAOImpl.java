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
            if (isTabelExist(kodeProdi, (String) data.get("angkatan"))) {
                System.out.println(kodeProdi + "" + data.get("angkatan"));
                String sql = "SELECT detail_mhs.Nomor_mhs, mhs.nama_mhs, prodi.Nama_prg, fakultas.Nama_fak, "
                        + " SUM(SKS * angka)/SUM(SKS) as ipk "
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

        for (Map map : allData) {
            System.out.print("-----" + map.get("Nomor_mhs"));
            System.out.print("-----" + map.get("nama_mhs"));
            System.out.println("-----" + map.get("ipk"));
            System.out.println("-----" + map.get("Nama_fak"));
        }
        return datas;
    }

    public boolean isTabelExist(String kodeProdi, String tahun) {
        String sql = "Show tables from db_" + kodeProdi + " like 'kh" + kodeProdi + tahun + "'";
        SqlRowSet rs = ClassConnection.getJdbc().queryForRowSet(sql);
        return rs.next();
    }
}
