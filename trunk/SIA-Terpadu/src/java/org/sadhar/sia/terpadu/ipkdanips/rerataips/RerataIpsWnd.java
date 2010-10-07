/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataips;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class RerataIpsWnd extends ClassApplicationModule {

    private RerataIpsDAO rerataIpsDAO;
    private Listbox listboxMahasiswa;
    private Combobox cmbboxProdi;
    private Intbox intboxTahunAngkatan;
    private String kodeProdi;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private List<Map> dataReport = new ArrayList<Map>();
    // private String tahunAngkatan = "1996";

    public RerataIpsWnd() {
        rerataIpsDAO = new RerataIpsDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        cmbboxProdi.setReadonly(true);
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        intboxTahunAngkatan = (Intbox) this.getFellow("intboxTahunAngkatan");
        cmbExportType.setSelectedIndex(0);
        this.loadDataToComboboxProdi();
    }

    public void loadDataToComboboxProdi() {
        Comboitem item = new Comboitem();
        item.setValue(null);
        item.setLabel("--Pilih Fakultas/Prodi--");
        cmbboxProdi.appendChild(item);

        for (Map data : this.rerataIpsDAO.getProdi()) {
            Comboitem comboitem = new Comboitem();
            comboitem.setValue(data.get("Kd_prg"));
            comboitem.setLabel(data.get("Kd_prg").toString() + " " + data.get("Nama_prg").toString());
            cmbboxProdi.appendChild(comboitem);
        }
        cmbboxProdi.setSelectedIndex(0);
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
        listboxMahasiswa.getChildren().clear();

        int colspanIPS = 40;
        String angkatan = "";
        if (intboxTahunAngkatan.getValue() != null) {
            angkatan = String.valueOf(intboxTahunAngkatan.getValue());
            colspanIPS = (new DateTime().getYear() - Integer.valueOf(angkatan)) * 2 + 2;

        }

        Listhead listhead = new Listhead();
        Auxhead auxhead = new Auxhead();

        Listheader listheaderNo = new Listheader();
        listheaderNo.setWidth("40px");
        listhead.appendChild(listheaderNo);
        Auxheader auxheaderNo = new Auxheader();
        auxheaderNo.setLabel("No");
        //auxheaderNo.setRowspan(2);
        auxheaderNo.setAlign("center");
        auxhead.appendChild(auxheaderNo);

        Listheader listheaderFakultas = new Listheader();
        listheaderFakultas.setWidth("250px");
        listhead.appendChild(listheaderFakultas);
        Auxheader auxheaderFakultas = new Auxheader();
        auxheaderFakultas.setLabel("Fakultas");
        auxheaderFakultas.setAlign("center");
        auxhead.appendChild(auxheaderFakultas);

        Listheader listheaderAngkatan = new Listheader();
        listheaderAngkatan.setWidth("120px");
        listheaderAngkatan.setAlign("right");
        listhead.appendChild(listheaderAngkatan);
        Auxheader auxheaderAngkatan = new Auxheader();
        auxheaderAngkatan.setLabel("Tahun Angkatan");
        auxheaderAngkatan.setWidth("120px");
        auxheaderAngkatan.setAlign("center");
        auxhead.appendChild(auxheaderAngkatan);

        Auxheader auxheaderIPS = new Auxheader();
        auxheaderIPS.setLabel("IPS");
        auxheaderIPS.setColspan(colspanIPS);
        auxheaderIPS.setAlign("center");
        auxhead.appendChild(auxheaderIPS);
        listboxMahasiswa.appendChild(auxhead);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset = (DefaultCategoryDataset) this.generateData(kodeProdi, String.valueOf(intboxTahunAngkatan.getValue()));

        int no = 1;
        for (Object row : dataset.getRowKeys()) {
            colspanIPS++;
            Listheader listheader = new Listheader();
            listheader.setWidth("70px");
            listheader.setAlign("right");
            listheader.setLabel(row.toString());
            listhead.appendChild(listheader);
        }

        for (Object column : dataset.getColumnKeys()) {
            int index = 0;
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(no + ""));
            listitem.appendChild(new Listcell(column.toString()));
            listitem.appendChild(new Listcell(angkatan + ""));
            for (Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                Double data = null;
                if (number != null) {
                    data = number.doubleValue();
                } else {
                    data = 0d;
                }
                listitem.appendChild(new Listcell(new DecimalFormat("# 0.00").format(data) + ""));
                listboxMahasiswa.appendChild(listitem);
            }
            no++;
        }

        listboxMahasiswa.appendChild(listhead);

    }

    public void cmbDataProdiOnSelect() {
        this.componentDisable();
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
    }

    public void btnShowOnClick() throws InterruptedException {
        try {
            this.componentEnable();
            loadDataToListbox();
        } catch (Exception e) {
            this.componentDisable();
            e.printStackTrace();
            //Messagebox.show("Data tidak ditemukan", "Konfirmasi", Messagebox.OK, Messagebox.EXCLAMATION);
        }
    }

    public void exportReport() throws Exception {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dataReport);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/ipkdanips/rerataips/RerataIps.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/ipkdanips/rerataips/RerataIps.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<Map> generateReport() {
        List<Map> datas = new ArrayList<Map>();

        if (kodeProdi != null && intboxTahunAngkatan.getValue() != null) {
            // System.out.println("MAsuk 1" + tahun);
            datas = new ArrayList<Map>();
            for (Map ips : rerataIpsDAO.getRerataIps(kodeProdi, intboxTahunAngkatan.getValue().toString())) {
                Map map = new HashMap();
                map.put("tahun", ips.get("tahun").toString() + "-" + ips.get("semester").toString());
                map.put("fakultas", ips.get("Nama_fak").toString());
                map.put("angkatan", ips.get("angkatan").toString());
                map.put("ips", ips.get("ips").toString().substring(0, 4));
                datas.add(map);
            }
        } else if (kodeProdi != null && intboxTahunAngkatan.getValue() == null) {
            datas = new ArrayList<Map>();
            System.out.println("MAsuk 2");
            for (Map ips : rerataIpsDAO.getRerataIps(kodeProdi, null)) {
                Map map = new HashMap();
                map.put("tahun", ips.get("tahun").toString() + "-" + ips.get("semester").toString());
                map.put("fakultas", ips.get("Nama_fak").toString());
                map.put("angkatan", "");
                map.put("ips", ips.get("ips").toString().substring(0, 4));
                datas.add(map);
            }
        }
        return datas;
    }

    private CategoryDataset generateData(String kodeProdi, String angkatan) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int currentYear = new DateTime().getYear() - 5;
        int maxYear = new DateTime().getYear();
        dataReport.clear();

        if (angkatan.equalsIgnoreCase("null")) {
            angkatan = null;
        }
        List<Map> result = new ArrayList<Map>();

        if (angkatan == null) {
            result = rerataIpsDAO.getRerataIps(kodeProdi, angkatan);
        } else {
            currentYear = Integer.valueOf(angkatan);
            result = rerataIpsDAO.getRerataIps(kodeProdi, angkatan);
        }

        //Tahun
        for (int i = currentYear; i <= maxYear; i++) {
            //Semester
            for (int j = 1; j <= 2; j++) {
                int count = 0;
                Map map = new HashMap();
                Double ips = null;
                Double ipsTotal = 0d;
                //Data
                for (Map data : result) {
                    if (data.get("ips") == null || data.get("ips") == "") {
                        ips = 0d;
                    } else {
                        ips = Double.parseDouble(data.get("ips").toString().substring(0, 4));
                    }
                    if (Integer.valueOf(data.get("tahun").toString()) == i && Integer.valueOf(data.get("semester").toString()) == j) {
                        ipsTotal += ips;
                        count++;
                    }

                    if (ipsTotal > 0) {
                        map.put("ips", new DecimalFormat("# 0.00").format(ipsTotal / count));
                    } else {
                        map.put("ips", ipsTotal.toString());
                    }

                    if (kodeProdi == null) {
                        map.put("angkatan", "");
                        map.put("fakultas", "Seluruh Prodi");
                    } else {
                        if (angkatan == null) {
                            angkatan = "";
                        }
                        map.put("angkatan", angkatan);
                        map.put("fakultas", data.get("Nama_fak"));
                    }
                    map.put("tahun", i);

                }
                map.put("semester", j);
                dataset.addValue(Double.valueOf(map.get("ips").toString()), map.get("tahun") + "-" + map.get("semester").toString(), map.get("fakultas").toString());
                map.put("tahun", map.get("tahun") + "-" + map.get("semester").toString());
                dataReport.add(map);
            }
        }
        return dataset;
    }
}
