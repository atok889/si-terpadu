/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.reratalamapengerjaanta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
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
        String sql = "SELECT Kd_prg,Nama_prg FROM kamus.prg_std  WHERE  Kd_prg != '0000' ORDER BY Kd_prg";
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

        List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
        progdis = getProgramStudi();
        RerataLamaPengerjaanTA rls = new RerataLamaPengerjaanTA();

        if (data == null) {
            data = getRecord();
        }

        for (ProgramStudi ps : progdis) {

            for (String thnAngkatan : rls.getTahunAngkatanList()) {
                String year = "";
                if (Integer.parseInt(thnAngkatan) > 80) {
                    year = "19" + thnAngkatan;
                } else {
                    year = "20" + thnAngkatan;
                }

                double count = 0;
                double total = 0;
                for (RerataLamaPengerjaanTA rlpta : data) {
                    if (rlpta.getProdi().equalsIgnoreCase(ps.getNama()) && rlpta.getTahun().equalsIgnoreCase(year)) {
                        total = total + rlpta.getLama();
                        count += 1;
//                        System.out.println(ps.getNama() + "-" + year + " ==> " + rlpta.getLama());
                    }
                }
                double rerata = 0.0;
                if (count == 0) {
                    rerata = 0.0;
                } else {
                    rerata = total / count;
                }

                dataset.addValue(rerata, year, ps.getNama());
            }
        }

        return dataset;
    }

    public CategoryDataset getDatasetx() throws Exception {
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

                List<Map> rows = null;
                try {
                    rows = ClassConnection.getJdbc().queryForList(sql);
                    BigDecimal result = new BigDecimal(0);
                    for (Map m : rows) {
                        if (m.get("rerata_pengerjaan") != null) {
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
                    System.out.println("Data invalid " + dae.getMessage());
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
                sql = "SELECT s.tglUjian,s.tgl_awal_ambil,s.nomor_mhs AS NIM,DATE_FORMAT(s.tglUjian,'%Y%m') AS tanggal_ujian,"
                        + " DATE_FORMAT(s.tgl_awal_ambil,'%Y%m') AS tanggal_ambil, "
                        + " PERIOD_DIFF(DATE_FORMAT(s.tglUjian,'%Y%m'),DATE_FORMAT(s.tgl_awal_ambil,'%Y%m')) AS lama_pengerjaan"
                        + " FROM db_" + ps.getKode() + ".skr" + ps.getKode() + " s WHERE s.nomor_mhs LIKE '" + thnAngkatan + "%'";

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
                        double lamaPengerjaan = 0.0;
                        Date tglUjian = (Date) m.get("tglUjian");
                        Date tglAwalAmbil = (Date) m.get("tgl_awal_ambil");
                        Object lamaO = m.get("lama_pengerjaan");
                        try {
                            if (tglUjian == null || tglAwalAmbil == null || lamaO == null) {
                            } else {
//                                System.out.println(tglUjian.getTime() + "-" + tglAwalAmbil.getTime());
                                if (lamaO instanceof BigDecimal) {
                                    lamaPengerjaan = ((BigDecimal) m.get("lama_pengerjaan")).doubleValue();
                                } else {
                                    lamaPengerjaan = ((Long) m.get("lama_pengerjaan")).doubleValue();
                                }
                                if (lamaPengerjaan < 0) {
                                    lamaPengerjaan = 0;
                                }
                            }
                        } catch (NullPointerException e) {
                            System.out.println("NPE " + e.getMessage());
                        }
                        RerataLamaPengerjaanTA rls = new RerataLamaPengerjaanTA();
                        rls.setTahun(year);
                        rls.setLama(lamaPengerjaan);
                        rls.setProdi(ps.getNama());
                        listRerata.add(rls);
                    }

                } catch (DataAccessException dae) {
                    System.out.println("Data invalid " + dae.getMessage());
                }
            }
        }
        data = listRerata;
        return listRerata;
    }
}
