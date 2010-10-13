/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.dosensedangmenempuhstudi;

import org.sadhar.sia.terpadu.jumlahmahasiswado.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.JFreeChart;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.jenjangstudi.JenjangStudi;
import org.sadhar.sia.terpadu.jenjangstudi.JenjangStudiDAO;
import org.sadhar.sia.terpadu.jenjangstudi.JenjangStudiDAOImpl;
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
 * @author Deny Prasetyo
 */
public class DosenSedangMenempuhStudiWnd extends ClassApplicationModule {

    Combobox cmbProgdi;
    Combobox cmbJenjangStudi;
    Jasperreport report;
//    Image chartImg;
    Combobox cmbExportType;
    Button btnExport;
    Listbox listb;
    JFreeChart chart = null;
    List<DosenSedangMenempuhStudi> datas;

    public DosenSedangMenempuhStudiWnd() {
    }

    public void onCreate() throws Exception {
        listb = (Listbox) getFellow("listb");
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        report = (Jasperreport) getFellow("report");
        cmbJenjangStudi = (Combobox) getFellow("cmbJenjangStudi");
//        chartImg = (Image) getFellow("chartImg");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        loadProgdi();
        loadJenjangStudi();
        cmbProgdi.setSelectedIndex(0);
        cmbJenjangStudi.setSelectedIndex(0);
        cmbExportType.setSelectedIndex(0);
    }

    private void loadProgdi() throws Exception {
        try {
            UKProgramStudiDAO dao = new UKProgramStudiDAOImpl();
            List<UKProgramStudi> progdis = dao.gets();
            cmbProgdi.getItems().clear();
            Comboitem item = new Comboitem();
            item.setValue(null);
            item.setLabel("--Pilih Prodi--");
            cmbProgdi.appendChild(item);
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

    private void loadJenjangStudi() throws Exception {
        try {
            JenjangStudiDAO dao = new JenjangStudiDAOImpl();
            List<JenjangStudi> jenjangStudi = dao.getUnfinised();
            cmbJenjangStudi.getItems().clear();
            Comboitem item = new Comboitem();
            item.setValue(null);
            item.setLabel("Semua");
            cmbJenjangStudi.appendChild(item);
            for (JenjangStudi o : jenjangStudi) {
                Comboitem items = new Comboitem();
                items.setValue(o);
                items.setLabel(o.getNama());
                cmbJenjangStudi.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public void viewReport() throws Exception {
        try {
            listb.getItems().clear();
            DosenSedangMenempuhStudiDAO dao = new DosenSedangMenempuhStudiDAOImpl();
            datas = dao.getData((UKProgramStudi) cmbProgdi.getSelectedItem().getValue(), (JenjangStudi) cmbJenjangStudi.getSelectedItem().getValue());

            for (DosenSedangMenempuhStudi o : datas) {
                Listitem li = new Listitem();
                Listcell cell = new Listcell(o.getNama());
                li.appendChild(cell);
                cell = new Listcell(o.getProdi());
                li.appendChild(cell);
                cell = new Listcell(o.getJenjangStudi());
                li.appendChild(cell);
                cell = new Listcell(o.getTempat());
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
                pdfReport.setSrc("reports/dosensedangmenempuhstudi/dosensedangmenempuhstudi.jasper");
                Map parameters = new HashMap();
//                parameters.put("chart", chart.createBufferedImage(500, 300));
                pdfReport.setParameters(parameters);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/dosensedangmenempuhstudi/dosensedangmenempuhstudi.jasper");
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
