/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.distribusipendaftarpergelombangpendaftaran;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author Hendro Steven
 */
public class DistribusiPendaftarDAOImpl implements DistribusiPendaftarDAO {

    public DistribusiPendaftarDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<DistribusiPendaftar> getBeanCollection(List<String> tahuns) throws Exception {
        List<DistribusiPendaftar> list = new ArrayList<DistribusiPendaftar>();
        for (String tahun : tahuns) {
            String sql = "SELECT SUBSTRING(nomor,3,1) AS gelombang,COUNT(nomor) AS jumlah "
                    + "FROM pmb.pdf" + tahun + " GROUP BY SUBSTRING(nomor,3,1);";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            DistribusiPendaftar dp = new DistribusiPendaftar();
            dp.setTahun(tahun);
            for (Map m : rows) {                
                if (m.get("gelombang").toString().trim().length() > 0) {
                    int gel = Integer.parseInt(m.get("gelombang").toString());
                    switch (gel) {
                        case 1:
                            dp.setGelombang1(Integer.parseInt(m.get("jumlah").toString()));
                            break;
                        case 2:
                            dp.setGelombang2(Integer.parseInt(m.get("jumlah").toString()));
                            break;
                        case 3:
                            dp.setGelombang3(Integer.parseInt(m.get("jumlah").toString()));
                            break;
                        case 0:
                            dp.setJalurKerjaSama(Integer.parseInt(m.get("jumlah").toString()));
                            break;
                        case 9:
                            dp.setJalurPrestasi(Integer.parseInt(m.get("jumlah").toString()));
                            break;
                    }
                }
            }
            list.add(dp);
        }
        return list;
    }
}
