/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswalulusdanbelumlulus;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class JumlahMahasiswaLulusDanBelumLulusDAOImpl implements JumlahMahasiswaLulusDanBelumLulusDAO {

    public JumlahMahasiswaLulusDanBelumLulusDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std ORDER BY Nama_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getDataMahasiswa(String kodeProdi) {
        String sql1 = " SELECT DISTINCT  'totalMahasiswa', IF(LEFT(nomor_mhs,1)='9', CONCAT('19', LEFT(nomor_mhs,2)), "
                + " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)),CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan, "
                + " COUNT(LEFT(nomor_mhs,2)) AS jumlah  "
                + " FROM db_" + kodeProdi + ".mhs" + kodeProdi + " GROUP BY angkatan";
        String sql2 = " SELECT DISTINCT  'totalMahasiswaLulus', IF(LEFT(nomor_mhs, 1)='9', CONCAT('19', LEFT(nomor_mhs, 2)), "
                + " IF(LEFT(nomor_mhs, 1)='8', CONCAT('19', LEFT(nomor_mhs,2)), CONCAT('20', LEFT(nomor_mhs, 2)))) AS angkatan, "
                + " COUNT(LEFT(nomor_mhs, 2)) AS jumlah  "
                + " FROM db_" + kodeProdi + ".ll" + kodeProdi + " GROUP BY angkatan";
        String sql = sql1 + " UNION " + sql2;
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getDataFromTableTahun(String kodeProdi) {
        String sql = "SELECT *FROM tempo.tahun" + kodeProdi;
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public void createDatabaseTempo() {
        String sql = "CREATE DATABASE IF NOT EXISTS tempo";
        ClassConnection.getJdbc().execute(sql);
    }

    public void createTableTahun(String kodeProdi) {
        String sql = "SHOW TABLES   FROM  tempo LIKE 'tahun" + kodeProdi + "'";
        SqlRowSet rs = ClassConnection.getJdbc().queryForRowSet(sql);
        if (rs.next()) {
            dropTableTahun(kodeProdi);
        }
        String sqlCreate = "CREATE TABLE tempo.tahun" + kodeProdi + "( "
                + "status varchar(150), "
                + "angkatan varchar(4) NOT NULL default '', "
                + "jumlah int(4) NOT NULL default '0' "
                + ") ENGINE=MyISAM DEFAULT CHARSET=latin1";
        ClassConnection.getJdbc().execute(sqlCreate);
        List<Map> tahun = this.getDataMahasiswa(kodeProdi);
        for (Map map : tahun) {
            String sqlInsert = "INSERT INTO tempo.tahun" + kodeProdi + "(status, angkatan, jumlah) VALUES('" + map.get("totalMahasiswa").toString() + "'," + map.get("angkatan") + "," + map.get("jumlah") + ")";
            ClassConnection.getJdbc().execute(sqlInsert);
        }
    }

    public void createTableLulus(String kodeProdi) {
        String sql = "SHOW TABLES   FROM  tempo LIKE 'lulus" + kodeProdi + "'";
        SqlRowSet rs = ClassConnection.getJdbc().queryForRowSet(sql);
        if (rs.next()) {
            dropTableLulus(kodeProdi);
        }
        String sqlCreate = "CREATE TABLE tempo.lulus" + kodeProdi + "( "
                + "angkatan varchar(150), "
                + "jumlah_total int(4) NOT NULL default '0', "
                + "jumlah_lulus  int(4) NOT NULL default '0')"
                + "ENGINE=MyISAM DEFAULT CHARSET=latin1";
        ClassConnection.getJdbc().execute(sqlCreate);
        List<Map> tahun = this.getTahunAngkatan(kodeProdi);
        for (Map map : tahun) {
            String sqlInsert = "INSERT INTO tempo.lulus" + kodeProdi + "(angkatan) VALUES('" + map.get("angkatan") + "')";
            ClassConnection.getJdbc().execute(sqlInsert);
        }
    }

    public void updateTableLulus(String kodeProdi) {
        List<Map> dataMahasiswa = this.getDataFromTableTahun(kodeProdi);
        for (Map map : dataMahasiswa) {
            int jumlah = 0;
            String sqlUpdate = null;
            if (map.get("status").toString().equalsIgnoreCase("totalMahasiswa")) {
                jumlah = Integer.parseInt(map.get("jumlah").toString());
                sqlUpdate = "UPDATE tempo.lulus" + kodeProdi + " SET jumlah_total = " + jumlah + " WHERE angkatan = " + map.get("angkatan");
            } else {
                jumlah = Integer.parseInt(map.get("jumlah").toString());
                sqlUpdate = "UPDATE tempo.lulus" + kodeProdi + " SET jumlah_lulus = " + jumlah + " WHERE angkatan = " + map.get("angkatan");
            }
            ClassConnection.getJdbc().execute(sqlUpdate);
        }
    }

    public void dropTableLulus(String kodeProdi) {
        String sql = "DROP TABLE tempo.lulus" + kodeProdi + "";
        ClassConnection.getJdbc().execute(sql);
    }

    public List<Map> getTahunAngkatan(String kodeProdi) {
        String sql = "SELECT DISTINCT angkatan FROM tempo.tahun" + kodeProdi;
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public void dropTableTahun(String kodeProdi) {
        String sql = "DROP TABLE tempo.tahun" + kodeProdi + "";
        ClassConnection.getJdbc().execute(sql);
    }

    public List<Map> getJumlahMahasiswaLulusDanBelumLulus(String kodeProdi) {
        this.createTableTahun(kodeProdi);
        this.createTableLulus(kodeProdi);
        this.updateTableLulus(kodeProdi);
        String sql = "SELECT angkatan, jumlah_total,jumlah_lulus,(jumlah_lulus/(jumlah_total*10/100)*10) AS prosentase_lulus, (jumlah_total-jumlah_lulus) AS jumlah_belum_lulus,"
                + "((jumlah_total-jumlah_lulus)/(jumlah_total*10/100)*10) AS prosentase_belum_lulus FROM tempo.lulus" + kodeProdi;
        return ClassConnection.getJdbc().queryForList(sql);
    }
}
