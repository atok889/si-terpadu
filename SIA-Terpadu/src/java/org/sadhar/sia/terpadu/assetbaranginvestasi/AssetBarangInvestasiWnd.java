/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.assetbaranginvestasi;

/**
 *
 * @author jasoet
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.JFreeChart;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

/**
 *
 * @author Jasoet
 */
public class AssetBarangInvestasiWnd
        extends ClassApplicationModule {

    private Jasperreport report;
    private Image chartImg;
    private Combobox cmbExportType;
    private Button btnExport;
    private JFreeChart chart = null;
    private Listbox listB;
    private AssetBarangInvestasiDAO assetDAO;
    private List<AssetBarangInvestasi> data;

    public AssetBarangInvestasiWnd() {
        assetDAO = new AssetBarangInvestasiDAOImpl();
    }

    public void onCreate() throws Exception {

        report = (Jasperreport) getFellow("report");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        listB = (Listbox) getFellow("listb");
        cmbExportType.setSelectedIndex(0);
        viewReport();
        btnExport.setDisabled(false);
    }

    public void viewReport() throws Exception {
        try {

            listB.getItems().clear();
            data = assetDAO.gets();
            int i = 1;
            for (AssetBarangInvestasi abi : data) {
                Listitem item = new Listitem();
                Listcell cell = new Listcell();
                cell.setLabel("" + i);
                item.appendChild(cell);



                cell = new Listcell();
                cell.setLabel(abi.getNama());
                item.appendChild(cell);

                cell = new Listcell();
                cell.setStyle("text-align:right");
                cell.setLabel(abi.getJumlah() + "");
                item.appendChild(cell);
                listB.appendChild(item);
                i++;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show(ex.getMessage());
        }
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/assetbaranginvestasi/AssetBarangInvestasi.jasper");
                Map parameters = new HashMap();
//                parameters.put("chart", chart.createBufferedImage(500, 300));
                pdfReport.setParameters(parameters);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/assetbaranginvestasi/AssetBarangInvestasi.jasper");
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
