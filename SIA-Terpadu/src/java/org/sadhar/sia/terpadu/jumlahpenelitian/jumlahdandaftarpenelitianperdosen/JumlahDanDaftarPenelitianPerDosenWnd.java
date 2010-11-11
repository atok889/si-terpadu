/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahpenelitian.jumlahdandaftarpenelitianperdosen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.jumlahpenelitian.jumlahpenelitianperprodi.JumlahPenelitianPerProdiDAO;
import org.zkoss.image.AImage;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class JumlahDanDaftarPenelitianPerDosenWnd extends ClassApplicationModule {

    private JumlahDanDaftarPenelitianPerDosenDAO jumlahDanDaftarPenelitianPerDosenDAO;
    private Combobox cmbboxProdi;
    private Combobox cmbExportType;
    private Combobox cmbboxDosen;
    private Jasperreport report;
    private String kodeProdi;
    private Button btnExport;
    private Image chartImg;
    private JFreeChart chart = null;

    public JumlahDanDaftarPenelitianPerDosenWnd() {
        jumlahDanDaftarPenelitianPerDosenDAO = new JumlahDanDaftarPenelitianPerDosenDAOImpl();
    }

    public void onCreate() throws Exception {
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        chartImg = (Image) getFellow("chartImg");
        cmbboxDosen = (Combobox) getFellow("cmbboxDosen");
        this.loadDataProdiToCombo();
    }

    private void loadDataProdiToCombo() {
        Comboitem item = new Comboitem("--Pilih Fakultas--");
        item.setValue(null);
        cmbboxProdi.appendChild(item);
        cmbboxProdi.setSelectedItem(item);

        for (Map map : jumlahDanDaftarPenelitianPerDosenDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Kd_prg").toString() + " " + map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
        cmbboxProdi.setReadonly(true);
    }

    private void loadDataDosenToCombo() {
        cmbboxDosen.getItems().clear();
        Comboitem item = new Comboitem("--Detail Dosen--");
        item.setValue(null);
        cmbboxDosen.appendChild(item);
        cmbboxDosen.setSelectedItem(item);
        List<Map> datas = jumlahDanDaftarPenelitianPerDosenDAO.getJumlahDanDaftarPenelitianPerDosen(kodeProdi);
        List<String> list = new ArrayList<String>();
        for (Map map : datas) {
            Comboitem items = new Comboitem();
            if (!list.contains(map.get("kdPegawai").toString())) {
                items.setValue(map.get("kdPegawai").toString());
                items.setLabel(map.get("Nama_peg").toString());
            }
            list.add(map.get("kdPegawai").toString());
            cmbboxDosen.appendChild(items);
        }
        cmbboxDosen.setReadonly(true);
    }

    private void componentDisable() {
        btnExport.setDisabled(true);
    }

    private void componentEnable() {
        btnExport.setDisabled(false);
    }

    public void cmbDataProdiOnSelect() {
        this.componentDisable();
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
        this.loadDataDosenToCombo();
    }

    public void cmbDataDosenOnSelect() throws IOException {
        List<Map> results = jumlahDanDaftarPenelitianPerDosenDAO.getDetailJumlahDanDaftarPenelitianPerDosen(cmbboxDosen.getSelectedItem().getValue().toString());
        List<Map> datas = new ArrayList<Map>();
        for (int i = new DateTime().getYear() - 5; i <= new DateTime().getYear(); i++) {
            Map map = new HashMap();
            String tahun = null;
            String nama = null;
            String jumlah = null;
            for (Map data : results) {
                if (Integer.valueOf(data.get("tahunPenilaian").toString()) == i) {
                    tahun = data.get("tahunPenilaian").toString();
                    nama = data.get("Nama_peg").toString();
                    jumlah = data.get("jumlah").toString();
                    break;
                } else {
                    tahun = String.valueOf(i);
                    nama = "";
                    jumlah = "0";
                }
            }
            map.put("tahun", tahun);
            map.put("nama", nama);
            map.put("jumlah", jumlah);
            datas.add(map);
        }

        final XYSeries series = new XYSeries("");

        for (Map map : datas) {
            series.add(Integer.valueOf(map.get("tahun").toString()), Integer.valueOf(map.get("jumlah").toString()));
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        // create the chart...
        chart = ChartFactory.createXYLineChart(
                "", // chart title
                "", // x axis label
                "", // y axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
                );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);

        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        plot.setRenderer(renderer);

        //  change the auto tick unit selection to integer units only...
        NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        ValueAxis valueAxis = plot.getDomainAxis();
        valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        //axis.setRange(2005, 2010);

        BufferedImage bi = chart.createBufferedImage(900, 500, BufferedImage.TRANSLUCENT, null);
        byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
        AImage image = new AImage("Bar Chart", bytes);
        chartImg.setContent(image);
        btnExport.setDisabled(false);

    }

    public void btnShowOnClick() throws InterruptedException {
        try {
            this.loadDataToGrafik();
            cmbboxDosen.setSelectedIndex(0);
            this.componentEnable();
        } catch (Exception e) {
            e.printStackTrace();
            this.componentDisable();
            Messagebox.show("Data tidak ditemukan", "Konfirmasi", Messagebox.OK, Messagebox.EXCLAMATION);
        }

    }

    public void loadDataToGrafik() throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        List<Map> results = jumlahDanDaftarPenelitianPerDosenDAO.getJumlahDanDaftarPenelitianPerDosen(kodeProdi);
        List<String> temp = new ArrayList<String>();
        List<Map> temps = new ArrayList<Map>();

        for (Map map : results) {
            Map m = new HashMap();
            if (!temp.contains(map.get("Nama_peg").toString())) {
                m.put("nama", map.get("Nama_peg"));
            }
            temp.add(map.get("Nama_peg").toString());
            temps.add(m);
        }

        List<Map> datas = new ArrayList<Map>();

        for (Map nama : temps) {
            int jumlah = 0;
            Map map = new HashMap();
            if (nama.get("nama") != null) {
                for (Map data : results) {
                    if (nama.get("nama").toString().equals(data.get("Nama_peg").toString())) {
                        jumlah += Integer.valueOf(data.get("jumlah").toString());
                    }
                }
            }
            map.put("jumlah", jumlah);
            map.put("nama", nama.get("nama"));
            datas.add(map);
        }

        for (Map map : datas) {
            if (map.get("nama") != null) {
                dataset.setValue(map.get("nama").toString() + " = " + map.get("jumlah").toString(), Double.valueOf(map.get("jumlah").toString()));
            }
        }

        chart =
                ChartFactory.createPieChart3D(
                "Jumlah Dan Daftar Penelitian Dosen", // chart title
                dataset, // data
                false, // include legend
                false,
                false);


        final PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setNoDataMessage("data tidak ditemukan");
        plot.setForegroundAlpha(0.6f);
        plot.setCircular(true);

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
}
