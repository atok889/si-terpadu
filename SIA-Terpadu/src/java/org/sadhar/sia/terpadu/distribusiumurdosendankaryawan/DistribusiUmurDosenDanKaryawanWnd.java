/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.distribusiumurdosendankaryawan;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.image.AImage;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class DistribusiUmurDosenDanKaryawanWnd extends ClassApplicationModule {

    private DistribusiUmurDosenDanKaryawanDAO distribusiUmurDosenDAO;
    private Combobox cmbboxProdi;
    private String kodeProdi;
    private Listbox listboxData;
    private Combobox cmbExportType;
    private Combobox cmbboxUmur;
    private Jasperreport report;
    private Image chartImg;
    private JFreeChart chart = null;
    private Button btnExport;
    private Groupbox groupDetail;

    public DistribusiUmurDosenDanKaryawanWnd() {
        distribusiUmurDosenDAO = new DistribusiUmurDosenDanKaryawanDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxData = (Listbox) this.getFellow("listboxData");
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        groupDetail = (Groupbox) getFellow("groupDetail");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbboxUmur = (Combobox) getFellow("cmbboxUmur");
        chartImg = (Image) getFellow("chartImg");
        cmbExportType.setSelectedIndex(0);
        cmbboxUmur.setSelectedIndex(0);
        this.loadDataProdiToCombo();
    }

    private void loadDataProdiToCombo() {
        Comboitem item = new Comboitem("--SEMUA FAKULTAS--");
        item.setValue(null);
        cmbboxProdi.appendChild(item);
        cmbboxProdi.setSelectedItem(item);

        for (Map map : distribusiUmurDosenDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("kodeUnitKerja").toString());
            items.setLabel(map.get("kodeUnitKerja").toString() + " " + map.get("nama").toString());
            cmbboxProdi.appendChild(items);
        }
    }

    public void cmbDataProdiOnSelect() {
        cmbboxUmur.setSelectedIndex(0);
        cmbboxUmur.setDisabled(true);
        groupDetail.setVisible(false);
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
    }

    public void btnShowOnClick() throws IOException {
        if (cmbboxUmur.getSelectedItem().getValue() != null) {
            this.loadDetail();
        } else {
            cmbboxUmur.setDisabled(false);
            loadDataToGrafik();
        }
    }

    private void loadDetail() {
        listboxData.getItems().clear();
        String umur = cmbboxUmur.getSelectedItem().getValue().toString();
        List<Map> results = distribusiUmurDosenDAO.getAll(kodeProdi, umur.substring(0, 2), umur.substring(3, 5));
        int no = 1;
        for (Map result : results) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(no + ""));
            listitem.appendChild(new Listcell(result.get("nama").toString()));
            listitem.appendChild(new Listcell(result.get("jabatan").toString()));
            listitem.appendChild(new Listcell(result.get("golongan").toString()));
            listitem.appendChild(new Listcell(result.get("umur").toString()));
            no++;
            listboxData.appendChild(listitem);
        }
        groupDetail.setVisible(true);
    }

    public void loadDataToGrafik() throws IOException {
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) this.generateData();
        chart = ChartFactory.createBarChart(
                "Distribusi Umur Dosen", "", "Jumlah", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());


        final CategoryItemRenderer renderer1 = plot.getRenderer();
        BarRenderer br = (BarRenderer) renderer1;
        br.setMaximumBarWidth(.03);
        br.setItemMargin(0.0);
        br.setShadowVisible(false);
        BufferedImage bi = chart.createBufferedImage(900, 400, BufferedImage.TRANSLUCENT, null);
        byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
        AImage image = new AImage("Bar Chart", bytes);
        chartImg.setContent(image);
        btnExport.setDisabled(false);
    }

    private CategoryDataset generateData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi != null) {
            results = distribusiUmurDosenDAO.getAll(kodeProdi);
        } else {
            results = distribusiUmurDosenDAO.getAll();
        }

        int rangeA = 0;
        int rangeB = 0;
        int rangeC = 0;
        int rangeD = 0;
        int rangeE = 0;

        for (Map result : results) {
            int umur = Integer.valueOf(result.get("umur").toString());
            if (umur >= 21 && umur <= 30) {
                rangeA++;
            } else if (umur >= 31 && umur <= 40) {
                rangeB++;
            } else if (umur >= 41 && umur <= 50) {
                rangeC++;
            } else if (umur >= 51 && umur <= 60) {
                rangeD++;
            } else if (umur >= 61 && umur <= 70) {
                rangeE++;
            }
        }

        List<Map> datas = new ArrayList<Map>();

        for (Map result : results) {
            Map map = new HashMap();
            int umur = Integer.valueOf(result.get("umur").toString());
            if (umur >= 21 && umur <= 30) {
                map.put("jumlah", rangeA);
                map.put("range", "21-30");
            } else if (umur >= 31 && umur <= 40) {
                map.put("jumlah", rangeB);
                map.put("range", "31-40");
            } else if (umur >= 41 && umur <= 50) {
                map.put("jumlah", rangeC);
                map.put("range", "41-50");
            } else if (umur >= 51 && umur <= 60) {
                map.put("jumlah", rangeD);
                map.put("range", "51-60");
            } else if (umur >= 61 && umur <= 70) {
                map.put("jumlah", rangeE);
                map.put("range", "61-70");
            }
            map.put("status", result.get("status"));
            map.put("umur", result.get("umur"));
            datas.add(map);
        }
        for (Map data : datas) {
            int jumlah = 0;
            if (data.get("jumlah") != null) {
                jumlah = Integer.valueOf(data.get("jumlah").toString());
            }
            if (jumlah != 0) {
                dataset.addValue(jumlah, data.get("range").toString(), data.get("status").toString());
            }
        }

        return dataset;
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList());
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/warning/warningadministratif/WarningAdministratif.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/warning/warningadministratif/WarningAdministratif.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
