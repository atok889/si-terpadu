/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningsemester;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.sadhar.sia.framework.ClassApplicationModule;
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
 * @author kris
 */
public class WarningSemesterWnd extends ClassApplicationModule {

    private WarningSemesterDAO warningSemesterDAO;
    private Combobox cmbboxProdi;
    private String kodeProdi;
    private Listbox listboxMahasiswa;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private String tahunAkademik = "2000";
    private String semester = "2";
    List<Map> datas = new ArrayList<Map>();

    public WarningSemesterWnd() {
        warningSemesterDAO = new WarningSemesterDAOImpl();
    }

    public void onCreate() throws Exception {
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        cmbboxProdi.setReadonly(true);
        cmbExportType.setReadonly(true);
        this.loadDataProdiToCombo();
    }

    private void loadDataProdiToCombo() {
        Comboitem item = new Comboitem();
        item.setValue("all");
        item.setLabel("--SEMUA FAKULTAS--");
        cmbboxProdi.appendChild(item);
        cmbboxProdi.setSelectedItem(item);

        for (Map map : warningSemesterDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
    }

    private void loadDataToListbox() {
        this.listboxMahasiswa.getChildren().clear();
        Listhead listhead = new Listhead();

        Listheader listheaderNo = new Listheader();
        listheaderNo.setLabel("No");
        listheaderNo.setWidth("50px");
        listheaderNo.setAlign("right");
        listhead.appendChild(listheaderNo);

        Listheader listheaderNama = new Listheader();
        listheaderNama.setLabel("Nama");
        listheaderNama.setWidth("300px");
        listhead.appendChild(listheaderNama);

        Listheader listheaderProdi = new Listheader();
        listheaderProdi.setLabel("Prodi");
        listhead.appendChild(listheaderProdi);

        Listheader listheaderFakultas = new Listheader();
        listheaderFakultas.setLabel("Fakultas");
        listhead.appendChild(listheaderFakultas);

        Listheader listheaderAngkatan = new Listheader();
        listheaderAngkatan.setLabel("Tahun Angkatan");
        listheaderAngkatan.setAlign("right");
        listheaderAngkatan.setWidth("120px");
        listhead.appendChild(listheaderAngkatan);

        if (kodeProdi.equalsIgnoreCase("all")) {
            datas = warningSemesterDAO.getWarningSemester(semester);
        } else {
            try {
                datas = warningSemesterDAO.getWarningSemester(kodeProdi, semester);
            } catch (Exception e) {
                try {
                    Messagebox.show("Data tidak ditemukan", "Information", Messagebox.OK, Messagebox.INFORMATION);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WarningSemesterWnd.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        int no = 1;
        for (Map item : datas) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(no + ""));
            listitem.appendChild(new Listcell(item.get("nama_mhs").toString()));
            listitem.appendChild(new Listcell(item.get("Nama_prg").toString()));
            listitem.appendChild(new Listcell(item.get("Nama_fak").toString()));
            listitem.appendChild(new Listcell(item.get("angkatan").toString()));
            listboxMahasiswa.appendChild(listitem);
            no++;
        }
        listboxMahasiswa.appendChild(listhead);
        listboxMahasiswa.setVisible(true);
    }

    public void cmbDataProdiOnSelect() {
        btnExport.setDisabled(true);
        listboxMahasiswa.setVisible(false);
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
    }

    public void btnShowOnClick() {
        this.loadDataToListbox();
        btnExport.setDisabled(false);
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/warning/warningsemester/WarningSemester.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/warning/warningsemester/WarningSemester.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
