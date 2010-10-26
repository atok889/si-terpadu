/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftargedungtanahdankendaraan;

import java.util.ArrayList;
import java.util.HashMap;
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
public class DaftarGedungTanahDanKendaraanWnd extends ClassApplicationModule {

    private Listbox listboxData;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private List<Map> dataReport = new ArrayList<Map>();
    private DaftarGedungTanahDanKendaraanDAO daftarGedungTanahDanKendaraanDAO;

    public DaftarGedungTanahDanKendaraanWnd() {
        daftarGedungTanahDanKendaraanDAO = new DaftarGedungTanahDanKendaraanDAOImpl();
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
        listheaderNo.setWidth("30px");
        listheaderNo.setAlign("right");
        listhead.appendChild(listheaderNo);

        Listheader listheaderJenis = new Listheader();
        listheaderJenis.setLabel("Jenis");
        listheaderJenis.setWidth("100px");
        listhead.appendChild(listheaderJenis);

        Listheader listheaderNama = new Listheader();
        listheaderNama.setLabel("Nama");
        listhead.appendChild(listheaderNama);

        Listheader listheaderKeterangan = new Listheader();
        listheaderKeterangan.setLabel("Keterangan");
        listheaderKeterangan.setAlign("right");
        listheaderKeterangan.setWidth("100px");
        listhead.appendChild(listheaderKeterangan);

        dataReport = generateData();

        for (Map row : dataReport) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(row.get("no").toString()));
            listitem.appendChild(new Listcell(row.get("jenis").toString()));
            listitem.appendChild(new Listcell(row.get("nama").toString()));
            listitem.appendChild(new Listcell(row.get("keterangan").toString()));
            listboxData.appendChild(listitem);
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
                pdfReport.setSrc("reports/daftargendungtanahdankendaraan/DaftarGedungTanahDanKendaraan.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/daftargendungtanahdankendaraan/DaftarGedungTanahDanKendaraan.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    private List<Map> generateData() {
        List<Map> datas = new ArrayList<Map>();
        String currentJenis = "";
        int no = 1;
        for (Map data : daftarGedungTanahDanKendaraanDAO.getDaftarGedungTanahDanKendaraan()) {
            Map map = new HashMap();

            if (currentJenis.equals(data.get("jenis"))) {
                map.put("jenis", "");
                map.put("no", "");
            } else {
                currentJenis = data.get("jenis").toString();
                map.put("no", String.valueOf(no++));
                map.put("jenis", currentJenis);
            }           
            map.put("nama", data.get("nama"));
            map.put("keterangan", data.get("keterangan"));
            datas.add(map);
        }
        return datas;
    }
}
