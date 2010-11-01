/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahpegawaiadministratif;

import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.unkerja.UnitKerja;

/**
 *
 * @author Hendro Steven
 */
public class JumlahPegawaiDAOImpl implements JumlahPegawaiDAO {

    public CategoryDataset getJumlahPegawai(UnitKerja uk) {
        //List<JumlahPegawai> data = new ArrayList<JumlahPegawai>();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "";
        if (uk == null) {
            sql = "SELECT COUNT(unit_peg.kdPegawai) AS jumlah "
                    + "FROM ((personalia.pegawai pegawai "
                    + "INNER JOIN personalia.unit_peg unit_peg "
                    + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                    + "LEFT JOIN personalia.pensiun pensiun "
                    + "ON (pegawai.kdPegawai = pensiun.kdPegawai)) "
                    + "WHERE (pegawai.Status_keluar = '1' "
                    + "OR pegawai.Status_keluar = '6' OR pegawai.Status_keluar = '7') AND "
                    + "(YEAR(unit_peg.tgl_sk_unit)<=? AND (ISNULL(pensiun.Tgl_sk_pensiun) OR "
                    + "YEAR(pensiun.Tgl_sk_pensiun)>=?))";
            DateTime dt = new DateTime();
            for (int thn = dt.getYear() - 10; thn <= dt.getYear(); thn++) {
                final int tahun = thn;
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql,new Object[]{thn,thn});
                for(Map mp:rows){
                    JumlahPegawai jp = new JumlahPegawai();
                    jp.setTahun(tahun+"");
                    jp.setJumlah(Integer.valueOf(mp.get("jumlah").toString()));
                    dataset.addValue(jp.getJumlah(), jp.getTahun(), "");
                }
            }
        } else {
            sql = "SELECT COUNT(unit_peg.kdPegawai) AS jumlah "
                    + "FROM ((personalia.pegawai pegawai "
                    + "INNER JOIN personalia.unit_peg unit_peg "
                    + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                    + "LEFT JOIN personalia.pensiun pensiun "
                    + "ON (pegawai.kdPegawai = pensiun.kdPegawai)) "
                    + "WHERE (pegawai.Status_keluar = '1' "
                    + "OR pegawai.Status_keluar = '6' OR pegawai.Status_keluar = '7') AND "
                    + "(YEAR(unit_peg.tgl_sk_unit)<=? AND (ISNULL(pensiun.Tgl_sk_pensiun) OR "
                    + "YEAR(pensiun.Tgl_sk_pensiun)>=?)) AND unit_peg.kd_unit=?";
            DateTime dt = new DateTime();
            for (int thn = dt.getYear() - 10; thn <= dt.getYear(); thn++) {
                final int tahun = thn;
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql,new Object[]{thn,thn,uk.getKode()});
                for(Map mp:rows){
                    JumlahPegawai jp = new JumlahPegawai();
                    jp.setTahun(tahun+"");
                    jp.setJumlah(Integer.valueOf(mp.get("jumlah").toString()));
                    dataset.addValue(jp.getJumlah(), jp.getTahun(), "");
                }
            }
        }
        return dataset;
    }
}
