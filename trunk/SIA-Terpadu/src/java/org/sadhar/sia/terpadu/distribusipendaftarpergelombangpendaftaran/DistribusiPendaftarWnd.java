/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.distribusipendaftarpergelombangpendaftaran;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
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
public class DistribusiPendaftarWnd extends ClassApplicationModule {

    //Textbox txtTahunMulai;
    Combobox cmbTahunMulai;
    Combobox cmbTahunSelesai;
    // Textbox txtTahunSelesai;
    Jasperreport report;
    Combobox cmbExportType;
    Listbox lstData;
    Button btnExport;
    List<DistribusiPendaftar> datas = new ArrayList<DistribusiPendaftar>();

    public DistribusiPendaftarWnd() {
    }

    public void onCreate() throws Exception {
        //txtTahunMulai = (Textbox) getFellow("txtTahunMulai");
        //txtTahunSelesai = (Textbox) getFellow("txtTahunSelesai");
        cmbTahunMulai = (Combobox) getFellow("cmbTahunMulai");
        cmbTahunSelesai = (Combobox) getFellow("cmbTahunSelesai");
        report = (Jasperreport) getFellow("report");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        lstData = (Listbox) getFellow("lstData");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType.setSelectedIndex(0);
        loadTahun();
        cmbTahunMulai.setSelectedIndex(0);
        cmbTahunSelesai.setSelectedIndex(0);
    }

    private void loadTahun() throws Exception {
        int currentYear = new DateTime().getYear();
        cmbTahunMulai.getItems().clear();
        cmbTahunSelesai.getItems().clear();
        Comboitem item1 = new Comboitem();
        item1.setValue("");
        item1.setLabel("-- Pilih Tahun --");
        cmbTahunMulai.appendChild(item1);

        Comboitem item2 = new Comboitem();
        item2.setValue("");
        item2.setLabel("-- Pilih Tahun --");
        cmbTahunSelesai.appendChild(item2);
        for (int x = 2000; x <= currentYear; x++) {
            Comboitem itm1 = new Comboitem();
            itm1.setValue(x);
            itm1.setLabel(x + "");
            cmbTahunMulai.appendChild(itm1);
            Comboitem itm2 = new Comboitem();
            itm2.setValue(x);
            itm2.setLabel(x + "");
            cmbTahunSelesai.appendChild(itm2);
        }
    }

    public void viewReport() throws Exception {
        try {
            if (!cmbTahunMulai.getSelectedItem().getValue().toString().isEmpty() && !cmbTahunSelesai.getSelectedItem().getValue().toString().isEmpty()) {
                int thnMulai = Integer.valueOf(cmbTahunMulai.getSelectedItem().getValue().toString());
                int thnSelesai = Integer.valueOf(cmbTahunSelesai.getSelectedItem().getValue().toString());
                if (thnMulai > thnSelesai) {
                    Messagebox.show("Tahun Pendaftaran tidak valid");
                    cmbTahunMulai.setFocus(true);
                    return;
                }
                List<String> tahuns = new ArrayList<String>();
                for (int x = thnMulai; x <= thnSelesai; x++) {
                    tahuns.add(String.valueOf(x));
                }
                DistribusiPendaftarDAO dao = new DistribusiPendaftarDAOImpl();
                datas = dao.getBeanCollection(tahuns);
                lstData.getItems().clear();
                for (DistribusiPendaftar dp : datas) {
                    Listitem item = new Listitem();
                    item.setValue(dp);
                    item.appendChild(new Listcell(dp.getTahun()));
                    item.appendChild(new Listcell(dp.getGelombang1() + ""));
                    item.appendChild(new Listcell(dp.getGelombang2() + ""));
                    item.appendChild(new Listcell(dp.getGelombang3() + ""));
                    item.appendChild(new Listcell(dp.getJalurKerjaSama() + ""));
                    item.appendChild(new Listcell(dp.getJalurPrestasi() + ""));
                    lstData.appendChild(item);
                }
                if (datas.size() > 0) {
                    btnExport.setDisabled(false);
                } else {
                    btnExport.setDisabled(true);
                }
            } else {
                Messagebox.show("Silahkan pilih tahun pendaftaran");
                cmbTahunMulai.setFocus(true);
                return;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show("Tahun pendaftaran tidak terdapat dalam database");
        }
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/distribusipendaftarpergelombangpendaftaran/DistribusiPendaftar.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/distribusipendaftarpergelombangpendaftaran/DistribusiPendaftar.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
