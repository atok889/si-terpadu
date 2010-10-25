/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.ipkdanipssetiapmahasiswa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
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
 * @author kris
 */
public class IpkdanIpsSetiapMahasiswaWnd extends ClassApplicationModule {

    private IpkdanIpsSetiapMahasiswaDAO ipkDanIpsSetiapMahasiswaDAO;
    private Listbox listboxMahasiswa;
    private Combobox cmbboxProdi;
    private Combobox cmbboxAngkatan;
    private Combobox cmbboxSemester;
    private Combobox cmbboxParam1;   
    private String param1;
    private String kodeProdi;
    private String angkatan;
    private String tahunSemester;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private List<Map> datas = new ArrayList<Map>();

    public IpkdanIpsSetiapMahasiswaWnd() {
        ipkDanIpsSetiapMahasiswaDAO = new IpkdanIpsSetiapMahasiswaDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        cmbboxAngkatan = (Combobox) this.getFellow("cmbboxAngkatan");
        cmbboxSemester = (Combobox) this.getFellow("cmbboxSemester");
        cmbboxParam1 = (Combobox) this.getFellow("cmbboxParam1");    
        cmbboxProdi.setReadonly(true);
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataToComboboxProdi();
        this.loadDataToComboboxAngkatan();
        this.loadDataToComboboxSemester();
        this.loadDataToComboParam1();     
    }

    public void loadDataToComboboxProdi() {
        Comboitem item = new Comboitem();
        item.setValue(null);
        item.setLabel("--Pilih Fakultas/Prodi--");
        cmbboxProdi.appendChild(item);

        for (Map data : this.ipkDanIpsSetiapMahasiswaDAO.getProdi()) {
            Comboitem comboitem = new Comboitem();
            comboitem.setValue(data.get("Kd_prg"));
            comboitem.setLabel(data.get("Kd_prg").toString() + " " + data.get("Nama_prg").toString());
            cmbboxProdi.appendChild(comboitem);
        }
        cmbboxProdi.setSelectedIndex(0);
    }

    private void loadDataToComboboxAngkatan() {
        Comboitem item = new Comboitem("--Pilih Angkatan--");
        item.setValue(null);
        cmbboxAngkatan.appendChild(item);
        cmbboxAngkatan.setSelectedItem(item);

        for (int i = 2000; i <= new DateTime().getYear(); i++) {
            Comboitem items = new Comboitem();
            items.setValue(i);
            items.setLabel(i + "");
            cmbboxAngkatan.appendChild(items);
        }
        cmbboxAngkatan.setReadonly(true);
    }

    private void loadDataToComboboxSemester() {
        Comboitem item = new Comboitem("--Pilih Semester--");
        item.setValue(null);
        cmbboxSemester.appendChild(item);
        cmbboxSemester.setSelectedItem(item);

        for (int i = 2000; i <= new DateTime().getYear(); i++) {
            for (int j = 1; j <= 2; j++) {
                Comboitem items = new Comboitem();
                items.setValue(i + "-" + j);
                items.setLabel(i + "-" + j);
                cmbboxSemester.appendChild(items);
            }
        }
        cmbboxSemester.setReadonly(true);
    }

    private void loadDataToComboParam1() {
        Comboitem item = new Comboitem("--Pilih Kolom--");
        item.setValue(null);
        cmbboxParam1.appendChild(item);
        item = new Comboitem("IPK");
        item.setValue("ipk");
        cmbboxParam1.appendChild(item);
        item = new Comboitem("IPS");
        item.setValue("ips");
        cmbboxParam1.appendChild(item);
        item = new Comboitem("Nama");
        item.setValue("nama_mhs");
        cmbboxParam1.appendChild(item);
        cmbboxParam1.setSelectedIndex(0);
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
        listboxMahasiswa.getItems().clear();

        datas = ipkDanIpsSetiapMahasiswaDAO.getIpkDanIpsSetiapMahasiswa(kodeProdi, angkatan, tahunSemester.substring(0, 4),
                tahunSemester.substring(5, 6), param1);

        int no = 1;
        for (Map row : datas) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(no + ""));
            listitem.appendChild(new Listcell(row.get("nama_mhs").toString()));
            listitem.appendChild(new Listcell(row.get("Nama_prg").toString()));
            listitem.appendChild(new Listcell(row.get("fakultas").toString()));
            listitem.appendChild(new Listcell(row.get("angkatan").toString()));
            listitem.appendChild(new Listcell(row.get("ipk").toString().substring(0, 4)));
            listitem.appendChild(new Listcell(row.get("ips").toString().substring(0, 4)));
            listboxMahasiswa.appendChild(listitem);
            no++;
        }      
    }

    public void cmbDataProdiOnSelect() {
        this.componentDisable();
    }

    public void btnShowOnClick() throws InterruptedException {
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
        angkatan = String.valueOf(cmbboxAngkatan.getSelectedItem().getValue());
        tahunSemester = String.valueOf(cmbboxSemester.getSelectedItem().getValue());
        param1 = (String) cmbboxParam1.getSelectedItem().getValue();
    

        if (kodeProdi != null && angkatan != null && tahunSemester != null) {
            try {
                this.componentEnable();
                loadDataToListbox();
            } catch (Exception e) {
                this.componentDisable();
                e.printStackTrace();
                Messagebox.show("Data tidak ditemukan", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
            }
        } else {
            Messagebox.show("Parameter tidak lengkap", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public void exportReport() throws Exception {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/ipkdanips/ipkdanipssetiapmahasiswa/IpkDanIpsSetiapMahasiswa.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/ipkdanips/ipkdanipssetiapmahasiswa/IpkDanIpsSetiapMahasiswa.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
