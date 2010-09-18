/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistikmahasiswa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class StatistikMahasiswaDAOImpl implements StatistikMahasiswaDAO {

    public StatistikMahasiswaDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List getListDataStatistik(String kodeProdi) {
        String sql = "select jenis, angkatan, jumlah "
                + "from tempo.tempmhs" + kodeProdi;

        return ClassConnection.getJdbc().query(sql, new Mapper());
    }

    public List getMhsAllTempo(String kodeProdi) {
        String sql = "select * "
                + "from tempo.tempmhs" + kodeProdi + " "
                + "where tempo.tempmhs" + kodeProdi + ".jenis like 'Total Mahasiswa' "
                + "order by angkatan ";

        String insertTempo = "insert into tempo.tempmhs" + kodeProdi + " "
                + "(jenis, angkatan, jumlah) "
                + "select distinct 'Total Mahasiswa', angkatan, sum(jumlah) as jumlah "
                + "from tempo.tempmhs" + kodeProdi
                + " group by angkatan "
                + " order by angkatan asc";

        ClassConnection.getJdbc().execute(insertTempo);

        return ClassConnection.getJdbc().query(sql, new Mapper());
    }

    public List getMhsLulus(String kodeProdi) {
        String sql = "select * "
                + "from tempo.tempmhs" + kodeProdi + " "
                + "where tempo.tempmhs" + kodeProdi + ".jenis like 'Mahasiswa Lulus' "
                + "order by angkatan ";

        String insertTempo = "insert into tempo.tempmhs" + kodeProdi + " "
                + "(jenis, angkatan, jumlah) "
                + "select distinct 'Mahasiswa Lulus',if(left(nomor_mhs,1)='9',concat('19',left(nomor_mhs,2)), "
                + "if(left(nomor_mhs,1)='8',concat('19',left(nomor_mhs,2)),concat('20',left(nomor_mhs,2)))) as angkatan, "
                + "COUNT(left(nomor_mhs,2)) as jumlah "
                + "from db_" + kodeProdi + ".ll" + kodeProdi + " "
                + "group by angkatan "
                + "order by angkatan asc";

        ClassConnection.getJdbc().execute(insertTempo);
        return ClassConnection.getJdbc().query(sql, new Mapper());
    }

    public List getMhsDO(String kodeProdi) {
        String sql = "select * "
                + "from tempo.tempmhs" + kodeProdi + " "
                + "where tempo.tempmhs" + kodeProdi + ".jenis like 'Mahasiswa DO' "
                + "order by angkatan ";

        String insertTempo = "insert into tempo.tempmhs" + kodeProdi + " "
                + "(jenis, angkatan, jumlah) "
                + "select distinct 'Mahasiswa DO',if(left(nomor_mhs,1)='9',concat('19',left(nomor_mhs,2)), "
                + "if(left(nomor_mhs,1)='8',concat('19',left(nomor_mhs,2)),concat('20',left(nomor_mhs,2)))) as angkatan, "
                + "COUNT(left(nomor_mhs,2)) as jumlah "
                + "from db_" + kodeProdi + ".do" + kodeProdi + " "
                + "group by angkatan "
                + "order by angkatan asc";

        ClassConnection.getJdbc().execute(insertTempo);

        return ClassConnection.getJdbc().query(sql, new Mapper());
    }

    public List getMhsReg(String kodeProdi, String akademik, String semester) {
        String xStr = "select * "
                + "from tempo.tempmhs" + kodeProdi + " "
                + "where tempo.tempmhs" + kodeProdi + ".jenis like 'Mahasiswa Registrasi' "
                + "order by angkatan ";

        String insertTempo = "insert into tempo.tempmhs" + kodeProdi + ""
                + "(jenis, angkatan, jumlah) "
                + " select distinct 'Mahasiswa Registrasi',if(left(nomor_mhs,1)='9',concat('19',left(nomor_mhs,2)), "
                + "if(left(nomor_mhs,1)='8',concat('19',left(nomor_mhs,2)),concat('20',left(nomor_mhs,2)))) as angkatan, "
                + "COUNT(left(nomor_mhs,2)) as jumlah "
                + "from db_" + kodeProdi + ".rg" + kodeProdi + akademik + semester + " "
                + "where st_mhs = '1'"
                + "group by angkatan "
                + "order by angkatan asc";

        ClassConnection.getJdbc().execute(insertTempo);

        return ClassConnection.getJdbc().query(xStr, new Mapper());
    }

    public List getMhsTidakReg(String kodeProdi, String akademik, String semester) {
        String sql = "select * "
                + "from tempo.tempmhs" + kodeProdi + " "
                + "where tempo.tempmhs" + kodeProdi + ".jenis like 'Mahasiswa Tidak Registrasi' "
                + "order by angkatan ";

        String insertTempo = "insert into tempo.tempmhs" + kodeProdi + " "
                + "(jenis, angkatan, jumlah) "
                + "select distinct 'Mahasiswa Tidak Registrasi',if(left(nomor_mhs,1)='9',concat('19',left(nomor_mhs,2)), "
                + "if(left(nomor_mhs,1)='8',concat('19',left(nomor_mhs,2)),concat('20',left(nomor_mhs,2)))) as angkatan, "
                + "COUNT(left(nomor_mhs,2)) as jumlah "
                + "from db_" + kodeProdi + ".rg" + kodeProdi + akademik + semester + " "
                + "where st_mhs = '2'"
                + "group by angkatan "
                + "order by angkatan asc";

        ClassConnection.getJdbc().execute(insertTempo);

        return ClassConnection.getJdbc().query(sql, new Mapper());
    }

    public List getMhsCuti(String kodeProdi, String akademik, String semester) {
        String sql = "select * "
                + "from tempo.tempmhs" + kodeProdi + " "
                + "where tempo.tempmhs" + kodeProdi + ".jenis like 'Mahasiswa Cuti' "
                + "order by angkatan ";

        String insertTempo = "insert into tempo.tempmhs" + kodeProdi + " "
                + "(jenis, angkatan, jumlah) "
                + "select distinct 'Mahasiswa Cuti',if(left(nomor_mhs,1)='9',concat('19',left(nomor_mhs,2)), "
                + "if(left(nomor_mhs,1)='8',concat('19',left(nomor_mhs,2)),concat('20',left(nomor_mhs,2)))) as angkatan, "
                + "COUNT(left(nomor_mhs,2)) as jumlah "
                + "from db_" + kodeProdi + ".rg" + kodeProdi + akademik + semester + " "
                + "where st_mhs = '3'"
                + "group by angkatan "
                + "order by angkatan asc";

        ClassConnection.getJdbc().execute(insertTempo);

        return ClassConnection.getJdbc().query(sql, new Mapper());
    }

    public boolean isTabelLulusAda(String kodeProdi) {
        String sql = "Show tables from db_" + kodeProdi + " like 'll" + kodeProdi + "'";
        SqlRowSet rs = ClassConnection.getJdbc().queryForRowSet(sql);
        return rs.next();
    }

    public boolean isTabelDOAda(String kodeProdi) {
        String sql = "Show tables from db_" + kodeProdi + " like 'do" + kodeProdi + "'";
        SqlRowSet rs = ClassConnection.getJdbc().queryForRowSet(sql);
        return rs.next();
    }

    public boolean isTabelrgXYZZyyyySAda(String kodeProdi, String akademik, String semester) {
        String sql = "Show tables from db_" + kodeProdi + " like 'rg" + kodeProdi + akademik + semester + "'";
        SqlRowSet rs = ClassConnection.getJdbc().queryForRowSet(sql);
        return rs.next();
    }

    public void createTabelTempo(String kodeProdi) {
        String sql1 = "Show tables from tempo like 'tempmhs" + kodeProdi + "'";
        SqlRowSet rs1 = ClassConnection.getJdbc().queryForRowSet(sql1);
        if (rs1.next()) {
            deleteTabelTempo(kodeProdi);
        }
        String create = "CREATE TABLE tempo.tempmhs" + kodeProdi + " ( "
                + "jenis varchar(150) NOT NULL default '', "
                + "angkatan varchar(4) NOT NULL default '', "
                + "jumlah int(4) NOT NULL default '0' "
                + ") ENGINE=MyISAM DEFAULT CHARSET=latin1";
        ClassConnection.getJdbc().execute(create);
    }

    public void createTabelStatistik(String kodeProdi, List<Map> tahun) {
        String sql = "Show tables from tempo like 'tempstatistik" + kodeProdi + "'";
        SqlRowSet rs = ClassConnection.getJdbc().queryForRowSet(sql);
        if (rs.next()) {
            deleteTabelStatistik(kodeProdi);
        }
        String create = "CREATE TABLE tempo.tempstatistik" + kodeProdi + " ( "
                + "status varchar(50) NOT NULL PRIMARY KEY ,"
                + "total int(4) NOT NULL default '0' "
                + ") ENGINE=MyISAM DEFAULT CHARSET=latin1";
        ClassConnection.getJdbc().execute(create);
        this.alterTableStatistik(kodeProdi, tahun);
    }

    public void alterTableStatistik(String prodi, List<Map> tahun) {
        for (Map angkatan : tahun) {
            String sql = "ALTER TABLE tempo.tempstatistik" + prodi + " ADD t" + angkatan.get("angkatan") + " int(4)";
            ClassConnection.getJdbc().execute(sql);
        }

        String sqlInsert = "INSERT INTO tempo.tempstatistik" + prodi + "(status) VALUES('Total Angkatan'),('Mahasiswa Registrasi'),"
                + " ('Mahasiswa Tidak Aktif'),('Mahasiswa Cuti'),('Mahasiswa DO'),('Mahasiswa Lulus')";
        ClassConnection.getJdbc().execute(sqlInsert);


        String sqlUpdateMhsReg = "UPDATE tempo.tempstatistik" + prodi + " SET total = '" + this.getJumlahMhsReg(prodi, null) + "' WHERE status = 'Mahasiswa Registrasi'";
        ClassConnection.getJdbc().execute(sqlUpdateMhsReg);

        String sqlUpdateMhsCuti = "UPDATE tempo.tempstatistik" + prodi + " SET total = '" + this.getJumlahMhsCuti(prodi, null) + "' WHERE status = 'Mahasiswa Cuti'";
        ClassConnection.getJdbc().execute(sqlUpdateMhsCuti);

        String sqlUpdateMhsTidakAktif = "UPDATE tempo.tempstatistik" + prodi + " SET total = '" + this.getJumlahMhsTidakAktif(prodi, null) + "' WHERE status = 'Mahasiswa Tidak Aktif'";
        ClassConnection.getJdbc().execute(sqlUpdateMhsTidakAktif);

        String sqlUpdateMhsDO = "UPDATE tempo.tempstatistik" + prodi + " SET total = '" + this.getJumlahMhsDO(prodi, null) + "' WHERE status = 'Mahasiswa DO'";
        ClassConnection.getJdbc().execute(sqlUpdateMhsDO);

        String sqlUpdateMhsLulus = "UPDATE tempo.tempstatistik" + prodi + " SET total = '" + this.getJumlahMhsLulus(prodi, null) + "' WHERE status = 'Mahasiswa Lulus'";
        ClassConnection.getJdbc().execute(sqlUpdateMhsLulus);

        int total = this.getJumlahMhsCuti(prodi, null) + this.getJumlahMhsDO(prodi, null) + this.getJumlahMhsLulus(prodi, null) + this.getJumlahMhsReg(prodi, null) + this.getJumlahMhsTidakAktif(prodi, null);

        String sqlUpdateTotal = "UPDATE tempo.tempstatistik" + prodi + " SET total = '" + total + "' WHERE status = 'Total Angkatan'";
        ClassConnection.getJdbc().execute(sqlUpdateTotal);


        for (Map angkatan : tahun) {

            sqlUpdateMhsReg = "UPDATE tempo.tempstatistik" + prodi + " SET t" + angkatan.get("angkatan") + " = " + this.getJumlahMhsReg(prodi, angkatan.get("angkatan").toString()) + ""
                    + " WHERE status = 'Mahasiswa Registrasi'  ";
            ClassConnection.getJdbc().execute(sqlUpdateMhsReg);

            sqlUpdateMhsCuti = "UPDATE tempo.tempstatistik" + prodi + " SET t" + angkatan.get("angkatan") + " = " + this.getJumlahMhsCuti(prodi, angkatan.get("angkatan").toString()) + ""
                    + " WHERE status = 'Mahasiswa Cuti'  ";
            ClassConnection.getJdbc().execute(sqlUpdateMhsCuti);

            sqlUpdateMhsTidakAktif = "UPDATE tempo.tempstatistik" + prodi + " SET t" + angkatan.get("angkatan") + " = " + this.getJumlahMhsTidakAktif(prodi, angkatan.get("angkatan").toString()) + ""
                    + " WHERE status = 'Mahasiswa Tidak Aktif'  ";
            ClassConnection.getJdbc().execute(sqlUpdateMhsTidakAktif);

            sqlUpdateMhsDO = "UPDATE tempo.tempstatistik" + prodi + " SET t" + angkatan.get("angkatan") + " = " + this.getJumlahMhsDO(prodi, angkatan.get("angkatan").toString()) + ""
                    + " WHERE status = 'Mahasiswa DO'  ";
            ClassConnection.getJdbc().execute(sqlUpdateMhsDO);

            sqlUpdateMhsLulus = "UPDATE tempo.tempstatistik" + prodi + " SET t" + angkatan.get("angkatan") + " = " + this.getJumlahMhsLulus(prodi, angkatan.get("angkatan").toString()) + ""
                    + " WHERE status = 'Mahasiswa Lulus'  ";
            ClassConnection.getJdbc().execute(sqlUpdateMhsLulus);

            total = this.getJumlahMhsReg(prodi, angkatan.get("angkatan").toString()) + this.getJumlahMhsCuti(prodi, angkatan.get("angkatan").toString())
                    + this.getJumlahMhsDO(prodi, angkatan.get("angkatan").toString()) + this.getJumlahMhsLulus(prodi, angkatan.get("angkatan").toString()) + this.getJumlahMhsTidakAktif(prodi, angkatan.get("angkatan").toString());

            sqlUpdateTotal = "UPDATE tempo.tempstatistik" + prodi + " SET t" + angkatan.get("angkatan") + " = " + total + ""
                    + " WHERE status = 'Total Angkatan'  ";
            ClassConnection.getJdbc().execute(sqlUpdateTotal);
        }

    }

    public int getJumlahMhsReg(String prodi, String tahun) {
        String sql = null;
        if (tahun == null) {
            sql = "select SUM(jumlah) as jumlah from tempo.tempmhs" + prodi + " WHERE jenis = 'Mahasiswa Registrasi'";
        } else {
            sql = "select SUM(jumlah) as jumlah from tempo.tempmhs" + prodi + " WHERE jenis = 'Mahasiswa Registrasi'  AND angkatan = '" + tahun + "'";
        }
        return ClassConnection.getJdbc().queryForInt(sql);
    }

    public int getJumlahMhsTidakAktif(String prodi, String tahun) {
        String sql = null;
        if (tahun == null) {
            sql = "select SUM(jumlah) as jumlah from tempo.tempmhs" + prodi + " WHERE jenis = 'Mahasiswa Tidak Registrasi '";
        } else {
            sql = "select SUM(jumlah) as jumlah from tempo.tempmhs" + prodi + " WHERE jenis = 'Mahasiswa Tidak Registrasi'  AND angkatan = '" + tahun + "'";
        }
        return ClassConnection.getJdbc().queryForInt(sql);
    }

    public int getJumlahMhsCuti(String prodi, String tahun) {
        String sql = null;
        if (tahun == null) {
            sql = "select SUM(jumlah) as jumlah from tempo.tempmhs" + prodi + " WHERE jenis = 'Mahasiswa Cuti'";
        } else {
            sql = "select SUM(jumlah) as jumlah from tempo.tempmhs" + prodi + " WHERE jenis = 'Mahasiswa Cuti'  AND angkatan = '" + tahun + "'";
        }
        return ClassConnection.getJdbc().queryForInt(sql);
    }

    public int getJumlahMhsDO(String prodi, String tahun) {
        String sql = null;
        if (tahun == null) {
            sql = "select SUM(jumlah) as jumlah from tempo.tempmhs" + prodi + " WHERE jenis = 'Mahasiswa DO'";
        } else {
            sql = "select SUM(jumlah) as jumlah from tempo.tempmhs" + prodi + " WHERE jenis = 'Mahasiswa DO'  AND angkatan = '" + tahun + "'";
        }
        return ClassConnection.getJdbc().queryForInt(sql);
    }

    public int getJumlahMhsLulus(String prodi, String tahun) {
        String sql = null;
        if (tahun == null) {
            sql = "select SUM(jumlah) as jumlah from tempo.tempmhs" + prodi + " WHERE jenis = 'Mahasiswa Lulus'";
        } else {
            sql = "select SUM(jumlah) as jumlah from tempo.tempmhs" + prodi + " WHERE jenis = 'Mahasiswa Lulus'  AND angkatan = '" + tahun + "'";
        }
        return ClassConnection.getJdbc().queryForInt(sql);
    }

    public void deleteTabelTempo(String kodeProdi) {
        String sql = "drop table tempo.tempmhs" + kodeProdi;
        ClassConnection.getJdbc().execute(sql);
    }

    public void deleteTabelStatistik(String kodeProdi) {
        String sql = "drop table tempo.tempstatistik" + kodeProdi;
        ClassConnection.getJdbc().execute(sql);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public StatistikMahasiswa getTempo(String prodi, String jenis, String tahun) {
        String sql = "SELECT * FROM tempo.tempmhs" + prodi + " WHERE jenis='" + jenis + "' AND angkatan='" + tahun + "'";
        return (StatistikMahasiswa) ClassConnection.getJdbc().queryForObject(sql, new Mapper());
    }

    public List<StatistikMahasiswa> getAllTempo(String prodi) {
        String sql = "SELECT * FROM tempo.tempmhs" + prodi + " ORDER BY angkatan";
        return ClassConnection.getJdbc().query(sql, new Mapper());
    }

    public List getAngkatan(String prodi) {
        String sql = "SELECT DISTINCT(angkatan) FROM tempo.tempmhs" + prodi + " ORDER BY angkatan";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getStatistikMahasiswa(String kodeProdi) {
        String sql = "SELECT *FROM tempo.tempstatistik" + kodeProdi;
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public LinkedList getFieldStatistikMahasiswa() {
        LinkedList xls = new LinkedList();
        xls.add("jenis");
        xls.add("angkatan");
        xls.add("jumlah");
        return xls;
    }

    public Object[][] SetDataToCetak(String kodeProdi) throws Exception {
        LinkedList xFieldStatistik = getFieldStatistikMahasiswa();
        List xlsStatistik = getListDataStatistik(kodeProdi);
        Object[][] xBufResult = new Object[xlsStatistik.size()][xFieldStatistik.size()];
        int i = 0;
        for (Iterator it = xlsStatistik.iterator(); it.hasNext();) {
            StatistikMahasiswa xStat = (StatistikMahasiswa) it.next();
            xBufResult[i][0] = xStat.getJenis();
            xBufResult[i][1] = xStat.getAngkatan();
            xBufResult[i][2] = xStat.getJumlah();
            i++;
        }
        return xBufResult;
    }

    protected class Mapper implements RowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException, SQLException {
            StatistikMahasiswa xstat = new StatistikMahasiswa();
            xstat.setJenis(rs.getString("jenis"));
            xstat.setAngkatan(rs.getString("angkatan"));
            xstat.setJumlah(rs.getInt("jumlah"));
            return xstat;
        }
    }
}
