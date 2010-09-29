/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswado;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Deny Prasetyo
 */
public class JumlahMahasiswaDOWnd extends ClassApplicationModule {

    Combobox cmbProgdi;
    Textbox txtTahunAngkatan;
    Jasperreport report;
//    Image chartImg;
    Combobox cmbExportType;
    Button btnExport;
    Listbox listb;
    JFreeChart chart = null;
    List<JumlahMahasiswaDO> datas;

    public JumlahMahasiswaDOWnd() {
    }

    public void onCreate() throws Exception {
        listb = (Listbox) getFellow("listb");
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        txtTahunAngkatan = (Textbox) getFellow("txtTahunAngkatan");
        report = (Jasperreport) getFellow("report");
//        chartImg = (Image) getFellow("chartImg");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        loadProgdi();
        cmbProgdi.setSelectedIndex(0);
        cmbExportType.setSelectedIndex(0);
    }

    private void loadProgdi() throws Exception {
        try {
            JumlahMahasiwaDODAO dao = new JumlahMahasiswaDODAOImpl();
            List<ProgramStudi> progdis = dao.getProgramStudi();
            cmbProgdi.getItems().clear();
            Comboitem item = new Comboitem();
            item.setValue(null);
            item.setLabel("--Pilih Prodi--");
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
            JumlahMahasiwaDODAO dao = new JumlahMahasiswaDODAOImpl();
            CategoryDataset dataset = dao.getDataset((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), txtTahunAngkatan.getValue());


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
            lhead.appendChild(lheader);


            for (Object s : dataset.getRowKeys()) {
                Listheader inlhd = new Listheader();
                inlhd.setLabel(s.toString());
                lhead.appendChild(inlhd);
            }

            Listheader inlhd = new Listheader();
            inlhd.setLabel("Total Prodi");
            lhead.appendChild(inlhd);

            datas = new ArrayList<JumlahMahasiswaDO>();

            int[] totalUniversitas = new int[dataset.getRowKeys().size()];
            int totalKeseluruhan = 0;

            for (Object s : dataset.getColumnKeys()) {//Prodi
                Listitem item = new Listitem();
                Listcell cell = new Listcell();
                cell.setLabel(s.toString());
                item.appendChild(cell);
                int jumlahPerProdi = 0;
                int indexTotalUniv = 0;
                for (Object f : dataset.getRowKeys()) {//Tahun
                    JumlahMahasiswaDO jmd = new JumlahMahasiswaDO();
                    cell = new Listcell();
                    Number nbr = dataset.getValue((Comparable) f, (Comparable) s);
                    jmd.setProdi(s.toString());
                    jmd.setTahun(f.toString());
                    if (nbr != null) {
                        cell.setLabel(nbr.toString());
                        jmd.setJumlah(nbr.intValue());
                        jumlahPerProdi += nbr.intValue();
                        totalUniversitas[indexTotalUniv] += nbr.intValue();
                    } else {
                        cell.setLabel("0");
                        jmd.setJumlah(0);
                        jumlahPerProdi += 0;
                        totalUniversitas[indexTotalUniv] += 0;
                    }
                    cell.setStyle("text-align:right");
                    item.appendChild(cell);
                    datas.add(jmd);
                    indexTotalUniv++;
                }
                cell = new Listcell();
                cell.setLabel(jumlahPerProdi + "");
                cell.setStyle("text-align:right");

                totalKeseluruhan += jumlahPerProdi;

                item.appendChild(cell);
                listb.appendChild(item);
            }

            Listitem item = new Listitem();
            Listcell cell = new Listcell();
            cell.setLabel("Total Universitas");
            item.appendChild(cell);

            for (int res : totalUniversitas) {
                cell = new Listcell();
                cell.setLabel(res + "");
                cell.setStyle("text-align:right");
                item.appendChild(cell);
            }

            cell = new Listcell();
            cell.setLabel(totalKeseluruhan + "");
            cell.setStyle("text-align:right");
            item.appendChild(cell);

            listb.appendChild(item);

            btnExport.setDisabled(false);
            /*
            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            BarRenderer.setDefaultBarPainter(new StandardBarPainter());
            if (cmbProgdi.getSelectedItem().getValue() == null) {
            chart = ChartFactory.createBarChart(
            "Jumlah Mahasiswa Drop Out", // chart title
            "Program Studi", // domain axis label
            "Jumlah Mahasiswa", // range axis label
            dataset, // data
            PlotOrientation.HORIZONTAL,
            true, // include legend
            true,
            false);
            } else {
            chart = ChartFactory.createBarChart(
            "Jumlah Mahasiswa Drop Out", // chart title
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

            final CategoryItemRenderer renderer1 = plot.getRenderer();
            renderer1.setSeriesPaint(0, Color.red);
            renderer1.setSeriesPaint(1, Color.yellow);
            renderer1.setSeriesPaint(2, Color.green);
            renderer1.setSeriesPaint(3, Color.blue);
            renderer1.setSeriesPaint(4, Color.cyan);
            BarRenderer br = (BarRenderer) renderer1;
            br.setShadowVisible(false);


            BufferedImage bi = chart.createBufferedImage(800, 400, BufferedImage.TRANSLUCENT, null);
            if (cmbProgdi.getSelectedItem().getValue() == null) {
            bi = chart.createBufferedImage(800, 1500, BufferedImage.TRANSLUCENT, null);
            }

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
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/jumlahmahasiswado/JumlahMahasiswaDO.jasper");
                Map parameters = new HashMap();
//                parameters.put("chart", chart.createBufferedImage(500, 300));
                pdfReport.setParameters(parameters);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/jumlahmahasiswado/JumlahMahasiswaDO.jasper");
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
