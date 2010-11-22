/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.daftarwisudaipktertinggi;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class DaftarWisudaIpkTertinggiWnd extends ClassApplicationModule {

    private DaftarWisudaIpkTertinggiDAO daftarWisudaIpkTertinggiDAO;
    private Listbox listboxMahasiswa;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private Combobox comboTanggalWisuda;
    private List<Map> datas = new ArrayList<Map>();

    public DaftarWisudaIpkTertinggiWnd() {
        daftarWisudaIpkTertinggiDAO = new DaftarWisudaIpkTertinggiDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        report = (Jasperreport) getFellow("report");
        comboTanggalWisuda = (Combobox) getFellow("dateboxWisuda");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataToCombo();
    }

    private void loadDataToCombo() {
        Comboitem comboitem = new Comboitem("--Pilih Tanggal Wisuda--");
        comboitem.setValue(null);
        comboTanggalWisuda.appendChild(comboitem);

        for (Map map : daftarWisudaIpkTertinggiDAO.getTanggalWisuda()) {
            Comboitem item = new Comboitem();
            item.setValue(map.get("tanggal").toString());
            item.setLabel(map.get("tanggal").toString());
            comboTanggalWisuda.appendChild(item);
        }
        comboTanggalWisuda.setSelectedIndex(0);
    }

    private void componentDisable() {
        listboxMahasiswa.setVisible(false);
        btnExport.setDisabled(true);
    }

    private void componentEnable() {
        listboxMahasiswa.setVisible(true);
        btnExport.setDisabled(false);
    }

    private void loadDataToListbox() {
        this.listboxMahasiswa.getChildren().clear();
        Listhead listhead = new Listhead();

        Listheader listheaderNo = new Listheader();
        listheaderNo.setLabel("No");
        listheaderNo.setWidth("50px");
        listheaderNo.setAlign("right");
        listhead.appendChild(listheaderNo);

        Listheader listheaderProdi = new Listheader();
        listheaderProdi.setLabel("Prodi");
        listhead.appendChild(listheaderProdi);

        Listheader listheaderNama = new Listheader();
        listheaderNama.setLabel("Nama");
        listhead.appendChild(listheaderNama);

        Listheader listheaderAngkatan = new Listheader();
        listheaderAngkatan.setLabel("IPK");
        listheaderAngkatan.setWidth("100px");
        listheaderAngkatan.setAlign("right");
        listhead.appendChild(listheaderAngkatan);

        datas = this.generateData(comboTanggalWisuda.getText());
        System.out.println(comboTanggalWisuda.getText());

        int no = 1;
        for (Map row : datas) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(no + ""));
            listitem.appendChild(new Listcell(row.get("prodi").toString()));
            listitem.appendChild(new Listcell(row.get("nama").toString()));
            listitem.appendChild(new Listcell(new DecimalFormat("# 0.00").format(Double.valueOf(row.get("ipk").toString()))));
            listboxMahasiswa.appendChild(listitem);
            no++;
        }
        listboxMahasiswa.appendChild(listhead);
    }

    public void dateboxOnChange() {
        this.componentDisable();
    }

    public void btnShowOnClick() {
        this.componentEnable();
        this.loadDataToListbox();
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/ipkdanips/daftarwisudaipktertinggi/DaftarWisudaIpkTertinggi.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/ipkdanips/daftarwisudaipktertinggi/DaftarWisudaIpkTertinggi.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    private List<Map> generateData(String date) {
        List<Map> results = daftarWisudaIpkTertinggiDAO.getDaftarWisudaIpkTertinggi(date);
        System.out.println(date);
        List<Map> prodis = daftarWisudaIpkTertinggiDAO.getProdi();
        List<Map> allData = new ArrayList<Map>();

        for (Map prodi : prodis) {
            Map data = new HashMap();
            double ipk = 0;
            String nama = null;
            for (Map map : results) {
                if (map.get("Nama_prg").toString().equalsIgnoreCase(prodi.get("Nama_prg").toString())) {
                    if (ipk < Double.valueOf(map.get("ipk").toString())) {
                        ipk = Double.valueOf(map.get("ipk").toString());
                        nama = map.get("nama_mhs").toString();
                    }
                }
            }
            if (nama != null) {
                data.put("prodi", prodi.get("Nama_prg").toString());
                data.put("nama", nama);
                data.put("ipk", ipk);
                allData.add(data);
            }
        }
        return allData;
    }
}
