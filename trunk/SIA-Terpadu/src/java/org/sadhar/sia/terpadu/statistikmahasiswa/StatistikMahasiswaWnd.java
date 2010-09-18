/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistikmahasiswa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sadhar.sia.common.ClassSession;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;

/**
 *
 * @author kris
 */
public class StatistikMahasiswaWnd extends ClassApplicationModule {

    private StatistikMahasiswaDAO statistikMahasiswaDAO;
    private Combobox cmbboxProdi;
    private String kodeProdi;
    private Listbox listboxMahasiswa;
    private String tahunAkademik = "2000";
    private String semester = "2";
    private List<StatistikMahasiswa> statistikMahasiswas;

    public StatistikMahasiswaWnd() {
        statistikMahasiswaDAO = new StatistikMahasiswaDAOImpl();
    }

    public void onCreate() throws Exception {
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        // tahunAkademik = ClassSession.getInstance().getTahunAkademik();
        //  semester = ClassSession.getInstance().getSemester();
        this.loadDataProdiToCombo();
    }

    private void loadDataProdiToCombo() {
        for (Map map : statistikMahasiswaDAO.getProdi()) {
            Comboitem item = new Comboitem();
            item.setValue(map.get("Kd_prg").toString());
            item.setLabel(map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(item);
        }
    }

    private void loadDataToListbox() {

        listboxMahasiswa.getChildren().clear();
        statistikMahasiswaDAO.createTabelTempo(kodeProdi);

        if (statistikMahasiswaDAO.isTabelrgXYZZyyyySAda(kodeProdi, tahunAkademik, semester)) {

            statistikMahasiswaDAO.getMhsCuti(kodeProdi, tahunAkademik, semester);
            statistikMahasiswaDAO.getMhsReg(kodeProdi, tahunAkademik, semester);
            statistikMahasiswaDAO.getMhsDO(kodeProdi);
            statistikMahasiswaDAO.getJumlahMhsLulus(kodeProdi, tahunAkademik);
            statistikMahasiswaDAO.getMhsTidakReg(kodeProdi, tahunAkademik, semester);

            Listhead listhead = new Listhead();
            listhead.setSizable(true);
            Listheader listheaderProdi = new Listheader("", "", "200px");
            listhead.appendChild(listheaderProdi);
            Listheader listheaderTotal = new Listheader("Total", "", "100px");
            listhead.appendChild(listheaderTotal);

            final List<Map> tahun = statistikMahasiswaDAO.getAngkatan(kodeProdi);
            statistikMahasiswaDAO.createTabelStatistik(kodeProdi, tahun);

            Listheader listheader = new Listheader();

            List<Map> data = statistikMahasiswaDAO.getStatistikMahasiswa(kodeProdi);
            for (Map header : tahun) {
                listheader = new Listheader();
                listheader.setAlign("center");
                listheader.setLabel(header.get("angkatan").toString());
                listheader.setWidth("60px");
                listhead.appendChild(listheader);
            }

            for (final Map row : data) {
                Listitem item = new Listitem();
                item.appendChild(new Listcell(row.get("status").toString()));
                item.appendChild(new Listcell(row.get("total").toString()));
                for (final Map header : tahun) {
                    Listcell listcell = new Listcell();
                    Toolbarbutton anchorDetail = new Toolbarbutton();
                    anchorDetail.setLabel(row.get("t" + header.get("angkatan")).toString());
                    anchorDetail.setStyle("color : red");

                    anchorDetail.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener() {

                        public void onEvent(Event event) throws Exception {
                            Messagebox.show(row.get("status").toString() + " " + header.get("angkatan").toString());
                        }
                    });
                    listcell.appendChild(anchorDetail);
                    item.appendChild(listcell);
                }
                listboxMahasiswa.appendChild(item);
            }
            listboxMahasiswa.appendChild(listhead);
        } else {
            try {
                Messagebox.show("Data Tidak Ditemukan !", "Error", Messagebox.OK, Messagebox.ERROR);
            } catch (InterruptedException ex) {
                Logger.getLogger(StatistikMahasiswaWnd.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cmbDataProdiOnSelect() {
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
        this.loadDataToListbox();
    }
}
