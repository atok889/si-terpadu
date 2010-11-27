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
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getStatistikLamaStudi(String kodeProdi, String semester) {
        List<Map> results = new ArrayList<Map>();
        if (semester == null) {
            if (isTabelYudExist(kodeProdi)) {
                String createViewMaster = "create or replace view kamus.rdm_master_mhs_vi as " +
                        " (select if((left(`yud`.`nomor_mhs`,1) = '9'),concat('19',left(`yud`.`nomor_mhs`,2)),if((left(`yud`.`nomor_mhs`,1) = '8')," +
                        " concat('19',left(`yud`.`nomor_mhs`,2)),concat('20',left(`yud`.`nomor_mhs`,2)))) AS `angkatan`,`yud`.`tgl_yudisium` AS `tgl_yudisium`," +
                        " month(`yud`.`tgl_yudisium`) AS `blnLulus`,year(`yud`.`tgl_yudisium`) AS `tahunLulus`,`prg`.`Nama_prg` AS `Nama_prg`,`yud`.`nomor_mhs` AS `nomor_mhs`" +
                        " from (db_" + kodeProdi + ".yud" + kodeProdi + " `yud` join `kamus`.`prg_std` `prg` on((`prg`.`Kd_prg` = " + kodeProdi + "))) order by if((left(`yud`.`nomor_mhs`,1) = '9')," +
                        " concat('19',left(`yud`.`nomor_mhs`,2)),if((left(`yud`.`nomor_mhs`,1) = '8'),concat('19',left(`yud`.`nomor_mhs`,2)),concat('20',left(`yud`.`nomor_mhs`,2)))))";
                ClassConnection.getJdbc().execute(createViewMaster);

                String createViewCuti = "create or replace view kamus.rdm_cuti_vi as " +
                        " (select 0 AS `lamastudi`,`ppj`.`Nomor_mhs` AS `nomor_mhs`,count(`ppj`.`Nomor_mhs`) AS `jumlahcuti`,' ' as Nama_prg from db_" + kodeProdi + ".perpanjanganstudi" + kodeProdi + " `ppj` " +
                        " where (`ppj`.`Jenis` = '3') group by `ppj`.`Nomor_mhs`)";
                ClassConnection.getJdbc().execute(createViewCuti);

                String createViewHasil = "create or replace view kamus.rdm_hasil_vi as " +
                        " select if(blnLulus<=8,(tahunLulus-angkatan)*2+1,(tahunLulus-angkatan)*2) as lamastudi, nomor_mhs, 0 as jumlahcuti,x.Nama_prg" +
                        "  from kamus.rdm_master_mhs_vi x group by nomor_mhs " +
                        " union select *from kamus.rdm_cuti_vi";
                ClassConnection.getJdbc().execute(createViewHasil);

                String createViewHasilAkhir = "create or replace view kamus.rdm_hasil_akhir_vi as ( " +
                        " select nomor_mhs, Nama_prg, lamastudi-sum(jumlahcuti) as lamastudi from kamus.rdm_hasil_vi  group by nomor_mhs) ";
                ClassConnection.getJdbc().execute(createViewHasilAkhir);

                String sql = "select  Nama_prg as prodi, count(lamastudi) as jumlah,CAST(lamastudi AS CHAR) semester from kamus.rdm_hasil_akhir_vi where lamastudi > 0  group by lamastudi";

                results = ClassConnection.getJdbc().queryForList(sql);
            }
        } else {
            for (Map prodi : this.getProdi()) {
                String kode = prodi.get("Kd_prg").toString();
                if (isTabelYudExist(kode)) {
                    String createViewMaster = "create or replace view kamus.rdm_master_mhs_vi as " +
                            " (select if((left(`yud`.`nomor_mhs`,1) = '9'),concat('19',left(`yud`.`nomor_mhs`,2)),if((left(`yud`.`nomor_mhs`,1) = '8')," +
                            " concat('19',left(`yud`.`nomor_mhs`,2)),concat('20',left(`yud`.`nomor_mhs`,2)))) AS `angkatan`,`yud`.`tgl_yudisium` AS `tgl_yudisium`," +
                            " month(`yud`.`tgl_yudisium`) AS `blnLulus`,year(`yud`.`tgl_yudisium`) AS `tahunLulus`,`prg`.`Nama_prg` AS `Nama_prg`,`yud`.`nomor_mhs` AS `nomor_mhs`" +
                            " from (db_" + kode + ".yud" + kode + " `yud` join `kamus`.`prg_std` `prg` on((`prg`.`Kd_prg` = " + kode + "))) order by if((left(`yud`.`nomor_mhs`,1) = '9')," +
                            " concat('19',left(`yud`.`nomor_mhs`,2)),if((left(`yud`.`nomor_mhs`,1) = '8'),concat('19',left(`yud`.`nomor_mhs`,2)),concat('20',left(`yud`.`nomor_mhs`,2)))))";
                    ClassConnection.getJdbc().execute(createViewMaster);

                    String createViewCuti = "create or replace view kamus.rdm_cuti_vi as " +
                            " (select 0 AS `lamastudi`,`ppj`.`Nomor_mhs` AS `nomor_mhs`,count(`ppj`.`Nomor_mhs`) AS `jumlahcuti`,' ' as Nama_prg from db_" + kode + ".perpanjanganstudi" + kode + " `ppj` " +
                            " where (`ppj`.`Jenis` = '3') group by `ppj`.`Nomor_mhs`)";
                    ClassConnection.getJdbc().execute(createViewCuti);

                    String createViewHasil = "create or replace view kamus.rdm_hasil_vi as " +
                            " select if(blnLulus<=8,(tahunLulus-angkatan)*2+1,(tahunLulus-angkatan)*2) as lamastudi, nomor_mhs, 0 as jumlahcuti,x.Nama_prg" +
                            "  from kamus.rdm_master_mhs_vi x group by nomor_mhs " +
                            " union select *from kamus.rdm_cuti_vi";
                    ClassConnection.getJdbc().execute(createViewHasil);

                    String createViewHasilAkhir = "create or replace view kamus.rdm_hasil_akhir_vi as ( " +
                            " select nomor_mhs, Nama_prg, lamastudi-sum(jumlahcuti) as lamastudi from kamus.rdm_hasil_vi  group by nomor_mhs) ";
                    ClassConnection.getJdbc().execute(createViewHasilAkhir);

                    String sql = "select  Nama_prg as prodi, count(lamastudi) as jumlah,CAST(lamastudi AS CHAR) semester from kamus.rdm_hasil_akhir_vi " +
                            " where lamastudi > 0  AND  lamastudi = " + semester + " group by lamastudi";

                    results.addAll(ClassConnection.getJdbc().queryForList(sql));
                }
            }
        }
        return results;
    }

