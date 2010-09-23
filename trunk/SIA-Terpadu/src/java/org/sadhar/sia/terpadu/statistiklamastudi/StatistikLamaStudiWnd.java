/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistiklamastudi;

import java.util.Map;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;

/**
 *
 * @author kris
 */
public class StatistikLamaStudiWnd extends ClassApplicationModule {

    private StatistikLamaStudiDAO statistikLamaStudiDAO;
    private Combobox cmbboxProdi;
    private Combobox cmbboxSemester;
    private String kodeProdi;
    private Listbox listboxMahasiswa;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private String tahunAkademik = "2000";
    private String semester = "2";

    public StatistikLamaStudiWnd() {
        statistikLamaStudiDAO = new StatistikLamaStudiDAOImpl();

    }

    public void onCreate() throws Exception {
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
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
        cmbboxProdi.appendChild(item);
        cmbboxProdi.setSelectedItem(item);

        for (Map map : statistikLamaStudiDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
    }

    private void loadDataSemesterToCombo() {
        Comboitem item = new Comboitem("--Pilih Semester--");
        cmbboxSemester.appendChild(item);
        cmbboxSemester.setSelectedItem(item);

        for (int i = 1; i <= 12; i++) {
            Comboitem items = new Comboitem();
            items.setValue(i);
            items.setLabel("Semester " + i);
            cmbboxSemester.appendChild(items);
        }
    }

    public void cmbDataProdiOnSelect() {
        if (cmbboxProdi.getSelectedItem().getValue() == null) {
            kodeProdi = null;
        } else {
            kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
        }
    }

    public void cmbDataSemesterOnSelect() {
        if (cmbboxSemester.getSelectedItem().getValue() == null) {
            kodeProdi = null;
        } else {
            kodeProdi = (String) cmbboxSemester.getSelectedItem().getValue();
        }
    }
}
