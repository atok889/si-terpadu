/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.riwayatdosen;

import java.util.List;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sia.terpadu.matrikborang.MatrikBorang;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudi;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudiDAO;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudiDAOImpl;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author jasoet
 */
public class RiwayatDosenWnd extends ClassApplicationModule {

    private Combobox cmbProgdi;
    private Jasperreport report;
    private Combobox cmbExportType;
    private Button btnExport;
    private Listbox lstDataDosen;
    private Listbox lstDataRiwayat;
    private List<DataDosen> dataDosens;
    private RiwayatDosenDAO dao;

    public RiwayatDosenWnd() {
        dao = new RiwayatDosenDAOImpl();
    }

    public void onCreate() throws Exception {
        lstDataDosen = (Listbox) getFellow("lstDataDosen");
        lstDataRiwayat = (Listbox) getFellow("lstDataRiwayat");
        cmbProgdi = (Combobox) getFellow("cmbProgdi");
        report = (Jasperreport) getFellow("report");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        loadProgdi();
        cmbProgdi.setSelectedIndex(0);
        cmbExportType.setSelectedIndex(0);
    }

    private void loadProgdi() throws Exception {
        try {
            UKProgramStudiDAO dao = new UKProgramStudiDAOImpl();
            List<UKProgramStudi> progdis = dao.gets();
            cmbProgdi.getItems().clear();
            Comboitem item = new Comboitem();
//            item.setValue(null);
//            item.setLabel("--Pilih Prodi--");
//            cmbProgdi.appendChild(item);
            for (UKProgramStudi ps : progdis) {
                Comboitem items = new Comboitem();
                items.setValue(ps);
                items.setLabel(ps.getKodeUnitKerja() + " " + ps.getNama());
                cmbProgdi.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public void dataOnSelect() throws Exception {
        try {
            lstDataRiwayat.getItems().clear();
            DataDosen o = (DataDosen) lstDataDosen.getSelectedItem().getValue();
            UKProgramStudi prodiSelected = (UKProgramStudi) cmbProgdi.getSelectedItem().getValue();

            List<RiwayatDosen> dataRiwayat = dao.getRiwayatDosenByKodeDosen(o.getKode(),prodiSelected.getKodeUnitKerja());
            int i = 1;
            for (RiwayatDosen rd : dataRiwayat) {
                Listitem li = new Listitem();
                li.setValue(rd);
                Listcell cell = new Listcell("" + (i++));
                li.appendChild(cell);
                cell = new Listcell(rd.getTahun());
                li.appendChild(cell);
                cell = new Listcell(rd.getRiwayat());
                li.appendChild(cell);
                cell = new Listcell(rd.getKeterangan());
                li.appendChild(cell);
                cell = new Listcell(rd.getAlamat());
                li.appendChild(cell);
                lstDataRiwayat.appendChild(li);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show(ex.getMessage());
        }
    }

    public void viewReport() throws Exception {
        try {
            lstDataDosen.getItems().clear();
            dao = new RiwayatDosenDAOImpl();
            UKProgramStudi prodiSelected = (UKProgramStudi) cmbProgdi.getSelectedItem().getValue();
            dataDosens = dao.getDataDosenByUnitKerja(prodiSelected.getKodeUnitKerja());
            int i = 1;
            for (DataDosen o : dataDosens) {
                Listitem li = new Listitem();
                li.setValue(o);
                Listcell cell = new Listcell("" + (i++));
                li.appendChild(cell);
                cell = new Listcell(o.getKode());
                li.appendChild(cell);
                cell = new Listcell(o.getNama());
                li.appendChild(cell);
                lstDataDosen.appendChild(li);
            }
            btnExport.setDisabled(false);


        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show(ex.getMessage());
        }
    }
}
