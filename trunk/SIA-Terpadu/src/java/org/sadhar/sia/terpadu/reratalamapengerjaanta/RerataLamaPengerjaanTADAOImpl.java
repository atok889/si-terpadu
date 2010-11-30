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
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Deny Prasetyo
 */
public class RerataLamaPengerjaanTADAOImpl implements RerataLamaPengerjaanTADAO {

    private List<RerataLamaPengerjaanTA> record = null;

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
        if (record == null) {
            record = getRecord();
        }

        for (RerataLamaPengerjaanTA rta : record) {
            dataset.addValue(rta.getLama(), rta.getTahun(), rta.getProdi());
        }
        return dataset;
    }

    public List<RerataLamaPengerjaanTA> getRecord() throws Exception {
        List<ProgramStudi> listProdi = getProgramStudi();
        List<String> listTahun = RerataLamaPengerjaanTA.tahunAngkatanList;

        List<RerataLamaPengerjaanTA> result = new ArrayList<RerataLamaPengerjaanTA>();

        for (ProgramStudi prodi : listProdi) {
            for (String tahun : listTahun) {
                String sql = "SELECT s.nomor_mhs,"
                        + "AVG(PERIOD_DIFF( "
                        + " DATE_FORMAT(s.tglUjian,'%Y%m'), "
                        + "  IF(SUBSTRING(s.ambil,5,1) = '1',INSERT(s.ambil,5,1,'08'),INSERT(s.ambil,5,1,'02')) "
                        + " )) AS Lama_pengerjaan"
                        + " FROM db_" + prodi.getKode() + ".skr" + prodi.getKode() + " s WHERE "
                        + " (s.tgl_awal_ambil != '00-00-0000')"
                        + " AND(s.tglUjian != '00-00-0000')"
                        + " AND(s.ambil LIKE '20" + tahun + "%')";
                try {
                    List<Map> row = ClassConnection.getJdbc().queryForList(sql);

                    RerataLamaPengerjaanTA rlpta = new RerataLamaPengerjaanTA();
                    rlpta.setProdi(prodi.getNama());
                    rlpta.setTahun("20" + tahun);

                    for (Map m : row) {
                        double d = ClassAntiNull.AntiNullDouble(m.get("Lama_pengerjaan"));
                        rlpta.setLama(d);
                    }
                    System.out.println(sql);
                    System.out.println(rlpta.getProdi() + "-" + rlpta.getTahun() + "-" + rlpta.getLama());
                    result.add(rlpta);
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
        return result;
    }

    public double getAvBulanByProdi(String prodi) throws Exception {
        if (record == null) {
            record = getRecord();
        }

        double count = 0;
        double total = 0;
        for (RerataLamaPengerjaanTA rta : record) {
            if (rta.getProdi().equalsIgnoreCase(prodi)) {
                total += rta.getLama();
                count++;
            }
        }

        if (count == 0) {
            return 0;
        } else {
            return total / count;
        }
    }

    public double getAvBulanByTahun(String tahun) throws Exception {
        if (record == null) {
            record = getRecord();
        }

        double count = 0;
        double total = 0;
        for (RerataLamaPengerjaanTA rta : record) {
            if (rta.getTahun().equalsIgnoreCase(tahun)) {
                total += rta.getLama();
                count++;
            }
        }

        if (count == 0) {
            return 0;
        } else {
            return total / count;
        }
    }

    public double getAvTotal() throws Exception {
        if (record == null) {
            record = getRecord();
        }

        double count = 0;
        double total = 0;
        for (RerataLamaPengerjaanTA rta : record) {
            total += rta.getLama();
            count++;
        }

        if (count == 0) {
            return 0;
        } else {
            return total / count;
        }
    }
    /*
     *
    sql = "SELECT s.nomor_mhs,"
    + "AVG(PERIOD_DIFF( "
    + " DATE_FORMAT(s.tglUjian,'%Y%m'), "
    + "  IF(SUBSTRING(s.ambil,5,1) = '1',INSERT(s.ambil,5,1,'08'),INSERT(s.ambil,5,1,'02')) "
    + " )) AS Lama_pengerjaan"
    + " FROM db_" + ps.getKode() + ".skr" + ps.getKode() + " s WHERE "
    + " (s.tgl_awal_ambil != '00-00-0000')"
    + " AND(s.tglUjian != '00-00-0000')"
    + " AND(s.ambil LIKE '" + thnAngkatan + "%')";

     *
     */
}
