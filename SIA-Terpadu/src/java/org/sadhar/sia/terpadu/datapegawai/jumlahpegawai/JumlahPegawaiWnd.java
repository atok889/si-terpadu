/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.datapegawai.jumlahpegawai;

import java.awt.Color;
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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
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
public class JumlahPegawaiWnd extends ClassApplicationModule {

    Combobox cmbCakupan;
    Combobox cmbJenis;
    Image chartImg;
    JFreeChart chart = null;

    public void onCreate() throws Exception {
        cmbCakupan = (Combobox) getFellow("cmbCakupan");
        cmbJenis = (Combobox) getFellow("cmbJenis");
        chartImg = (Image) getFellow("chartImg");
        loadUnitKerja();
        loadJenis();
        cmbCakupan.setSelectedIndex(0);
        cmbJenis.setSelectedIndex(0);
    }

    private void loadJenis() throws Exception {
        Comboitem item1 = new Comboitem();
        item1.setValue("total");
        item1.setLabel("Total");
        cmbJenis.appendChild(item1);

        Comboitem item2 = new Comboitem();
        item2.setValue("status");
        item2.setLabel("Status");
        cmbJenis.appendChild(item2);

        Comboitem item3 = new Comboitem();
        item3.setValue("pangkat");
        item3.setLabel("Pangkat");
        cmbJenis.appendChild(item3);

        Comboitem item4 = new Comboitem();
        item4.setValue("golongan");
        item4.setLabel("Golongan");
        cmbJenis.appendChild(item4);

        Comboitem item5 = new Comboitem();
        item5.setValue("jabatan");
        item5.setLabel("Jabatan Akademik");
        cmbJenis.appendChild(item5);
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
            JumlahPegawaiDAO dao = new JumlahPegawaiDAOImpl();
            CategoryDataset dataset = null;
            UnitKerja selectedUnit = (UnitKerja) cmbCakupan.getSelectedItem().getValue();
            if (cmbJenis.getSelectedItem().getValue().toString().equalsIgnoreCase("total")) {
                dataset = dao.getJumlahPegawaiByTotal(selectedUnit);
            } else if (cmbJenis.getSelectedItem().getValue().toString().equalsIgnoreCase("status")) {
                dataset = dao.getJumlahPegawaiByStatus(selectedUnit);
            } else if (cmbJenis.getSelectedItem().getValue().toString().equalsIgnoreCase("pangkat")) {
                dataset = dao.getJumlahPegawaiByPangkat(selectedUnit);
            } else if (cmbJenis.getSelectedItem().getValue().toString().equalsIgnoreCase("golongan")) {
                dataset = dao.getJumlahPegawaiByGolongan(selectedUnit);
            } else if (cmbJenis.getSelectedItem().getValue().toString().equalsIgnoreCase("jabatan")) {
                dataset = dao.getJumlahPegawaiByJabatanAkademik(selectedUnit);
            }
            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            BarRenderer.setDefaultBarPainter(new StandardBarPainter());
            chart = ChartFactory.createBarChart(
                    "Jumlah Pegawai", // chart title
                    "Tahun", // domain axis label
                    "Jumlah Pegawai", // range axis label
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
            br.setMaximumBarWidth(0.5);
            br.setMinimumBarLength(0.5);
            br.setShadowVisible(false);

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
