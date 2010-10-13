/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahdosen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author Hendro Steven
 */
public class JumlahDosenWnd extends ClassApplicationModule {

    Jasperreport report;
    Listbox lstData;
    Combobox cmbExportType;
    Button btnExport;
    Intbox txtTotal;
    List<JumlahDosen> dosens = new ArrayList<JumlahDosen>();

    public JumlahDosenWnd() {
    }

    public void onCreate() throws Exception {
        report = (Jasperreport) getFellow("report");
        lstData = (Listbox) getFellow("lstData");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        txtTotal = (Intbox) getFellow("txtTotal");
        cmbExportType.setSelectedIndex(0);
        btnExport.setDisabled(true);
        load();
    }
    int grandTotal = 0;

    private void load() throws Exception {
        try {
            JumlahDosenDAO dao = new JumlahDosenDAOImpl();
            List<JumlahFakultas> jml = dao.getJumlahDosen();
            lstData.getItems().clear();
            for (JumlahFakultas jf : jml) {
                Listitem item = new Listitem();
                item.setValue(jf);
                item.appendChild(new Listcell(jf.getNama().toString()));
                item.appendChild(new Listcell(""));
                item.appendChild(new Listcell(jf.getJumlah() + ""));
                lstData.appendChild(item);

                JumlahDosen jd1 = new JumlahDosen();
                jd1.setNama(jf.getNama());
                jd1.setJumlah("");
                jd1.setTotal(jf.getJumlah() + "");
                dosens.add(jd1);

                for (JumlahProdi jp : jf.getProdis()) {
                    Listitem item2 = new Listitem();
                    item2.appendChild(new Listcell("* " + jp.getNama()));
                    item2.appendChild(new Listcell(jp.getJumlah() + ""));
                    item2.appendChild(new Listcell(""));
                    lstData.appendChild(item2);

                    JumlahDosen jd2 = new JumlahDosen();
                    jd2.setNama("   " + jp.getNama());
                    jd2.setJumlah(jp.getJumlah() + "");
                    jd2.setTotal("");
                    dosens.add(jd2);
                }
                grandTotal += jf.getJumlah();
            }
            txtTotal.setValue(grandTotal);
            btnExport.setDisabled(false);
        } catch (Exception ex) {
            Messagebox.show("Data gagal ditampilkan");
        }
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dosens);
            Map param = new HashMap();
            param.put("grandTotal", Integer.valueOf(grandTotal));
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/jumlahdosen/JumlahDosen.jasper");
                pdfReport.setParameters(param);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/jumlahdosen/JumlahDosen.jasper");
                report.setParameters(param);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
