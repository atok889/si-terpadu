/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswalulusdanbelumlulus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
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
public class JumlahMahasiswaLulusDanBelumLulusWnd extends ClassApplicationModule {

    private JumlahMahasiswaLulusDanBelumLulusDAO jumlahMahasiswaLulusDanBelumLulusDAO;
    private Combobox cmbboxProdi;
    private String kodeProdi;
    private Listbox listboxMahasiswa;
    private String tahunAkademik = "2000";
    private String semester = "2";
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    DecimalFormat dfJumlah = new DecimalFormat("# 0;-0");
    DecimalFormat dfProsen = new DecimalFormat("# 0.00");
    List<JumlahMahasiswaLulusDanBelumLulus> datas = new ArrayList<JumlahMahasiswaLulusDanBelumLulus>();

    public JumlahMahasiswaLulusDanBelumLulusWnd() {
        jumlahMahasiswaLulusDanBelumLulusDAO = new JumlahMahasiswaLulusDanBelumLulusDAOImpl();
    }

    public void onCreate() throws Exception {
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataProdiToCombo();

    }

    private void loadDataProdiToCombo() {
        Comboitem item = new Comboitem();
        item.setValue(null);
        item.setLabel("--Pilih Prodi--");
        cmbboxProdi.appendChild(item);
        cmbboxProdi.setSelectedItem(item);

        for (Map map : jumlahMahasiswaLulusDanBelumLulusDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Kd_prg").toString() + " " + map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
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
        DefaultCategoryDataset dataset = this.generateDataMahasiswa(kodeProdi);
        this.listboxMahasiswa.getChildren().clear();
        Listhead listhead = new Listhead();

        Listheader listheaderAngkatan = new Listheader();
        listheaderAngkatan.setLabel("Angkatan");
        listheaderAngkatan.setAlign("right");
        listheaderAngkatan.setWidth("130px");
        listhead.appendChild(listheaderAngkatan);

        for (Object column : dataset.getColumnKeys()) {
            Listheader listheader = new Listheader();
            String header = column.toString();
            if (header.equalsIgnoreCase("Prosentase Belum Lulus") || header.equalsIgnoreCase("Prosentase Lulus")) {
                header = "";
            }
            listheader.setLabel(header);
            listheader.setAlign("right");
            listheader.setWidth("130px");
            listhead.appendChild(listheader);
        }

        for (Object row : dataset.getRowKeys()) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(row.toString()));

            for (Object column : dataset.getColumnKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                String value = "";

                if (number == null) {
                    value = "0";
                } else {
                    if (column.toString().equalsIgnoreCase("Jumlah Mahasiswa") || column.toString().equalsIgnoreCase("Jumlah Lulus") || column.toString().equalsIgnoreCase("Jumlah Belum Lulus")) {
                        value = dfJumlah.format(number.doubleValue());
                    } else {
                        value = dfProsen.format(number.doubleValue()) + " %";
                    }
                }
                listitem.appendChild(new Listcell(value));
            }
            listboxMahasiswa.appendChild(listitem);
        }
        listboxMahasiswa.appendChild(listhead);
    }

    public void cmbDataProdiOnSelect() {
        this.componentDisable();
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
    }

    public void btnShowOnClick() throws InterruptedException {
        this.componentEnable();
        this.loadDataToListbox();
    }

    public void exportReport() throws Exception {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
            if (cmbExportType.getSelectedItem().getValue().toString().equals("pdf")) {
                Window pdfPreviewWnd = (Window) Executions.createComponents("/zul/pdfpreview/PdfPreview.zul", null, null);
                Jasperreport pdfReport = (Jasperreport) pdfPreviewWnd.getFellow("report");
                pdfReport.setType(cmbExportType.getSelectedItem().getValue().toString());
                pdfReport.setSrc("reports/jumlahmahasiswalulusdanbelumlulus/JumlahMahasiswaLulusDanBelumLulus1.jasper");
                pdfReport.setParameters(null);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/jumlahmahasiswalulusdanbelumlulus/JumlahMahasiswaLulusDanBelumLulus1.jasper");
                report.setParameters(null);
                report.setDatasource(dataSource);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    private DefaultCategoryDataset generateDataMahasiswa(String kodeProdi) {
        datas = new ArrayList<JumlahMahasiswaLulusDanBelumLulus>();
        List<Map> mahasiswas = new ArrayList<Map>();
        List<Map> jumlahMahasiswas = jumlahMahasiswaLulusDanBelumLulusDAO.getJumlahMahasiswa(kodeProdi);
        List<Map> jumlahLulus = jumlahMahasiswaLulusDanBelumLulusDAO.getJumlahMahasiswaLulus(kodeProdi);
        for (int i = 1980; i <= new DateTime().getYear(); i++) {
            double jumlah = 0;
            int angkatan = 0;
            double jumlahMahasiswa = 0;
            double jumlahMahasiswaLulus = 0;
            double prosentasiMahasiswa = 0d;
            double prosentaseLulus = 0d;
            double prosentasiBelumLulus = 0d;
            Map mahasiswa = new HashMap();

            if (jumlahMahasiswas.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0d);
                mahasiswa.put("tahun", i);
                mahasiswa.put("header", "Jumlah Mahasiswa");
            } else {
                jumlah = 0;
                mahasiswa = new HashMap();
                for (Map map : jumlahMahasiswas) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Mahasiswa");
                        } else {
                            jumlah += Double.parseDouble(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Mahasiswa");
                        }
                    }
                }
                jumlahMahasiswa = (Double) mahasiswa.get("jumlah");
            }
            mahasiswas.add(mahasiswa);

            if (jumlahLulus.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0d);
                mahasiswa.put("tahun", i);
                mahasiswa.put("header", "Jumlah Lulus");
            } else {
                jumlah = 0;
                mahasiswa = new HashMap();
                for (Map map : jumlahLulus) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Lulus");
                        } else {
                            jumlah += Double.parseDouble(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Lulus");
                        }
                    }
                }
                jumlahMahasiswaLulus = (Double) mahasiswa.get("jumlah");
            }
            mahasiswas.add(mahasiswa);

            if (jumlahMahasiswas.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0d);
                mahasiswa.put("tahun", i);
                mahasiswa.put("header", "Prosentase Lulus");
            } else {
                mahasiswa = new HashMap();
                prosentasiMahasiswa = (jumlahMahasiswa * 10 / 100);
                if (prosentasiMahasiswa != 0) {
                    prosentaseLulus = 100 - ((jumlahMahasiswa - jumlahMahasiswaLulus) / prosentasiMahasiswa) * 10;
                }

                for (Map map : jumlahMahasiswas) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", prosentaseLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Prosentase Lulus");
                        } else {
                            jumlah += Double.parseDouble(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", prosentaseLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Prosentase Lulus");
                        }
                    }
                }
            }
            mahasiswas.add(mahasiswa);

            if (jumlahMahasiswas.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0d);
                mahasiswa.put("tahun", i);
                mahasiswa.put("header", "Jumlah Belum Lulus");
            } else {
                mahasiswa = new HashMap();
                for (Map map : jumlahMahasiswas) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", jumlahMahasiswa - jumlahMahasiswaLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Belum Lulus");
                        } else {
                            jumlah += Double.parseDouble(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", jumlahMahasiswa - jumlahMahasiswaLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Belum Lulus");
                        }
                    }
                }
            }
            mahasiswas.add(mahasiswa);

            if (jumlahMahasiswas.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0d);
                mahasiswa.put("tahun", i);
                mahasiswa.put("header", "Prosentase Belum Lulus");
            } else {
                mahasiswa = new HashMap();
                if (jumlahMahasiswa - jumlahMahasiswaLulus == 0) {
                    prosentasiBelumLulus = 0;
                } else {
                    prosentasiBelumLulus = 100 - prosentaseLulus;
                }
                for (Map map : jumlahMahasiswas) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", prosentasiBelumLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Prosentase Belum Lulus");
                        } else {
                            jumlah += Double.parseDouble(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", prosentasiBelumLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Prosentase Belum Lulus");
                        }
                    }
                }
            }
            mahasiswas.add(mahasiswa);
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map map : mahasiswas) {
            System.out.println(map.get("tahun") + "::" + map.get("jumlah") + "::" + map.get("header"));
            Double jml = 0d;
            try {
                jml = (Double) map.get("jumlah");
            } catch (NullPointerException npe) {
                jml = 0d;
            }
            //Exporting data to dataset
            dataset.addValue(jml, map.get("tahun").toString(), map.get("header").toString());

            //Exporting data to report ....
            String jumlah = "";
            String header = "";
            if (map.get("header").toString().equalsIgnoreCase("Jumlah Mahasiswa") || map.get("header").toString().equalsIgnoreCase("Jumlah Belum Lulus") || map.get("header").toString().equalsIgnoreCase("Jumlah Lulus")) {
                jumlah = dfJumlah.format(jml);
                header = map.get("header").toString();
            } else {
                jumlah = dfProsen.format(jml) + "%";
                header = "";
            }
            String tahun = map.get("tahun").toString();
            JumlahMahasiswaLulusDanBelumLulus jmldbl = new JumlahMahasiswaLulusDanBelumLulus();
            jmldbl.setTahun(map.get("tahun").toString());
            jmldbl.setJumlah(jumlah);
            jmldbl.setHeader(header);
            datas.add(jmldbl);

        }
        mahasiswas.clear();
        return dataset;
    }
}
