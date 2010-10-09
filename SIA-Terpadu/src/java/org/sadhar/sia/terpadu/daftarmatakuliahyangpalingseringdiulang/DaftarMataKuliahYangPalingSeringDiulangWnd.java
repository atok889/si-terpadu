/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftarmatakuliahyangpalingseringdiulang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class DaftarMataKuliahYangPalingSeringDiulangWnd extends ClassApplicationModule {

    private DaftarMataKuliahYangPalingSeringDiulangDAO daftarMataKuliahYangPalingSeringDiulangDAO;
    private Combobox cmbboxProdi;
    private Combobox cmbboxSemester;
    private Listbox listboxData;
    private Combobox cmbExportType;
    private Jasperreport report;
    private String kodeProdi;
    private Button btnExport;

    public DaftarMataKuliahYangPalingSeringDiulangWnd() {
        daftarMataKuliahYangPalingSeringDiulangDAO = new DaftarMataKuliahYangPalingSeringDiulangDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxData = (Listbox) this.getFellow("listboxData");
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        cmbboxSemester = (Combobox) this.getFellow("cmbboxSemester");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataProdiToCombo();
        this.loadDataSemesterToCombo();
    }

    private void loadDataProdiToCombo() {
        Comboitem item = new Comboitem("--Pilih Fakultas--");
        item.setValue(null);
        cmbboxProdi.appendChild(item);
        cmbboxProdi.setSelectedItem(item);

        for (Map map : daftarMataKuliahYangPalingSeringDiulangDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Kd_prg").toString() + " " + map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
        cmbboxProdi.setReadonly(true);
    }

    private void loadDataSemesterToCombo() {
        Comboitem item = new Comboitem("--Pilih Semester--");
        cmbboxSemester.appendChild(item);
        cmbboxSemester.setSelectedItem(item);

        for (int i = 1; i <= 16; i++) {
            Comboitem items = new Comboitem();
            items.setValue(i);
            items.setLabel("Semester " + i);
            cmbboxSemester.appendChild(items);
        }
        cmbboxSemester.setReadonly(true);
    }

    private void componentDisable() {
        listboxData.setVisible(false);
        btnExport.setDisabled(true);
    }

    private void componentEnable() {
        listboxData.setVisible(true);
        btnExport.setDisabled(false);
    }

    private void loadDataToListbox() {
        listboxData.getChildren().clear();
        Listhead listhead = new Listhead();

        Listheader listheaderNo = new Listheader("No");
        listheaderNo.setWidth("40px");
        listheaderNo.setAlign("right");
        listhead.appendChild(listheaderNo);

        Listheader listheaderMataKuliah = new Listheader("Mata Kuliah");
        listheaderMataKuliah.setWidth("200px");
        listhead.appendChild(listheaderMataKuliah);

        Listheader listheaderSKS = new Listheader("SKS");
        listheaderSKS.setWidth("60px");
        listheaderSKS.setAlign("right");
        listhead.appendChild(listheaderSKS);

        Listheader listheaderSemester = new Listheader("Semester(dalam kurikulum)");
        listheaderSemester.setWidth("200px");
        listheaderSemester.setAlign("right");
        listhead.appendChild(listheaderSemester);

        Listheader listheaderJumlah = new Listheader("Jumlah Mahasiswa Mengulang");
        listheaderJumlah.setAlign("right");
        listhead.appendChild(listheaderJumlah);

        Listheader listheaderDosen = new Listheader("Dosen");
        listhead.appendChild(listheaderDosen);

        listboxData.appendChild(listhead);


    }

    public void cmbDataProdiOnSelect() {
        this.componentDisable();
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
    }

    public void btnShowOnClick() throws InterruptedException {
        try {
            loadDataToListbox();
            this.componentEnable();
        } catch (Exception e) {
            this.componentDisable();
            Messagebox.show("Data tidak ditemukan", "Konfirmasi", Messagebox.OK, Messagebox.EXCLAMATION);
        }
    }

    public void exportReport() throws Exception {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(this.generateReport(kodeProdi));
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/statistiklamastudi/StatistikLamaStudi.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/statistiklamastudi/StatistikLamaStudi.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    private List<Map> generateReport(String kodeProdi) {
        List<Map> datas = new ArrayList<Map>();
        return datas;
    }
}
