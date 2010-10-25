/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.sebaranipkmahasiswa;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class SebaranIpkMahasiswaWnd extends ClassApplicationModule {

    private SebaranIpkMahasiswaDAO sebaranIpkLulusanDAO;
    private Listbox listboxMahasiswa;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private List<Map> dataReport = new ArrayList<Map>();

    public SebaranIpkMahasiswaWnd() {
        sebaranIpkLulusanDAO = new SebaranIpkMahasiswaDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataToListbox();
    }

    private void loadDataToListbox() {

        List<Map> datas = this.generateData();
        int no = 1;
        for (Map row : datas) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(no + ""));
            listitem.appendChild(new Listcell(row.get("prodi").toString()));
            listitem.appendChild(new Listcell(row.get("ipkE").toString()));
            listitem.appendChild(new Listcell(new DecimalFormat("# 0.00").format(Double.parseDouble(row.get("prosentaseE").toString())) + "%"));
            listitem.appendChild(new Listcell(row.get("ipkD1").toString()));
            listitem.appendChild(new Listcell(new DecimalFormat("# 0.00").format(Double.parseDouble(row.get("prosentaseD1").toString())) + "%"));
            listitem.appendChild(new Listcell(row.get("ipkD2").toString()));
            listitem.appendChild(new Listcell(new DecimalFormat("# 0.00").format(Double.parseDouble(row.get("prosentaseD2").toString())) + "%"));
            listitem.appendChild(new Listcell(row.get("ipkC1").toString()));
            listitem.appendChild(new Listcell(new DecimalFormat("# 0.00").format(Double.parseDouble(row.get("prosentaseC1").toString())) + "%"));
            listitem.appendChild(new Listcell(row.get("ipkC2").toString()));
            listitem.appendChild(new Listcell(new DecimalFormat("# 0.00").format(Double.parseDouble(row.get("prosentaseC2").toString())) + "%"));
            listitem.appendChild(new Listcell(row.get("ipkB1").toString()));
            listitem.appendChild(new Listcell(new DecimalFormat("# 0.00").format(Double.parseDouble(row.get("prosentaseB1").toString())) + "%"));
            listitem.appendChild(new Listcell(row.get("ipkB2").toString()));
            listitem.appendChild(new Listcell(new DecimalFormat("# 0.00").format(Double.parseDouble(row.get("prosentaseB2").toString())) + "%"));
            listitem.appendChild(new Listcell(row.get("ipkA1").toString()));
            listitem.appendChild(new Listcell(new DecimalFormat("# 0.00 ").format(Double.parseDouble(row.get("prosentaseA1").toString())) + "%"));
            listitem.appendChild(new Listcell(row.get("ipkA2").toString()));
            listitem.appendChild(new Listcell(new DecimalFormat("# 0.00 ").format(Double.parseDouble(row.get("prosentaseA2").toString())) + "%"));
            listboxMahasiswa.appendChild(listitem);
            no++;
        }
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataReport);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/ipkdanips/sebaranipkmahasiswa/SebaranIpkMahasiswa.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/ipkdanips/sebaranipkmahasiswa/SebaranIpkMahasiswa.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<Map> generateData() {
        List<Map> datas = sebaranIpkLulusanDAO.getSebaranIpkLulusan();
        List<Map> prodis = sebaranIpkLulusanDAO.getProdi();
        List<Map> rerata = new ArrayList<Map>();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map prodi : prodis) {
            int countE = 0;
            int countD1 = 0;
            int countD2 = 0;
            int countC1 = 0;
            int countC2 = 0;
            int countB1 = 0;
            int countB2 = 0;
            int countA1 = 0;
            int countA2 = 0;
            int totalMahasiswa = 0;
            Map map = new HashMap();
            for (Map data : datas) {

                if (prodi.get("Kd_prg").toString().equalsIgnoreCase(data.get("Kd_prg").toString())) {
                    double ipk = Double.parseDouble(data.get("ipk").toString());
                    if (ipk >= 0 && ipk <= 2) {
                        countE += 1;
                    } else if (ipk > 2.01 && ipk <= 2.25) {
                        countD1 += 1;
                    } else if (ipk > 2.26 && ipk <= 2.5) {
                        countD2 += 1;
                    } else if (ipk > 2.51 && ipk <= 2.75) {
                        countC1 += 1;
                    } else if (ipk > 2.76 && ipk <= 3) {
                        countC2 += 1;
                    } else if (ipk > 3.01 && ipk <= 3.25) {
                        countB1 += 1;
                    } else if (ipk > 3.26 && ipk <= 3.5) {
                        countB2 += 1;
                    } else if (ipk > 3.51 && ipk <= 3.75) {
                        countA1 += 1;
                    } else if (ipk > 3.76 && ipk <= 4) {
                        countA2 += 1;
                    }
                    map.put("ipkA1", countA1);
                    map.put("ipkA2", countA2);
                    map.put("ipkB1", countB1);
                    map.put("ipkB2", countB2);
                    map.put("ipkC1", countC1);
                    map.put("ipkC2", countC2);
                    map.put("ipkD1", countD1);
                    map.put("ipkD2", countD2);
                    map.put("ipkE", countE);
                    totalMahasiswa++;
                } else {
                    map.put("ipkA1", countA1);
                    map.put("ipkA2", countA2);
                    map.put("ipkB1", countB1);
                    map.put("ipkB2", countB2);
                    map.put("ipkC1", countC1);
                    map.put("ipkC2", countC2);
                    map.put("ipkD1", countD1);
                    map.put("ipkD2", countD2);
                    map.put("ipkE", countE);
                }
            }
            map.put("total", totalMahasiswa);
            map.put("prodi", prodi.get("Nama_prg").toString());
            //Hitung prosentase
            int ipkA1 = Integer.parseInt(map.get("ipkA1").toString());
            int ipkB1 = Integer.parseInt(map.get("ipkB1").toString());
            int ipkC1 = Integer.parseInt(map.get("ipkC1").toString());
            int ipkD1 = Integer.parseInt(map.get("ipkD1").toString());
            int ipkA2 = Integer.parseInt(map.get("ipkA2").toString());
            int ipkB2 = Integer.parseInt(map.get("ipkB2").toString());
            int ipkC2 = Integer.parseInt(map.get("ipkC2").toString());
            int ipkD2 = Integer.parseInt(map.get("ipkD2").toString());
            int ipkE = Integer.parseInt(map.get("ipkE").toString());
            double prosentaseA1 = 0;
            double prosentaseB1 = 0;
            double prosentaseC1 = 0;
            double prosentaseD1 = 0;
            double prosentaseA2 = 0;
            double prosentaseB2 = 0;
            double prosentaseC2 = 0;
            double prosentaseD2 = 0;
            double prosentaseE = 0;
            if (totalMahasiswa != 0) {
                prosentaseA1 = ((double) ipkA1 / totalMahasiswa) * 100;
                prosentaseB1 = ((double) ipkB1 / totalMahasiswa) * 100;
                prosentaseC1 = ((double) ipkC1 / totalMahasiswa) * 100;
                prosentaseD1 = ((double) ipkD1 / totalMahasiswa) * 100;
                prosentaseA2 = ((double) ipkA2 / totalMahasiswa) * 100;
                prosentaseB2 = ((double) ipkB2 / totalMahasiswa) * 100;
                prosentaseC2 = ((double) ipkC2 / totalMahasiswa) * 100;
                prosentaseD2 = ((double) ipkD2 / totalMahasiswa) * 100;
                prosentaseE = ((double) ipkE / totalMahasiswa) * 100;
            }

            map.put("prosentaseA1", prosentaseA1);
            map.put("prosentaseB1", prosentaseB1);
            map.put("prosentaseC1", prosentaseC1);
            map.put("prosentaseD1", prosentaseD1);
            map.put("prosentaseA2", prosentaseA2);
            map.put("prosentaseB2", prosentaseB2);
            map.put("prosentaseC2", prosentaseC2);
            map.put("prosentaseD2", prosentaseD2);
            map.put("prosentaseE", prosentaseE);
            rerata.add(map);
        }
        dataReport.clear();
        dataReport.addAll(rerata);
        return rerata;
    }
}
