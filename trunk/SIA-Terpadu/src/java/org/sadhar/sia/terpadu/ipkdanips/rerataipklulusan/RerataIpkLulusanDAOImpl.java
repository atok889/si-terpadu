/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataipklulusan;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class RerataIpkLulusanDAOImpl implements RerataIpkLulusanDAO {

    public RerataIpkLulusanDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std ORDER BY Nama_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }
}
