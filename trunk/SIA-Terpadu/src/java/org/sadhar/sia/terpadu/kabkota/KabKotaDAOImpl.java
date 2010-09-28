/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.kabkota;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.provinsi.Provinsi;

/**
 *
 * @author Hendro Steven
 */
public class KabKotaDAOImpl implements KabKotaDAO {

    public List<KabKota> getKabKota(Provinsi provinsi) throws Exception {
        List<KabKota> kk = new ArrayList<KabKota>();
        String sql = "SELECT kd_kab,nama_kab FROM kamus.kabupaten WHERE SUBSTRING(kd_kab,1,2)='" + provinsi.getKode() + "'";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            KabKota kab = new KabKota();
            kab.setKode(m.get("kd_kab").toString());
            kab.setNama(m.get("nama_kab").toString());
            kk.add(kab);
        }
        return kk;
    }
}
