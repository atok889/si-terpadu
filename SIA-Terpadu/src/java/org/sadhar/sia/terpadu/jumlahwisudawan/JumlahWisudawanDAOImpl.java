/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahwisudawan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
public class JumlahWisudawanDAOImpl implements JumlahWisudawanDAO {

    private List<JumlahWisudawan> data;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

    public JumlahWisudawanDAOImpl() {
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
        if (data == null) {
            data = getRecord();
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (JumlahWisudawan jw : data) {
            dataset.addValue(jw.getJumlah(), jw.getTanggal(), jw.getProdi());
        }
        return dataset;
    }

    public List<JumlahWisudawan> getRecord() throws Exception {
        data = new ArrayList<JumlahWisudawan>();
        String sql = "";

        List<ProgramStudi> progdis = new ArrayList<ProgramStudi>();
        progdis = getProgramStudi();

        for (ProgramStudi ps : progdis) {
            sql = "SELECT count(u.NoMhs) as jumlah,u.TglWsd AS tanggal,YEAR(u.TglWsd) AS tahun "
                    + " FROM db_" + ps.getKode() + ".urutwsd" + ps.getKode() + " u group by u.TglWsd";

            List<Map> rows = null;

            try {
                rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    String tahun;
                    if (m.get("tahun") instanceof Long) {
                        tahun = ((Long) m.get("tahun")).toString();
                    } else {
                        tahun = ((Integer) m.get("tahun")).toString();
                    }

                    String prodi = ps.getNama();
                    String tanggal = sdf.format(m.get("tanggal"));
                    long jumlah = (Long) m.get("jumlah");

                    JumlahWisudawan jw = new JumlahWisudawan();
                    jw.setTahun(tahun);
                    jw.setProdi(prodi);
                    jw.setJumlah(jumlah);
                    jw.setTanggal(tanggal);
                    data.add(jw);
                }
            } catch (DataAccessException dae) {
                System.out.println("Data invalid " + dae.getMessage());
            }
        }
        Collections.sort(data);

        for(JumlahWisudawan jw : data){
            System.out.println(jw.getTahun()+"-"+jw.getTanggal()+"-"+jw.getProdi()+" ==> "+jw.getJumlah());
        }
        return data;
    }
}
