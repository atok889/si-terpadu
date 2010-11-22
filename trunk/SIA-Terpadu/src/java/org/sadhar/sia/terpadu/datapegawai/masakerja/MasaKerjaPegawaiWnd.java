/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.datapegawai.masakerja;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.general.PieDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.unkerja.UnitKerja;
import org.sadhar.sia.terpadu.unkerja.UnitKerjaDAO;
import org.sadhar.sia.terpadu.unkerja.UnitKerjaDAOImpl;
import org.zkoss.image.AImage;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author Hendro Steven
 */
public class MasaKerjaPegawaiWnd extends ClassApplicationModule {

    Combobox cmbCakupan;
    Image chartImg;
    JFreeChart chart = null;

    public void onCreate() throws Exception {
        cmbCakupan = (Combobox) getFellow("cmbCakupan");
        chartImg = (Image) getFellow("chartImg");
        loadUnitKerja();
        cmbCakupan.setSelectedIndex(0);
    }

    private void loadUnitKerja() throws Exception {
        try {
            UnitKerjaDAO dao = new UnitKerjaDAOImpl();
            List<UnitKerja> units = dao.gets();
            cmbCakupan.getItems().clear();
            Comboitem item = new Comboitem();
            item.setValue(null);
            item.setLabel("-- Seluruh Universitas --");
            cmbCakupan.appendChild(item);
            for (UnitKerja unit : units) {
                Comboitem itemx = new Comboitem();
                itemx.setValue(unit);
                itemx.setLabel(unit.getKode() + " " + unit.getNama());
                cmbCakupan.appendChild(itemx);
            }
        } catch (Exception ex) {
            Messagebox.show("Gagal manampilkan data Unit Kerja");
        }
    }

    public void viewReport() throws Exception {
        try {
            MasaKerjaPegawaiDAO dao = new MasaKerjaPegawaiDAOImpl();
            PieDataset dataset = dao.getMasaKerja((UnitKerja) cmbCakupan.getSelectedItem().getValue());
            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            BarRenderer.setDefaultBarPainter(new StandardBarPainter());
            chart = ChartFactory.createPieChart(
                    "Masa Kerja Pegawai", // chart title
                    dataset, // data
                    true, // include legend
                    true,
                    false);

            chart.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));

            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setNoDataMessage("No data available");
            plot.setCircular(false);
            plot.setLabelGap(0.02);

            BufferedImage bi = chart.createBufferedImage(800, 400, BufferedImage.TRANSLUCENT, null);
            byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);

            AImage image = new AImage("Bar Chart", bytes);
            chartImg.setContent(image);

        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show("Gagal menampilkan data Jumlah Pegawai Administratif");
        }
    }
}
