/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.dptiga;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.JFreeChart;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudi;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudiDAO;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudiDAOImpl;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author Deny Prasetyo
 */
public class DPTigaWnd extends ClassApplicationModule {

    Combobox cmbProgdi;
    Jasperreport report;
//    Image chartImg;
    Combobox cmbExportType;
    Button btnExport;
    Combobox cmbboxTahun;
    Label labelRataRata;
    Listbox listb;
    JFreeChart chart = null;
    List<DPTiga> datas;

    public DPTigaWnd() {
    }

    public void onCreate() throws Exception {
        listb = (Listbox) getFellow("listb");
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        report = (Jasperreport) getFellow("report");
        labelRataRata = (Label) getFellow("labelRataRata");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        cmbboxTahun = (Combobox) getFellow("cmbboxTahun");
//        chartImg = (Image) getFellow("chartImg");
        btnExport.setDisabled(true);
        loadTahun();
        loadProgdi();
        cmbboxTahun.setSelectedIndex(cmbboxTahun.getItems().size() - 1);
        cmbProgdi.setSelectedIndex(0);
        cmbExportType.setSelectedIndex(0);
    }

    private void loadTahun() throws Exception {
        int year = 2005;
        while (year <= Calendar.getInstance().get(Calendar.YEAR)) {
            Comboitem item = new Comboitem();
            item.setLabel("" + year);
            item.setValue("" + year);
            year++;
            cmbboxTahun.appendChild(item);
        }
    }

    private void loadProgdi() throws Exception {
        try {
            UKProgramStudiDAO dao = new UKProgramStudiDAOImpl();
            List<UKProgramStudi> progdis = dao.gets();
            cmbProgdi.getItems().clear();
            Comboitem item = new Comboitem();
            item.setValue(null);
            item.setLabel("--Pilih Prodi--");
            cmbProgdi.appendChild(item);
            for (UKProgramStudi ps : progdis) {
                Comboitem items = new Comboitem();
                items.setValue(ps);
                items.setLabel(ps.getShortKode() + " " + ps.getNama());
                cmbProgdi.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public void viewReport() throws Exception {
        try {
            String tahun = cmbboxTahun.getSelectedItem().getLabel();
            listb.getItems().clear();
            DPTigaDAO dao = new DPTigaDAOImpl();
            UKProgramStudi prodiSelected = (UKProgramStudi) cmbProgdi.getSelectedItem().getValue();
            if (prodiSelected == null) {
                datas = dao.getAll(tahun);
            } else {

                datas = dao.getByKodeUnit(tahun,prodiSelected.getKodeUnitKerja());
            }

            DecimalFormat decimalFormat = new DecimalFormat("##0.00");
            int i = 1;
            double total = 0;
            for (DPTiga o : datas) {
                Listitem li = new Listitem();
                Listcell cell = new Listcell("" + (i++));
                li.appendChild(cell);
                cell = new Listcell(o.getNamaPegawai());
                li.appendChild(cell);
                cell = new Listcell(decimalFormat.format(o.getNilaiDP3()));
                cell.setStyle("text-align:right");
                total += o.getNilaiDP3();
                li.appendChild(cell);
                listb.appendChild(li);
            }

            double rataRata = total / datas.size();
            String prodi = "";
            if (prodiSelected == null) {
                prodi = "Keseluruhan";
            } else {
                prodi = prodiSelected.getNama();
            }
            String rataString = "Rerata " + prodi + " : " + rataRata;
            labelRataRata.setValue(rataString);
            btnExport.setDisabled(false);


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
                pdfReport.setSrc("reports/dptiga/DPTiga.jasper");
                Map parameters = new HashMap();
//                parameters.put("chart", chart.createBufferedImage(500, 300));
                pdfReport.setParameters(parameters);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/dptiga/DPTiga.jasper");
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
