/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswa;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
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
public class JumlahMahasiswaWnd extends ClassApplicationModule {

    Combobox cmbProgdi;
    Textbox txtTahunAngkatan;
    Jasperreport report;
    Image chartImg;
    Combobox cmbExportType;
    Button btnExport;

    public JumlahMahasiswaWnd() {
    }

    public void onCreate() throws Exception {
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        txtTahunAngkatan = (Textbox) getFellow("txtTahunAngkatan");
        report = (Jasperreport) getFellow("report");
        chartImg = (Image) getFellow("chartImg");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        loadProgdi();
        cmbProgdi.setSelectedIndex(0);
    }

    private void loadProgdi() throws Exception {
        try {
            JumlahMahasiwaDAO dao = new JumlahMahasiswaDAOImpl();
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
            JumlahMahasiwaDAO dao = new JumlahMahasiswaDAOImpl();
            CategoryDataset dataset = dao.getDataset((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), txtTahunAngkatan.getValue());
            JFreeChart chart = null;
            if (cmbProgdi.getSelectedItem().getValue() == null && txtTahunAngkatan.getValue().isEmpty()) {
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
            final CategoryItemRenderer renderer1 = plot.getRenderer();

            renderer1.setSeriesPaint(0, Color.red);
            renderer1.setSeriesPaint(1, Color.yellow);
            renderer1.setSeriesPaint(2, Color.green);
            renderer1.setSeriesPaint(3, Color.blue);
            renderer1.setSeriesPaint(4, Color.cyan);

            BufferedImage bi = chart.createBufferedImage(500, 300, BufferedImage.TRANSLUCENT, null);
            byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);

            AImage image = new AImage("Bar Chart", bytes);
            chartImg.setContent(image);

        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show(ex.getMessage());
        }
    }

    public void exportReport() throws Exception {
    }

    private CategoryDataset createDataset1() {

        // row keys...
        final String series1 = "First";
        final String series2 = "Second";
        final String series3 = "Third";

        // column keys...
        final String category1 = "Category 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";
        final String category5 = "Category 5";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, series1, category1);
        dataset.addValue(4.0, series1, category2);
        dataset.addValue(3.0, series1, category3);
        dataset.addValue(5.0, series1, category4);
        dataset.addValue(5.0, series1, category5);

        dataset.addValue(5.0, series2, category1);
        dataset.addValue(7.0, series2, category2);
        dataset.addValue(6.0, series2, category3);
        dataset.addValue(8.0, series2, category4);
        dataset.addValue(4.0, series2, category5);

        dataset.addValue(4.0, series3, category1);
        dataset.addValue(3.0, series3, category2);
        dataset.addValue(2.0, series3, category3);
        dataset.addValue(3.0, series3, category4);
        //dataset.addValue(6.0, series3, category5);

        return dataset;

    }
}
