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
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg ";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getRerataIps(String kodeProdi, String tahunAngkatan) {
        List<Map> result = new ArrayList<Map>();
        List<Map> results = new ArrayList<Map>();
        int currentYear = new DateTime().getYear();
        if (kodeProdi != null && tahunAngkatan != null) {
            //Jika prodi/fakultas dan tahun angkatan ditentukan, maka dihitung rerata IPS dari
            //prodi dan tahun angkatan yang dipilih tersebut.
            if (isTabelKTExist(kodeProdi, tahunAngkatan)) {
                String sql = "SELECT  IF(LEFT(nomor_mhs, 1)='9', CONCAT('19', LEFT(nomor_mhs, 2))," +
                        " IF(LEFT(nomor_mhs, 1)='8', CONCAT('19', LEFT(nomor_mhs,2)), CONCAT('20', LEFT( nomor_mhs, 2)))) AS angkatan," +
                        " SUBSTRING(ambil,1,4) as tahun, SUBSTRING(ambil,5,1) AS semester, " +
                        " SUM(SKS * angka)/SUM(SKS) as ips,fak.Nama_fak FROM db_" + kodeProdi + ".kt" + kodeProdi + tahunAngkatan + " as kt " +
                        " INNER JOIN kamus.nilai nilai ON (nilai.huruf = kt.Nilai)" +
                        " INNER JOIN kamus.prg_std prg ON (prg.Kd_prg = '" + kodeProdi + "') " +
                        " INNER JOIN kamus.fakultas fak ON (prg.Kd_fak = fak.Kd_fakultas)" +
                        " WHERE SUBSTRING(ambil,5,1) = 1 OR SUBSTRING(ambil,5,1) = 2" +
                        " GROUP BY tahun, semester order by tahun,semester";

                results.addAll(ClassConnection.getJdbc().queryForList(sql));
            }
        } else if (kodeProdi != null && tahunAngkatan == null) {
            //Jika prodi/fakultas ditentukan, maka dihitung rerata IPS untuk tiap prodi atau
            //fakultas yang dipilih, dengan tiap tahun angkatan dipisah dalam baris data yang berbeda
            for (int i = currentYear - 5; i <= currentYear; i++) {
                if (isTabelKTExist(kodeProdi, String.valueOf(i))) {
                    String sql = " SELECT  IF(LEFT(nomor_mhs, 1)='9', CONCAT('19', LEFT(nomor_mhs, 2))," +
                            " IF(LEFT(nomor_mhs, 1)='8', CONCAT('19', LEFT(nomor_mhs,2)), CONCAT('20', LEFT( nomor_mhs, 2)))) AS angkatan," +
                            " SUBSTRING(ambil,1,4) as tahun, SUBSTRING(ambil,5,1) AS semester, " +
                            " SUM(SKS * angka)/SUM(SKS) as ips,fak.Nama_fak FROM db_" + kodeProdi + ".kt" + kodeProdi + String.valueOf(i) + " as kt " +
                            " INNER JOIN kamus.nilai nilai ON (nilai.huruf = kt.Nilai)" +
                            " INNER JOIN kamus.prg_std prg ON (prg.Kd_prg = '" + kodeProdi + "') " +
                            " INNER JOIN kamus.fakultas fak ON (prg.Kd_fak = fak.Kd_fakultas)" +
                            " WHERE SUBSTRING(ambil,5,1) = 1 OR SUBSTRING(ambil,5,1) = 2" +
                            " GROUP BY tahun, semester order by tahun,semester";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                } else {
                    String namaFakultas = this.getNamaFakultas(kodeProdi).get("Nama_fak").toString();
                    Map mapSemester1 = new HashMap();
                    mapSemester1.put("tahun", i);
                    mapSemester1.put("semester", 1);
                    mapSemester1.put("angkatan", i);
                    mapSemester1.put("ips", "0.0000");
                    mapSemester1.put("Nama_fak", namaFakultas);
                    results.add(mapSemester1);

                    Map mapSemester2 = new HashMap();
                    mapSemester2.put("tahun", i);
                    mapSemester2.put("semester", 2);
                    mapSemester2.put("angkatan", i);
                    mapSemester2.put("ips", "0.0000");
                    mapSemester2.put("Nama_fak", namaFakultas);
                    results.add(mapSemester2);
                }

            }

        } else if (kodeProdi == null && tahunAngkatan != null) {
            //Jika tahun angkatan ditentukan, maka dihitung rerata IPS untuk tahun angkatan
            //yang dipilih, dengan semua prodi dipisah dalam baris data yang berbeda.
            List<Map> prodis = this.getProdi();
            for (Map prodi : prodis) {
                kodeProdi = prodi.get("Kd_prg").toString();
                if (isTabelKTExist(kodeProdi, tahunAngkatan)) {
                    String sql = "SELECT  IF(LEFT(nomor_mhs, 1)='9', CONCAT('19', LEFT(nomor_mhs, 2))," +
                            " IF(LEFT(nomor_mhs, 1)='8', CONCAT('19', LEFT(nomor_mhs,2)), CONCAT('20', LEFT( nomor_mhs, 2)))) AS angkatan," +
                            " SUBSTRING(ambil,1,4) as tahun, SUBSTRING(ambil,5,1) AS semester, " +
                            " SUM(SKS * angka)/SUM(SKS) as ips,fak.Nama_fak,prg.Kd_prg FROM db_" + kodeProdi + ".kt" + kodeProdi + tahunAngkatan + " as kt " +
                            " INNER JOIN kamus.nilai nilai ON (nilai.huruf = kt.Nilai)" +
                            " INNER JOIN kamus.prg_std prg ON (prg.Kd_prg = '" + kodeProdi + "') " +
                            " INNER JOIN kamus.fakultas fak ON (prg.Kd_fak = fak.Kd_fakultas)" +
                            " WHERE SUBSTRING(ambil,5,1) = 1 OR SUBSTRING(ambil,5,1) = 2" +
                            " GROUP BY tahun, semester order by tahun,semester";
                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        } else {
            //Jika tidak ada parameter yang dipilih, maka dihitung rerata IPS untuk semua mahasiswa,
            //dari data 5 tahun sebelumnya.
            List<Map> prodis = this.getProdi();
            for (Integer i = currentYear - 5; i <= currentYear + 5; i++) {
                for (Map prodi : prodis) {
                    kodeProdi = prodi.get("Kd_prg").toString();
                    if (isTabelKTExist(kodeProdi, i.toString())) {
                        result = getRerataIps(prodi.get("Kd_prg").toString(), i.toString());
                        results.addAll(result);
                    }
                }
            }
        }
        return results;
    }

    public Map getNamaFakultas(String kodeProdi) {
        String sql = "SELECT fak.Nama_fak FROM kamus.prg_std prg " +
                " INNER JOIN fakultas fak ON fak.Kd_fakultas = prg.Kd_fak " +
                " where prg.Kd_prg = '" + kodeProdi + "';";
        return ClassConnection.getJdbc().queryForMap(sql);
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
