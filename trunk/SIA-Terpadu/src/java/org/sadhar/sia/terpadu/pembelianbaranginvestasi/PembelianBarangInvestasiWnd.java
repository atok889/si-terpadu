/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pembelianbaranginvestasi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author Hendro Steven
 */
public class PembelianBarangInvestasiWnd extends ClassApplicationModule {

    Listbox lstData;
    Doublebox txtTotal;
    Button btnExport;
    Combobox cmbExportType;
    Jasperreport report;
    List<PembelianBarangInvestasi> datas = new ArrayList<PembelianBarangInvestasi>();

    public void onCreate() throws Exception {
        lstData = (Listbox) getFellow("lstData");
        txtTotal = (Doublebox) getFellow("txtTotal");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        report = (Jasperreport) getFellow("report");
        cmbExportType.setSelectedIndex(0);
        btnExport.setDisabled(true);
        load();
    }

    double total = 0;
    private void load() throws Exception {
        NumberFormat formatter = new DecimalFormat("#,##0.##");
        try{
            PembelianBarangInvestasiDAO dao = new PembelianBarangInvestasiDAOImpl();
            datas = dao.loadData();
            lstData.getItems().clear();
            int no = 1;
            for(PembelianBarangInvestasi pbi : datas){
                Listitem item = new Listitem();
                item.setValue(pbi);
                item.appendChild(new Listcell(String.valueOf(no++)));
                item.appendChild(new Listcell(pbi.getNama()));
                item.appendChild(new Listcell(formatter.format(pbi.getNilai())));
                item.appendChild(new Listcell(formatter.format(pbi.getJumlah())));
                item.appendChild(new Listcell(formatter.format(pbi.getSubtotal())));
                lstData.appendChild(item);
                total += pbi.getSubtotal();
            }
            txtTotal.setValue(total);
            if(datas.size()>0){
                btnExport.setDisabled(false);
            }
        }catch(Exception ex){
            Messagebox.show("Gagal menampilkan data Pembelian Barang Investasi");
        }
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            Map param = new HashMap();
            param.put("grandTotal", total);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/pembelianbaranginvestasi/PembelianBarangInvestasi.jasper");
                pdfReport.setParameters(param);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/pembelianbaranginvestasi/PembelianBarangInvestasi.jasper");
                report.setParameters(param);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
