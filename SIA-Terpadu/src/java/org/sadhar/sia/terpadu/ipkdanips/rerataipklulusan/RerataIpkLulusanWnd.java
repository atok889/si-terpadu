/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataipklulusan;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
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
public class RerataIpkLulusanWnd extends ClassApplicationModule {

    private RerataIpkLulusanDAO rerataIpkLulusanDAO;
    private Listbox listboxMahasiswa;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private List<Map> dataReport = new ArrayList<Map>();

    public RerataIpkLulusanWnd() {
        rerataIpkLulusanDAO = new RerataIpkLulusanDAOImpl();
    }

    public void onCreate() throws Exception {
        rerataIpkLulusanDAO = new RerataIpkLulusanDAOImpl();
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataToListbox();
    }

    private void loadDataToListbox() {
        listboxMahasiswa.getChildren().clear();
        CategoryDataset dataset = this.generatedData();
        Listhead listhead = new Listhead();

        Listheader listheaderProdi = new Listheader();
        listheaderProdi.appendChild(new Label(""));
        listheaderProdi.setWidth("300px");
        listhead.appendChild(listheaderProdi);

        for (Object header : dataset.getRowKeys()) {
            Listheader listheader = new Listheader();
            listheader.setAlign("right");
            listheader.setWidth("70px");
            listheader.appendChild(new Label(header.toString()));
            listhead.appendChild(listheader);
        }

        for (Object row : dataset.getColumnKeys()) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(row.toString()));


            for (Object column : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) column, (Comparable) row);
                listitem.appendChild(new Listcell(new DecimalFormat("# 0.00").format(number)));
                listboxMahasiswa.appendChild(listitem);
            }
        }
        listboxMahasiswa.appendChild(listhead);
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataReport);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/ipkdanips/rerataipklulusan/RerataIpkLulusan.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/ipkdanips/rerataips/RerataIpkLulusan.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private CategoryDataset generatedData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> datas = rerataIpkLulusanDAO.getRerataIPKLulusan();
        List<Map> prodis = rerataIpkLulusanDAO.getProdi();
        List<Map> rerata = new ArrayList<Map>();
        for (Map prodi : prodis) {
            for (int i = new DateTime().getYear() - 5; i <= new DateTime().getYear(); i++) {
                Map map = new HashMap();
                int count = 0;
                double totalIpk = 0d;
                for (Map data : datas) {
                    if (i == Integer.valueOf(data.get("angkatan").toString()) && data.get("Kd_prg").toString().equalsIgnoreCase(prodi.get("Kd_prg").toString())) {
                        totalIpk += Double.valueOf(data.get("ipk").toString());
                        count += 1;
                    }
                }
                map.put("angkatan", i);
                if (totalIpk == 0d) {
                    map.put("ipk", 0d);
                } else {
                    map.put("ipk", totalIpk / count);
                }
                System.out.println(prodi.get("Nama_prg"));
                map.put("prodi", prodi.get("Nama_prg"));
                rerata.add(map);
            }
        }

        for (Map map : rerata) {
            System.out.println(map.get("prodi") + "::" + map.get("angkatan") + "::" + map.get("ipk"));
            dataset.addValue(Double.valueOf(map.get("ipk").toString()), map.get("angkatan").toString(), map.get("prodi").toString());
        }
        dataReport.addAll(rerata);
        return dataset;
    }
}
