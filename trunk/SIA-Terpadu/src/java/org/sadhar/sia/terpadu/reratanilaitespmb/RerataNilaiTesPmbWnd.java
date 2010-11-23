/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.reratanilaitespmb;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author Hendro Steven
 */
public class RerataNilaiTesPmbWnd extends ClassApplicationModule {

    Combobox cmbTahun;
    Jasperreport report;
    Combobox cmbExportType;
    Listbox lstData;
    Button btnExport;
    List<RerataNilaiTesPmb> datas = new ArrayList<RerataNilaiTesPmb>();

    public void onCreate() throws Exception {
        cmbTahun = (Combobox) getFellow("cmbTahun");
        report = (Jasperreport) getFellow("report");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        lstData = (Listbox) getFellow("lstData");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType.setSelectedIndex(0);
        loadTahun();
        cmbTahun.setSelectedIndex(0);
    }

    private void loadTahun() {
        int currentYear = new DateTime().getYear();
        cmbTahun.getItems().clear();
        Comboitem item1 = new Comboitem();
        item1.setValue(null);
        item1.setLabel("-- Pilih Tahun --");
        cmbTahun.appendChild(item1);
        for (int x = 2000; x <= currentYear; x++) {
            Comboitem itm1 = new Comboitem();
            itm1.setValue(x);
            itm1.setLabel(x + "");
            cmbTahun.appendChild(itm1);

        }
    }

    public void viewReport() throws Exception {
        try {
            RerataNilaiTesPmbDAO dao = new RerataNilaiTesPmbDAOImpl();
            datas = dao.getAllRerataNilaiTesPmb(cmbTahun.getSelectedItem().getValue().toString());
            lstData.getItems().clear();
            for (RerataNilaiTesPmb rt : datas) {
                Listitem item = new Listitem();
                item.setValue(rt);
                item.appendChild(new Listcell(rt.getProgdi()));
                item.appendChild(new Listcell(String.valueOf(rt.getPv())));
                item.appendChild(new Listcell(String.valueOf(rt.getKn())));
                item.appendChild(new Listcell(String.valueOf(rt.getPm())));
                item.appendChild(new Listcell(String.valueOf(rt.getHr())));
                item.appendChild(new Listcell(String.valueOf(rt.getBi())));
                item.appendChild(new Listcell(String.valueOf(rt.getNilaiFinal())));
                lstData.appendChild(item);
            }
            if (datas.size() > 0) {
                btnExport.setDisabled(false);
            } else {
                btnExport.setDisabled(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show("Gagal menampilkan data");
        }
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/reratanilaitespmb/RerataNilaiTesPmb.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/reratanilaitespmb/RerataNilaiTesPmb.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
