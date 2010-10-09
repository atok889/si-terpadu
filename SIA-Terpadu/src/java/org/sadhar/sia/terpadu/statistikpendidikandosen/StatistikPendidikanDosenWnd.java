/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistikpendidikandosen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
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
import org.sadhar.sia.terpadu.util.WarnaBarChart;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class StatistikPendidikanDosenWnd extends ClassApplicationModule {

    private Jasperreport report;
    private Image chartImg;
    private Button btnExport;
    private Combobox cmbExportType;
    private JFreeChart chart = null;
    private StatistikPendidikanDosenDAO statistikPendidikanDosenDAO;

    public StatistikPendidikanDosenWnd() {
        statistikPendidikanDosenDAO = new StatistikPendidikanDAOImpl();
    }

    public void onCreate() throws Exception {
        btnExport = (Button) getFellow("btnExport");
        report = (Jasperreport) getFellow("report");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        chartImg = (Image) getFellow("chartImg");
        cmbExportType.setSelectedIndex(0);
        loadDataToGrafik();
    }

    public void loadDataToGrafik() throws IOException {
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) this.generateData();
        chart = ChartFactory.createBarChart(
                "", "Prodi", "Jumlah Dosen", dataset, PlotOrientation.HORIZONTAL, true, true, false);
        chart.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        final CategoryItemRenderer renderer1 = plot.getRenderer();
        renderer1.setSeriesPaint(0, Color.red);
        renderer1.setSeriesPaint(1, Color.yellow);
        renderer1.setSeriesPaint(2, Color.green);
        BarRenderer br = (BarRenderer) renderer1;
        br.setShadowVisible(false);

        br.setShadowVisible(false);
        BufferedImage bi = chart.createBufferedImage(900, 1500, BufferedImage.TRANSLUCENT, null);
        byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
        AImage image = new AImage("Bar Chart", bytes);
        chartImg.setContent(image);
        btnExport.setDisabled(false);

    }

    public void exportReport() throws Exception {
        try {
            //JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/statistikpendidikandosen/StatistikPendidikanDosen.jasper");
                Map parameters = new HashMap();
                parameters.put("chart", chart.createBufferedImage(500, 300));
                pdfReport.setParameters(parameters);
                pdfReport.setDatasource(null);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/statistikpendidikandosen/StatistikPendidikanDosen.jasper");
                Map parameters = new HashMap();
                parameters.put("chart", chart.createBufferedImage(500, 300, BufferedImage.TRANSLUCENT, null));
                report.setParameters(parameters);
                report.setDatasource(null);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public CategoryDataset generateData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> results = statistikPendidikanDosenDAO.getStatistikPendidikan();
        String[] jenjangs = {"S1", "S2", "S3"};

        for (Map prodi : statistikPendidikanDosenDAO.getProdi()) {
            for (String jenjang : jenjangs) {
                Map map = new HashMap();
                int pendidikan = 0;
                for (Map data : results) {
                    if (prodi.get("Nama_prg").toString().equalsIgnoreCase(data.get("Nama_prg").toString())) {
                        if (data.get("Nm_jenjang").toString().equals(jenjang.toString())) {
                            pendidikan++;
                        }
                    }
                }
                map.put("prodi", prodi.get("Nama_prg"));
                map.put("pendidikan", jenjang.toString());
                map.put("jumlah", pendidikan);
                dataset.addValue(Integer.valueOf(map.get("jumlah").toString()), map.get("pendidikan").toString(), map.get("prodi").toString());
            }
        }
        return dataset;
    }
}
