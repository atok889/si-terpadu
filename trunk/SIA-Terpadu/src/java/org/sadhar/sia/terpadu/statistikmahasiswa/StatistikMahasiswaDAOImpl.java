/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistikmahasiswa;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class StatistikMahasiswaDAOImpl implements StatistikMahasiswaDAO {

    public StatistikMahasiswaDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> list() {
        String sql = "SELECT *FROM db_1114.rg111400131";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }
}
