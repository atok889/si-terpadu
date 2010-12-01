/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.bantuan;

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
        //tabel kamus.saran jenis_sia=99, jenis_menu=input terbuka, saran = input terbuka
        //String sql = "INSERT INTO usd.bantuan(nama,jenis,pesan,tanggal) VALUES(?,?,?,?)";
        String sql = "INSERT INTO kamus.saran(pengirim,jenis_sia,jenis_menu,saran,tanggal) VALUES(?,?,?,?,?)";
        //DateTime dt = new DateTime(new Date());
        ClassConnection.getJdbc().update(sql, new Object[]{bantuan.getNama(), bantuan.getJenisSIA(),bantuan.getJenisMenu(), bantuan.getPesan(), bantuan.getTanggal()});
    }
}
