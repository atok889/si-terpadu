/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jadwaldosen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.DateTime;
import org.sadhar.sia.terpadu.dosen.Dosen;
import org.sadhar.sia.terpadu.dosen.DosenDAO;
import org.sadhar.sia.terpadu.dosen.DosenDAOImpl;
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
public class JadwalDosenWnd extends ClassApplicationModule {

    Combobox cmbNamaDosen;
    Combobox cmbProgdi;
    //Textbox txtTahun;
    Combobox cmbTahun;
    Combobox cmbSemester;
    Jasperreport report;
    Listbox lstData;
    Button btnExport;
    Combobox cmbExportType;
    List<JadwalDosen> datas;

    public JadwalDosenWnd() {
    }

    public void onCreate() throws Exception {
        cmbNamaDosen = (Combobox) getFellow("cmbNamaDosen");
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        //txtTahun = (Textbox) getFellow("txtTahun");
        cmbTahun = (Combobox)getFellow("cmbTahun");
        cmbSemester = (Combobox) getFellow("cmbSemester");
        report = (Jasperreport) getFellow("report");
        lstData = (Listbox) getFellow("lstData");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        btnExport.setDisabled(true);
        loadProgdi();
        loadSemester();
        //loadDosen();
        loadTahun();
        cmbTahun.setSelectedIndex(0);
        cmbProgdi.setSelectedIndex(0);
        cmbSemester.setSelectedIndex(0);
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

    private void loadDosen() throws Exception {
        try {
            DosenDAO dao = new DosenDAOImpl();
            List<Dosen> dosens = dao.gets();
            cmbNamaDosen.getItems().clear();
            for (Dosen d : dosens) {
                Comboitem items = new Comboitem();
                items.setValue(d);
                items.setLabel(d.getNama());
                cmbNamaDosen.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public void loadDosenOnChanging(String nama) throws Exception {
        try {
            DosenDAO dao = new DosenDAOImpl();
            List<Dosen> dosens = dao.getsByName(nama);
            cmbNamaDosen.getItems().clear();
            for (Dosen d : dosens) {
                Comboitem items = new Comboitem();
                items.setValue(d);
                items.setLabel(d.getNama());
                cmbNamaDosen.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
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

    public void viewReport() throws Exception {
        try {
            JadwalDosenDAO dao = new JadwalDosenDAOImpl();
            if (cmbNamaDosen.getValue() != null && cmbProgdi.getValue() != null && !cmbTahun.getSelectedItem().getValue().toString().isEmpty() && cmbSemester.getValue() != null) {
                datas = dao.getJadwalDosen((Dosen) cmbNamaDosen.getSelectedItem().getValue(),
                        (ProgramStudi) cmbProgdi.getSelectedItem().getValue(), cmbTahun.getSelectedItem().getValue().toString(),
                        Integer.valueOf(cmbSemester.getSelectedItem().getValue().toString()));
                lstData.getItems().clear();
                for (JadwalDosen jd : datas) {
                    Listitem item = new Listitem();
                    item.setValue(jd);
                    item.appendChild(new Listcell(jd.getJam()));
                    item.appendChild(new Listcell(jd.getSenin()));
                    item.appendChild(new Listcell(jd.getSelasa()));
                    item.appendChild(new Listcell(jd.getRabu()));
                    item.appendChild(new Listcell(jd.getKamis()));
                    item.appendChild(new Listcell(jd.getJumat()));
                    item.appendChild(new Listcell(jd.getSabtu()));
                    lstData.appendChild(item);
                }
                btnExport.setDisabled(false);
            } else {
                Messagebox.show("Silahkan Input Parameter dengan benar");
                btnExport.setDisabled(true);
            }
        } catch (Exception ex) {
            Messagebox.show("Telah terjadi kesalahan..");
        }
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            Map param = new HashMap();
            param.put("dosen", ((Dosen) cmbNamaDosen.getSelectedItem().getValue()).getNama());
            param.put("prodi", ((ProgramStudi) cmbProgdi.getSelectedItem().getValue()).getNama());
            param.put("tahun", cmbTahun.getSelectedItem().getValue().toString());
            param.put("semester", cmbSemester.getSelectedItem().getValue() + "");
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/jadwaldosen/JadwalDosen.jasper");
                pdfReport.setParameters(param);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/jadwaldosen/JadwalDosen.jasper");
                report.setParameters(param);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
