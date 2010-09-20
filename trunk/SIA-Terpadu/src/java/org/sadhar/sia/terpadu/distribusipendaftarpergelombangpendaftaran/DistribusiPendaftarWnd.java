/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.distribusipendaftarpergelombangpendaftaran;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Hendro Steven
 */
public class DistribusiPendaftarWnd extends ClassApplicationModule {

    Textbox txtTahunMulai;
    Textbox txtTahunSelesai;
    Jasperreport report;
    Combobox cmbExportType;
    Listbox lstData;
    Button btnExport;
    List<DistribusiPendaftar> datas = new ArrayList<DistribusiPendaftar>();

    public DistribusiPendaftarWnd() {
    }

    public void onCreate() throws Exception {
        txtTahunMulai = (Textbox) getFellow("txtTahunMulai");
        txtTahunSelesai = (Textbox) getFellow("txtTahunSelesai");
        report = (Jasperreport) getFellow("report");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        lstData = (Listbox) getFellow("lstData");
        btnExport = (Button) getFellow("btnExport");
    }

    public void viewReport() throws Exception {
        try {
            if (!txtTahunMulai.getValue().isEmpty() && !txtTahunSelesai.getValue().isEmpty()) {
                int thnMulai = Integer.valueOf(txtTahunMulai.getValue());
                int thnSelesai = Integer.valueOf(txtTahunSelesai.getValue());
                if (thnMulai > thnSelesai) {
                    Messagebox.show("Tahun Pendaftaran tidak valid");
                    txtTahunMulai.setFocus(true);
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
                btnExport.setDisabled(false);
            } else {
                Messagebox.show("Silahkan input tahun pendaftaran");
                txtTahunMulai.setFocus(true);
                return;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show(ex.getMessage());
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
