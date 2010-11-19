/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.distribusiumurdosendankaryawan;

import java.util.ArrayList;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class DistribusiUmurDosenDanKaryawanWnd extends ClassApplicationModule {

    private DistribusiUmurDosenDanKaryawanDAO distribusiUmurDosenDAO;
    private Combobox cmbboxProdi;
    private String kodeProdi;
    private Listbox listboxData;
    private Combobox cmbExportType;
    private Combobox cmbboxUmur;
    private Jasperreport report;
    private Button btnExport;
    private Groupbox groupDetail;

    public DistribusiUmurDosenDanKaryawanWnd() {
        distribusiUmurDosenDAO = new DistribusiUmurDosenDanKaryawanDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxData = (Listbox) this.getFellow("listboxData");
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        groupDetail = (Groupbox) getFellow("groupDetail");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbboxUmur = (Combobox) getFellow("cmbboxUmur");       
        cmbExportType.setSelectedIndex(0);
        this.loadDataProdiToCombo();
    }

    private void loadDataProdiToCombo() {
//        Comboitem item = new Comboitem("--SEMUA FAKULTAS--");
//        item.setValue(null);
//        cmbboxProdi.appendChild(item);
//        cmbboxProdi.setSelectedItem(item);
//
//        for (Map map : warningAdministratifDAO.getProdi()) {
//            Comboitem items = new Comboitem();
//            items.setValue(map.get("Kd_prg").toString());
//            items.setLabel(map.get("Kd_prg").toString() + " " + map.get("Nama_prg").toString());
//            cmbboxProdi.appendChild(items);
//        }
    }

    private void loadDataToListbox() {
        this.listboxData.getItems().clear();
//        datas = warningAdministratifDAO.getWarningAdministratif(kodeProdi);
//
//        int no = 1;
//        for (Map row : datas) {
//            Listitem listitem = new Listitem();
//            listitem.appendChild(new Listcell(no + ""));
//            listitem.appendChild(new Listcell(row.get("nama_mhs").toString()));
//            listitem.appendChild(new Listcell(row.get("Nama_prg").toString()));
//            listitem.appendChild(new Listcell(row.get("Nama_fak").toString()));
//            listitem.appendChild(new Listcell(row.get("angkatan").toString()));
//            listboxMahasiswa.appendChild(listitem);
//            no++;
//        }
    }

    public void cmbDataProdiOnSelect() {
//        this.componentDisable();
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
    }

    public void cmbDataUmurOnSelect() {
    }

    public void btnShowOnClick() {
//        this.componentEnable();
        this.loadDataToListbox();
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList());
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/warning/warningadministratif/WarningAdministratif.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/warning/warningadministratif/WarningAdministratif.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
