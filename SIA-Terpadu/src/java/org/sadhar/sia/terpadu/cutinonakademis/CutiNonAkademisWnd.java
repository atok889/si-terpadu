/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.cutinonakademis;

import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.dptiga.DPTiga;
import org.sadhar.sia.terpadu.dptiga.DPTigaDAO;
import org.sadhar.sia.terpadu.dptiga.DPTigaDAOImpl;
import org.sadhar.sia.terpadu.matrikborang.MatrikBorang;
import org.sadhar.sia.terpadu.matrikborang.MatrikBorangDAO;
import org.sadhar.sia.terpadu.matrikborang.MatrikBorangDAOImpl;
import org.sadhar.sia.terpadu.nepdosen.NEPDosen;
import org.sadhar.sia.terpadu.nepdosen.NEPDosenDAO;
import org.sadhar.sia.terpadu.nepdosen.NEPDosenDAOImpl;
import org.sadhar.sia.terpadu.pendidikankaryawan.PendidikanKaryawanDAO;
import org.sadhar.sia.terpadu.pendidikankaryawan.PendidikanKaryawanDAOImpl;
import org.sadhar.sia.terpadu.reratamatakuliah.RerataMataKuliahDAO;
import org.sadhar.sia.terpadu.reratamatakuliah.RerataMataKuliahDAOImpl;
import org.zkoss.zhtml.Messagebox;
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
import org.zkoss.zul.Window;

/**
 *
 * @author jasoet
 */
public class CutiNonAkademisWnd extends ClassApplicationModule {

    private PendidikanKaryawanDAO pendidikanKaryawanDAO;
    private CutiNonAkademisDAO cutiNonAkademisDAO;
    private Combobox cmbboxUnitKerja;
    private Combobox cmbExportType;
    private Listbox listboxKaryawan;
    private Jasperreport report;
    private Button btnExport;
    private String unitKerja;
    private List<CutiNonAkademis> datas;

    public CutiNonAkademisWnd() {
        cutiNonAkademisDAO = new CutiNonAkademisDAOImpl();
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
//        testDAOS();
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

    public void loadDataToListbox() throws Exception {
        listboxKaryawan.getChildren().clear();
        Listhead listhead = new Listhead();

        Listheader listheaderNama = new Listheader("Nama");
        listheaderNama.setWidth("270px");
        listhead.appendChild(listheaderNama);

        Listheader listheaderUnitKerja = new Listheader("Unit Kerja");
        listheaderUnitKerja.setWidth("480px");
        listhead.appendChild(listheaderUnitKerja);

        Listheader listheaderSisaCuti = new Listheader("Sisa Cuti Tahun Ini");
        listhead.appendChild(listheaderSisaCuti);

        datas = cutiNonAkademisDAO.gets(unitKerja);

        for (CutiNonAkademis obj : datas) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(obj.getNamaPegawai()));
            listitem.appendChild(new Listcell(obj.getUnitKerja()));
            Listcell cell = new Listcell(obj.getSisaCuti() + "");
            cell.setStyle("text-align:right");
            listitem.appendChild(cell);
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
            unitKerja = "";
        }
        try {
            this.loadDataToListbox();
            this.componentEnable();
        } catch (Exception e) {
            Messagebox.show("Data tidak ditemukan", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
        }

    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/cutinonakademis/CutiNonAkademis.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/cutinonakademis/CutiNonAkademis.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
