/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningipkrendah;

import java.text.DecimalFormat;
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
import org.zkoss.zul.Comboitem;
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
public class WarningIPKRendahWnd extends ClassApplicationModule {

    private WarningIPKRendahDAO warningIPKRendahDAO;
    private Combobox cmbboxProdi;
    private String kodeProdi;
    private Listbox listboxMahasiswa;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private List<Map> dataReport = new ArrayList<Map>();

    public WarningIPKRendahWnd() {
        warningIPKRendahDAO = new WarningIPKRendahDAOImpl();
    }

    public void onCreate() throws Exception {
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataProdiToCombo();
    }

    private void loadDataProdiToCombo() {
        Comboitem item = new Comboitem("--SEMUA FAKULTAS--");
        cmbboxProdi.appendChild(item);
        cmbboxProdi.setSelectedItem(item);

        for (Map map : warningIPKRendahDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Kd_prg").toString()+" "+map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
    }

    private void loadDataToListbox() {
        this.listboxMahasiswa.getChildren().clear();
        Listhead listhead = new Listhead();

        Listheader listheaderNo = new Listheader();
        listheaderNo.setLabel("No");
        listheaderNo.setWidth("50px");
        listheaderNo.setAlign("right");
        listhead.appendChild(listheaderNo);

        Listheader listheaderNama = new Listheader();
        listheaderNama.setLabel("Nama");
        listhead.appendChild(listheaderNama);

        Listheader listheaderProdi = new Listheader();
        listheaderProdi.setLabel("Prodi");
        listhead.appendChild(listheaderProdi);

        Listheader listheaderFakultas = new Listheader();
        listheaderFakultas.setLabel("Fakultas");
        listhead.appendChild(listheaderFakultas);

        Listheader listheaderAngkatan = new Listheader();
        listheaderAngkatan.setLabel("Tahun Angkatan");
        listheaderAngkatan.setWidth("120px");
        listheaderAngkatan.setAlign("center");
        listhead.appendChild(listheaderAngkatan);

        Listheader listheaderIpk = new Listheader();
        listheaderIpk.setLabel("IPK");
        listheaderIpk.setWidth("80px");
        listheaderIpk.setAlign("right");
        listhead.appendChild(listheaderIpk);
        List<Map> datas = null;

        if (kodeProdi == null) {
            datas = warningIPKRendahDAO.getWarningIPKRendah();
        } else {
            datas = warningIPKRendahDAO.getWarningIPKRendah(kodeProdi);
        }
        DecimalFormat df = new DecimalFormat("#,0.00");
        int no = 1;
        for (Map data : datas) {
            if (Double.parseDouble(data.get("ipk").toString()) < 2d) {
                Listitem listitem = new Listitem();
                listitem.appendChild(new Listcell(no + ""));
                listitem.appendChild(new Listcell(data.get("nama_mhs").toString()));
                listitem.appendChild(new Listcell(data.get("Nama_prg").toString()));
                listitem.appendChild(new Listcell(data.get("Nama_fak").toString()));
                listitem.appendChild(new Listcell(data.get("angkatan").toString()));
                listitem.appendChild(new Listcell(df.format(data.get("ipk"))));
                this.listboxMahasiswa.appendChild(listitem);
                dataReport.add(data);
                no++;
            }
        }
        this.btnExport.setDisabled(false);
        this.listboxMahasiswa.appendChild(listhead);
        this.listboxMahasiswa.setVisible(true);
    }

    public void cmbDataProdiOnSelect() {
        if (cmbboxProdi.getSelectedItem().getValue() == null) {
            kodeProdi = null;
        } else {
            kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
        }
    }

    public void btnShowOnClick() {
        if (kodeProdi == null) {
            this.loadDataToListbox();
        } else {
            this.loadDataToListbox();
        }
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataReport);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/warning/warningipkrendah/WarningIPKRendah.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/warning/warningipkrendah/WarningIPKRendah.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
