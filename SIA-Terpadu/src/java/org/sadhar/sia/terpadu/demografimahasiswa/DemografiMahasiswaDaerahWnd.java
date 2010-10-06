/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.demografimahasiswa;

import org.sadhar.sia.terpadu.kabkota.KabKota;
import org.sadhar.sia.terpadu.provinsi.Provinsi;
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
import org.sadhar.sia.terpadu.jumlahmahasiswa.JumlahMahasiswaDAOImpl;
import org.sadhar.sia.terpadu.jumlahmahasiswa.JumlahMahasiswaDAO;
import org.sadhar.sia.terpadu.kabkota.KabKotaDAOImpl;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;
import org.sadhar.sia.terpadu.provinsi.ProvinsiDAOImpl;
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
public class DemografiMahasiswaDaerahWnd extends ClassApplicationModule {

    Combobox cmbProgdi;
    Combobox cmbProvinsi;
    Combobox cmbKabKota;
    Textbox txtTahunAngkatan;
    Jasperreport report;
    Image chartImg;
    Combobox cmbExportType;
    Button btnExport;
    JFreeChart chart = null;
    Combobox cmbJumlah;

    public DemografiMahasiswaDaerahWnd() {
    }

    public void onCreate() throws Exception {
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        cmbProvinsi = (Combobox) getFellow("cmbProvinsi");
        cmbKabKota = (Combobox) getFellow("cmbKabKota");
        txtTahunAngkatan = (Textbox) getFellow("txtTahunAngkatan");
        report = (Jasperreport) getFellow("report");
        chartImg = (Image) getFellow("chartImg");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        cmbJumlah = (Combobox) getFellow("cmbJumlah");
        btnExport.setDisabled(true);
        loadProgdi();
        loadProvinsi();
        loadJumlah();
        cmbJumlah.setSelectedIndex(0);
        cmbProgdi.setSelectedIndex(0);
        cmbProvinsi.setSelectedIndex(0);

        cmbExportType.setSelectedIndex(0);
        cmbKabKota.setSelectedIndex(0);
        cmbKabKota.setDisabled(true);
    }

    private void loadJumlah() throws Exception {
        cmbJumlah.getItems().clear();
        Comboitem item1 = new Comboitem();
        item1.setValue(0);
        item1.setLabel("--Seluruh Jumlah--");
        cmbJumlah.appendChild(item1);

        Comboitem item2 = new Comboitem();
        item2.setValue(1);
        item2.setLabel("< 50");
        cmbJumlah.appendChild(item2);

        Comboitem item3 = new Comboitem();
        item3.setValue(2);
        item3.setLabel("50 - 100");
        cmbJumlah.appendChild(item3);

        Comboitem item4 = new Comboitem();
        item4.setValue(3);
        item4.setLabel("100 - 250");
        cmbJumlah.appendChild(item4);

        Comboitem item5 = new Comboitem();
        item5.setValue(4);
        item5.setLabel("250 - 500");
        cmbJumlah.appendChild(item5);

        Comboitem item6 = new Comboitem();
        item6.setValue(5);
        item6.setLabel("> 500");
        cmbJumlah.appendChild(item6);
    }

    private void loadProvinsi() throws Exception {
        try {
            DemografiMahasiswaDAO dao = new DemografiMahasiswaDAOImpl();
            List<Provinsi> provinsi = new ProvinsiDAOImpl().getProvinsi();
            cmbProvinsi.getItems().clear();
            Comboitem item = new Comboitem();
            item.setValue(null);
            item.setLabel("--Seluruh Provinsi--");
            cmbProvinsi.appendChild(item);
            for (Provinsi p : provinsi) {
                Comboitem items = new Comboitem();
                items.setValue(p);
                items.setLabel(p.getKode() + " " + p.getNama());
                cmbProvinsi.appendChild(items);
            }

            Comboitem itemx = new Comboitem();
            itemx.setValue(null);
            itemx.setLabel("--Seluruh Kab/Kota--");
            cmbKabKota.appendChild(itemx);

        } catch (Exception ex) {
            Messagebox.show("Ambil data provinsi gagal");
        }
    }

    public void cmbProvinsiSelect() throws Exception {
        if (cmbProvinsi.getSelectedItem().getValue() != null) {
            cmbKabKota.setDisabled(false);
            Provinsi prov = (Provinsi) cmbProvinsi.getSelectedItem().getValue();
            loadKabKota(prov);
            cmbKabKota.setSelectedIndex(0);
        } else {
            cmbKabKota.getItems().clear();
            cmbKabKota.setDisabled(true);
        }
    }

    private void loadKabKota(Provinsi prov) throws Exception {
        try {
            DemografiMahasiswaDAO dao = new DemografiMahasiswaDAOImpl();
            List<KabKota> kabkota = new KabKotaDAOImpl().getKabKota(prov);
            cmbKabKota.getItems().clear();
            Comboitem item = new Comboitem();
            item.setValue(null);
            item.setLabel("--Seluruh Kab/Kota--");
            cmbKabKota.appendChild(item);
            for (KabKota kk : kabkota) {
                Comboitem items = new Comboitem();
                items.setValue(kk);
                items.setLabel(kk.getKode() + " " + kk.getNama());
                cmbKabKota.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show("Ambil data Kab/Kota gagal");
        }
    }

    private void loadProgdi() throws Exception {
        try {
            JumlahMahasiswaDAO dao = new JumlahMahasiswaDAOImpl();
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
            Messagebox.show("Ambil data Prodi gagal");
        }
    }

    public void viewReport() throws Exception {
        try {
            DemografiMahasiswaDAO dao = new DemografiMahasiswaDAOImpl();
            CategoryDataset dataset = dao.getAsalDaerahDataset((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), txtTahunAngkatan.getValue(), (Provinsi) cmbProvinsi.getSelectedItem().getValue(), (KabKota) cmbKabKota.getSelectedItem().getValue(), 
                    Integer.valueOf(cmbJumlah.getSelectedItem().getValue().toString()));

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
            plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//            NumberAxis axis = (NumberAxis)chart.getXYPlot().getDomainAxis();
//            axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());


            //ngatur warna barchart
            final CategoryItemRenderer renderer1 = plot.getRenderer();
            for (int x = 0; x <= 34; x++) {
                renderer1.setSeriesPaint(x, new WarnaBarChart().getColor(x));
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
