/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pinjamanunit;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class PinjamanUnitWnd extends ClassApplicationModule {

    private PinjamanUnitDAO pinjamanUnitDAO;
    private Listbox listboxData;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private List<Map> dataReport = new ArrayList<Map>();

    public PinjamanUnitWnd() {
        pinjamanUnitDAO = new PinjamanUnitDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxData = (Listbox) this.getFellow("listboxData");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataToListbox();
    }

    private void loadDataToListbox() {
        Listhead listhead = new Listhead();

        Listheader listheaderNo = new Listheader();
        listheaderNo.setLabel("No");
        listheaderNo.setWidth("50px");
        listheaderNo.setAlign("right");
        listhead.appendChild(listheaderNo);

        Listheader listheaderNama = new Listheader();
        listheaderNama.setLabel("Prodi/Unit Kerja");
        listhead.appendChild(listheaderNama);

        Listheader listheaderJumlah = new Listheader();
        listheaderJumlah.setLabel("Jumlah Pinjaman Unit");
        listheaderJumlah.setAlign("right");
        listhead.appendChild(listheaderJumlah);

        dataReport = pinjamanUnitDAO.getPinjamanUnit();
        int no = 1;
        for (Map row : dataReport) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(no + ""));
            listitem.appendChild(new Listcell(row.get("nama").toString()));
            listitem.appendChild(new Listcell(setFormat().format(Double.valueOf(row.get("jumlah").toString()))));
            listboxData.appendChild(listitem);
            no++;
        }
        listboxData.appendChild(listhead);
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataReport);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/pinjamanunit/PinjamanUnit.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/pinjamanunit/PinjamanUnit.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    private DecimalFormat setFormat() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        DecimalFormatSymbols formatSymbols = df.getDecimalFormatSymbols();
        formatSymbols.setGroupingSeparator('.');
        formatSymbols.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(formatSymbols);
        return df;
    }
}
