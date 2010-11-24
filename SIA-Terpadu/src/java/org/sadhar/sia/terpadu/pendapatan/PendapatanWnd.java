/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pendapatan;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import org.sadhar.sia.framework.ClassApplicationModule;
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
public class PendapatanWnd extends ClassApplicationModule {

    private Combobox cmbboxProdi;
    private Jasperreport report;
    private Combobox cmbExportType;
    private Combobox cmbboxTahun;
    private Button btnExport;
    private Listbox listboxRencana;
    private Listbox listboxRealisasi;
    private PendapatanDAO dao;

    public PendapatanWnd() {
        dao = new PendapatanDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxRencana = (Listbox) getFellow("listboxRencana");
        listboxRealisasi = (Listbox) getFellow("listboxRealisasi");
        cmbboxProdi = (Combobox) getFellow("cmbboxProdi");
        cmbboxTahun = (Combobox) getFellow("cmbboxTahun");
        report = (Jasperreport) getFellow("report");
        cmbExportType = (Combobox) getFellow("cmbExportType");
        btnExport = (Button) getFellow("btnExport");
        btnExport.setDisabled(true);
        loadProgdi();
        loadTahun();
        cmbboxTahun.setSelectedIndex(cmbboxTahun.getItems().size() - 1);
        cmbboxProdi.setSelectedIndex(0);
        cmbExportType.setSelectedIndex(0);
    }

    private void loadTahun() throws Exception {
        int year = 2005;
        while (year <= Calendar.getInstance().get(Calendar.YEAR)) {
            Comboitem item = new Comboitem();
            item.setLabel("" + year);
            item.setValue("" + year);
            year++;
            cmbboxTahun.appendChild(item);
        }
    }

    private void loadProgdi() throws Exception {
        try {
            UKProgramStudiDAO daor = new UKProgramStudiDAOImpl();
            List<UKProgramStudi> progdis = daor.gets();
            cmbboxProdi.getItems().clear();
//            Comboitem item = new Comboitem();
//            item.setValue(null);
//            item.setLabel("--Pilih Prodi--");
//            cmbProgdi.appendChild(item);
            for (UKProgramStudi ps : progdis) {
                Comboitem items = new Comboitem();
                items.setValue(ps);
                items.setLabel(ps.getKodeUnitKerja() + " " + ps.getNama());
                cmbboxProdi.appendChild(items);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }

    public void viewReport() throws Exception {
        try {
            listboxRencana.getItems().clear();
            listboxRealisasi.getItems().clear();
            UKProgramStudi prodiSelected = (UKProgramStudi) cmbboxProdi.getSelectedItem().getValue();
            DecimalFormat df = new DecimalFormat("###0.00");
            List<PendapatanRencana> lpr = dao.getAllRencana(cmbboxTahun.getSelectedItem().getValue() + "");
            int i = 1;
            for (PendapatanRencana o : lpr) {
                Listitem li = new Listitem();
                li.setValue(o);
                Listcell cell = new Listcell("" + (i++));
                li.appendChild(cell);
                cell = new Listcell(o.getJenisPendapatan());
                li.appendChild(cell);
                cell = new Listcell(df.format(o.getRencana()));
                li.appendChild(cell);
                listboxRencana.appendChild(li);
            }

            List<PendapatanRealisasi> lprea = dao.getAllRealisasi(prodiSelected.getShortKode(), cmbboxTahun.getSelectedItem().getValue() + "");
             i = 1;
            for (PendapatanRealisasi o : lprea) {
                Listitem li = new Listitem();
                li.setValue(o);
                Listcell cell = new Listcell("" + (i++));
                li.appendChild(cell);
                cell = new Listcell(o.getNamaTagih());
                li.appendChild(cell);
                cell = new Listcell(df.format(o.getRealisasi()));
                li.appendChild(cell);
                listboxRealisasi.appendChild(li);
            }
            
            btnExport.setDisabled(false);


        } catch (Exception ex) {
            ex.printStackTrace();
            Messagebox.show(ex.getMessage());
        }
    }
}
