/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.datapegawai.rekruitmen;

import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.unkerja.UnitKerja;

/**
 *
 * @author Hendro Steven
 */
public class JumlahRekruitmenPegawaiDAOImpl implements JumlahRekruitmenPegawaiDAO {

    public JumlahRekruitmenPegawaiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public CategoryDataset getJumlahRekruitmen(UnitKerja unKerja) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (unKerja == null) {
            sql = "SELECT year(dt_status_peg.tgl_sk_status) as tahun, count(pegawai.kdPegawai) AS jumlah "
                    + "FROM    personalia.pegawai pegawai "
                    + "INNER JOIN "
                    + "personalia.dt_status_peg dt_status_peg "
                    + "ON (pegawai.kdPegawai = dt_status_peg.kdPegawai) "
                    + "WHERE (pegawai.stat_peg = '1') and "
                    + "year(dt_status_peg.tgl_sk_status) between '2000' and year(curdate()) "
                    + "group by year(dt_status_peg.tgl_sk_status) "
                    + "order by year(dt_status_peg.tgl_sk_status)";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), "Jumlah", m.get("tahun").toString());
            }
        } else {
            sql = "SELECT year(dt_status_peg.tgl_sk_status) as tahun, count(pegawai.kdPegawai) AS jumlah "
                    + "FROM   (personalia.pegawai pegawai "
                    + "INNER JOIN personalia.unit_peg unit_peg "
                    + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                    + "INNER JOIN personalia.dt_status_peg dt_status_peg "
                    + "ON (pegawai.kdPegawai = dt_status_peg.kdPegawai) "
                    + "WHERE (pegawai.stat_peg = '1') and "
                    + "year(dt_status_peg.tgl_sk_status) between '2000' and year(curdate()) and "
                    + "unit_peg.kd_unit=? "
                    + "group by year(dt_status_peg.tgl_sk_status) "
                    + "order by year(dt_status_peg.tgl_sk_status)";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[]{unKerja.getKode()});
            for (Map m : rows) {
                dataset.addValue(Integer.valueOf(m.get("jumlah").toString()), "Jumlah", m.get("tahun").toString());
            }
        }
        return dataset;
    }
}
