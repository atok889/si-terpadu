/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.matrikborang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            + " FROM mutu.isianmonevinrka imovrka ";

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

    public List<MatrikBorang> getByTahunBetween(String awal, String akhir) throws Exception {
        String insql = sql + " WHERE imovrka.tahun BETWEEN '" + awal + "' AND '" + akhir + "'";
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
}
