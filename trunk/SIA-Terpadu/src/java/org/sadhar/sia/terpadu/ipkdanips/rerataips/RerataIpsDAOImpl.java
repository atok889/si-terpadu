/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataips;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class RerataIpsDAOImpl implements RerataIpsDAO {

    public RerataIpsDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    //Jika prodi/fakultas dan tahun angkatan ditentukan, maka dihitung rerata IPS dari
    //prodi dan tahun angkatan yang dipilih tersebut.
    public List<Map> getRerataIps(String kodeProdi, String tahunAngkatan) {
        String sql = "SELECT  IF(LEFT(nomor_mhs, 1)='9', CONCAT('19', LEFT(nomor_mhs, 2))," +
                " IF(LEFT(nomor_mhs, 1)='8', CONCAT('19', LEFT(nomor_mhs,2)), CONCAT('20', LEFT( nomor_mhs, 2)))) AS angkatan," +
                " SUBSTRING(ambil,1,4) as tahun, SUBSTRING(ambil,5,1) AS semester, " +
                " SUM(SKS * angka)/SUM(SKS) as ips,fak.Nama_fak FROM db_" + kodeProdi + ".kt" + kodeProdi + tahunAngkatan + " as kt " +
                " INNER JOIN kamus.nilai nilai ON (nilai.huruf = kt.Nilai)" +
                " INNER JOIN kamus.prg_std prg ON (prg.Kd_prg = '" + kodeProdi + "') " +
                " INNER JOIN kamus.fakultas fak ON (prg.Kd_fak = fak.Kd_fakultas)" +
                " GROUP BY tahun, semester order by tahun,semester";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    //Jika tidak ada parameter yang dipilih, maka dihitung rerata IPS untuk semua mahasiswa,
    //dari data 5 tahun sebelumnya.
    public List<Map> getRerataIps() {
        List<Map> prodis = this.getProdi();
        Integer currentYear = new DateTime().getYear();
        List<Map> rerataIpsPerProdi;
        List<Map> datas = new ArrayList<Map>();
        for (Integer i = currentYear; i >= currentYear - 5; i--) {
            for (Map prodi : prodis) {
                System.out.println("------" + prodi.get("Kd_prg").toString());
                if (isTabelKTExist(prodi.get("Kd_prg").toString(), i.toString())) {
                    rerataIpsPerProdi = getRerataIps(prodi.get("Kd_prg").toString(), i.toString());
                    datas.addAll(rerataIpsPerProdi);
                }
            }
        }
        return datas;
    }

    public boolean isTabelKTExist(String kodeProdi, String tahun) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'kt" + kodeProdi + tahun + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }
}
