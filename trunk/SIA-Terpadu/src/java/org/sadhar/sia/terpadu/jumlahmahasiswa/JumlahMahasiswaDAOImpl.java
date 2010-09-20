/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author Hendro Steven
 */
public class JumlahMahasiswaDAOImpl implements JumlahMahasiwaDAO {

    public JumlahMahasiswaDAOImpl() {
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
            sql = "SELECT YEAR(tglmskusd) AS tahun,COUNT(nomor_mhs) AS jumlah "
                    + "FROM db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " WHERE YEAR(tglmskusd) BETWEEN '" + (dt.getYear() - 5) + "' AND '" + dt.getYear() + "' GROUP BY YEAR(tglmskusd)";

            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("tahun").toString(), progdi.getNama());
            }
        } else if (progdi != null && !tahun.isEmpty()) {
            sql = "SELECT YEAR(tglmskusd) AS tahun,COUNT(nomor_mhs) AS jumlah "
                    + "FROM db_" + progdi.getKode() + ".mhs" + progdi.getKode() + " WHERE YEAR(tglmskusd)='" + tahun + "' GROUP BY YEAR(tglmskusd)";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("tahun").toString(), progdi.getNama());
            }
        } else if (progdi == null && !tahun.isEmpty()) {
            List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
            progdis = getProgramStudi();
            //for sampe
//            progdis.add(new ProgramStudi("5013", "MEKATRONIKA (D3)"));
//            progdis.add(new ProgramStudi("5314", "TEKNIK INFORMATIKA"));

            for (ProgramStudi ps : progdis) {
                sql = "SELECT YEAR(tglmskusd) AS tahun,COUNT(nomor_mhs) AS jumlah "
                        + "FROM db_" + ps.getKode() + ".mhs" + ps.getKode() + " WHERE YEAR(tglmskusd)='" + tahun + "' GROUP BY YEAR(tglmskusd)";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("tahun").toString(), ps.getNama());
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
                sql = "SELECT YEAR(tglmskusd) AS tahun,COUNT(nomor_mhs) AS jumlah "
                        + "FROM db_" + ps.getKode() + ".mhs" + ps.getKode() + " WHERE YEAR(tglmskusd) BETWEEN '" + (dt.getYear() - 5) + "' AND '" + dt.getYear() + "' GROUP BY YEAR(tglmskusd)";
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), m.get("tahun").toString(), ps.getNama());
                }
            }
        }

        return dataset;
    }
}
