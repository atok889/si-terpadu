/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahpenelitian.jumlahpenelitianperprodi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
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
public class JumlahPenelitianPerProdiWnd extends ClassApplicationModule {

    private Jasperreport report;
    private Image chartImg;
    private Button btnExport;
    private Combobox cmbExportType;
    private JFreeChart chart = null;
    private JumlahPenelitianPerProdiDAO jumlahPenelitianPerProdiDAO;

    public JumlahPenelitianPerProdiWnd() {
        jumlahPenelitianPerProdiDAO = new JumlahPenelitianPerProdiDAOImpl();
    }

    public void onCreate() throws Exception {
        btnExport = (Button) getFellow("btnExport");
        report = (Jasperreport) getFellow("report");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        chartImg = (Image) getFellow("chartImg");
        cmbExportType.setSelectedIndex(0);
        this.loadDataToGrafik();
    }

    public void loadDataToGrafik() throws IOException {
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) this.generateData();
        chart = ChartFactory.createBarChart(
                "", "", "Jumlah", dataset, PlotOrientation.HORIZONTAL, true, true, false);
        chart.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());


        final CategoryItemRenderer renderer1 = plot.getRenderer();
        renderer1.setSeriesPaint(0, Color.red);
        renderer1.setSeriesPaint(1, Color.yellow);
        renderer1.setSeriesPaint(2, Color.green);
        renderer1.setSeriesPaint(3, Color.blue);
        renderer1.setSeriesPaint(4, Color.cyan);
        BarRenderer br = (BarRenderer) renderer1;
        br.setMaximumBarWidth(.03);
        br.setItemMargin(0.0);
        br.setShadowVisible(false);

        br.setShadowVisible(false);
        BufferedImage bi = chart.createBufferedImage(900, 500, BufferedImage.TRANSLUCENT, null);
        byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
        AImage image = new AImage("Bar Chart", bytes);
        chartImg.setContent(image);
        btnExport.setDisabled(false);

    }

    public void exportReport() throws Exception {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(new ArrayList());
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/statistiklamastudi/StatistikLamaStudi.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/statistiklamastudi/StatistikLamaStudi.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    private CategoryDataset generateData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map map : jumlahPenelitianPerProdiDAO.getJumlahPenelitianPerProdi()) {
            System.out.println(map.get("Nama_unit_kerja") + "=>" + map.get("jumlah") + "=>" + map.get("tahunPenilaian"));
            int jumlah = Integer.valueOf(map.get("jumlah").toString());
            if (jumlah != 0) {
                dataset.addValue(jumlah, map.get("tahunPenilaian").toString(), map.get("Nama_unit_kerja").toString());
            }
        }
        return dataset;
    }
}
