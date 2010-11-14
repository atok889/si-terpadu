/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.reratalamapengerjaanta;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author Deny Prasetyo
 */
public class RerataLamaPengerjaanTAWnd extends ClassApplicationModule {

    Jasperreport report;
//    Image chartImg;
    Combobox cmbExportType;
    Button btnExport;
    Listbox listb;
    JFreeChart chart = null;
    private DecimalFormat df = new DecimalFormat("#0.00");

    public RerataLamaPengerjaanTAWnd() {
    }

    public void onCreate() throws Exception {
        listb = (Listbox) getFellow("listb");
        report = (Jasperreport) getFellow("report");
//        chartImg = (Image) getFellow("chartImg");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        loadProgdi();
        cmbExportType.setSelectedIndex(0);
        viewReport();
    }

    private void loadProgdi() throws Exception {
    }

    public void viewReport() throws Exception {
        try {
            RerataLamaPengerjaanTADAO dao = new RerataLamaPengerjaanTADAOImpl();
            CategoryDataset dataset = dao.getDataset();

            listb.getItems().clear();


            Listhead lhead;
            if (listb.getListhead() != null) {
                lhead = listb.getListhead();
                lhead.getChildren().clear();
            } else {
                lhead = new Listhead();
                listb.appendChild(lhead);
            }
            Listheader lheader = new Listheader();
            lheader.setLabel("Program Studi");
            lheader.setWidth("230px");
            lhead.appendChild(lheader);

            for (Object s : dataset.getRowKeys()) {
                Listheader inlhd = new Listheader();
                inlhd.setLabel(s.toString());
                inlhd.setWidth("40px");
                inlhd.setAlign("right");
                lhead.appendChild(inlhd);
            }

            lheader = new Listheader();
            lheader.setLabel("Rerata Prodi");
            lheader.setWidth("100px");
            lheader.setAlign("right");
            lhead.appendChild(lheader);

            for (Object s : dataset.getColumnKeys()) {
                Listitem item = new Listitem();
                Listcell cell = new Listcell();
                cell.setLabel(s.toString());
                item.appendChild(cell);
                for (Object f : dataset.getRowKeys()) {
                    cell = new Listcell();
                    Number nbr = dataset.getValue((Comparable) f, (Comparable) s);
                    if (nbr != null) {
                        String rerataString = df.format(nbr.doubleValue());
                        cell.setLabel(rerataString);
                    } else {
                        cell.setLabel("0");
                    }
                    item.appendChild(cell);
                }
                cell = new Listcell();
                cell.setLabel(df.format(dao.getAvBulanByProdi(s.toString())));
                cell.setStyle("text-align:right");
                item.appendChild(cell);
                listb.appendChild(item);
            }

            Listitem item = new Listitem();
            Listcell cell = new Listcell();
            cell.setLabel("RERATA UNIVERSITAS");
            cell.setStyle("font-weight:bold");
            item.appendChild(cell);
            for (Object f : dataset.getRowKeys()) {
                cell = new Listcell();
                cell.setLabel(df.format(dao.getAvBulanByTahun(f.toString())));
                item.appendChild(cell);
            }
            cell = new Listcell();
            cell.setLabel(df.format(dao.getAvTotal()));
            cell.setStyle("text-align:right");
            item.appendChild(cell);
            listb.appendChild(item);

            btnExport.setDisabled(false);



            /*
            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            BarRenderer.setDefaultBarPainter(new StandardBarPainter());

            chart = ChartFactory.createBarChart(
            "Statistik lama Studi", // chart title
            "Program Studi", // domain axis label
            "Jumlah Mahasiswa", // range axis label
            dataset, // data
            PlotOrientation.VERTICAL,
            true, // include legend
            true,
            false);

            chart.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));

            final CategoryPlot plot = chart.getCategoryPlot();
            plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
            plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);

            final CategoryItemRenderer renderer1 = plot.getRenderer();
            renderer1.setSeriesPaint(0, Color.red);
            renderer1.setSeriesPaint(1, Color.yellow);
            renderer1.setSeriesPaint(2, Color.green);
            renderer1.setSeriesPaint(3, Color.blue);
            renderer1.setSeriesPaint(4, Color.cyan);
            BarRenderer br = (BarRenderer) renderer1;
            br.setShadowVisible(false);


            BufferedImage bi = chart.createBufferedImage(800, 400, BufferedImage.TRANSLUCENT, null);
            bi = chart.createBufferedImage(800, 1500, BufferedImage.TRANSLUCENT, null);

            byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);

            AImage image = new AImage("Bar Chart", bytes);
            chartImg.setContent(image);
             */


        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show(ex.getMessage());
        }
    }

    public void exportReport() throws Exception {
        try {
            RerataLamaPengerjaanTADAO dao = new RerataLamaPengerjaanTADAOImpl();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dao.getRecord());
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/reratalamapengerjaanta/RerataLamaPengerjaanTA.jasper");
                Map parameters = new HashMap();
//                parameters.put("chart", chart.createBufferedImage(500, 300));
                pdfReport.setParameters(parameters);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/reratalamapengerjaanta/RerataLamaPengerjaanTA.jasper");
                Map parameters = new HashMap();
//                parameters.put("chart", chart.createBufferedImage(500, 300, BufferedImage.TRANSLUCENT, null));
                report.setParameters(parameters);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
