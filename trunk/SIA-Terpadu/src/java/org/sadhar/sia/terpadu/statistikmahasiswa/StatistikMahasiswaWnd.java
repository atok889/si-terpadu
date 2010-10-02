/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistikmahasiswa1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class StatistikMahasiswaWnd extends ClassApplicationModule {

    private StatistikMahasiswaDAO statistikMahasiswaDAO;
    private Combobox cmbboxProdi;
    private Combobox cmbExportType;
    private Jasperreport report;
    private String kodeProdi;
    private Button btnExport;
    private Listbox listboxMahasiswa;
    private Listbox listboxDetailMahasiswa;
    private String tahunAkademik = "2000";
    private String semester = "2";
    List<Map> datas = new ArrayList<Map>();
    Integer tahunMulai = 1980;

    public StatistikMahasiswaWnd() {
        statistikMahasiswaDAO = new StatistikMahasiswaDAOImpl();
    }

    public void onCreate() throws Exception {
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        cmbboxProdi.setReadonly(true);
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setReadonly(true);
        cmbExportType.setSelectedIndex(0);
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        listboxDetailMahasiswa = (Listbox) this.getFellow("listboxDetailMahasiswa");
        report = (Jasperreport) getFellow("report");
        this.loadDataProdiToCombo();

    }

    private void loadDataProdiToCombo() {
        Comboitem item = new Comboitem();
        item.setValue(null);
        item.setLabel("--TOTAL--");
        cmbboxProdi.appendChild(item);

        for (Map map : statistikMahasiswaDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Kd_prg").toString() + " " + map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
        cmbboxProdi.setSelectedIndex(0);
        cmbboxProdi.setSelectedItem(item);
    }

    private void componentDisable() {
        listboxMahasiswa.setVisible(false);
        listboxDetailMahasiswa.setVisible(false);
        btnExport.setDisabled(true);
    }

    private void componentEnable() {
        listboxMahasiswa.setVisible(true);
        listboxDetailMahasiswa.getChildren().clear();
        listboxDetailMahasiswa.setVisible(true);
        btnExport.setDisabled(false);
    }

    private void loadDataToListbox() {
        listboxMahasiswa.getChildren().clear();
        Listhead listhead = new Listhead();
        Listheader listheaderProdi = new Listheader("", "", "200px");
        listhead.appendChild(listheaderProdi);
        Listheader listheaderTotal = new Listheader("Total", "", "60px");
        listheaderTotal.setAlign("right");
        listhead.appendChild(listheaderTotal);

        Listheader listheader = new Listheader();

        final DefaultCategoryDataset dataset = (DefaultCategoryDataset) this.generateDataMahasiswa(kodeProdi);

        for (Object header : dataset.getRowKeys()) {
            listheader = new Listheader();
            listheader.setAlign("right");
            listheader.setLabel(header.toString());
            listheader.setWidth("60px");
            listhead.appendChild(listheader);
        }

        Listitem listitemTotal = new Listitem("Total Angkatan");
        listboxMahasiswa.appendChild(listitemTotal);

        int[] totalPerAngkatan = new int[dataset.getRowKeys().size()];
        int totalKeseluruhan = 0;
        int indexTotalSemua = 0;


        for (final Object column : dataset.getColumnKeys()) {
            int totalPerProdi = 0;
            int index = 0;
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(column.toString()));

            Listcell listcellTotal = new Listcell();
            Toolbarbutton anchorDetailTotal = new Toolbarbutton();
            listcellTotal.appendChild(anchorDetailTotal);
            listitem.appendChild(listcellTotal);


            for (final Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                Toolbarbutton anchorDetail = new Toolbarbutton();
                listcellTotal = new Listcell();
                anchorDetail.setLabel(number.intValue() + "");
                listcellTotal.appendChild(anchorDetail);
                listitem.appendChild(listcellTotal);

                //Anchor for total 
                anchorDetail.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener() {

                    public void onEvent(Event arg0) throws Exception {
                        List<Map> detail = new ArrayList<Map>();
                        String currentStatus = column.toString();
                        System.out.println(currentStatus);

                        if (currentStatus.equalsIgnoreCase("Mahasiswa Registrasi")) {
                            detail = statistikMahasiswaDAO.getDetailMahasiswaRegistrasi(kodeProdi, row.toString());
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Tidak Registrasi")) {
                            detail = statistikMahasiswaDAO.getDetailMahasiswaTidakRegistrasi(kodeProdi, row.toString());
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Cuti")) {
                            detail = statistikMahasiswaDAO.getDetailMahasiswaCuti(kodeProdi, row.toString());
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa DO")) {
                            detail = statistikMahasiswaDAO.getDetailMahasiswaDO(kodeProdi, row.toString());
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Lulus")) {
                            detail = statistikMahasiswaDAO.getDetailMahasiswaLulus(kodeProdi, row.toString());
                        }

                        int no = 1;
                        listboxDetailMahasiswa.getChildren().clear();
                        Listhead listhead = new Listhead();
                        Listheader listheaderNo = new Listheader("NO");
                        listheaderNo.setAlign("right");
                        listheaderNo.setWidth("50px");
                        listhead.appendChild(listheaderNo);
                        listhead.appendChild(new Listheader("Nama", "", "400px"));
                        listhead.appendChild(new Listheader("Angkatan", "", "150px"));
                        listhead.appendChild(new Listheader("Program Studi", "", "300px"));
                        listboxDetailMahasiswa.appendChild(listhead);
                        for (Map m : detail) {
                            Listitem item = new Listitem();
                            item.appendChild(new Listcell(no + ""));
                            item.appendChild(new Listcell(m.get("nama_mhs").toString()));
                            item.appendChild(new Listcell(m.get("angkatan").toString()));
                            item.appendChild(new Listcell(m.get("Nama_prg").toString()));
                            listboxDetailMahasiswa.appendChild(item);
                            no++;
                        }
                    }
                });

                //Hitung total
                totalPerProdi += number.intValue();
                totalPerAngkatan[index] += number.intValue();
                totalKeseluruhan += number.intValue();
                anchorDetailTotal.setLabel(totalPerProdi + "");
                index++;
                listboxMahasiswa.appendChild(listitem);

            }
            //Anchor for total by status
            anchorDetailTotal.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener() {

                public void onEvent(Event arg0) throws Exception {
                    List<Map> detail = new ArrayList<Map>();
                    String currentStatus = column.toString();

                    if (currentStatus.equalsIgnoreCase("Mahasiswa Registrasi")) {
                        detail = statistikMahasiswaDAO.getDetailAllMahasiswaRegistrasi(kodeProdi, tahunMulai.toString());
                    } else if (currentStatus.equalsIgnoreCase("Mahasiswa Tidak Registrasi")) {
                        detail = statistikMahasiswaDAO.getDetailAllMahasiswaTidakRegistrasi(kodeProdi, tahunMulai.toString());
                    } else if (currentStatus.equalsIgnoreCase("Mahasiswa Cuti")) {
                        detail = statistikMahasiswaDAO.getDetailAllMahasiswaCuti(kodeProdi, tahunMulai.toString());
                    } else if (currentStatus.equalsIgnoreCase("Mahasiswa DO")) {
                        detail = statistikMahasiswaDAO.getDetailAllMahasiswaDO(kodeProdi, tahunMulai.toString());
                    } else if (currentStatus.equalsIgnoreCase("Mahasiswa Lulus")) {
                        detail = statistikMahasiswaDAO.getDetailAllMahasiswaLulus(kodeProdi, tahunMulai.toString());
                    }

                    int no = 1;
                    listboxDetailMahasiswa.getChildren().clear();
                    Listhead listhead = new Listhead();
                    Listheader listheaderNo = new Listheader("NO");
                    listheaderNo.setAlign("right");
                    listheaderNo.setWidth("50px");
                    listhead.appendChild(listheaderNo);
                    listhead.appendChild(new Listheader("Nama", "", "400px"));
                    listhead.appendChild(new Listheader("Angkatan", "", "150px"));
                    listhead.appendChild(new Listheader("Program Studi", "", "300px"));
                    listboxDetailMahasiswa.appendChild(listhead);
                    for (Map m : detail) {
                        Listitem item = new Listitem();
                        item.appendChild(new Listcell(no + ""));
                        item.appendChild(new Listcell(m.get("nama_mhs").toString()));
                        item.appendChild(new Listcell(m.get("angkatan").toString()));
                        item.appendChild(new Listcell(m.get("Nama_prg").toString()));
                        listboxDetailMahasiswa.appendChild(item);
                        no++;
                    }
                }
            });
        }


        //Anchor for total data
        Toolbarbutton anchorDetailTotalAll = new Toolbarbutton();
        Listcell listcellTotalKeseluruhan = new Listcell();
        anchorDetailTotalAll.setLabel(totalKeseluruhan + "");
        listcellTotalKeseluruhan.appendChild(anchorDetailTotalAll);
        listitemTotal.appendChild(listcellTotalKeseluruhan);

        anchorDetailTotalAll.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener() {

            public void onEvent(Event arg0) throws Exception {
                List<Map> detail = new ArrayList<Map>();

                detail = statistikMahasiswaDAO.getDetailAllMahasiswa(kodeProdi, String.valueOf(tahunMulai));

                int no = 1;
                listboxDetailMahasiswa.getChildren().clear();
                Listhead listhead = new Listhead();
                Listheader listheaderNo = new Listheader("NO");
                listheaderNo.setAlign("right");
                listheaderNo.setWidth("50px");
                listhead.appendChild(listheaderNo);
                listhead.appendChild(new Listheader("Nama", "", "400px"));
                listhead.appendChild(new Listheader("Angkatan", "", "150px"));
                listhead.appendChild(new Listheader("Program Studi", "", "300px"));
                listboxDetailMahasiswa.appendChild(listhead);
                for (Map m : detail) {
                    Listitem item = new Listitem();
                    item.appendChild(new Listcell(no + ""));
                    item.appendChild(new Listcell(m.get("nama_mhs").toString()));
                    item.appendChild(new Listcell(m.get("angkatan").toString()));
                    item.appendChild(new Listcell(m.get("Nama_prg").toString()));
                    listboxDetailMahasiswa.appendChild(item);
                    no++;
                }
            }
        });


        for (int x = 0; x < dataset.getRowKeys().size(); x++) {
            Toolbarbutton anchorDetailTotal = new Toolbarbutton();
            anchorDetailTotal.setId((String) dataset.getRowKeys().get(x));
            Listcell listcellTotal = new Listcell();
            anchorDetailTotal.setLabel(totalPerAngkatan[x] + "");
            listcellTotal.appendChild(anchorDetailTotal);
            listitemTotal.appendChild(listcellTotal);

            //Anchor for total by angkatan
            anchorDetailTotal.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener() {

                public void onEvent(Event evt) throws Exception {
                    List<Map> detail = new ArrayList<Map>();

                    detail = statistikMahasiswaDAO.getDetailMahasiswa(kodeProdi, evt.getTarget().getId());

                    int no = 1;
                    listboxDetailMahasiswa.getChildren().clear();
                    Listhead listhead = new Listhead();
                    Listheader listheaderNo = new Listheader("NO");
                    listheaderNo.setAlign("right");
                    listheaderNo.setWidth("50px");
                    listhead.appendChild(listheaderNo);
                    listhead.appendChild(new Listheader("Nama", "", "400px"));
                    listhead.appendChild(new Listheader("Angkatan", "", "150px"));
                    listhead.appendChild(new Listheader("Program Studi", "", "300px"));
                    listboxDetailMahasiswa.appendChild(listhead);
                    for (Map m : detail) {
                        Listitem item = new Listitem();
                        item.appendChild(new Listcell(no + ""));
                        item.appendChild(new Listcell(m.get("nama_mhs").toString()));
                        item.appendChild(new Listcell(m.get("angkatan").toString()));
                        item.appendChild(new Listcell(m.get("Nama_prg").toString()));
                        listboxDetailMahasiswa.appendChild(item);
                        no++;
                    }

                }
            });
        }

        listboxMahasiswa.appendChild(listhead);
        this.componentEnable();
    }

    public void cmbDataProdiOnSelect() {
        this.componentDisable();
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();

    }

    public void btnShowOnClick() {
        this.componentEnable();
        this.loadDataToListbox();
    }

    public void exportReport() throws InterruptedException {
        try {
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(datas);

            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/statistikmahasiswa/StatistikMahasiswa.jasper");
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

    private CategoryDataset generateDataMahasiswa(String kodeProdi) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map> mahasiswas = new ArrayList<Map>();
        List<Map> mahasiswaRegistrasi = statistikMahasiswaDAO.getMahasiswaRegistrasi(kodeProdi);
        List<Map> mahasiswaCuti = statistikMahasiswaDAO.getMahasiswaCuti(kodeProdi);
        List<Map> mahasiswaTidakRegistrasi = statistikMahasiswaDAO.getMahasiswaTidakRegistrasi(kodeProdi);
        List<Map> mahasiswaDO = statistikMahasiswaDAO.getMahasiswaDO(kodeProdi);
        List<Map> mahasiswaLulus = statistikMahasiswaDAO.getMahasiswaLulus(kodeProdi);

        for (int i = tahunMulai; i <= new DateTime().getYear(); i++) {
            Integer jumlah = 0;
            Integer angkatan = 0;
            String status = null;
            Map mahasiswa = new HashMap();

            if (mahasiswaRegistrasi.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0);
                mahasiswa.put("tahun", i);
                mahasiswa.put("status", "Mahasiswa Registrasi");
            } else {
                jumlah = 0;
                mahasiswa = new HashMap();
                for (Map map : mahasiswaRegistrasi) {
                    if (map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = i;
                    } else {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                    }
                    status = map.get("status").toString();
                    if (angkatan != i) {
                        angkatan = i;
                        mahasiswa.put("jumlah", jumlah);
                        mahasiswa.put("tahun", angkatan);
                        mahasiswa.put("status", status);
                    } else {
                        jumlah += Integer.parseInt(map.get("jumlah").toString());
                        mahasiswa.put("jumlah", jumlah);
                        mahasiswa.put("tahun", angkatan);
                        mahasiswa.put("status", status);
                    }
                }
            }
            mahasiswas.add(mahasiswa);

            if (mahasiswaTidakRegistrasi.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", null);
                mahasiswa.put("tahun", i);
                mahasiswa.put("status", "Mahasiswa Tidak Registrasi");
            } else {
                jumlah = 0;
                mahasiswa = new HashMap();
                for (Map map : mahasiswaTidakRegistrasi) {
                    if (map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = i;
                    } else {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                    }
                    status = map.get("status").toString();
                    if (angkatan != i) {
                        angkatan = i;
                        mahasiswa.put("jumlah", jumlah);
                        mahasiswa.put("tahun", angkatan);
                        mahasiswa.put("status", status);
                    } else {
                        jumlah += Integer.parseInt(map.get("jumlah").toString());
                        mahasiswa.put("jumlah", jumlah);
                        mahasiswa.put("tahun", angkatan);
                        mahasiswa.put("status", status);
                    }
                }
            }
            mahasiswas.add(mahasiswa);

            if (mahasiswaCuti.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", null);
                mahasiswa.put("tahun", i);
                mahasiswa.put("status", "Mahasiswa Cuti");
            } else {
                jumlah = 0;
                mahasiswa = new HashMap();
                for (Map map : mahasiswaCuti) {
                    if (map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = i;
                    } else {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                    }
                    status = map.get("status").toString();
                    if (angkatan != i) {
                        angkatan = i;
                        mahasiswa.put("jumlah", jumlah);
                        mahasiswa.put("tahun", angkatan);
                        mahasiswa.put("status", status);
                    } else {
                        jumlah += Integer.parseInt(map.get("jumlah").toString());
                        mahasiswa.put("jumlah", jumlah);
                        mahasiswa.put("tahun", angkatan);
                        mahasiswa.put("status", status);
                    }
                }
            }
            mahasiswas.add(mahasiswa);

            if (mahasiswaDO.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", null);
                mahasiswa.put("tahun", i);
                mahasiswa.put("status", "Mahasiswa Do");
            } else {
                jumlah = 0;
                mahasiswa = new HashMap();
                for (Map map : mahasiswaDO) {
                    if (map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = i;
                    } else {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                    }
                    status = map.get("status").toString();
                    if (angkatan != i) {
                        angkatan = i;
                        mahasiswa.put("jumlah", jumlah);
                        mahasiswa.put("tahun", angkatan);
                        mahasiswa.put("status", status);
                    } else {
                        jumlah += Integer.parseInt(map.get("jumlah").toString());
                        mahasiswa.put("jumlah", jumlah);
                        mahasiswa.put("tahun", angkatan);
                        mahasiswa.put("status", status);
                    }
                }
            }
            mahasiswas.add(mahasiswa);

            //Special case for input string 20M. 
            if (mahasiswaLulus.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", null);
                mahasiswa.put("tahun", i);
                mahasiswa.put("status", "Mahasiswa Lulus");
            } else {
                jumlah = 0;
                mahasiswa = new HashMap();
                for (Map map : mahasiswaLulus) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        status = map.get("status").toString();
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("status", status);
                        } else {
                            jumlah += Integer.parseInt(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("status", status);
                        }
                    }
                }
            }
            mahasiswas.add(mahasiswa);
        }

        datas.clear();
        Map data = new HashMap();
        for (Map map : mahasiswas) {
            // System.out.println(map.get("tahun") + "-------" + map.get("jumlah") + "--------------" + map.get("status"));
            Integer jumlah = 0;
            try {
                jumlah = Integer.parseInt(map.get("jumlah").toString());
            } catch (NullPointerException npe) {
                jumlah = 0;
            }

            map.put("tahun", map.get("tahun"));
            map.put("status", map.get("status").toString());
            map.put("jumlah", jumlah);
            datas.add(map);
            dataset.addValue(jumlah, map.get("tahun").toString(), map.get("status").toString());
        }
        mahasiswas.clear();
        return dataset;
    }
}
