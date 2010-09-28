/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningadministratif;

import java.util.Map;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;

/**
 *
 * @author kris
 */
public class WarningAdministratifWnd extends ClassApplicationModule {

    private WarningAdministratifDAO warningAdministratifDAO;
    private Combobox cmbboxProdi;
    private String kodeProdi;
    private Listbox listboxMahasiswa;
    private Combobox cmbExportType;
    private Jasperreport report;
    private Button btnExport;
    private String tahunAkademik = "2000";
    private String semester = "2";

    public WarningAdministratifWnd() {
        warningAdministratifDAO = new WarningAdministratifDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        report = (Jasperreport) getFellow("report");
        btnExport = (Button) getFellow("btnExport");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        cmbExportType.setSelectedIndex(0);
        this.loadDataProdiToCombo();
    }

    private void loadDataProdiToCombo() {
        Comboitem item = new Comboitem("--SEMUA FAKULTAS--");
        cmbboxProdi.appendChild(item);
        cmbboxProdi.setSelectedItem(item);

        for (Map map : warningAdministratifDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Kd_prg").toString()+" "+map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
    }

    private void loadDataToListbox() {
        this.listboxMahasiswa.getChildren().clear();
        Listhead listhead = new Listhead();

        Listheader listheaderNo = new Listheader();
        listheaderNo.setLabel("No");
        listheaderNo.setWidth("50px");
        listhead.appendChild(listheaderNo);

        Listheader listheaderNama = new Listheader();
        listheaderNama.setLabel("Nama");
        listhead.appendChild(listheaderNama);

        Listheader listheaderProdi = new Listheader();
        listheaderProdi.setLabel("Prodi");
        listhead.appendChild(listheaderProdi);

        Listheader listheaderFakultas = new Listheader();
        listheaderFakultas.setLabel("Fakultas");
        listhead.appendChild(listheaderFakultas);

        Listheader listheaderAngkatan = new Listheader();
        listheaderAngkatan.setLabel("Tahun Angkatan");
        listhead.appendChild(listheaderAngkatan);

        Listheader listheaderIpk = new Listheader();
        listheaderIpk.setLabel("IPK");
        listheaderIpk.setWidth("100px");
        listhead.appendChild(listheaderIpk);
    }

    public void cmbDataProdiOnSelect() {
        if (cmbboxProdi.getSelectedItem().getValue() == null) {
            kodeProdi = null;
        } else {
            kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
            warningAdministratifDAO.getWarningAdministratif(kodeProdi, tahunAkademik, semester);
        }
    }
}
