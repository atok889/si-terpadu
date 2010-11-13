/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.laporankinerjapegawai;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;

/**
 *
 * @author kris
 */
public class LaporanKinerjaPegawaiWnd extends ClassApplicationModule {

    private LaporanKinerjaPegawaiDAO laporanKinerjaPegawaiDAO;
    private Listbox listboxData;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private List<Map> dataReport = new ArrayList<Map>();

    public LaporanKinerjaPegawaiWnd() {
        laporanKinerjaPegawaiDAO = new LaporanKinerjaPegawaiDAOImpl();
    }

    public void onCreate() {
        listboxData = (Listbox) this.getFellow("listboxData");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
    }

    public void loadData() {
    }

    public CategoryDataset generateData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> results = laporanKinerjaPegawaiDAO.getLaporanKinerjaPegawaiAdministratif();
        List<Map> unitKerjas = laporanKinerjaPegawaiDAO.getUnitKerja();
        List<Map> datas = new ArrayList<Map>();

        for (int i = new DateTime().getYear() - 5; i <= new DateTime().getYear(); i++) {
            for (int j = 0; j <= 2; j++) {
                for (Map unitKerja : unitKerjas) {
                    Map map = new HashMap();
                    String nama = "";
                    String tahun = "";
                    String rerata = "0.00";
                    String semester = "";
                    for (Map result : results) {
                        if (result.get("nama") != null && result.get("tahun").toString().equals(String.valueOf(i)) &&
                                result.get("semester").toString().equals(String.valueOf(j))) {
                            if (unitKerja.get("nama").toString().equalsIgnoreCase(result.get("nama").toString())) {
                                nama = result.get("nama").toString();
                                tahun = result.get("tahun").toString();
                                rerata = result.get("rerata").toString();
                                semester = result.get("semester").toString();
                            } else {
                                nama = unitKerja.get("nama").toString();
                                tahun = String.valueOf(i);
                                semester = String.valueOf(i);
                            }
                        } else {
                            nama = unitKerja.get("nama").toString();
                            tahun = String.valueOf(i);
                            semester = String.valueOf(j);
                        }
                    }
                    map.put("nama", nama);
                    map.put("tahun", tahun);
                    map.put("rerata", new DecimalFormat(" #0.00").format(Double.valueOf(rerata)));
                    map.put("semester", semester);
                    datas.add(map);
                }
            }
        }

        for (Map map : datas) {
            if (map.get("semester").toString().equals("0")) {
                dataset.addValue(Double.valueOf(map.get("rerata").toString()), map.get("tahun").toString() + "|", map.get("nama").toString());
            } else {
                dataset.addValue(Double.valueOf(map.get("rerata").toString()), map.get("tahun").toString() + "-" + map.get("semester") + "|", map.get("nama").toString());
            }
        }
        return dataset;
    }
}
