/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.rasiodosenmahasiswa;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
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
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.image.AImage;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;

/**
 *
 * @author kris
 */
public class RasioDosenMahasiswaWnd extends ClassApplicationModule {

//    private Combobox cmbboxProdi;
    private Combobox cmbboxSemester;
//    private Combobox cmbExportType;
//    private Jasperreport report;
    private String kodeProdi;
    private String tahunSemester;
    private Button btnExport;
    private Image chartImg;
    private JFreeChart chart = null;
    private List<Map> datas = new ArrayList<Map>();
    private RasioDosenMahasiswaDAO rasioDosenMahasiswaDAO;
    private String semester = "2";
    private String tahun = "2009";

    public RasioDosenMahasiswaWnd() {
        rasioDosenMahasiswaDAO = new RasioDosenMahasiswaDAOImpl();
    }

    public void onCreate() throws Exception {
//        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        cmbboxSemester = (Combobox) this.getFellow("cmbboxSemester");
//        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
//        cmbExportType = (Combobox) getFellow("cmbExportType");
//        cmbExportType.setSelectedIndex(0);
        chartImg = (Image) getFellow("chartImg");
        //this.loadDataProdiToCombo();
        this.loadDataSemesterToCombo();
        //loadDataToGrafik();
    }

    private void loadDataSemesterToCombo() {
        Comboitem item = new Comboitem("--Pilih Semester--");
        cmbboxSemester.appendChild(item);
        cmbboxSemester.setSelectedItem(item);

        for (int i = 2000; i <= new DateTime().getYear(); i++) {
            for (int j = 1; j <= 2; j++) {
                Comboitem items = new Comboitem();
                items.setValue(String.valueOf(i) + String.valueOf(j));
                items.setLabel(i + "-" + j);
                cmbboxSemester.appendChild(items);
            }
        }
        cmbboxSemester.setReadonly(true);
    }

    public void btnShowOnClick() throws InterruptedException, IOException {
        tahunSemester = (String) cmbboxSemester.getSelectedItem().getValue();
        if (tahunSemester != null) {
            try {
                this.loadDataToGrafik();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Messagebox.show("Data tidak ditemukan", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
            }
        } else {
            Messagebox.show("Parameter tidak lengkap", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public void loadDataToGrafik() throws IOException {
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) this.generateData();
        chart = ChartFactory.createBarChart(
                "Rasio Dosen Mahasiswa", "", "Mahasiswa", dataset, PlotOrientation.HORIZONTAL, false, true, true);
        chart.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        final CategoryItemRenderer renderer = plot.getRenderer();
        BarRenderer br = (BarRenderer) renderer;
        br.setShadowVisible(false);

        br.setShadowVisible(false);
        BufferedImage bi = chart.createBufferedImage(1050, 1400, BufferedImage.TRANSLUCENT, null);
        byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
        AImage image = new AImage("Bar Chart", bytes);
        chartImg.setContent(image);
        btnExport.setDisabled(false);

    }

    private CategoryDataset generateData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> results = rasioDosenMahasiswaDAO.getRasioDosenMahasiswa(tahunSemester.substring(0, 4), tahunSemester.substring(4, 5));
        for (Map result : results) {
            dataset.addValue(Double.valueOf(result.get("jumlah").toString()), "", result.get("prodi").toString());
        }
        return dataset;
    }
}
