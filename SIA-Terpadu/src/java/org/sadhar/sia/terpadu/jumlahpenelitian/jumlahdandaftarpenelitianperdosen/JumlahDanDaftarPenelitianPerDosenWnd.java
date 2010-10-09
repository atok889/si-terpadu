/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahpenelitian.jumlahdandaftarpenelitianperdosen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class JumlahDanDaftarPenelitianPerDosenWnd extends ClassApplicationModule {

    private JumlahDanDaftarPenelitianPerDosenDAO jumlahDanDaftarPenelitianPerDosenDAO;
    private Combobox cmbboxProdi;
    private Combobox cmbExportType;
    private Jasperreport report;
    private String kodeProdi;
    private Button btnExport;

    public JumlahDanDaftarPenelitianPerDosenWnd() {
        jumlahDanDaftarPenelitianPerDosenDAO = new JumlahDanDaftarPenelitianPerDosenDAOImpl();
    }

    public void onCreate() throws Exception {
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataProdiToCombo();
    }

    private void loadDataProdiToCombo() {
        Comboitem item = new Comboitem("--Pilih Fakultas--");
        item.setValue(null);
        cmbboxProdi.appendChild(item);
        cmbboxProdi.setSelectedItem(item);

        for (Map map : jumlahDanDaftarPenelitianPerDosenDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Kd_prg").toString() + " " + map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
        cmbboxProdi.setReadonly(true);
    }

    private void componentDisable() {
        btnExport.setDisabled(true);
    }

    private void componentEnable() {
        btnExport.setDisabled(false);
    }

    public void cmbDataProdiOnSelect() {
        this.componentDisable();
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
    }

    public void btnShowOnClick() throws InterruptedException {
        try {
            this.generateData(kodeProdi);
            this.componentEnable();
        } catch (Exception e) {
            this.componentDisable();
            Messagebox.show("Data tidak ditemukan", "Konfirmasi", Messagebox.OK, Messagebox.EXCLAMATION);
        }
    }

    public void exportReport() throws Exception {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(this.generateData(kodeProdi));
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

    private List<Map> generateData(String kodeProdi) {
        List<Map> datas = new ArrayList<Map>();
        return datas;
    }
}
