/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.sebaranipklulusan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;

/**
 *
 * @author kris
 */
public class SebaranIpkLulusanWnd extends ClassApplicationModule {

    private SebaranIpkLulusanDAO sebaranIpkLulusanDAO;
    private Listbox listboxMahasiswa;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private List<Map> dataReport = new ArrayList<Map>();

    public SebaranIpkLulusanWnd() {
        sebaranIpkLulusanDAO = new SebaranIpkLulusanDAOImpl();
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
        listboxMahasiswa.getChildren().clear();

        Auxhead auxhead = new Auxhead();
        Listhead listhead = new Listhead();

        Auxheader auxheaderNo = new Auxheader();
        auxheaderNo.setLabel("No");
        auxheaderNo.setAlign("center");
        auxhead.appendChild(auxheaderNo);

        Auxheader auxheaderProdi = new Auxheader();
        auxheaderProdi.setLabel("Prodi");
        auxheaderProdi.setAlign("center");
        auxhead.appendChild(auxheaderProdi);

        Auxheader auxheaderKategoriA = new Auxheader();
        auxheaderKategoriA.setLabel("0.0-2.0");
        auxheaderKategoriA.setAlign("center");
        auxhead.appendChild(auxheaderKategoriA);

        Auxheader auxheaderKategoriB = new Auxheader();
        auxheaderKategoriB.setLabel("2.0-2.5");
        auxheaderKategoriB.setAlign("center");
        auxhead.appendChild(auxheaderKategoriB);


    }

    private List<Map> generateData() {
        List<Map> datas = sebaranIpkLulusanDAO.getSebaranIpkLulusan();
        List<Map> prodis = sebaranIpkLulusanDAO.getProdi();
        List<Map> rerata = new ArrayList<Map>();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map prodi : prodis) {
            int countE = 0;
            int countD = 0;
            int countC = 0;
            int countB = 0;
            int countA = 0;
            int totalMahasiswa = 0;
            Map map = new HashMap();
            for (Map data : datas) {

                if (prodi.get("Kd_prg").toString().equalsIgnoreCase(data.get("Kd_prg").toString())) {
                    double ipk = Double.parseDouble(data.get("ipk").toString());
                    if (ipk >= 0 && ipk <= 2) {
                        countE += 1;
                    } else if (ipk > 2 && ipk <= 2.5) {
                        countD += 1;
                    } else if (ipk > 2.5 && ipk <= 3) {
                        countC += 1;
                    } else if (ipk > 3 && ipk <= 3.5) {
                        countB += 1;
                    } else if (ipk > 3 && ipk <= 4) {
                        countA += 1;
                    }
                    map.put("ipkA", countA);
                    map.put("ipkB", countB);
                    map.put("ipkC", countC);
                    map.put("ipkD", countD);
                    map.put("ipkE", countE);
                    totalMahasiswa++;
                } else {
                    map.put("ipkA", countA);
                    map.put("ipkB", countB);
                    map.put("ipkC", countC);
                    map.put("ipkD", countD);
                    map.put("ipkE", countE);
                }
            }
            map.put("total", totalMahasiswa);
            map.put("prodi", prodi.get("Nama_prg").toString());
            //Hitung prosentase
            int ipkA = Integer.parseInt(map.get("ipkA").toString());
            int ipkB = Integer.parseInt(map.get("ipkB").toString());
            int ipkC = Integer.parseInt(map.get("ipkC").toString());
            int ipkD = Integer.parseInt(map.get("ipkD").toString());
            int ipkE = Integer.parseInt(map.get("ipkE").toString());
            double prosentaseA = 0;
            double prosentaseB = 0;
            double prosentaseC = 0;
            double prosentaseD = 0;
            double prosentaseE = 0;
            if (totalMahasiswa != 0) {
                prosentaseA = ((double) ipkA / totalMahasiswa) * 100;
                prosentaseB = ((double) ipkB / totalMahasiswa) * 100;
                prosentaseC = ((double) ipkC / totalMahasiswa) * 100;
                prosentaseD = ((double) ipkD / totalMahasiswa) * 100;
                prosentaseE = ((double) ipkE / totalMahasiswa) * 100;
            }

            map.put("prosentaseA", prosentaseA);
            map.put("prosentaseB", prosentaseB);
            map.put("prosentaseC", prosentaseC);
            map.put("prosentaseD", prosentaseD);
            map.put("prosentaseE", prosentaseE);
            rerata.add(map);
        }
        return rerata;
    }
}
