/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.matrikborang;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class MatrikBorangDAOImpl implements MatrikBorangDAO {

    public MatrikBorangDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }
    private String sql = "SELECT "
            + " imovrka.tahun, "
            + " imovrka.kodeUnit, "
            + " imovrka.noIsian, "
            + " imovrka.isianMonevinRKA, "
            + " IF(imovrka.skorPascaMonev <=> null,0.0,imovrka.skorPascaMonev) AS skorPascaMonev, "
            + " IF(imovrka.skorPraMonev <=> null,0.0,imovrka.skorPraMonev) AS skorPraMonev "
            + " FROM mutu.isianMonevinRKA imovrka ";

    public List<MatrikBorang> getByKodeUnit(String kodeUnit) throws Exception {
        String insql = sql + " WHERE imovrka.kodeUnit LIKE '%" + kodeUnit + "%'";
        List<MatrikBorang> list = new ArrayList<MatrikBorang>();
        List<Map> rows = ClassConnection.getJdbc().queryForList(insql);
        for (Map m : rows) {
            MatrikBorang matrikBorang = new MatrikBorang();
            matrikBorang.setKodeUnit(ClassAntiNull.AntiNullString(m.get("kodeUnit")));
            matrikBorang.setIsianMonevinRKA(ClassAntiNull.AntiNullString(m.get("isianMonevinRKA")));
            matrikBorang.setNoIsian(ClassAntiNull.AntiNullInt(m.get("noIsian")));
            matrikBorang.setTahun(ClassAntiNull.AntiNullString(m.get("tahun")));
            matrikBorang.setSkorPascaMonev(ClassAntiNull.AntiNullDouble(m.get("skorPascaMonev")));
            matrikBorang.setSkorPraMonev(ClassAntiNull.AntiNullDouble(m.get("skorPraMonev")));
            list.add(matrikBorang);
        }
        return list;

    }

    public List<MatrikBorang> getByTahunBetween(String kodeUnit) throws Exception {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String insql = sql + " WHERE imovrka.tahun BETWEEN '" + (year - 5) + "' AND '" + (year) + "' AND  WHERE imovrka.kodeUnit LIKE '%" + kodeUnit + "%'";
        List<MatrikBorang> list = new ArrayList<MatrikBorang>();
        List<Map> rows = ClassConnection.getJdbc().queryForList(insql);
        for (Map m : rows) {
            MatrikBorang matrikBorang = new MatrikBorang();
            matrikBorang.setKodeUnit(ClassAntiNull.AntiNullString(m.get("kodeUnit")));
            matrikBorang.setIsianMonevinRKA(ClassAntiNull.AntiNullString(m.get("isianMonevinRKA")));
            matrikBorang.setNoIsian(ClassAntiNull.AntiNullInt(m.get("noIsian")));
            matrikBorang.setTahun(ClassAntiNull.AntiNullString(m.get("tahun")));
            matrikBorang.setSkorPascaMonev(ClassAntiNull.AntiNullDouble(m.get("skorPascaMonev")));
            matrikBorang.setSkorPraMonev(ClassAntiNull.AntiNullDouble(m.get("skorPraMonev")));
            list.add(matrikBorang);
        }
        return list;
    }

    public List<MatrikBorang> getByKodeUnitDanTahun(String KodeUnit, String tahun) throws Exception {
        String insql = sql + " WHERE imovrka.kodeUnit LIKE '%" + KodeUnit + "%' AND imovrka.tahun='" + tahun + "'";
        List<MatrikBorang> list = new ArrayList<MatrikBorang>();
        List<Map> rows = ClassConnection.getJdbc().queryForList(insql);
        for (Map m : rows) {
            MatrikBorang matrikBorang = new MatrikBorang();
            matrikBorang.setKodeUnit(ClassAntiNull.AntiNullString(m.get("kodeUnit")));
            matrikBorang.setIsianMonevinRKA(ClassAntiNull.AntiNullString(m.get("isianMonevinRKA")));
            matrikBorang.setNoIsian(ClassAntiNull.AntiNullInt(m.get("noIsian")));
            matrikBorang.setTahun(ClassAntiNull.AntiNullString(m.get("tahun")));
            matrikBorang.setSkorPascaMonev(ClassAntiNull.AntiNullDouble(m.get("skorPascaMonev")));
            matrikBorang.setSkorPraMonev(ClassAntiNull.AntiNullDouble(m.get("skorPraMonev")));
            list.add(matrikBorang);
        }
        return list;
    }

    public CategoryDataset getDatasetSkor(String kodeUnit) throws Exception {
        String sql = "SELECT "
                + "   imovrka.tahun, "
                + "     imovrka.kodeUnit, "
                + "       SUM(IF(imovrka.skorPascaMonev <=> null,0.0,imovrka.skorPascaMonev)) AS skor  "
                + " FROM mutu.isianMonevinRKA imovrka "
                + " WHERE imovrka.tahun BETWEEN (YEAR(NOW())-5) AND YEAR(NOW()) AND imovrka.kodeUnit LIKE '%" + kodeUnit + "%' "
                + " group by imovrka.tahun;";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            dataset.addValue(ClassAntiNull.AntiNullDouble(m.get("skor")), ClassAntiNull.AntiNullString(m.get("tahun")), "");
        }

        return dataset;

    }

    public CategoryDataset getDatasetSkorAll() throws Exception {
        String sql = "SELECT "
                + "    imovrka.kodeUnit, "
                + "      unkerja.Nama_unit_kerja as nama, "
                + "        SUM(IF(imovrka.skorPascaMonev <=> null,0.0,imovrka.skorPascaMonev)) AS skor "
                + " FROM mutu.isianMonevinRKA imovrka "
                + " INNER JOIN kamus.unkerja unkerja on (unkerja.Kd_unit_kerja = imovrka.kodeUnit) "
                + " WHERE  imovrka.tahun=YEAR(NOW()) group by imovrka.kodeUnit";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            dataset.addValue(ClassAntiNull.AntiNullDouble(m.get("skor")), ClassAntiNull.AntiNullString(m.get("nama")), "");
        }

        return dataset;
    }
}
