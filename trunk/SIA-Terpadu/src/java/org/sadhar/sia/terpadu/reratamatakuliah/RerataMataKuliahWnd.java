package org.sadhar.sia.terpadu.reratamatakuliah;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudi;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudiDAO;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudiDAOImpl;
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
import org.zkoss.zul.Window;

/**
 *
 * @author Deny Prasetyo
 */
public class RerataMataKuliahWnd extends ClassApplicationModule {

    Combobox cmbProgdi;
    Combobox cmbTahunAwal;
    Combobox cmbTahunAkhir;
    Jasperreport report;
//    Image chartImg;
    Combobox cmbExportType;
    Button btnExport;
    Listbox listb;
    JFreeChart chart = null;
    CategoryDataset dataset;
    List<RerataMataKuliah> data;
    RerataMataKuliahDAO dao;

    public RerataMataKuliahWnd() {
        dao = new RerataMataKuliahDAOImpl();
    }

    public void onCreate() throws Exception {
        listb = (Listbox) getFellow("listb");
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        cmbTahunAwal = (Combobox) getFellow("cmbTahunAwal");
        cmbTahunAkhir = (Combobox) getFellow("cmbTahunAkhir");
        report = (Jasperreport) getFellow("report");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
//        chartImg = (Image) getFellow("chartImg");
        btnExport.setDisabled(true);
        loadProgdi();
        loadTahunCombo();
        cmbProgdi.setSelectedIndex(0);
        cmbTahunAwal.setSelectedIndex(0);
        cmbTahunAkhir.setSelectedIndex(cmbTahunAkhir.getItemCount() - 1);
        cmbExportType.setSelectedIndex(0);
    }

    private void loadTahunCombo() throws Exception {
        int tahunAwal = 2005;
        int tahunAkhir = Calendar.getInstance().get(Calendar.YEAR);
        cmbTahunAwal.getItems().clear();
        cmbTahunAkhir.getItems().clear();

        for (int i = tahunAwal; i <= tahunAkhir; i++) {
            Comboitem item1 = new Comboitem();
            item1.setValue(i);
            item1.setLabel(i + "");
            Comboitem item = new Comboitem();
            item.setValue(i);
            item.setLabel(i + "");

            cmbTahunAwal.appendChild(item);
            cmbTahunAkhir.appendChild(item1);
        }
    }

    private void loadProgdi() throws Exception {
        try {
            UKProgramStudiDAO dao = new UKProgramStudiDAOImpl();
            List<UKProgramStudi> progdis = dao.gets();
            cmbProgdi.getItems().clear();
//            Comboitem item = new Comboitem();
//            item.setValue(null);
//            item.setLabel("--Pilih Prodi--");
//            cmbProgdi.appendChild(item);
            for (UKProgramStudi ps : progdis) {
                Comboitem items = new Comboitem();
                items.setValue(ps);
                items.setLabel(ps.getKodeUnitKerja() + " " + ps.getNama());
                cmbProgdi.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public void viewReport() throws Exception {
        try {
            listb.getItems().clear();

            UKProgramStudi prodiSelected = (UKProgramStudi) cmbProgdi.getSelectedItem().getValue();
            Integer tahunAwal = (Integer) cmbTahunAwal.getSelectedItem().getValue();
            Integer tahunAkhir = (Integer) cmbTahunAkhir.getSelectedItem().getValue();
            dataset = dao.getDataset(prodiSelected.getShortKode(), tahunAwal.intValue(), tahunAkhir.intValue());

            DecimalFormat decimalFormat = new DecimalFormat("##0.00");

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
            lheader.setLabel("Mata Kuliah");
            lhead.appendChild(lheader);
            lheader.setWidth("400px");


            for (Object s : dataset.getColumnKeys()) {
                Listheader inlhd = new Listheader();
                inlhd.setLabel(s.toString());
                lhead.appendChild(inlhd);
            }

            for (Object s : dataset.getRowKeys()) {//Prodi
                Listitem item = new Listitem();
                Listcell cell = new Listcell();
                cell.setLabel(s.toString());
                item.appendChild(cell);
                for (Object f : dataset.getColumnKeys()) {//Tahun
                    cell = new Listcell();
                    Number nbr = dataset.getValue((Comparable) s, (Comparable) f);
                    if (nbr != null) {
                        cell.setLabel(decimalFormat.format(nbr.doubleValue()));
                    } else {
                        cell.setLabel("0");
                    }
                    cell.setStyle("text-align:right");
                    item.appendChild(cell);
                }
                listb.appendChild(item);
            }
            btnExport.setDisabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show(ex.getMessage());
        }
    }

    public void exportReport() throws Exception {
        try {
            UKProgramStudi prodiSelected = (UKProgramStudi) cmbProgdi.getSelectedItem().getValue();
            Integer tahunAwal = (Integer) cmbTahunAwal.getSelectedItem().getValue();
            Integer tahunAkhir = (Integer) cmbTahunAkhir.getSelectedItem().getValue();
            data = dao.gets(prodiSelected.getShortKode(), tahunAwal.intValue(), tahunAkhir.intValue());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/reratamatakuliah/RerataMataKuliah.jasper");
                Map parameters = new HashMap();
//                parameters.put("chart", chart.createBufferedImage(500, 300));
                pdfReport.setParameters(parameters);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/reratamatakuliah/RerataMataKuliah.jasper");
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
