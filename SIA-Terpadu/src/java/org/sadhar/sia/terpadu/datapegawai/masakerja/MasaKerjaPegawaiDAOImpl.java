/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.datapegawai.masakerja;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.unkerja.UnitKerja;

/**
 *
 * @author Hendro Steven
 */
public class MasaKerjaPegawaiDAOImpl implements MasaKerjaPegawaiDAO {

    public MasaKerjaPegawaiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public PieDataset getMasaKerja(UnitKerja unKerja) throws Exception {
        DefaultPieDataset dataset = new DefaultPieDataset();
        String sql = "";
        int j0_5 = 0;
        int j5_10 = 0;
        int j10_15 = 0;
        int j15_20 = 0;
        int j20_ = 0;
        double total = 0;
        if (unKerja == null) {
            sql = "SELECT  count(pegawai.kdPegawai) AS jumlah "
                    + ",(year(curdate())-year(dt_status_peg.tmt_status)) as masakerja  "
                    + "FROM "
                    + "personalia.pegawai pegawai "
                    + "INNER JOIN "
                    + "personalia.dt_status_peg dt_status_peg "
                    + "ON (pegawai.kdPegawai = dt_status_peg.kdPegawai) "
                    + "WHERE (pegawai.Status_keluar = '1'  or pegawai.Status_keluar = '7') "
                    + "group by (year(curdate())-year(dt_status_peg.tmt_status))";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
            for (Map m : rows) {
                int masakerja = ClassAntiNull.AntiNullInt(m.get("masakerja"));
                if (masakerja >= 0 && masakerja < 5) {
                    j0_5 += Integer.valueOf(m.get("jumlah").toString());
                } else if (masakerja >= 5 && masakerja < 10) {
                    j5_10 += Integer.valueOf(m.get("jumlah").toString());
                } else if (masakerja >= 10 && masakerja < 15) {
                    j10_15 += Integer.valueOf(m.get("jumlah").toString());
                } else if (masakerja >= 15 && masakerja < 20) {
                    j15_20 += Integer.valueOf(m.get("jumlah").toString());
                } else if (masakerja >= 20) {
                    j20_ += Integer.valueOf(m.get("jumlah").toString());
                }
                total += Integer.valueOf(m.get("jumlah").toString());
            }
            DecimalFormat df = new DecimalFormat("#.##");
            dataset.setValue("0-5 tahun : " + df.format(((double) j0_5 / total) * 100) + " %", ((double) j0_5 / total) * 100);
            dataset.setValue("5-10 tahun : " + df.format(((double) j5_10 / total) * 100) + " %", ((double) j5_10 / total) * 100);
            dataset.setValue("10-15 tahun : " + df.format(((double) j10_15 / total) * 100) + " %", ((double) j10_15 / total) * 100);
            dataset.setValue("15-20 tahun : " + df.format(((double) j15_20 / total) * 100) + " %", ((double) j15_20 / total) * 100);
            dataset.setValue(">20 tahun : " + df.format(((double) j20_ / total) * 100) + " %", ((double) j20_ / total) * 100);

        } else {
            sql = "SELECT  count(pegawai.kdPegawai) AS jumlah "
                    + ",(year(curdate())-year(dt_status_peg.tmt_status)) as masakerja  "
                    + "FROM "
                    + "(personalia.pegawai pegawai "
                    + "INNER JOIN "
                    + "personalia.unit_peg unit_peg "
                    + "ON (pegawai.kdPegawai = unit_peg.kdPegawai)) "
                    + "INNER JOIN "
                    + "personalia.dt_status_peg dt_status_peg "
                    + "ON (pegawai.kdPegawai = dt_status_peg.kdPegawai) "
                    + "WHERE (pegawai.Status_keluar = '1'  or pegawai.Status_keluar = '7') and "
                    + "unit_peg.kd_unit=? "
                    + "group by (year(curdate())-year(dt_status_peg.tmt_status))";
            List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[]{unKerja.getKode()});
            for (Map m : rows) {
                int masakerja = Integer.valueOf(m.get("masakerja").toString());
                if (masakerja >= 0 && masakerja < 5) {
                    j0_5 += Integer.valueOf(m.get("jumlah").toString());
                } else if (masakerja >= 5 && masakerja < 10) {
                    j5_10 += Integer.valueOf(m.get("jumlah").toString());
                } else if (masakerja >= 10 && masakerja < 15) {
                    j10_15 += Integer.valueOf(m.get("jumlah").toString());
                } else if (masakerja >= 15 && masakerja < 20) {
                    j15_20 += Integer.valueOf(m.get("jumlah").toString());
                } else if (masakerja >= 20) {
                    j20_ += Integer.valueOf(m.get("jumlah").toString());
                }
                total += Integer.valueOf(m.get("jumlah").toString());
            }
            dataset.setValue("0-5 tahun", ((double) j0_5 / total) * 100);
            dataset.setValue("5-10 tahun", ((double) j5_10 / total) * 100);
            dataset.setValue("10-15 tahun", ((double) j10_15 / total) * 100);
            dataset.setValue("15-20 tahun", ((double) j15_20 / total) * 100);
            dataset.setValue(">20 tahun", ((double) j20_ / total) * 100);
        }
        return dataset;
    }
}
