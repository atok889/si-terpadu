/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftarmatakuliahyangpalingseringdiulang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author kris
 */
public class DaftarMataKuliahYangPalingSeringDiulangDAOImpl implements DaftarMataKuliahYangPalingSeringDiulangDAO {

    public DaftarMataKuliahYangPalingSeringDiulangDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getMataKuliah(String kodeProdi) {
        String sql = "SELECT distinct nama_mtk FROM db_" + kodeProdi + ".mtk" + kodeProdi + " WHERE kd_mtk!='' ";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getDaftarMataKuliahYangPalingSeringDiulang(String kodeProdi, String tahunSemester) {
        List<Map> results = new ArrayList<Map>();
        String sql = "SELECT mtk.kd_mtk,mtk.nama_mtk,dosen.nama_peg, " +
                " IF(LEFT(kr.nomor_mhs, 1)='9', CONCAT('19', LEFT(kr.nomor_mhs, 2)), IF(LEFT(kr.nomor_mhs, 1)='8'," +
                " CONCAT('19', LEFT(kr.nomor_mhs,2)), CONCAT('20', LEFT(kr.nomor_mhs, 2)))) AS angkatan, mtk.SKS " +
                " FROM db_" + kodeProdi + ".kr" + kodeProdi + tahunSemester + " kr " +
                " INNER JOIN db_" + kodeProdi + ".mtk" + kodeProdi + " mtk ON kr.kd_mtk =  mtk.kd_mtk " +
                " LEFT  JOIN db_" + kodeProdi + ".dosen" + kodeProdi + " dosen ON mtk.DsnKoordinator = dosen.npp " +
                " WHERE kr.ulang = 'Y' AND kr.kd_mtk != ''";

        List<Map> datas = new ArrayList<Map>();
        if (isTabelKrExist(kodeProdi, tahunSemester)) {
            results = ClassConnection.getJdbc().queryForList(sql);
            for (Map mk : this.getMataKuliah(kodeProdi)) {
                Map map = new HashMap();
                int count = 0;
                String namaMK = null;
                String namaDosen = null;
                int sks = 0;
                for (Map result : results) {
                    if (result.get("nama_mtk").toString().equalsIgnoreCase(mk.get("nama_mtk").toString())) {
                        count++;
                        namaMK = result.get("nama_mtk").toString();
                        sks = Integer.valueOf(result.get("SKS").toString());
                        if (result.get("nama_peg") != null) {
                            namaDosen = result.get("nama_peg").toString();
                        } else {
                            namaDosen = "Tanpa Nama";
                        }
                    }
                }
                if (namaMK != null) {
                    map.put("dosen", namaDosen);
                    map.put("mataKuliah", namaMK);
                    map.put("jumlah", count);
                    map.put("semester", tahunSemester.substring(4, 5));
                    map.put("sks", sks);
                    datas.add(map);
                }
            }
        }
        return datas;
    }

    public boolean isTabelKrExist(String kodeProdi, String tahun) {
        SqlRowSet rs = null;
        try {
            String sql = "Show tables from db_" + kodeProdi + " like 'kr" + kodeProdi + tahun + "'";
            rs = ClassConnection.getJdbc().queryForRowSet(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs.next();
    }
}