//    public StatistikLamaStudi getStatistikLamaStudi(String kodeProdi) {
//        List<Map> results = new ArrayList<Map>();
//        StatistikLamaStudi statistikLamaStudi = new StatistikLamaStudi();
//
//        String sql = "SELECT IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
//                " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)), " +
//                " CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan, " +
//                " tgl_yudisium, prg.Nama_prg, yud.* FROM db_" + kodeProdi + ".yud" + kodeProdi + " yud " +
//                " INNER JOIN kamus.prg_std prg ON prg.Kd_prg = '" + kodeProdi + "' ORDER BY angkatan;";
//
//        if (isTabelYudExist(kodeProdi)) {
//            results = ClassConnection.getJdbc().queryForList(sql);
//            for (Map result : results) {
//                DateTime dateTimeYudisium = new DateTime(result.get("tgl_yudisium"));
//                int bulanLulus = dateTimeYudisium.getMonthOfYear();
//                int lamaTahun = dateTimeYudisium.getYear() - Integer.parseInt(result.get("angkatan").toString());
//                int semesterLulus = 0;
//                if (bulanLulus <= 8) {
//                    semesterLulus = lamaTahun * 2 + 1;
//                } else {
//                    semesterLulus = lamaTahun * 2;
//                }
//                statistikLamaStudi.addSemesterValue(semesterLulus);
//                statistikLamaStudi.setProdi(result.get("Nama_prg").toString());
//            }
//        }
//
//        return statistikLamaStudi;
//
//    }
//
//    public List<StatistikLamaStudi> getStatistikLamaStudi() {
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        List<StatistikLamaStudi> statistikLamaStudis = new ArrayList<StatistikLamaStudi>();
//        List<Map> results = new ArrayList<Map>();
//        for (Map prodi : this.getProdi()) {
//            String kodeProdi = prodi.get("Kd_prg").toString();
//            String sql = "SELECT IF(LEFT(nomor_mhs,1)='9',CONCAT('19',LEFT(nomor_mhs,2)), " +
//                    " IF(LEFT(nomor_mhs,1)='8',CONCAT('19',LEFT(nomor_mhs,2)), " +
//                    " CONCAT('20',LEFT(nomor_mhs,2)))) AS angkatan, " +
//                    " tgl_yudisium, prg.Nama_prg, yud.* FROM db_" + kodeProdi + ".yud" + kodeProdi + " yud " +
//                    " INNER JOIN kamus.prg_std prg ON prg.Kd_prg = '" + kodeProdi + "' ORDER BY angkatan;";
//
//            if (isTabelYudExist(kodeProdi)) {
//                StatistikLamaStudi statistikLamaStudi = new StatistikLamaStudi();
//                results = ClassConnection.getJdbc().queryForList(sql);
//                statistikLamaStudi.setProdi(prodi.get("Nama_prg").toString());
//                for (Map result : results) {
//                    Map map = new HashMap();
//                    DateTime dateTimeYudisium = new DateTime(result.get("tgl_yudisium"));
//                    int bulanLulus = dateTimeYudisium.getMonthOfYear();
//                    int lamaTahun = dateTimeYudisium.getYear() - Integer.parseInt(result.get("angkatan").toString());
//                    int semesterLulus = 0;
//                    if (bulanLulus <= 8) {
//                        semesterLulus = lamaTahun * 2 + 1;
//                    } else {
//                        semesterLulus = lamaTahun * 2;
//                    }
//                    statistikLamaStudi.addSemesterValue(semesterLulus);
//                }
//                statistikLamaStudis.add(statistikLamaStudi);
//            }
//        }
//
//        return statistikLamaStudis;
//
//    }
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
