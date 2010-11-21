/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftarkaryawandandosenyangakanpensiun;

import java.util.ArrayList;
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
 * @author kris
 */
public class DaftarKaryawanDanDosenYangAkanPensiunWnd extends ClassApplicationModule {

    private Jasperreport report;
    private Button btnExport;
    private Listbox listboxData;
    private Combobox cmbExportType;
    private Intbox txtboxTahun;
    private List<Map> datas = new ArrayList<Map>();
    private DaftarKaryawanDanDosenYangAkanPensiunDAO daftarKaryawanDanDosenYangAkanPensiunDAO;

    public void onCreate() throws Exception {
        daftarKaryawanDanDosenYangAkanPensiunDAO = new DaftarKaryawanDanDosenYangAkanPensiunDAOImpl();
        listboxData = (Listbox) this.getFellow("listboxData");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        txtboxTahun = (Intbox) getFellow("txtboxTahun");

    }

    private void loadData() {
        listboxData.getItems().clear();
        int no = 1;
        if (!txtboxTahun.getText().isEmpty()) {
            datas = daftarKaryawanDanDosenYangAkanPensiunDAO.getDaftarDosenDanKaryawanYangAkanPensiun(String.valueOf(txtboxTahun.getText()));
        }
        for (Map data : datas) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(no + ""));
            listitem.appendChild(new Listcell(data.get("nama").toString()));
            listitem.appendChild(new Listcell(data.get("unit_kerja").toString()));
            listitem.appendChild(new Listcell(data.get("umur").toString() + " tahun"));
            listitem.appendChild(new Listcell(data.get("pensiun").toString() + " tahun"));
            no++;
            listboxData.appendChild(listitem);
        }
    }

    public void btnShowOnClick() {
        btnExport.setDisabled(false);
        this.loadData();
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/daftarkaryawandandosenyangakanpensiun/DaftarKaryawanDanDosenYangAkanPensiun.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/daftarkaryawandandosenyangakanpensiun/DaftarKaryawanDanDosenYangAkanPensiun.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
