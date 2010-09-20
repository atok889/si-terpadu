/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswa;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Hendro Steven
 */
public class JumlahMahasiswaWnd extends ClassApplicationModule {

    Combobox cmbProgdi;
    Textbox txtTahunAngkatan;
    Jasperreport report;
    Image chartImg;
    Combobox cmbExportType;
    Button btnExport;
    JFreeChart chart = null;

    public JumlahMahasiswaWnd() {
    }

    public void onCreate() throws Exception {
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        txtTahunAngkatan = (Textbox) getFellow("txtTahunAngkatan");
        report = (Jasperreport) getFellow("report");
        chartImg = (Image) getFellow("chartImg");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        loadProgdi();
        cmbProgdi.setSelectedIndex(0);
        cmbExportType.setSelectedIndex(0);
    }

    private void loadProgdi() throws Exception {
        try {
            JumlahMahasiwaDAO dao = new JumlahMahasiswaDAOImpl();
            List<ProgramStudi> progdis = dao.getProgramStudi();
            cmbProgdi.getItems().clear();
            Comboitem item = new Comboitem();
            item.setValue(null);
            item.setLabel("--Pilih Prodi--");
            cmbProgdi.appendChild(item);
            for (ProgramStudi ps : progdis) {
                Comboitem items = new Comboitem();
                items.setValue(ps);
                items.setLabel(ps.getKode() + " " + ps.getNama());
                cmbProgdi.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public void viewReport() throws Exception {
        try {
            JumlahMahasiwaDAO dao = new JumlahMahasiswaDAOImpl();
            CategoryDataset dataset = dao.getDataset((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), txtTahunAngkatan.getValue());
            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            BarRenderer.setDefaultBarPainter(new StandardBarPainter());
            if (cmbProgdi.getSelectedItem().getValue() == null) {
                chart = ChartFactory.createBarChart(
                        "Jumlah Mahasiswa", // chart title
                        "Program Studi", // domain axis label
                        "Jumlah Mahasiswa", // range axis label
                        dataset, // data
                        PlotOrientation.HORIZONTAL,
                        true, // include legend
                        true,
                        false);
            } else {
                chart = ChartFactory.createBarChart(
                        "Jumlah Mahasiswa", // chart title
                        "Program Studi", // domain axis label
                        "Jumlah Mahasiswa", // range axis label
                        dataset, // data
                        PlotOrientation.VERTICAL,
                        true, // include legend
                        true,
                        false);
            }
            chart.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));

            final CategoryPlot plot = chart.getCategoryPlot();
            plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
            plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);

            final CategoryItemRenderer renderer1 = plot.getRenderer();
            renderer1.setSeriesPaint(0, Color.red);
            renderer1.setSeriesPaint(1, Color.yellow);
            renderer1.setSeriesPaint(2, Color.green);
            renderer1.setSeriesPaint(3, Color.blue);
            renderer1.setSeriesPaint(4, Color.cyan);
            BarRenderer br = (BarRenderer)renderer1;
            br.setShadowVisible(false);


            BufferedImage bi = chart.createBufferedImage(800, 400, BufferedImage.TRANSLUCENT, null);
            if (cmbProgdi.getSelectedItem().getValue() == null) {
                bi = chart.createBufferedImage(800, 1500, BufferedImage.TRANSLUCENT, null);
            }

            byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);

            AImage image = new AImage("Bar Chart", bytes);
            chartImg.setContent(image);
            btnExport.setDisabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show(ex.getMessage());
        }
    }

    public void exportReport() throws Exception {
        try {
            //JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/jumlahmahasiswa/JumlahMahasiswa.jasper");
                Map parameters = new HashMap();
                parameters.put("chart", chart.createBufferedImage(500, 300));
                pdfReport.setParameters(parameters);
                pdfReport.setDatasource(null);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/jumlahmahasiswa/JumlahMahasiswa.jasper");
                Map parameters = new HashMap();
                parameters.put("chart", chart.createBufferedImage(500, 300, BufferedImage.TRANSLUCENT, null));
                report.setParameters(parameters);
                report.setDatasource(null);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
