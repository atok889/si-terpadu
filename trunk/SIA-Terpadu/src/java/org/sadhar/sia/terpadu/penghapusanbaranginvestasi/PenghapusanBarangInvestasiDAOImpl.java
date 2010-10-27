/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.penghapusanbaranginvestasi;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class PenghapusanBarangInvestasiDAOImpl implements PenghapusanBarangInvestasiDAO {

    public PenghapusanBarangInvestasiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getModelPenghapusanBarang() {
        String sql = "select * from asset.modelPenghapusanBarang";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getPenghapusanBarangInvestasi(String tahun, String kodeModel) {
        String sql = "select ab.tglHapus tanggal, sk.subKelompokBarangInvestasi, mp.modelPenghapusanBarang, COUNT(ab.kodeSubKelompokBarangInvestasi) " +
                " as jumlah from  asset.baranginvestasi ab, kamus.subkelompokbaranginvestasi sk," +
                " asset.modelpenghapusanbarang mp where ab.tglHapus > 0 and" +
                " sk.kodeSubKelompokBarangInvestasi = ab.kodeSubKelompokBarangInvestasi and" +
                " ab.kodeModelPenghapusan = " + kodeModel + " and mp.kodeModelPenghapusanBarang = ab.kodeModelPenghapusan and" +
                " YEAR(ab.tglHapus)= '" + tahun + "'";
        return ClassConnection.getJdbc().queryForList(sql);
    }
}
