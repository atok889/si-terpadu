/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahpenelitian.jumlahpenelitianperprodi;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class JumlahPenelitianPerProdiDAOImpl implements JumlahPenelitianPerProdiDAO {

    public JumlahPenelitianPerProdiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }
}
