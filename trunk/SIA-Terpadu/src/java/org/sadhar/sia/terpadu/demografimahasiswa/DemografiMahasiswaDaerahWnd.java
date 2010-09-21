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
        btnExport.setDisabled(true);
        loadProgdi();
        loadProvinsi();
        cmbProgdi.setSelectedIndex(0);
        cmbProvinsi.setSelectedIndex(0);
        
        cmbExportType.setSelectedIndex(0);
        cmbKabKota.setSelectedIndex(0);
        cmbKabKota.setDisabled(true);
    }

    private void loadProvinsi() throws Exception {
        try {
            DemografiMahasiswaDAO dao = new DemografiMahasiswaDAOImpl();
            List<Provinsi> provinsi = dao.getProvinsi();
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
            List<KabKota> kabkota = dao.getKabKota(prov);
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
            Messagebox.show("Ambil data Prodi gagal");
        }
    }

    public void viewReport() throws Exception {
        try {
            DemografiMahasiswaDAO dao = new DemografiMahasiswaDAOImpl();
            CategoryDataset dataset = dao.getAsalDaerahDataset((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), txtTahunAngkatan.getValue(), (Provinsi) cmbProvinsi.getSelectedItem().getValue(), (KabKota) cmbKabKota.getSelectedItem().getValue());

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
            for (int x = 0; x <= 34; x++) {
                renderer1.setSeriesPaint(x, new WarnaBarChart().getColor(x));
            }

            BarRenderer br = (BarRenderer) renderer1;
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
}
