/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.reratalamapengerjaanta;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Deny Prasetyo
 */
public class RerataLamaPengerjaanTADAOImpl implements RerataLamaPengerjaanTADAO {

    private List<RerataLamaPengerjaanTA> data;

    public RerataLamaPengerjaanTADAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<ProgramStudi> getProgramStudi() throws Exception {
        List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
        String sql = "SELECT Kd_prg,Nama_prg FROM kamus.prg_std ORDER BY Kd_prg";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            ProgramStudi ps = new ProgramStudi();
            ps.setKode(m.get("Kd_prg").toString());
            ps.setNama(m.get("Nama_prg").toString());
            progdis.add(ps);
        }
        return progdis;
    }

    public CategoryDataset getDataset() throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";

        List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
        progdis = getProgramStudi();

        RerataLamaPengerjaanTA rls = new RerataLamaPengerjaanTA();

        for (ProgramStudi ps : progdis) {

            for (String thnAngkatan : rls.getTahunAngkatanList()) {
                sql = "SELECT s.nomor_mhs,"
                        + "avg( PERIOD_DIFF(DATE_FORMAT(s.tglUjian,'%Y%m'),DATE_FORMAT(s.tgl_awal_ambil,'%Y%m'))) AS rerata_pengerjaan"
                        + " FROM db_" + ps.getKode() + ".skr" + ps.getKode() + " s WHERE s.nomor_mhs LIKE '" + thnAngkatan + "%'";

                System.out.println(sql);
                List<Map> rows = null;
                try {
                    rows = ClassConnection.getJdbc().queryForList(sql);
                    BigDecimal result = new BigDecimal(0);
                    for (Map m : rows) {
                        if(m.get("rerata_pengerjaan") !=null){
                            result = (BigDecimal) m.get("rerata_pengerjaan");
                        }
                    }

                    String year = "";

                    if (Integer.parseInt(thnAngkatan) > 80) {
                        year = "19" + thnAngkatan;
                    } else {
                        year = "20" + thnAngkatan;
                    }

                    dataset.addValue(result.doubleValue(), year, ps.getNama());
                } catch (DataAccessException dae) {
//                    dae.printStackTrace();
                    System.out.println("Data invalid Silahkan perbaiki Validitas dan Integritas Data");
                }
            }
        }
        return dataset;
    }

    public double getAvBulanByProdi(String prodi) throws Exception {
        if (data == null) {
            data = getRecord();
        }
        double count = 0;
        double total = 0;
        for (RerataLamaPengerjaanTA rls : data) {
            if (rls.getProdi().equalsIgnoreCase(prodi)) {
                total = total + rls.getLama();
                count += 1;
            }
        }

        if (count == 0) {
            return 0;
        } else {
            return total / count;
        }
    }

    public double getAvBulanByTahun(String tahun) throws Exception {
        if (data == null) {
            data = getRecord();
        }
        double count = 0;
        double total = 0;
        for (RerataLamaPengerjaanTA rls : data) {
            if (rls.getTahun().equalsIgnoreCase(tahun)) {
                total = total + rls.getLama();
                count += 1;
            }
        }

        if (count == 0) {
            return 0;
        } else {
            return total / count;
        }
    }

    public double getAvTotal() throws Exception {
        if (data == null) {
            data = getRecord();
        }
        double count = 0;
        double total = 0;
        for (RerataLamaPengerjaanTA rls : data) {
            total = total + rls.getLama();
            count += 1;
        }
        if (count == 0) {
            return 0;
        } else {
            return total / count;
        }
    }

    public List<RerataLamaPengerjaanTA> getRecord() throws Exception {
        List<RerataLamaPengerjaanTA> listRerata = new ArrayList<RerataLamaPengerjaanTA>();
        String sql = "";

        List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
        progdis = getProgramStudi();



        for (ProgramStudi ps : progdis) {

            for (String thnAngkatan : RerataLamaPengerjaanTA.tahunAngkatanList) {
                sql = "SELECT s.nomor_mhs AS NIM,DATE_FORMAT(s.tglUjian,'%Y%m') AS tanggal_ujian,"
                        + " DATE_FORMAT(s.tgl_awal_ambil,'%Y%m') AS tanggal_ambil, "
                        + " PERIOD_DIFF(DATE_FORMAT(s.tglUjian,'%Y%m'),DATE_FORMAT(s.tgl_awal_ambil,'%Y%m')) AS lama_pengerjaan"
                        + "FROM db_" + ps.getKode() + ".skr" + ps.getKode() + " s WHERE s.nomor_mhs LIKE '" + thnAngkatan + "%'";

                List<Map> rows = null;

                String year = "";

                if (Integer.parseInt(thnAngkatan) > 80) {
                    year = "19" + thnAngkatan;
                } else {
                    year = "20" + thnAngkatan;
                }
                try {
                    rows = ClassConnection.getJdbc().queryForList(sql);
                    for (Map m : rows) {
                        BigDecimal lamaPengerjaan = new BigDecimal(0);
                        try {
                            lamaPengerjaan = (BigDecimal) m.get("lama_pengerjaan");
                        } catch (NullPointerException e) {
                        }
                        RerataLamaPengerjaanTA rls = new RerataLamaPengerjaanTA();
                        rls.setTahun(year);
                        rls.setLama(lamaPengerjaan.doubleValue());
                        rls.setProdi(ps.getNama());
                        listRerata.add(rls);
                    }

                } catch (DataAccessException dae) {
                    System.out.println("Data invalid Silahkan perbaiki Validitas dan Integritas Data");
                }
            }
        }
        data = listRerata;
        return listRerata;
    }
}
