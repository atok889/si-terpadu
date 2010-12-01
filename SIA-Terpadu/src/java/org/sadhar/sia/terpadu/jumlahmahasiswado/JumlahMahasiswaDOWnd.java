/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswado;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Deny Prasetyo
 */
public class JumlahMahasiswaDOWnd extends ClassApplicationModule {

    Combobox cmbProgdi;
    Textbox txtTahunAngkatan;
    Jasperreport report;
//    Image chartImg;
    Combobox cmbExportType;
    Button btnExport;
    Listbox listb;
    JFreeChart chart = null;
    List<JumlahMahasiswaDO> datas;

    public JumlahMahasiswaDOWnd() {
    }

    public void onCreate() throws Exception {
        listb = (Listbox) getFellow("listb");
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        txtTahunAngkatan = (Textbox) getFellow("txtTahunAngkatan");
        report = (Jasperreport) getFellow("report");
//        chartImg = (Image) getFellow("chartImg");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        loadProgdi();
        cmbProgdi.setSelectedIndex(0);
        cmbExportType.setSelectedIndex(0);
    }

    private void loadProgdi() throws Exception {
        try {
            JumlahMahasiwaDODAO dao = new JumlahMahasiswaDODAOImpl();
            List<ProgramStudi> progdis = dao.getProgramStudi();
            cmbProgdi.getItems().clear();
            Comboitem item = new Comboitem();
            item.setValue(null);
            item.setLabel("--Pilih Prodi--");
            cmbProgdi.appendChild(item);
            for (ProgramStudi ps : progdis) {
                Comboitem items = new Comboitem();
                items.setValue(ps);
                items.setLabel(ps.getKode() + " " + ps.getNama());
                cmbProgdi.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public void viewReport() throws Exception {
        try {
            JumlahMahasiwaDODAO dao = new JumlahMahasiswaDODAOImpl();
            CategoryDataset dataset = dao.getDataset((ProgramStudi) cmbProgdi.getSelectedItem().getValue(), "");


            String tahun = txtTahunAngkatan.getText();
            try {
                int tahunI = Integer.parseInt(tahun);

                if (tahunI < 2000 || tahunI > Calendar.getInstance().get(Calendar.YEAR)) {
                    tahun = Calendar.getInstance().get(Calendar.YEAR) + "";
                }
            } catch (NumberFormatException ex) {
                tahun = "";
            }

            txtTahunAngkatan.setText(tahun);

            listb.getItems().clear();


            Listhead lhead;
            if (listb.getListhead() != null) {
                lhead = listb.getListhead();
                lhead.getChildren().clear();
            } else {
                lhead = new Listhead();
                listb.appendChild(lhead);
            }
            Listheader lheader = new Listheader();
            lheader.setLabel("Program Studi");
            lheader.setWidth("20%");
            lhead.appendChild(lheader);

            if (tahun.isEmpty()) {
                for (int i = 2000; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
                    Listheader inlhd = new Listheader();
                    inlhd.setLabel(i + "");
                    inlhd.setWidth("5%");
                    lhead.appendChild(inlhd);
                }
            } else {
                Listheader inlhd = new Listheader();
                inlhd.setLabel(tahun + "");
                inlhd.setWidth("5%");
                lhead.appendChild(inlhd);
            }


            Listheader inlhd = new Listheader();
            inlhd.setLabel("Total Prodi");
            inlhd.setWidth("10%");
            lhead.appendChild(inlhd);

            datas = new ArrayList<JumlahMahasiswaDO>();

            for (Object s : dataset.getColumnKeys()) {//Prodi
                Listitem item = new Listitem();
                Listcell cell = new Listcell();
                cell.setLabel(s.toString());
                item.appendChild(cell);
                int jumlahPerProdi = 0;
                int indexTotalUniv = 0;
                for (int i = 2000; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {//Tahun
                    JumlahMahasiswaDO jmd = new JumlahMahasiswaDO();
                    Number nbr = null;
                    try {
                        nbr = dataset.getValue((Comparable) (i + ""), (Comparable) s);
                    } catch (Exception e) {
                        nbr = new Double(0);
                    }
                    jmd.setProdi(s.toString());
                    jmd.setTahun(i + "");

                    if (nbr != null) {
                        jumlahPerProdi += nbr.intValue();
                        jmd.setJumlah(nbr.intValue());
                    } else {
                        jmd.setJumlah(0);
                        jumlahPerProdi += 0;
                    }

                    if (tahun.isEmpty()) {
                        cell = new Listcell();
                        cell.setLabel(jmd.getJumlah() + "");
                        cell.setStyle("text-align:right");
                        item.appendChild(cell);
                        datas.add(jmd);
                    } else {
                        if (tahun.equalsIgnoreCase(i + "")) {
                            cell = new Listcell();
                            cell.setLabel(jmd.getJumlah() + "");
                            cell.setStyle("text-align:right");
                            item.appendChild(cell);
                            datas.add(jmd);
                        }
                    }
                    indexTotalUniv++;
                }

                JumlahMahasiswaDO jmd = new JumlahMahasiswaDO();
                jmd.setProdi(s.toString());
                jmd.setTahun("Total Prodi");
                jmd.setJumlah(jumlahPerProdi);
                datas.add(jmd);
                cell = new Listcell();
                cell.setLabel(jumlahPerProdi + "");
                cell.setStyle("text-align:right");

                item.appendChild(cell);
                listb.appendChild(item);
            }

            dataset = dao.getDataset(null, "");
            int[] totalUniversitas;
            String[] tahunTotal;
            if (tahun.isEmpty()) {
                int sumIndex = Calendar.getInstance().get(Calendar.YEAR) - 2000;
                tahunTotal = new String[sumIndex + 1];
                totalUniversitas = new int[sumIndex + 1];
            } else {
                tahunTotal = new String[1];
                totalUniversitas = new int[1];
            }
            int totalKeseluruhan = 0;

            for (Object s : dataset.getColumnKeys()) {//Prodi
                int jumlahPerProdi = 0;
                int indexTotalUniv = 0;
                for (Object f : dataset.getRowKeys()) {//Tahun
                    Number nbr = dataset.getValue((Comparable) f, (Comparable) s);
                    if (nbr != null) {
                        jumlahPerProdi += nbr.intValue();
                        if (tahun.isEmpty()) {
                            totalUniversitas[indexTotalUniv] += nbr.intValue();
                            tahunTotal[indexTotalUniv] = f.toString();
                            indexTotalUniv++;

                        } else {
                            if (f.equals(tahun)) {
                                totalUniversitas[indexTotalUniv] += nbr.intValue();
                                tahunTotal[indexTotalUniv] = f.toString();
                                indexTotalUniv++;
                            }
                        }
                    } else {
                        jumlahPerProdi += 0;
                        if (tahun.isEmpty()) {
                            totalUniversitas[indexTotalUniv] += 0;
                            tahunTotal[indexTotalUniv] = f.toString();
                            indexTotalUniv++;
                        } else {
                            if (f.equals(tahun)) {
                                tahunTotal[indexTotalUniv] = f.toString();
                                totalUniversitas[indexTotalUniv] += 0;
                                indexTotalUniv++;
                            }
                        }
                    }

                }

                totalKeseluruhan += jumlahPerProdi;
            }

            Listitem item = new Listitem();
            Listcell cell = new Listcell();
            cell.setLabel("Total Universitas");
            item.appendChild(cell);

            for (int i = 0; i < totalUniversitas.length; i++) {
                int res = totalUniversitas[i];
                String tahunT = tahunTotal[i];
                JumlahMahasiswaDO jmd = new JumlahMahasiswaDO();
                jmd.setProdi("Total Universitas");
                jmd.setTahun(tahunT);
                jmd.setJumlah(res);
                datas.add(jmd);
                cell = new Listcell();
                cell.setLabel(res + "");
                cell.setStyle("text-align:right");
                item.appendChild(cell);
            }


            JumlahMahasiswaDO jmd = new JumlahMahasiswaDO();
            jmd.setProdi("Total Universitas");
            jmd.setTahun("Total Prodi");
            jmd.setJumlah(totalKeseluruhan);
            datas.add(jmd);
            cell = new Listcell();
            cell.setLabel(totalKeseluruhan + "");
            cell.setStyle("text-align:right");
            item.appendChild(cell);

            listb.appendChild(item);

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
                pdfReport.setSrc("reports/jumlahmahasiswado/JumlahMahasiswaDO.jasper");
                Map parameters = new HashMap();
//                parameters.put("chart", chart.createBufferedImage(500, 300));
                pdfReport.setParameters(parameters);
                pdfReport.setDatasource(dataSource);
                pdfPreviewWnd.doModal();
            } else {
                report.setType(cmbExportType.getSelectedItem().getValue().toString());
                report.setSrc("reports/jumlahmahasiswado/JumlahMahasiswaDO.jasper");
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
