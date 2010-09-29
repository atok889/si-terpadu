/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswado;

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
public class JumlahMahasiswaDODAOImpl implements JumlahMahasiwaDODAO {

    public JumlahMahasiswaDODAOImpl() {
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

    public CategoryDataset getDataset(ProgramStudi progdi, String tahun) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (progdi != null && tahun.isEmpty()) {
            DateTime dt = new DateTime(new Date());
            sql = "SELECT YEAR(tgl_sk_do) AS tahun,COUNT(nomor_mhs) AS jumlah "
                    + "FROM db_" + progdi.getKode() + ".do" + progdi.getKode() + " WHERE YEAR(tgl_sk_do) BETWEEN '" + (dt.getYear() - 5) + "' AND '" + dt.getYear() + "' GROUP BY YEAR(tgl_sk_do)";

            List<Map> rows = null;
                try {
                    rows = ClassConnection.getJdbc().queryForList(sql);
                    for (Map m : rows) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("tahun").toString(), progdi.getNama());
                    }
                } catch (DataAccessException dae) {
                    System.out.println("Data invalid Silahkan perbaiki Validitas dan Integritas Data");
                }

        } else if (progdi != null && !tahun.isEmpty()) {
            sql = "SELECT YEAR(tgl_sk_do) AS tahun,COUNT(nomor_mhs) AS jumlah "
                    + "FROM db_" + progdi.getKode() + ".do" + progdi.getKode() + " WHERE YEAR(tgl_sk_do)='" + tahun + "' GROUP BY YEAR(tgl_sk_do)";
            List<Map> rows = null;
            try {
                rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("tahun").toString(), progdi.getNama());
                }
            } catch (DataAccessException dae) {
              System.out.println("Data invalid Silahkan perbaiki Validitas dan Integritas Data");
            }

        } else if (progdi == null && !tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();
            //for sampe
//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));

            for (ProgramStudi ps : progdis) {
                sql = "SELECT YEAR(tgl_sk_do) AS tahun,COUNT(nomor_mhs) AS jumlah "
                        + "FROM db_" + ps.getKode() + ".do" + ps.getKode() + " WHERE YEAR(tgl_sk_do)='" + tahun + "' GROUP BY YEAR(tgl_sk_do)";
                List<Map> rows = null;
                try {
                    rows = ClassConnection.getJdbc().queryForList(sql);
                    for (Map m : rows) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("tahun").toString(), ps.getNama());
                    }
                } catch (DataAccessException dae) {
                    System.out.println("Data invalid Silahkan perbaiki Validitas dan Integritas Data");
                }

            }
        } else if (progdi == null && tahun.isEmpty()) {
            DateTime dt = new DateTime(new Date());
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();
            //for sampe
//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));

            for (ProgramStudi ps : progdis) {
                sql = "SELECT YEAR(tgl_sk_do) AS tahun,COUNT(nomor_mhs) AS jumlah "
                        + "FROM db_" + ps.getKode() + ".do" + ps.getKode() + " WHERE YEAR(tgl_sk_do) BETWEEN '" + (dt.getYear() - 5) + "' AND '" + dt.getYear() + "' GROUP BY YEAR(tgl_sk_do)";
                List<Map> rows = null;
                try {
                    rows = ClassConnection.getJdbc().queryForList(sql);
                    for (Map m : rows) {
                        dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("tahun").toString(), ps.getNama());
                    }
                } catch (DataAccessException dae) {
                    System.out.println("Data invalid Silahkan perbaiki Validitas dan Integritas Data");
                }

            }
        }

        return dataset;
    }
}
