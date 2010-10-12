/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataipstotal;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.image.AImage;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author kris
 */
public class RerataIpsTotalWnd extends ClassApplicationModule {

    private RerataIpsTotalDAO rerataIpsTotalDAO;
    private Jasperreport report;
    private Image chartImg;
    private Button btnExport;
    private Combobox cmbboxProdi;
    private Combobox cmbExportType;
    private String kodeProdi;
    private JFreeChart chart = null;

    public RerataIpsTotalWnd() {
        rerataIpsTotalDAO = new RerataIpsTotalDAOImpl();
    }

    public void onCreate() throws Exception {
        btnExport = (Button) getFellow("btnExport");
        report = (Jasperreport) getFellow("report");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbboxProdi = (Combobox) getFellow("cmbboxProdi");
        btnExport = (Button) getFellow("btnExport");
        chartImg = (Image) getFellow("chartImg");
        cmbExportType.setSelectedIndex(0);
        loadDataProdiToCombo();
    }

    private void loadDataProdiToCombo() {
        Comboitem item = new Comboitem();
        item.setValue(null);
        item.setLabel("Pilih Prodi");
        cmbboxProdi.appendChild(item);

        for (Map map : rerataIpsTotalDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Kd_prg").toString() + " " + map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
        cmbboxProdi.setSelectedIndex(0);
        cmbboxProdi.setSelectedItem(item);
    }

    public void btnShowOnClick() throws IOException, InterruptedException {
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
        if (kodeProdi == null) {
            Messagebox.show("Silahkan pilih prodi", "Konfirmasi", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            this.loadDataToGrafik();
        }
    }

    public void loadDataToGrafik() throws IOException {
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) this.generateData();
        chart = ChartFactory.createBarChart(
                "", "", "IPS", dataset, PlotOrientation.VERTICAL, false, false, false);
        chart.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        plot.getRangeAxis().setRange(new Range(0, 4));

        final CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.red);
        BarRenderer br = (BarRenderer) renderer;
        br.setShadowVisible(false);

        br.setShadowVisible(false);
        BufferedImage bi = chart.createBufferedImage(900, 400, BufferedImage.TRANSLUCENT, null);
        byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
        AImage image = new AImage("Bar Chart", bytes);
        chartImg.setContent(image);
        btnExport.setDisabled(false);

    }

    public CategoryDataset generateData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<Map> results = rerataIpsTotalDAO.getRerataIpsTotal(kodeProdi);
        List<Map> datas = new ArrayList<Map>();
        for (int i = 1; i <= 14; i++) {
            int count = 0;
            double ips = 0;
            for (Map result : results) {
                if (Integer.valueOf(result.get("semester").toString()) == i) {
                    count++;
                    ips += Double.valueOf(result.get("ips").toString());
                }
            }
            dataset.addValue(ips / count, "", "Smt "+String.valueOf(i));
        }
        return dataset;

    }
}
