/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pangkatjabatandosen;

import java.util.List;
import org.jfree.chart.JFreeChart;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.fakultas.Fakultas;
import org.sadhar.sia.terpadu.fakultas.FakultasDAOImpl;
import org.sadhar.sia.terpadu.fakultas.FakultasDAO;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;

/**
 *
 * @author Jasoet
 */
public class PangkatJabatanDosenWnd extends ClassApplicationModule {

    Jasperreport report;
    Image chartImg;
    Combobox cmbExportType;
    Button btnExport;
    JFreeChart chart = null;
    Combobox cmbCakupan;
    Combobox cmbFakultas;
    Combobox cmbJenis;
    private int cmbJenisIndex;
    private int cmbCakupanIndex;
    private int cmbFakultasIndex;
    private FakultasDAO fakultasDAO;

    public PangkatJabatanDosenWnd() {
        fakultasDAO = new FakultasDAOImpl();
    }

    public void onCreate() throws Exception {

        report = (Jasperreport) getFellow("report");
        chartImg = (Image) getFellow("chartImg");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        cmbCakupan = (Combobox) getFellow("cmbCakupan");
        cmbFakultas = (Combobox) getFellow("cmbFakultas");
        cmbJenis = (Combobox) getFellow("cmbJenis");
        btnExport.setDisabled(true);

        cmbCakupan.setSelectedIndex(0);
        cmbJenis.setSelectedIndex(0);
        cmbJenisIndex = 0;
        cmbCakupanIndex = 0;
        cmbFakultasIndex = 0;
    }

    public void cmbJenisSelect() throws Exception {
        cmbJenisIndex = cmbJenis.getSelectedIndex();
    }

    public void cmbCakupanSelect() throws Exception {
        cmbCakupanIndex = cmbCakupan.getSelectedIndex();
        if (cmbCakupanIndex > 0) {
            cmbFakultas.setDisabled(false);
            cmbFakultas.getChildren().clear();
            List<Fakultas> data = fakultasDAO.gets();
            for (Fakultas f : data) {
                Comboitem ci = new Comboitem();
                ci.setLabel(f.getNama());
                ci.setValue(f);
                cmbFakultas.appendChild(ci);
            }
            cmbFakultas.setSelectedIndex(0);

        } else {
            cmbFakultas.setDisabled(true);
            cmbFakultas.getChildren().clear();
        }
    }

    public void cmbFakultasSelect() throws Exception {
        cmbFakultasIndex = cmbFakultas.getSelectedIndex();
    }

    public void viewReport() throws Exception {
//        try {
//            DemografiMahasiswaDAO dao = new DemografiMahasiswaDAOImpl();
//            CategoryDataset dataset = dao.getAsalDaerahDataset((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), txtTahunAngkatan.getValue(), (Provinsi) cmbProvinsi.getSelectedItem().getValue(), (KabKota) cmbKabKota.getSelectedItem().getValue(),
//                    Integer.valueOf(cmbJumlah.getSelectedItem().getValue().toString()));
//
//            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
//            BarRenderer.setDefaultBarPainter(new StandardBarPainter());
//
//            if (cmbProgdi.getSelectedItem().getValue() == null) {
//                chart = ChartFactory.createBarChart(
//                        "Jumlah Mahasiswa", // chart title
//                        "Program Studi", // domain axis label
//                        "Jumlah Mahasiswa", // range axis label
//                        dataset, // data
//                        PlotOrientation.HORIZONTAL,
//                        true, // include legend
//                        true,
//                        false);
//            } else {
//                chart = ChartFactory.createBarChart(
//                        "Jumlah Mahasiswa", // chart title
//                        "Program Studi", // domain axis label
//                        "Jumlah Mahasiswa", // range axis label
//                        dataset, // data
//                        PlotOrientation.VERTICAL,
//                        true, // include legend
//                        true,
//                        false);
//            }
//
//            chart.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));
//
//            final CategoryPlot plot = chart.getCategoryPlot();
//            plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
//            plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
//            plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//
//
//            //ngatur warna barchart
//            final CategoryItemRenderer renderer1 = plot.getRenderer();
//            for (int x = 0; x <= 34; x++) {
//                renderer1.setSeriesPaint(x, new WarnaBarChart().getColor(x));
//            }
//
//            BarRenderer br = (BarRenderer) renderer1;
//            br.setShadowVisible(false);
//
//            BufferedImage bi = chart.createBufferedImage(900, 500, BufferedImage.TRANSLUCENT, null);
//            if (cmbProgdi.getSelectedItem().getValue() == null) {
//                bi = chart.createBufferedImage(900, 2000, BufferedImage.TRANSLUCENT, null);
//            }
//
//            byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
//
//            AImage image = new AImage("Bar Chart", bytes);
//            chartImg.setContent(image);
//            btnExport.setDisabled(false);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            Messagebox.show(ex.getMessage());
//        }
    }
}
