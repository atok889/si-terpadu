/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pendidikankaryawan;

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
public class PendidikanKaryawanWnd extends ClassApplicationModule {

    private PendidikanKaryawanDAO pendidikanKaryawanDAO;
    private Combobox cmbboxUnitKerja;
    private Combobox cmbExportType;
    private Listbox listboxKaryawan;
    private Jasperreport report;
    private Button btnExport;
    private String unitKerja;
    private List<Map> datas;

    public PendidikanKaryawanWnd() {
        pendidikanKaryawanDAO = new PendidikanKaryawanDAOImpl();
    }

    public void onCreate() {
        cmbboxUnitKerja = (Combobox) this.getFellow("cmbboxUnitKerja");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        listboxKaryawan = (Listbox) this.getFellow("listboxKaryawan");
        report = (Jasperreport) this.getFellow("report");
        btnExport = (Button) this.getFellow("btnExport");
        cmbExportType.setSelectedIndex(0);
        this.loadDataToComboboxUnitKerja();
    }

    public void loadDataToComboboxUnitKerja() {
        Comboitem item = new Comboitem();
        item.setValue(null);
        item.setLabel("--Pilih Unit Kerja--");
        cmbboxUnitKerja.appendChild(item);

        for (Map data : this.pendidikanKaryawanDAO.getUnitKerja()) {
            Comboitem comboitem = new Comboitem();
            comboitem.setValue(data.get("Kd_unit_kerja"));
            comboitem.setLabel(data.get("Kd_unit_kerja").toString() + " " + data.get("Nama_unit_kerja").toString());
            cmbboxUnitKerja.appendChild(comboitem);
        }
        cmbboxUnitKerja.setSelectedIndex(0);
    }

    private void componentDisable() {
        listboxKaryawan.setVisible(false);
        btnExport.setDisabled(true);
    }

    private void componentEnable() {
        listboxKaryawan.setVisible(true);
        btnExport.setDisabled(false);
    }

    public void loadDataToListbox() {
        listboxKaryawan.getChildren().clear();
        Listhead listhead = new Listhead();

        Listheader listheaderNama = new Listheader("Nama");
        listheaderNama.setWidth("270px");
        listhead.appendChild(listheaderNama);

        Listheader listheaderUnitKerja = new Listheader("Unit Kerja");
        listheaderUnitKerja.setWidth("480px");
        listhead.appendChild(listheaderUnitKerja);

        Listheader listheaderJenjang = new Listheader("Jenjang Pendidikan");
        listhead.appendChild(listheaderJenjang);

        datas = pendidikanKaryawanDAO.getPendidikanKaryawan(unitKerja);

        for (Map row : datas) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(row.get("Nama_peg").toString()));
            listitem.appendChild(new Listcell(row.get("Nama_unit_kerja").toString()));
            listitem.appendChild(new Listcell(row.get("Nm_jenjang").toString()));
            listboxKaryawan.appendChild(listitem);
        }
        listboxKaryawan.appendChild(listhead);
    }

    public void cmbUnitKerjaOnSelect() {
        this.componentDisable();
        unitKerja = (String) cmbboxUnitKerja.getSelectedItem().getValue();
    }

    public void btnShowOnClick() throws InterruptedException {
        if (unitKerja == null) {
            Messagebox.show("Pilih unit kerja !", "Error", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            this.loadDataToListbox();
            this.componentEnable();
        }
    }

    public void exportReport() throws Exception {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(new ArrayList());
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
