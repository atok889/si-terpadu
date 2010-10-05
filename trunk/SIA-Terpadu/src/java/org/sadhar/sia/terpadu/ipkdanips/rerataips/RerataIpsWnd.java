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
    private List<Map> reportData = new ArrayList<Map>();
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
        System.out.println(intboxTahunAngkatan.getValue());
        System.out.println(kodeProdi);
        //Export data to dataset
        int colspanIPS = 36;
        String angkatan = "";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (kodeProdi == null && intboxTahunAngkatan.getValue() != null) {
            dataset = new DefaultCategoryDataset();
            colspanIPS = 0;
            for (Map data : rerataIpsDAO.getRerataIps(null, intboxTahunAngkatan.getValue().toString())) {
                Double ips = null;
                if (data.get("ips") == null) {
                    ips = 0d;
                } else {
                    ips = Double.parseDouble(data.get("ips").toString().substring(0, 4));
                }
                dataset.addValue(ips, data.get("tahun").toString() + "-" + data.get("semester").toString(), data.get("Nama_fak").toString());
            }
            //For positive only exception
            if (colspanIPS == 0) {
                colspanIPS = 1;
            }
            colspanIPS = (new DateTime().getYear() - intboxTahunAngkatan.getValue()) * 2 + 1;
            angkatan = intboxTahunAngkatan.getValue() + "";

        } else if (kodeProdi == null && intboxTahunAngkatan.getValue() == null) {
            dataset = new DefaultCategoryDataset();
            for (Map data : rerataIpsDAO.getRerataIps(null, null)) {
                dataset.addValue(Double.parseDouble(data.get("ips").toString().substring(0, 4)),
                        data.get("tahun").toString() + "-" + data.get("semester").toString(), data.get("Nama_fak").toString());
            }
        } else if (kodeProdi != null && intboxTahunAngkatan.getValue() == null) {
            dataset = new DefaultCategoryDataset();
            for (Map data : rerataIpsDAO.getRerataIps(kodeProdi, null)) {
                dataset.addValue(Double.parseDouble(data.get("ips").toString().substring(0, 4)),
                        data.get("tahun").toString() + "-" + data.get("semester").toString(), data.get("Nama_fak").toString());
            }

        } else if (kodeProdi != null && intboxTahunAngkatan.getValue() != null) {
            colspanIPS = 0;
            dataset = new DefaultCategoryDataset();
            for (Map data : rerataIpsDAO.getRerataIps(kodeProdi, intboxTahunAngkatan.getValue().toString())) {
                Double ips = null;
                if (data.get("ips") == null) {
                    ips = 0d;
                } else {
                    ips = Double.parseDouble(data.get("ips").toString().substring(0, 4));
                }
                dataset.addValue(ips, data.get("tahun").toString() + "-" + data.get("semester").toString(), data.get("Nama_fak").toString());
                colspanIPS++;
            }
            //For positive only exception
            if (colspanIPS == 0) {
                colspanIPS = 1;
            }
            angkatan = intboxTahunAngkatan.getValue() + "";
        }

        System.out.println(colspanIPS);

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

        int totalFakultas = 0;
        for (Object column : dataset.getColumnKeys()) {
            totalFakultas++;
        }

        //Untuk mencari total jika pilihan tidak ada pilihan
        double[] ips = new double[dataset.getRowKeys().size()];

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
            listitem.appendChild(new Listcell(angkatan));
            for (Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                Double data = null;
                if (number != null) {
                    data = number.doubleValue();
                } else {
                    data = 0d;
                }

                if (kodeProdi != null && intboxTahunAngkatan.getValue() != null) {
                    listitem.appendChild(new Listcell(data + ""));
                    listboxMahasiswa.appendChild(listitem);
                } else if (kodeProdi != null && intboxTahunAngkatan.getValue() == null) {
                    listitem.appendChild(new Listcell(data + ""));
                    listboxMahasiswa.appendChild(listitem);
                } else {
                    ips[index] += data / totalFakultas;
                    index++;
                }
            }
            no++;
        }

        if (kodeProdi == null && (intboxTahunAngkatan.getValue() == null || intboxTahunAngkatan.getValue() != null)) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell("1"));
            listitem.appendChild(new Listcell("Seluruh Prodi"));
            listitem.appendChild(new Listcell(angkatan));
            for (double i : ips) {
                listitem.appendChild(new Listcell(new DecimalFormat("# 0.00").format(i) + ""));
                listboxMahasiswa.appendChild(listitem);
            }
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
            //Messagebox.show("Data tidak ditemukan", "Konfirmasi", Messagebox.OK, Messagebox.EXCLAMATION);
        }
    }

    public void exportReport() throws Exception {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(this.generateReport());
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
}
