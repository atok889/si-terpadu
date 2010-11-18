/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.matrikborang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudi;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudiDAO;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudiDAOImpl;
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
 * @author jasoet
 */
public class PerbandinganPraPascaMonevWnd extends ClassApplicationModule {

    private Combobox cmbProgdi;
    private Jasperreport report;
    private Combobox cmbExportType;
    private Button btnExport;
    private Listbox listb;
    private List<MatrikBorang> datas;

    public PerbandinganPraPascaMonevWnd() {
    }

    public void onCreate() throws Exception {
        listb = (Listbox) getFellow("listb");
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        report = (Jasperreport) getFellow("report");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        loadProgdi();
        cmbProgdi.setSelectedIndex(0);
        cmbExportType.setSelectedIndex(0);
    }

    private void loadProgdi() throws Exception {
        try {
            UKProgramStudiDAO dao = new UKProgramStudiDAOImpl();
            List<UKProgramStudi> progdis = dao.gets();
            cmbProgdi.getItems().clear();
            Comboitem item = new Comboitem();
//            item.setValue(null);
//            item.setLabel("--Pilih Prodi--");
//            cmbProgdi.appendChild(item);
            for (UKProgramStudi ps : progdis) {
                Comboitem items = new Comboitem();
                items.setValue(ps);
                items.setLabel(ps.getKodeUnitKerja() + " " + ps.getNama());
                cmbProgdi.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public void viewReport() throws Exception {
        try {
            listb.getItems().clear();
            MatrikBorangDAO dao = new MatrikBorangDAOImpl();
            UKProgramStudi prodiSelected = (UKProgramStudi) cmbProgdi.getSelectedItem().getValue();
            datas = dao.getByKodeUnit(prodiSelected.getKodeUnitKerja());
            int i = 1;
            for (MatrikBorang o : datas) {
                Listitem li = new Listitem();
                Listcell cell = new Listcell("" + (i++));
                li.appendChild(cell);
                cell = new Listcell(o.getIsianMonevinRKA());
                li.appendChild(cell);
                cell = new Listcell(o.getSkorPraMonev() + "");
                li.appendChild(cell);
                cell = new Listcell(o.getSkorPascaMonev() + "");
                li.appendChild(cell);
                listb.appendChild(li);

            }
            btnExport.setDisabled(false);


        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show(ex.getMessage());
        }
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/perbandinganprapascamonev/PerbandinganPraPascaMonev.jasper");
                Map parameters = new HashMap();
//                parameters.put("chart", chart.createBufferedImage(500, 300));
                pdfReport.setParameters(parameters);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/perbandinganprapascamonev/PerbandinganPraPascaMonev.jasper");
                Map parameters = new HashMap();
//                parameters.put("chart", chart.createBufferedImage(500, 300, BufferedImage.TRANSLUCENT, null));
                report.setParameters(parameters);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
