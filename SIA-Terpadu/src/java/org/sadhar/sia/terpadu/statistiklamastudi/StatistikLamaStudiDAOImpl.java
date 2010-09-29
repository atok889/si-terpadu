/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistiklamastudi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author Deny Prasetyo
 */
public class StatistikLamaStudiDAOImpl implements StatistikLamaStudiDAO {

    public StatistikLamaStudiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public StatistikLamaStudi getStatistikLamaStudi(String kodeProdi) {
        List<Map> results = new ArrayList<Map>();
        StatistikLamaStudi statistikLamaStudi = new StatistikLamaStudi();

        String sql = "SELECT IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)), " +
                " CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan, " +
                " tgl_yudisium, prg.Nama_prg, yud.* FROM db_" + kodeProdi + ".yud" + kodeProdi + " yud " +
                " INNER JOIN kamus.prg_std prg ON prg.Kd_prg = '" + kodeProdi + "' ORDER BY angkatan;";

        if (isTabelYudExist(kodeProdi)) {
            results = ClassConnection.getJdbc().queryForList(sql);
            for (Map result : results) {
                DateTime dateTimeYudisium = new DateTime(result.get("tgl_yudisium"));
                int bulanLulus = dateTimeYudisium.getMonthOfYear();
                int lamaTahun = dateTimeYudisium.getYear() - Integer.parseInt(result.get("angkatan").toString());
                int semesterLulus = 0;
                if (bulanLulus <= 8) {
                    semesterLulus = lamaTahun * 2 + 1;
                } else {
                    semesterLulus = lamaTahun * 2;
                }
                statistikLamaStudi.addSemesterValue(semesterLulus);
                statistikLamaStudi.setProdi(result.get("Nama_prg").toString());
            }
        }

        return statistikLamaStudi;

    }

    public List<StatistikLamaStudi> getStatistikLamaStudi() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<StatistikLamaStudi> statistikLamaStudis = new ArrayList<StatistikLamaStudi>();
        List<Map> results = new ArrayList<Map>();
        for (Map prodi : this.getProdi()) {
            String kodeProdi = prodi.get("Kd_prg").toString();
            String sql = "SELECT IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)), " +
                    " CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan, " +
                    " tgl_yudisium, prg.Nama_prg, yud.* FROM db_" + kodeProdi + ".yud" + kodeProdi + " yud " +
                    " INNER JOIN kamus.prg_std prg ON prg.Kd_prg = '" + kodeProdi + "' ORDER BY angkatan;";

            if (isTabelYudExist(kodeProdi)) {
                StatistikLamaStudi statistikLamaStudi = new StatistikLamaStudi();
                results = ClassConnection.getJdbc().queryForList(sql);
                statistikLamaStudi.setProdi(prodi.get("Nama_prg").toString());
                for (Map result : results) {
                    Map map = new HashMap();
                    DateTime dateTimeYudisium = new DateTime(result.get("tgl_yudisium"));
                    int bulanLulus = dateTimeYudisium.getMonthOfYear();
                    int lamaTahun = dateTimeYudisium.getYear() - Integer.parseInt(result.get("angkatan").toString());
                    int semesterLulus = 0;
                    if (bulanLulus <= 8) {
                        semesterLulus = lamaTahun * 2 + 1;
                    } else {
                        semesterLulus = lamaTahun * 2;
                    }
                    statistikLamaStudi.addSemesterValue(semesterLulus);
                }
                statistikLamaStudis.add(statistikLamaStudi);
            }
        }

        return statistikLamaStudis;

    }

    public boolean isTabelYudExist(String kodeProdi) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'yud" + kodeProdi + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }
}
