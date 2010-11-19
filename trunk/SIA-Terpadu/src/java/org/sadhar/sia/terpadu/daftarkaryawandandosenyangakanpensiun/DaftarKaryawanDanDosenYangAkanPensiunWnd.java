/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftarkaryawandandosenyangakanpensiun;

import java.util.ArrayList;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
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
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void btnShowOnClick() {
        this.loadData();
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList());
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/warning/warningadministratif/WarningAdministratif.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/warning/warningadministratif/WarningAdministratif.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
