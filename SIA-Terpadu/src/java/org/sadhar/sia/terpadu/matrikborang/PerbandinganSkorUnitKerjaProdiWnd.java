/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.matrikborang;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
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
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zul.Image;

/**
 *
 * @author jasoet
 */
public class PerbandinganSkorUnitKerjaProdiWnd extends ClassApplicationModule {

    Image chartImg;
    JFreeChart chart = null;

    public void onCreate() throws Exception {
        chartImg = (Image) getFellow("chartImg");
        viewReport();
    }

    public void viewReport() throws Exception {
        try {
            MatrikBorangDAO dao = new MatrikBorangDAOImpl();
            CategoryDataset dataset = dao.getDatasetSkorAll();
            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            BarRenderer.setDefaultBarPainter(new StandardBarPainter());
            chart = ChartFactory.createBarChart(
                    "Perbandingan Skor Unit Kerja", // chart title
                    "Prodi", // domain axis label
                    "Jumlah", // range axis label
                    dataset, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true,
                    false);

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
            br.setShadowVisible(false);

            BufferedImage bi = chart.createBufferedImage(800, 400, BufferedImage.TRANSLUCENT, null);
            byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);

            AImage image = new AImage("Bar Chart", bytes);
            chartImg.setContent(image);

        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show("Gagal Menampilkan Perbandingan Skor Unit Kerja Prodi");
        }
    }
}
