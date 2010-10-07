/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.ipkdanipssetiapmahasiswa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class IpkdanIpsSetiapMahasiswaWnd extends ClassApplicationModule {

    private IpkdanIpsSetiapMahasiswaDAO ipkDanIpsSetiapMahasiswaDAO;
    private Listbox listboxMahasiswa;
    private Combobox cmbboxProdi;
    private Combobox cmbboxAngkatan;
    private Combobox cmbboxSemester;
    private Combobox cmbboxKolom1;
    private Combobox cmbboxKolom2;
    private Combobox cmbboxKolom3;
    private String kodeProdi;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private List<Map> dataReport = new ArrayList<Map>();

    public IpkdanIpsSetiapMahasiswaWnd() {
        ipkDanIpsSetiapMahasiswaDAO = new IpkdanIpsSetiapMahasiswaDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        cmbboxAngkatan = (Combobox) this.getFellow("cmbboxAngkatan");
        cmbboxSemester = (Combobox) this.getFellow("cmbboxSemester");
        cmbboxKolom1 = (Combobox) this.getFellow("cmbboxKolom1");
        cmbboxKolom2 = (Combobox) this.getFellow("cmbboxKolom2");
        cmbboxKolom3 = (Combobox) this.getFellow("cmbboxKolom3");
        cmbboxProdi.setReadonly(true);
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataToComboboxProdi();
        this.loadDataToComboboxAngkatan();
        this.loadDataToComboboxSemester();
    }

    public void loadDataToComboboxProdi() {
        Comboitem item = new Comboitem();
        item.setValue(null);
        item.setLabel("--Pilih Fakultas/Prodi--");
        cmbboxProdi.appendChild(item);

        for (Map data : this.ipkDanIpsSetiapMahasiswaDAO.getProdi()) {
            Comboitem comboitem = new Comboitem();
            comboitem.setValue(data.get("Kd_prg"));
            comboitem.setLabel(data.get("Kd_prg").toString() + " " + data.get("Nama_prg").toString());
            cmbboxProdi.appendChild(comboitem);
        }
        cmbboxProdi.setSelectedIndex(0);
    }

    private void loadDataToComboboxAngkatan() {
        Comboitem item = new Comboitem("--Pilih Angkatan--");
        item.setValue(null);
        cmbboxAngkatan.appendChild(item);
        cmbboxAngkatan.setSelectedItem(item);

        for (int i = 1980; i <= new DateTime().getYear(); i++) {
            Comboitem items = new Comboitem();
            items.setValue(i);
            items.setLabel(i + "");
            cmbboxAngkatan.appendChild(items);
        }
        cmbboxAngkatan.setReadonly(true);
    }

    private void loadDataToComboboxSemester() {
        Comboitem item = new Comboitem("--Pilih Semester--");
        item.setValue(null);
        cmbboxSemester.appendChild(item);
        cmbboxSemester.setSelectedItem(item);

        for (int i = 1; i <= 16; i++) {
            for (int j = 1; j <= 2; j++) {
                Comboitem items = new Comboitem();
                items.setValue(i);
                items.setLabel("Semester " + i + "-" + j);
                cmbboxSemester.appendChild(items);
            }
        }
        cmbboxSemester.setReadonly(true);
    }

    private void componentDisable() {
        listboxMahasiswa.setVisible(false);
        btnExport.setDisabled(true);
    }

    private void componentEnable() {
        listboxMahasiswa.setVisible(true);
        btnExport.setDisabled(false);
    }

    private void loadDataToListbox() {
    }

    public void cmbDataProdiOnSelect() {
        this.componentDisable();
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
    }

    public void btnShowOnClick() throws InterruptedException {
        try {
            this.componentEnable();
            loadDataToListbox();
        } catch (Exception e) {
            this.componentDisable();
            e.printStackTrace();
            //Messagebox.show("Data tidak ditemukan", "Konfirmasi", Messagebox.OK, Messagebox.EXCLAMATION);
        }
    }

    public void exportReport() throws Exception {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dataReport);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/ipkdanips/rerataips/RerataIps.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/ipkdanips/rerataips/RerataIps.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
