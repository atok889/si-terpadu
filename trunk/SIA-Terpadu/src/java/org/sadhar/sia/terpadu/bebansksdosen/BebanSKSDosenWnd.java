/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.bebansksdosen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;
import org.sadhar.sia.terpadu.prodi.ProgramStudiDAO;
import org.sadhar.sia.terpadu.prodi.ProgramStudiDAOImpl;
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
public class BebanSKSDosenWnd extends ClassApplicationModule {

    Combobox cmbProgdi;
    //Textbox txtTahun;
    Combobox cmbTahun;
    Combobox cmbSemester;
    Jasperreport report;
    Listbox lstData;
    Combobox cmbExportType;
    Button btnExport;

    public BebanSKSDosenWnd() {
    }

    public void onCreate() throws Exception {
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        //txtTahun = (Textbox) getFellow("txtTahun");
        cmbTahun = (Combobox)getFellow("cmbTahun");
        cmbSemester = (Combobox) getFellow("cmbSemester");
        report = (Jasperreport) getFellow("report");
        lstData = (Listbox) getFellow("lstData");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        cmbExportType.setSelectedIndex(0);
        loadProgdi();
        loadSemester();
        cmbProgdi.setSelectedIndex(0);
        cmbSemester.setSelectedIndex(0);
        loadTahun();
        cmbTahun.setSelectedIndex(0);

    }

    private void loadTahun() throws Exception {
        int currentYear = new DateTime().getYear();
        cmbTahun.getItems().clear();
        Comboitem item = new Comboitem();
        item.setValue("");
        item.setLabel("-- Pilih Tahun --");
        cmbTahun.appendChild(item);
        for (int x = 2000; x <= currentYear; x++) {
            Comboitem itm = new Comboitem();
            itm.setValue(x);
            itm.setLabel(x + "");
            cmbTahun.appendChild(itm);
        }
    }

    private void loadProgdi() throws Exception {
        try {
            ProgramStudiDAO dao = new ProgramStudiDAOImpl();
            List<ProgramStudi> progdis = dao.getProgramStudi();
            cmbProgdi.getItems().clear();
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

    private void loadSemester() throws Exception {
        cmbSemester.getItems().clear();
        Comboitem item1 = new Comboitem();
        item1.setValue(1);
        item1.setLabel("Gasal (1)");
        cmbSemester.appendChild(item1);
        Comboitem item2 = new Comboitem();
        item2.setValue(2);
        item2.setLabel("Genap (2)");
        cmbSemester.appendChild(item2);
    }
    List<BebanSKSDosen> bebans = null;

    public void viewReport() throws Exception {
        try {
            lstData.getItems().clear();
            if (!cmbTahun.getSelectedItem().getValue().toString().isEmpty()) {
                BebanSKSDosenDAO dao = new BebanSKSDosenDAOImpl();
                bebans = dao.getBebanSKSDosen((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), cmbTahun.getSelectedItem().getValue().toString(), Integer.valueOf(cmbSemester.getSelectedItem().getValue().toString()));
                for (BebanSKSDosen bsd : bebans) {
                    Listitem item = new Listitem();
                    item.setValue(bsd);
                    item.appendChild(new Listcell(bsd.getNama()));
                    item.appendChild(new Listcell(bsd.getSks() + ""));
                    lstData.appendChild(item);
                }
                btnExport.setDisabled(false);
            } else {
                Messagebox.show("Silahkan input Tahun Ajaran");
            }
        } catch (Exception ex) {
            Messagebox.show("Data gagal ditampilkan");
        }
    }

    public void exportReport() throws Exception {

        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(bebans);
            Map param = new HashMap();            
            param.put("prodi", ((ProgramStudi) cmbProgdi.getSelectedItem().getValue()).getNama());
            param.put("tahun", cmbTahun.getSelectedItem().getValue().toString());
            param.put("semester", cmbSemester.getSelectedItem().getValue() + "");
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/bebansksdosen/BebanSKSDosen.jasper");
                pdfReport.setParameters(param);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/bebansksdosen/BebanSKSDosen.jasper");
                report.setParameters(param);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
