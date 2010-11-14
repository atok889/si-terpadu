/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.reratamatakuliah;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author jasoet
 */
public class RerataMataKuliahDAOImpl implements RerataMataKuliahDAO {

    public RerataMataKuliahDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public CategoryDataset getDataset(String prodi, int tahunAwal, int tahunAkhir) throws Exception {
        List<String> tahunParameter = generateSemester(tahunAwal, tahunAkhir);
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        for (String param : tahunParameter) {
            String sql = "SELECT mtk" + prodi + ".kd_mtk as kodeMK, "
                    + " mtk" + prodi + ".nama_mtk as namaMK, "
                    + " mtk" + prodi + ".SKS, "
                    + "   if((SUM(nilai.angka)/COUNT(DISTINCT kr" + prodi + "" + param + ".nomor_mhs)) <=> null,"
                    + " 0.0,(SUM(nilai.angka)/ "
                    + "     COUNT(DISTINCT kr" + prodi + "" + param + ".nomor_mhs))) as rataNilaiMK "
                    + "  FROM (db_" + prodi + ".kr" + prodi + "" + param + " kr" + prodi + "" + param + " "
                    + "     RIGHT OUTER JOIN db_" + prodi + ".mtk" + prodi + " mtk" + prodi + " "
                    + "          ON (kr" + prodi + "" + param + ".kd_mtk = mtk" + prodi + ".kd_mtk)) "
                    + "        LEFT OUTER JOIN kamus.nilai nilai "
                    + "             ON (nilai.huruf = kr" + prodi + "" + param + ".nilai) "
                    + " GROUP BY mtk" + prodi + ".kd_mtk";

            try {
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    String mataKuliah = "(" + ClassAntiNull.AntiNullString(m.get("kodeMK")) + ") " + ClassAntiNull.AntiNullString(m.get("namaMK"));
                    String semester = param.substring(0, 4) + "-" + param.substring(4, 5);
                    double rataNilaiMK = ClassAntiNull.AntiNullDouble(m.get("rataNilaiMK"));

                    result.addValue(rataNilaiMK, mataKuliah, semester);
                }

            } catch (DataAccessException ex) {
                System.err.println(ex.getMessage());
            }
        }

        return result;
    }

    private List<String> generateSemester(int tahunAwal, int tahunAkhir) {
        List<String> result = new ArrayList<String>();
        if (tahunAwal > tahunAkhir) {
            int temp = tahunAwal;
            tahunAwal = tahunAkhir;
            tahunAkhir = temp;
        }
        if (tahunAwal <= 2005) {
            result.add("20052");
            tahunAwal = 2006;
        }

        int tahunSekarang = Calendar.getInstance().get(Calendar.YEAR);

        if (tahunAkhir >= tahunSekarang) {
            tahunAkhir = tahunSekarang;
        }

        for (int i = tahunAwal; i <= tahunAkhir; i++) {
            result.add(i + "1");
            result.add(i + "2");
        }

        return result;

    }

    public List<RerataMataKuliah> gets(String prodi, int tahunAwal, int tahunAkhir) throws Exception {
        List<String> tahunParameter = generateSemester(tahunAwal, tahunAkhir);
        List<RerataMataKuliah> result = new ArrayList<RerataMataKuliah>();
        for (String param : tahunParameter) {
            String sql = "SELECT mtk" + prodi + ".kd_mtk as kodeMK, "
                    + " mtk" + prodi + ".nama_mtk as namaMK, "
                    + " mtk" + prodi + ".SKS, "
                    + "   if((SUM(nilai.angka)/COUNT(DISTINCT kr" + prodi + "" + param + ".nomor_mhs)) <=> null,"
                    + " 0.0,(SUM(nilai.angka)/ "
                    + "     COUNT(DISTINCT kr" + prodi + "" + param + ".nomor_mhs))) as rataNilaiMK "
                    + "  FROM (db_" + prodi + ".kr" + prodi + "" + param + " kr" + prodi + "" + param + " "
                    + "     RIGHT OUTER JOIN db_" + prodi + ".mtk" + prodi + " mtk" + prodi + " "
                    + "          ON (kr" + prodi + "" + param + ".kd_mtk = mtk" + prodi + ".kd_mtk)) "
                    + "        LEFT OUTER JOIN kamus.nilai nilai "
                    + "             ON (nilai.huruf = kr" + prodi + "" + param + ".nilai) "
                    + " GROUP BY mtk" + prodi + ".kd_mtk";

            try {
                List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
                for (Map m : rows) {
                    String mataKuliah = "(" + ClassAntiNull.AntiNullString(m.get("kodeMK")) + ") " + ClassAntiNull.AntiNullString(m.get("namaMK"));
                    String semester = param.substring(0, 4) + "-" + param.substring(4, 5);
                    double rataNilaiMK = ClassAntiNull.AntiNullDouble(m.get("rataNilaiMK"));
                    RerataMataKuliah rmk = new RerataMataKuliah();
                    rmk.setMataKuliah(mataKuliah);
                    rmk.setSemester(semester);
                    rmk.setRataNilaiMK(rataNilaiMK);
                    result.add(rmk);
                }

            } catch (DataAccessException ex) {
                System.err.println(ex.getMessage());
            }
        }

        return result;
    }
}
