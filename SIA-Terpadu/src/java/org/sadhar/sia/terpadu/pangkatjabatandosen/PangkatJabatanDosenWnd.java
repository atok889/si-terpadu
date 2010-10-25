/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pangkatjabatandosen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import org.sadhar.sia.terpadu.fakultas.Fakultas;
import org.sadhar.sia.terpadu.fakultas.FakultasDAOImpl;
import org.sadhar.sia.terpadu.fakultas.FakultasDAO;
import org.sadhar.sia.terpadu.jabatanakademikdosen.JabatanAkademikDosen;
import org.sadhar.sia.terpadu.jabatanakademikdosen.JabatanAkademikDosenDAO;
import org.sadhar.sia.terpadu.jabatanakademikdosen.JabatanAkademikDosenDAOImpl;
import org.sadhar.sia.terpadu.pangkatdosen.PangkatDosen;
import org.sadhar.sia.terpadu.pangkatdosen.PangkatDosenDAO;
import org.sadhar.sia.terpadu.pangkatdosen.PangkatDosenDAOImpl;
import org.zkoss.image.AImage;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;
import org.sadhar.sia.terpadu.util.WarnaBarChart;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

/**
 *
 * @author Jasoet
 */
public class PangkatJabatanDosenWnd extends ClassApplicationModule {

    private Jasperreport report;
    private Image chartImg;
    private Combobox cmbExportType;
    private Button btnExport;
    private JFreeChart chart = null;
    private Combobox cmbCakupan;
    private Combobox cmbFakultas;
    private Combobox cmbJenis;
    private Listbox listB;
    private int cmbJenisIndex;
    private int cmbCakupanIndex;
    private int cmbFakultasIndex;
    private FakultasDAO fakultasDAO;
    private JabatanAkademikDosenDAO jabatanDAO;
    private PangkatDosenDAO pangkatDAO;
    private List<PangkatDosen> listPangkat;
    private List<JabatanAkademikDosen> listJabatan;

    public PangkatJabatanDosenWnd() {
        fakultasDAO = new FakultasDAOImpl();
        jabatanDAO = new JabatanAkademikDosenDAOImpl();
        pangkatDAO = new PangkatDosenDAOImpl();
    }

    public void onCreate() throws Exception {

        report = (Jasperreport) getFellow("report");
        chartImg = (Image) getFellow("chartImg");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        cmbCakupan = (Combobox) getFellow("cmbCakupan");
        cmbFakultas = (Combobox) getFellow("cmbFakultas");
        cmbJenis = (Combobox) getFellow("cmbJenis");
        listB = (Listbox) getFellow("listb");
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
        try {


            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            BarRenderer.setDefaultBarPainter(new StandardBarPainter());

            if (cmbCakupanIndex == 0) {
                CategoryDataset dataset = null;

                if (cmbJenisIndex == 0) {
                    dataset = jabatanDAO.getCountJabatanAll();
                    listJabatan = jabatanDAO.gets();
                } else {
                    dataset = pangkatDAO.getCountPangkatAll();
                    listPangkat = pangkatDAO.gets();
                }
                chart = ChartFactory.createBarChart(
                        "Jumlah", // chart title
                        "Fakultas", // domain axis label
                        "Jumlah", // range axis label
                        dataset, // data
                        PlotOrientation.HORIZONTAL,
                        true, // include legend
                        true,
                        false);
            } else {
                CategoryDataset dataset = null;
                Fakultas f = (Fakultas) cmbFakultas.getSelectedItem().getValue();

                if (cmbJenisIndex == 0) {
                    dataset = jabatanDAO.getCountJabatanByFaculty(f);
                    listJabatan = jabatanDAO.getByFaculty(f);
                } else {
                    dataset = pangkatDAO.getCountPangkatByFaculty(f);
                    listPangkat = pangkatDAO.getByFaculty(f);
                }
                chart = ChartFactory.createBarChart(
                        "Jumlah", // chart title
                        "Fakultas", // domain axis label
                        "Jumlah", // range axis label
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


            //ngatur warna barchart
            final CategoryItemRenderer renderer1 = plot.getRenderer();
            for (int x = 0; x <= 34; x++) {
                renderer1.setSeriesPaint(x, new WarnaBarChart().getColor(x));
            }

            BarRenderer br = (BarRenderer) renderer1;
            br.setShadowVisible(false);

            BufferedImage bi = chart.createBufferedImage(900, 500, BufferedImage.TRANSLUCENT, null);
            if (cmbCakupanIndex == 0) {
                bi = chart.createBufferedImage(900, 2000, BufferedImage.TRANSLUCENT, null);
            }

            byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);

            AImage image = new AImage("Bar Chart", bytes);
            chartImg.setContent(image);
            btnExport.setDisabled(false);

            listB.getItems().clear();
            if (cmbJenisIndex == 0) {
                for (JabatanAkademikDosen o : listJabatan) {
                    Listitem item = new Listitem();
                    Listcell cell = new Listcell();
                    cell.setLabel(o.getNpp());
                    item.appendChild(cell);

                    cell = new Listcell();
                    cell.setLabel(o.getNama());
                    item.appendChild(cell);

                    cell = new Listcell();
                    cell.setLabel(o.getUmur() + "");
                    item.appendChild(cell);

                    cell = new Listcell();
                    cell.setLabel(o.getNamaUnitKerja());
                    item.appendChild(cell);

                    cell = new Listcell();
                    cell.setLabel(o.getNamaJabatan());
                    item.appendChild(cell);
                    listB.appendChild(item);
                }
            } else {
                for (PangkatDosen o : listPangkat) {
                    Listitem item = new Listitem();
                    Listcell cell = new Listcell();
                    cell.setLabel(o.getNpp());
                    item.appendChild(cell);

                    cell = new Listcell();
                    cell.setLabel(o.getNama());
                    item.appendChild(cell);

                    cell = new Listcell();
                    cell.setLabel(o.getUmur() + "");
                    item.appendChild(cell);

                    cell = new Listcell();
                    cell.setLabel(o.getNamaUnitKerja());
                    item.appendChild(cell);

                    cell = new Listcell();
                    cell.setLabel(o.getNamaPangkat());
                    item.appendChild(cell);
                    listB.appendChild(item);
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show(ex.getMessage());
        }
    }

     public void exportReport() throws Exception {
//        try {
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
//            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
//                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
//                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
//                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
//                pdfReport.setSrc("reports/jumlahmahasiswado/JumlahMahasiswaDO.jasper");
//                Map parameters = new HashMap();
////                parameters.put("chart", chart.createBufferedImage(500, 300));
//                pdfReport.setParameters(parameters);
//                pdfReport.setDatasource(dataSource);
//                pdfPreviewWnd.doModal();
//            } else {
//                report.setType(cmbExportType.getSelectedItem().getValue().toString());
//                report.setSrc("reports/jumlahmahasiswado/JumlahMahasiswaDO.jasper");
//                Map parameters = new HashMap();
////                parameters.put("chart", chart.createBufferedImage(500, 300, BufferedImage.TRANSLUCENT, null));
//                report.setParameters(parameters);
//                report.setDatasource(dataSource);
//            }
//        } catch (Exception ex) {
//            Messagebox.show(ex.getMessage());
//        }
    }
}
