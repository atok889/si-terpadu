/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.demografimahasiswa;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
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
import org.sadhar.sia.terpadu.jumlahmahasiswa.JumlahMahasiswaDAOImpl;
import org.sadhar.sia.terpadu.jumlahmahasiswa.JumlahMahasiwaDAO;
import org.sadhar.sia.terpadu.jumlahmahasiswa.ProgramStudi;
import org.sadhar.sia.terpadu.util.WarnaBarChart;
import org.zkoss.image.AImage;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author Hendro Steven
 */
public class DemografiMahasiswaWnd extends ClassApplicationModule {

    Combobox cmbProgdi;
    Textbox txtTahunAngkatan;
    Combobox cmbJenis;
    Jasperreport report;
    Image chartImg;
    Combobox cmbExportType;
    Button btnExport;
    JFreeChart chart = null;

    public DemografiMahasiswaWnd() {
    }

    public void onCreate() throws Exception {
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        txtTahunAngkatan = (Textbox) getFellow("txtTahunAngkatan");
        cmbJenis = (Combobox) getFellow("cmbJenis");
        report = (Jasperreport) getFellow("report");
        chartImg = (Image) getFellow("chartImg");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        loadProgdi();
        cmbProgdi.setSelectedIndex(0);
        cmbExportType.setSelectedIndex(0);
        cmbJenis.setSelectedIndex(0);
    }

    private void loadProgdi() throws Exception {
        try {
            JumlahMahasiwaDAO dao = new JumlahMahasiswaDAOImpl();
            List<ProgramStudi> progdis = dao.getProgramStudi();
            cmbProgdi.getItems().clear();
            Comboitem item = new Comboitem();
            item.setValue(null);
            item.setLabel("--Seluruh Universitas--");
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
            DemografiMahasiswaDAO dao = new DemografiMahasiswaDAOImpl();
            CategoryDataset dataset = null;
            if (cmbJenis.getSelectedItem().getLabel().toString().equalsIgnoreCase("Jenis Kelamin")) {
                dataset = dao.getJenisKelaminDataset((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), txtTahunAngkatan.getValue());
            } else if (cmbJenis.getSelectedItem().getLabel().toString().equalsIgnoreCase("Agama")) {
                dataset = dao.getAgamaDataset((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), txtTahunAngkatan.getValue());
            } else if (cmbJenis.getSelectedItem().getLabel().toString().equalsIgnoreCase("Penghasilan Orang Tua")) {
                dataset = dao.getEkonomiOrtuDataset((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), txtTahunAngkatan.getValue());
            } else if (cmbJenis.getSelectedItem().getLabel().toString().equalsIgnoreCase("Pekerjaan Orang Tua")) {
                dataset = dao.getPekerjaanOrtuDataset((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), txtTahunAngkatan.getValue());
            }

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

            //ngatur warna barchart
            final CategoryItemRenderer renderer1 = plot.getRenderer();
            for (int x = 0; x <= 15; x++) {
                renderer1.setSeriesPaint(x, new WarnaBarChart().getColor(x+8));
            }


            BarRenderer br = (BarRenderer) renderer1;
            br.setShadowVisible(false);

            BufferedImage bi = chart.createBufferedImage(900, 500, BufferedImage.TRANSLUCENT, null);
            if (cmbProgdi.getSelectedItem().getValue() == null) {
                bi = chart.createBufferedImage(900, 2000, BufferedImage.TRANSLUCENT, null);
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
}
