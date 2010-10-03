/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.reratalamastudi;

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
public class RerataLamaStudiDAOImpl implements RerataLamaStudiDAO {

    private List<RerataLamaStudi> data;

    private int lamaStudiBySemester(String nim, Date tanggalYudisium) {

        int tahunAngkatan = Integer.parseInt(nim.substring(0, 2));
        if (tahunAngkatan > 80) {
            tahunAngkatan = tahunAngkatan + 1900;
        } else {
            tahunAngkatan = tahunAngkatan + 2000;
        }


        DateTime dt = new DateTime(tanggalYudisium);


        int yearLulus = dt.getYear();

        int yearTempuh = yearLulus - tahunAngkatan;
        int semester = yearTempuh * 2;

        if (dt.getMonthOfYear() <= 1) {
            semester -= 2;
        } else if (dt.getMonthOfYear() <= 8) {
            semester -= 1;
        }

        return semester;
    }

    public RerataLamaStudiDAOImpl() {
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

        RerataLamaStudi rls = new RerataLamaStudi();

        for (ProgramStudi ps : progdis) {

            for (String thnAngkatan : rls.getTahunAngkatanList()) {
                sql = "SELECT nomor_mhs AS NIM,tgl_yudisium AS YUD "
                        + "FROM db_" + ps.getKode() + ".yud" + ps.getKode() + " WHERE nomor_mhs LIKE '" + thnAngkatan + "%'";

                List<Map> rows = null;
                try {
                    rows = ClassConnection.getJdbc().queryForList(sql);
                    int count = 0;
                    double total = 0;
                    for (Map m : rows) {
                        String nim = m.get("NIM").toString();
                        Date tanggalYudisium = (Date) m.get("YUD");
                        if (!nim.isEmpty() && tanggalYudisium != null) {
                            int semesters = lamaStudiBySemester(nim, tanggalYudisium);
                            count++;
                            total += (double) semesters;
                        }
                    }
                    double result;
                    if (count == 0) {
                        result = 0;
                    } else {
                        result = (double) total / (double) count;
                    }
                    String year = "";

                    if (Integer.parseInt(thnAngkatan) > 80) {
                        year = "19" + thnAngkatan;
                    } else {
                        year = "20" + thnAngkatan;
                    }

                    System.out.println(year + " hasil " + result + " dari Total " + total + " jumlah " + count);
                    dataset.addValue(result, year, ps.getNama());
                } catch (DataAccessException dae) {
                    System.out.println("Data invalid Silahkan perbaiki Validitas dan Integritas Data");
                }
            }
        }
        return dataset;
    }

    public double getAvSemesterByProdi(String prodi) throws Exception {
        if (data == null) {
            data = getRecord();
        }
        double count = 0;
        double total = 0;
        for (RerataLamaStudi rls : data) {
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

    public double getAvSemesterByTahun(String tahun) throws Exception {
        if (data == null) {
            data = getRecord();
        }
        double count = 0;
        double total = 0;
        for (RerataLamaStudi rls : data) {
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
        for (RerataLamaStudi rls : data) {
            total = total + rls.getLama();
            count += 1;
        }
        if (count == 0) {
            return 0;
        } else {
            return total / count;
        }
    }

    public List<RerataLamaStudi> getRecord() throws Exception {
        List<RerataLamaStudi> listRerata = new ArrayList<RerataLamaStudi>();
        String sql = "";

        List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
        progdis = getProgramStudi();



        for (ProgramStudi ps : progdis) {

            for (String thnAngkatan : RerataLamaStudi.tahunAngkatanList) {
                sql = "SELECT nomor_mhs AS NIM,tgl_yudisium AS YUD "
                        + "FROM db_" + ps.getKode() + ".yud" + ps.getKode() + " WHERE nomor_mhs LIKE '" + thnAngkatan + "%'";

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
                        String nim = m.get("NIM").toString();
                        Date tanggalYudisium = (Date) m.get("YUD");
                        if (!nim.isEmpty() && tanggalYudisium != null) {
                            int semesters = lamaStudiBySemester(nim, tanggalYudisium);
                            RerataLamaStudi rls = new RerataLamaStudi();
                            rls.setTahun(year);
                            rls.setLama((double) semesters);
                            rls.setProdi(ps.getNama());
                            listRerata.add(rls);
                        }
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
