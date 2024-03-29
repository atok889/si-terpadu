/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistiklamastudi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
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
public class StatistikLamaStudiWnd extends ClassApplicationModule {

    private StatistikLamaStudiDAO statistikLamaStudiDAO;
    private Combobox cmbboxProdi;
    private Combobox cmbboxSemester;
    private String kodeProdi;
    private Label labelPilihan;
    private Listbox listboxMahasiswa;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private String tahunAkademik = "2000";
    private String semester = "2";
    private int smstr = 0;
    private List<Map> dataReport;

    public StatistikLamaStudiWnd() {
        statistikLamaStudiDAO = new StatistikLamaStudiDAOImpl();

    }

    public void onCreate() throws Exception {
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        labelPilihan = (Label) this.getFellow("labelPilihan");
        cmbboxSemester = (Combobox) this.getFellow("cmbboxSemester");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataProdiToCombo();
        this.loadDataSemesterToCombo();
    }

    private void loadDataProdiToCombo() {
        Comboitem item = new Comboitem("--Pilih Fakultas--");
        item.setValue(null);
        cmbboxProdi.appendChild(item);
        cmbboxProdi.setSelectedItem(item);

        for (Map map : statistikLamaStudiDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Kd_prg").toString() + " " + map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
        cmbboxProdi.setReadonly(true);
    }

    private void loadDataSemesterToCombo() {
        Comboitem item = new Comboitem("--Pilih Semester--");
        cmbboxSemester.appendChild(item);
        cmbboxSemester.setSelectedItem(item);

        for (int i = 1; i <= 16; i++) {
            Comboitem items = new Comboitem();
            items.setValue(i);
            items.setLabel("Semester " + i);
            cmbboxSemester.appendChild(items);
        }
        cmbboxSemester.setReadonly(true);
    }

    private void componentDisable() {
        listboxMahasiswa.setVisible(false);
        btnExport.setDisabled(true);
    }

    private void componentEnable() {
        listboxMahasiswa.setVisible(true);
        btnExport.setDisabled(false);
    }

    private void loadDataToListbox() throws InterruptedException {
        listboxMahasiswa.getChildren().clear();
        dataReport = new ArrayList<Map>();
        int colspanSemester = 16;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (labelPilihan.getValue().equalsIgnoreCase("prodi")) {
            if (kodeProdi != null) {
                List<Map> results = statistikLamaStudiDAO.getStatistikLamaStudi(kodeProdi, null);
                for (int i = 1; i <= 16; i++) {
                    Map map = new HashMap();
                    for (Map result : results) {
                        if (Integer.valueOf(result.get("semester").toString()) == i) {
                            map.put("jumlah", Integer.valueOf(result.get("jumlah").toString()));
                            map.put("semester", i);
                        } else {
                            map.put("semester", i);
                        }
                        map.put("prodi", result.get("prodi"));
                    }
                    int lama = 0;
                    if (map.get("jumlah") != null) {
                        lama = Integer.valueOf(map.get("jumlah").toString());
                    } else {
                        map.put("jumlah", lama);
                    }
                    dataset.addValue(lama, "Smt " + map.get("semester"), map.get("prodi").toString());
                    dataReport.add(map);
                }
            } else {
                Messagebox.show("Masukan pilihan", "Konfirmasi", Messagebox.OK, Messagebox.EXCLAMATION);
                this.componentDisable();
            }
        } else {
            colspanSemester = 1;
            List<Map> results = statistikLamaStudiDAO.getStatistikLamaStudi(null, String.valueOf(smstr));
            for (Map prodi : statistikLamaStudiDAO.getProdi()) {
                Map map = new HashMap();
                for (Map result : results) {
                    if (result.get("prodi").toString().equals(prodi.get("Nama_prg").toString())) {
                        map.put("jumlah", Integer.valueOf(result.get("jumlah").toString()));
                        map.put("semester", smstr);
                    } else {
                        map.put("semester", smstr);
                    }
                    map.put("prodi", prodi.get("Nama_prg"));
                }
                int lama = 0;
                if (map.get("jumlah") != null) {
                    lama = Integer.valueOf(map.get("jumlah").toString());
                } else {
                    map.put("jumlah", lama);
                }
                dataset.addValue(lama, "Smt " + map.get("semester"), map.get("prodi").toString());
                dataReport.add(map);
            }
        }

        Listhead listhead = new Listhead();
        Auxhead auxhead = new Auxhead();

        Listheader listheaderProdi = new Listheader();
        listheaderProdi.setWidth("250px");
        listhead.appendChild(listheaderProdi);
        Auxheader auxheaderProdi = new Auxheader();
        auxheaderProdi.setLabel("Prodi");
        auxheaderProdi.setAlign("center");
        auxhead.appendChild(auxheaderProdi);

        Auxheader auxheaderSemester = new Auxheader();
        auxheaderSemester.setLabel("Jumlah Mahasiswa");
        auxheaderSemester.setColspan(colspanSemester);
        auxheaderSemester.setAlign("center");
        auxhead.appendChild(auxheaderSemester);

        for (Object row : dataset.getRowKeys()) {
            Listheader listheader = new Listheader();
            listheader.setWidth("70px");
            listheader.setAlign("right");
            listheader.setLabel(row.toString());
            listhead.appendChild(listheader);
        }

        for (Object column : dataset.getColumnKeys()) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(column.toString()));
            for (Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                listitem.appendChild(new Listcell(ClassAntiNull.AntiNullIntString(number) + ""));
                listboxMahasiswa.appendChild(listitem);
            }
        }
        listboxMahasiswa.appendChild(auxhead);
        listboxMahasiswa.appendChild(listhead);
    }

    public void radioProdiOnCheck() {
        cmbboxProdi.setDisabled(false);
        cmbboxSemester.setDisabled(true);
    }

    public void radioSemesterCheck() {
        cmbboxProdi.setDisabled(true);
        cmbboxSemester.setDisabled(false);
    }

    public void cmbDataProdiOnSelect() {
        this.componentDisable();
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
    }

    public void cmbDataSemesterOnSelect() {
        this.componentDisable();
        if (cmbboxSemester.getSelectedItem().getValue() == null) {
            smstr = 0;
        } else {
            smstr = (Integer) cmbboxSemester.getSelectedItem().getValue();
        }
    }

    public void btnShowOnClick() throws InterruptedException {
        try {
            this.componentEnable();
            loadDataToListbox();
        } catch (Exception e) {
            this.componentDisable();
            Messagebox.show("Data tidak ditemukan", "Konfirmasi", Messagebox.OK, Messagebox.INFORMATION);
            e.printStackTrace();
        }
    }

    public void exportReport() throws Exception {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dataReport);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/statistiklamastudi/StatistikLamaStudi.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/statistiklamastudi/StatistikLamaStudi.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    private List<Map> generateReport(String kodeProdi) {
        List<Map> datas = new ArrayList<Map>();
//        if (labelPilihan.getValue().equalsIgnoreCase("prodi")) {
//            if (kodeProdi == null || kodeProdi.equalsIgnoreCase("all")) {
//                List<StatistikLamaStudi> statistikLamaStudis = statistikLamaStudiDAO.getStatistikLamaStudi();
//                for (StatistikLamaStudi statistikLamaStudi : statistikLamaStudis) {
//
//                    for (int i = 1; i <= 16; i++) {
//                        Map map = new HashMap();
//                        map.put("prodi", statistikLamaStudi.getProdi());
//                        map.put("semester", i);
//                        map.put("jumlah", statistikLamaStudi.getSemesterValue(i));
//                        datas.add(map);
//                    }
//                }
//            } else {
//                StatistikLamaStudi statistikLamaStudi = statistikLamaStudiDAO.getStatistikLamaStudi(kodeProdi);
//                for (int i = 1; i <= 16; i++) {
//                    Map map = new HashMap();
//                    map.put("prodi", statistikLamaStudi.getProdi());
//                    map.put("semester", i);
//                    map.put("jumlah", statistikLamaStudi.getSemesterValue(i));
//                    datas.add(map);
//                }
//            }
//        } else {
//            List<StatistikLamaStudi> statistikLamaStudis = statistikLamaStudiDAO.getStatistikLamaStudi();
//            for (StatistikLamaStudi statistikLamaStudi : statistikLamaStudis) {
//                Map map = new HashMap();
//                map.put("prodi", statistikLamaStudi.getProdi());
//                map.put("semester", smstr);
//                map.put("jumlah", statistikLamaStudi.getSemesterValue(smstr));
//                datas.add(map);
//
//            }
//        }
        return datas;
    }
}
