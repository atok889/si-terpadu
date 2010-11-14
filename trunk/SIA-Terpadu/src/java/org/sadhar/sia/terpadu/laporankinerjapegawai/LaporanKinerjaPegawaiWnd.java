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
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

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
        this.loadData();
    }

    public void loadData() {

        DefaultCategoryDataset dataset = (DefaultCategoryDataset) this.generateData();

        Listhead listhead = new Listhead();
        Auxhead auxhead = new Auxhead();

        Listheader listheaderNo = new Listheader();
        listheaderNo.setWidth("40px");
        listheaderNo.setAlign("right");
        listhead.appendChild(listheaderNo);
        Auxheader auxheaderNo = new Auxheader();
        auxheaderNo.setLabel("No");
        auxheaderNo.setAlign("center");
        auxhead.appendChild(auxheaderNo);

        Listheader listheaderUnitKerja = new Listheader();
        listheaderUnitKerja.setWidth("400px");
        listhead.appendChild(listheaderUnitKerja);
        Auxheader auxheaderUnitKerja = new Auxheader();
        auxheaderUnitKerja.setLabel("Unit Kerja");
        auxheaderUnitKerja.setAlign("center");
        auxhead.appendChild(auxheaderUnitKerja);

        for (Object row : dataset.getRowKeys()) {
            Listheader listheader = new Listheader();
            listheader.setWidth("70px");
            listheader.setAlign("right");
            listheader.setLabel(row.toString());
            listhead.appendChild(listheader);
        }

        for (int i = 0; i <= 5; i++) {
            Auxheader auxheaderUnitNEP = new Auxheader();
            auxheaderUnitNEP.setLabel("NEP");
            auxheaderUnitNEP.setAlign("center");
            auxheaderUnitNEP.setColspan(2);
            auxhead.appendChild(auxheaderUnitNEP);

            Auxheader auxheaderUnitDP = new Auxheader();
            auxheaderUnitDP.setLabel("DP3");
            auxheaderUnitDP.setAlign("center");
            auxheaderUnitDP.setColspan(1);
            auxhead.appendChild(auxheaderUnitDP);
        }

        int no = 1;
        for (Object column : dataset.getColumnKeys()) {
            int index = 0;
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(no + ""));
            listitem.appendChild(new Listcell(column.toString()));

            for (Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                Double data = null;
                if (number != null) {
                    data = number.doubleValue();
                } else {
                    data = 0d;
                }
                listitem.appendChild(new Listcell(data + ""));
                listboxData.appendChild(listitem);
            }
            no++;
        }
        listboxData.appendChild(listhead);
        listboxData.appendChild(auxhead);

    }

    public void exportReport() throws Exception {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dataReport);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/laporankinerjapegawai/LaporanKinerjaPegawai.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/laporankinerjapegawai/LaporanKinerjaPegawai.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public CategoryDataset generateData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> results = laporanKinerjaPegawaiDAO.getLaporanKinerjaPegawaiAdministratif();
        List<Map> unitKerjas = laporanKinerjaPegawaiDAO.getUnitKerja();
        List<Map> datas = new ArrayList<Map>();

        for (int i = new DateTime().getYear() - 5; i <= new DateTime().getYear(); i++) {
            for (int j = 1; j <= 3; j++) {
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

        for (Map data : datas) {
            if (data.get("semester").toString().equals("3")) {
                dataset.addValue(Double.valueOf(data.get("rerata").toString()), data.get("tahun").toString(), data.get("nama").toString());
            } else {
                dataset.addValue(Double.valueOf(data.get("rerata").toString()), data.get("tahun").toString() + "-" + data.get("semester"), data.get("nama").toString());
            }

            //report
            Map map = new HashMap();
            map.put("nama", data.get("nama"));

            if (data.get("semester").toString().equals("3")) {
                map.put("kategori", "DP3");
                map.put("tahun", data.get("tahun").toString() + "");
            } else {
                map.put("kategori", "NEP");
                map.put("tahun", data.get("tahun").toString() + " " + data.get("semester"));
            }
            map.put("rerata", data.get("rerata"));
            dataReport.add(map);
        }
        return dataset;
    }
}
