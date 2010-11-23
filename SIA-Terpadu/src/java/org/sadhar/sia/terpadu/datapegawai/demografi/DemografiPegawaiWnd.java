/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.datapegawai.demografi;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.image.AImage;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;

/**
 *
 * @author Hendro Steven
 */
public class DemografiPegawaiWnd extends ClassApplicationModule {

    Combobox cmbJenis;
    Image chartImg1;
    Image chartImg2;
    JFreeChart chart1 = null;
    JFreeChart chart2 = null;

    public void onCreate() throws Exception {
        cmbJenis = (Combobox) getFellow("cmbJenis");
        chartImg1 = (Image) getFellow("chartImg1");
        chartImg2 = (Image) getFellow("chartImg2");
        loadJenis();
        cmbJenis.setSelectedIndex(0);
    }

    private void loadJenis() throws Exception {
        cmbJenis.getItems().clear();
        Comboitem item1 = new Comboitem();
        item1.setValue(1);
        item1.setLabel("Jenis Kelamin");
        cmbJenis.appendChild(item1);

        Comboitem item2 = new Comboitem();
        item2.setValue(2);
        item2.setLabel("Agama");
        cmbJenis.appendChild(item2);

        Comboitem item3 = new Comboitem();
        item3.setValue(3);
        item3.setLabel("Pendidikan");
        cmbJenis.appendChild(item3);
    }

    public void viewReport() throws Exception {
        try {
            DemografiPegawaiDAO dao = new DemografiPegawaiDAOImpl();
            PieDataset pieDataset = null;
            CategoryDataset barDataset = null;
            if (Integer.valueOf(cmbJenis.getSelectedItem().getValue().toString()) == 1) {
                pieDataset = dao.getPieDemografiBySex();
                barDataset = dao.getBarDemografiBySex();
            } else if (Integer.valueOf(cmbJenis.getSelectedItem().getValue().toString()) == 2) {
                pieDataset = dao.getPieDemografiByAgama();
                barDataset = dao.getBarDemografiByAgama();
            } else if (Integer.valueOf(cmbJenis.getSelectedItem().getValue().toString()) == 3) {
                pieDataset = dao.getPieDemografiByPendidikan();
                barDataset = dao.getBarDemografiByPendidikan();
            }
            createPieCart(pieDataset);
            createBarCart(barDataset);
        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show("Gagal menampilkan data Jumlah Pegawai Administratif");
        }
    }

    private void createPieCart(PieDataset pieDataset) throws Exception {
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());
        chart2 = ChartFactory.createPieChart(
                "Demografi Pegawai Total", // chart title
                pieDataset, // data
                true, // include legend
                true,
                false);

        chart2.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));

        PiePlot plot = (PiePlot) chart2.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);

        BufferedImage bi = chart2.createBufferedImage(400, 300, BufferedImage.TRANSLUCENT, null);
        byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);

        AImage image = new AImage("Bar Chart", bytes);
        chartImg2.setContent(image);
    }

    private void createBarCart(CategoryDataset dataset) throws Exception {
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());
        chart1 = ChartFactory.createBarChart(
                "Demografi Pegawai", // chart title
                "Tahun", // domain axis label
                "Jumlah", // range axis label
                dataset, // data
                PlotOrientation.HORIZONTAL,
                true, // include legend
                true,
                false);

        chart1.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));

        final CategoryPlot plot = chart1.getCategoryPlot();
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.setNoDataMessage("No data available");

        final CategoryItemRenderer renderer1 = plot.getRenderer();
        renderer1.setSeriesPaint(0, Color.red);
        renderer1.setSeriesPaint(1, Color.yellow);
        renderer1.setSeriesPaint(2, Color.green);
        renderer1.setSeriesPaint(3, Color.blue);
        renderer1.setSeriesPaint(4, Color.cyan);
        BarRenderer br = (BarRenderer) renderer1;
        br.setMaximumBarWidth(0.5);
        br.setMinimumBarLength(0.5);
        br.setShadowVisible(false);

        BufferedImage bi = chart1.createBufferedImage(1000, 3000, BufferedImage.TRANSLUCENT, null);
        byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);

        AImage image = new AImage("Bar Chart", bytes);
        chartImg1.setContent(image);
    }
}
