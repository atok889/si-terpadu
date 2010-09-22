/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftarmatakuliah;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.jumlahmahasiswa.JumlahMahasiswaDAOImpl;
import org.sadhar.sia.terpadu.jumlahmahasiswa.JumlahMahasiwaDAO;
import org.sadhar.sia.terpadu.jumlahmahasiswa.ProgramStudi;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

/**
 *
 * @author Hendro Steven
 */
public class DaftarMatakuliahWnd extends ClassApplicationModule {

    Combobox cmbProdi;
    Jasperreport report;
    Listbox lstData;
    Combobox cmbExportType;
    Button btnExport;
    List<DaftarMatakuliah> datas = new ArrayList<DaftarMatakuliah>();

    public DaftarMatakuliahWnd() {
    }

    public void onCreate() throws Exception {
        cmbProdi = (Combobox) getFellow("cmbProdi");
        report = (Jasperreport) getFellow("report");
        lstData = (Listbox) getFellow("lstData");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        loadProgdi();
        cmbProdi.setSelectedIndex(0);
        cmbExportType.setSelectedIndex(0);
    }

    private void loadProgdi() throws Exception {
        try {
            JumlahMahasiwaDAO dao = new JumlahMahasiswaDAOImpl();
            List<ProgramStudi> progdis = dao.getProgramStudi();
            cmbProdi.getItems().clear();
            for (ProgramStudi ps : progdis) {
                Comboitem items = new Comboitem();
                items.setValue(ps);
                items.setLabel(ps.getKode() + " " + ps.getNama());
                cmbProdi.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public void viewReport() throws Exception {
        try {
            DaftarMatakuliahDAO dao = new DaftarMatakuliahDAOImpl();
            datas = dao.getBeanCollection((ProgramStudi) cmbProdi.getSelectedItem().getValue());
            lstData.getItems().clear();
            for (DaftarMatakuliah dm : datas) {
                Listitem item = new Listitem();
                item.setValue(dm);
                item.appendChild(new Listcell(dm.getNomor() + "."));
                item.appendChild(new Listcell(dm.getKode()));
                item.appendChild(new Listcell(dm.getNama()));
                item.appendChild(new Listcell(dm.getSks() + ""));
                item.appendChild(new Listcell(dm.getDosen()));
                lstData.appendChild(item);
            }
            if (datas.size() > 0) {
                btnExport.setDisabled(false);
            } else {
                btnExport.setDisabled(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show("Menampilkan daftar matakuliah gagal");
        }
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/daftarmatakuliah/DaftarMatakuliah.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/daftarmatakuliah/DaftarMatakuliah.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public void cmbProdiOnChange()throws Exception{
        lstData.getItems().clear();
        btnExport.setDisabled(true);
        datas.clear();
    }
}

