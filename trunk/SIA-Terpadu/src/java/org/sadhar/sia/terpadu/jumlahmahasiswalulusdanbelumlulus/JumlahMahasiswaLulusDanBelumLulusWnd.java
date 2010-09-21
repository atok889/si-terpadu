/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswalulusdanbelumlulus;

import java.text.DecimalFormat;
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
import org.zkoss.zul.Label;
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
public class JumlahMahasiswaLulusDanBelumLulusWnd extends ClassApplicationModule {

    private JumlahMahasiswaLulusDanBelumLulusDAO jumlahMahasiswaLulusDanBelumLulusDAO;
    private Combobox cmbboxProdi;
    private String kodeProdi;
    private Listbox listboxMahasiswa;
    private String tahunAkademik = "2000";
    private String semester = "2";
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;

    public JumlahMahasiswaLulusDanBelumLulusWnd() {
        jumlahMahasiswaLulusDanBelumLulusDAO = new JumlahMahasiswaLulusDanBelumLulusDAOImpl();
    }

    public void onCreate() throws Exception {
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        this.loadDataProdiToCombo();

    }

    private void loadDataProdiToCombo() {
        for (Map map : jumlahMahasiswaLulusDanBelumLulusDAO.getProdi()) {
            Comboitem item = new Comboitem();
            item.setValue(map.get("Kd_prg").toString());
            item.setLabel(map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(item);
        }
    }

    private void loadDataToListbox() {
        this.listboxMahasiswa.getChildren().clear();
        Listhead listhead = new Listhead();
        Listheader listheaderAngkatan = new Listheader();
        listheaderAngkatan.setLabel("Tahun Angkatan");
        listheaderAngkatan.setAlign("center");
        listheaderAngkatan.setWidth("130px");
        listhead.appendChild(listheaderAngkatan);
        Listheader listheaderJumlahMahasiswa = new Listheader();
        listheaderJumlahMahasiswa.setLabel("Jumlah Mahasiswa");
        listheaderJumlahMahasiswa.setAlign("center");
        listheaderJumlahMahasiswa.setWidth("130px");
        listhead.appendChild(listheaderJumlahMahasiswa);

        Listheader listheaderMhsLulus = new Listheader();
        listheaderMhsLulus.setLabel("Jumlah Lulus");
        listheaderMhsLulus.setAlign("center");
        listheaderMhsLulus.setWidth("130px");
        listhead.appendChild(listheaderMhsLulus);
        Listheader listheaderPersenMhsLulus = new Listheader();

        listhead.appendChild(listheaderPersenMhsLulus);

        Listheader listheaderMhsBelumLulus = new Listheader();
        listheaderMhsBelumLulus.setLabel("Jumlah Belum Lulus");
        listheaderMhsBelumLulus.setAlign("center");
        listheaderMhsBelumLulus.setWidth("130px");
        listhead.appendChild(listheaderMhsBelumLulus);
        Listheader listheaderPersenMhsBelumLulus = new Listheader();

        listhead.appendChild(listheaderPersenMhsBelumLulus);

        List<Map> mahasiswa = jumlahMahasiswaLulusDanBelumLulusDAO.getJumlahMahasiswaLulusDanBelumLulus(kodeProdi);
        DecimalFormat df = new DecimalFormat("# 0.00");
        for (Map data : mahasiswa) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(data.get("angkatan").toString()));
            listitem.appendChild(new Listcell(data.get("jumlah_total").toString()));
            listitem.appendChild(new Listcell(data.get("jumlah_lulus").toString()));
            if (!data.get("prosentase_lulus").toString().equals("0.0000")) {
                listitem.appendChild(new Listcell(df.format(Double.parseDouble(data.get("prosentase_lulus").toString())) + "%"));
            } else {
                listitem.appendChild(new Listcell("0" + "%"));
            }
            listitem.appendChild(new Listcell(data.get("jumlah_belum_lulus").toString()));
            if (!data.get("prosentase_belum_lulus").toString().equals("0.0000")) {
                listitem.appendChild(new Listcell(df.format(Double.parseDouble(data.get("prosentase_belum_lulus").toString())) + "%"));
            } else {
                listitem.appendChild(new Listcell("0" + "%"));
            }
            listboxMahasiswa.appendChild(listitem);
        }
        listboxMahasiswa.appendChild(listhead);
    }

    public void cmbDataProdiOnSelect() {
        if (cmbboxProdi.getSelectedItem().getValue() == null) {
            kodeProdi = null;
        } else {
            kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
        }
    }

    public void btnShowOnClick() throws InterruptedException {
        if (kodeProdi == null) {
        } else {
            try {
                this.listboxMahasiswa.setVisible(true);
                this.loadDataToListbox();
                this.btnExport.setDisabled(false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Messagebox.show("Data tidak ditemukan !", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
                this.listboxMahasiswa.setVisible(false);
            }
        }
    }

    public void exportReport() throws Exception {
        List<Map> datas = jumlahMahasiswaLulusDanBelumLulusDAO.getJumlahMahasiswaLulusDanBelumLulus(kodeProdi);
//        for(Map map: datas){
//            System.out.println(map.get("angkatan"));
//        }
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdfgg")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/jumlahmahasiswalulusdanbelumlulus/JumlahMahasiswaLulusDanBelumLulus.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/jumlahmahasiswalulusdanbelumlulus/JumlahMahasiswaLulusDanBelumLulus.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
