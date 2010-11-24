/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.bantuan;

import java.util.Date;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author Hendro Steven
 */
public class BantuanDAOImpl implements BantuanDAO {

    public BantuanDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public void insert(Bantuan bantuan) throws Exception {
        String sql = "INSERT INTO usd.bantuan(nama,jenis,pesan,tanggal) VALUES(?,?,?,?)";
        //DateTime dt = new DateTime(new Date());
        ClassConnection.getJdbc().update(sql, new Object[]{bantuan.getNama(), bantuan.getJenis(), bantuan.getPesan(), bantuan.getTanggal()});
    }
}
