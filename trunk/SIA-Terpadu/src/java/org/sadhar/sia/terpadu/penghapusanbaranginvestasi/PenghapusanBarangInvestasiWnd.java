/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.penghapusanbaranginvestasi;

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
public class PenghapusanBarangInvestasiWnd extends ClassApplicationModule {

    private Combobox cmbboxTahun;
    private Combobox cmbboxModel;
    private Combobox cmbExportType;
    private Button btnExport;
    private Listbox listboxBarang;
    private Jasperreport report;
    private List<Map> datas;
    private PenghapusanBarangInvestasiDAO penghapusanBarangInvestasiDAO;

    public PenghapusanBarangInvestasiWnd() {
        penghapusanBarangInvestasiDAO = new PenghapusanBarangInvestasiDAOImpl();
    }

    public void onCreate() {
        cmbboxTahun = (Combobox) this.getFellow("cmbboxTahun");
        cmbboxModel = (Combobox) this.getFellow("cmbboxModel");
        cmbExportType = (Combobox) this.getFellow("cmbExportType");
        btnExport = (Button) this.getFellow("btnExport");
        listboxBarang = (Listbox) this.getFellow("listboxBarang");
        report = (Jasperreport) this.getFellow("report");
        this.loadDataToComboboxTahun();
        this.loadDataToComboboxModel();
        cmbboxTahun.setSelectedIndex(0);
        cmbboxModel.setSelectedIndex(0);
        cmbExportType.setSelectedIndex(0);
    }

    private void componentDisable() {
        listboxBarang.setVisible(false);
        btnExport.setDisabled(true);
    }

    private void componentEnable() {
        listboxBarang.setVisible(true);
        btnExport.setDisabled(false);
    }

    private void loadDataToComboboxTahun() {
        Comboitem defaultItem = new Comboitem();
        defaultItem.setValue(null);
        defaultItem.setLabel("--Pilih Tahun--");
        cmbboxTahun.appendChild(defaultItem);
        for (int i = 2005; i <= new DateTime().getYear(); i++) {
            Comboitem item = new Comboitem();
            item.setValue(i);
            item.setLabel(i + "");
            cmbboxTahun.appendChild(item);
        }
    }

    private void loadDataToComboboxModel() {
        Comboitem defaultItem = new Comboitem();
        defaultItem.setValue(null);
        defaultItem.setLabel("--Pilih Model Penghapusan--");
        cmbboxModel.appendChild(defaultItem);
        for (Map data : penghapusanBarangInvestasiDAO.getModelPenghapusanBarang()) {
            Comboitem item = new Comboitem();
            item.setValue(data.get("kodeModelPenghapusanBarang"));
            item.setLabel(data.get("modelPenghapusanBarang").toString());
            cmbboxModel.appendChild(item);
        }
    }

    public void cmbTahunOnSelect() {
        this.componentDisable();
    }

    public void btnShowOnClick() throws InterruptedException {
        if (cmbboxTahun.getSelectedItem().getValue() == null || cmbboxModel.getSelectedItem().getValue() == null) {
            Messagebox.show("Parameter tidak lengkap !", "Error", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            try {
                this.loadDataToListbox();
                this.componentEnable();
            } catch (Exception e) {
                Messagebox.show("Data tidak ditemukan", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
                this.componentDisable();
            }
        }
    }

    private void loadDataToListbox() {
        this.listboxBarang.getItems().clear();
        datas = penghapusanBarangInvestasiDAO.getPenghapusanBarangInvestasi(cmbboxTahun.getSelectedItem().getValue().toString(), cmbboxModel.getSelectedItem().getValue().toString());
        int no = 1;
        for (Map row : datas) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(no + ""));
            listitem.appendChild(new Listcell(row.get("subKelompokBarangInvestasi").toString()));
            listitem.appendChild(new Listcell(row.get("jumlah").toString()));
            listboxBarang.appendChild(listitem);
        }
    }

    public void exportReport() throws Exception {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(datas);
            System.out.println(datas.size());
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/penghapusanbaranginvestasi/PenghapusanBarangInvestasi.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/penghapusanbaranginvestasi/PenghapusanBarangInvestasi.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
