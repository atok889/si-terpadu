/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistikmahasiswa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private Listbox listboxDetailMahasiswa;
    private String tahunAkademik = "2000";
    private String semester = "2";
    private List<StatistikMahasiswa> statistikMahasiswas;

    public StatistikMahasiswaWnd() {
        statistikMahasiswaDAO = new StatistikMahasiswaDAOImpl();
    }

    public void onCreate() throws Exception {
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        listboxDetailMahasiswa = (Listbox) this.getFellow("listboxDetailMahasiswa");
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

        Comboitem item = new Comboitem("--TOTAL--");
        cmbboxProdi.appendChild(item);
        cmbboxProdi.setSelectedItem(item);
    }

    private void loadDataToListbox() {
        listboxMahasiswa.getChildren().clear();
        if (statistikMahasiswaDAO.isTabelrgXYZZyyyySAda(kodeProdi, tahunAkademik, semester)) {
            this.generateDataMahasiswa();
            Listhead listhead = new Listhead();
            listhead.setSizable(true);
            Listheader listheaderProdi = new Listheader("", "", "150px");
            listhead.appendChild(listheaderProdi);
            Listheader listheaderTotal = new Listheader("Total", "", "60px");
            listheaderTotal.setAlign("center");
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
                    //anchorDetail.setStyle("color : red");

                    anchorDetail.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener() {

                        public void onEvent(Event event) throws Exception {
                            List<Map> detail = new ArrayList<Map>();
                            String database = null;
                            String currentStatus = row.get("status").toString();
                            if (currentStatus.equalsIgnoreCase("Mahasiswa Registrasi")) {
                                database = "db_" + kodeProdi + ".rg" + kodeProdi + tahunAkademik + semester;
                                detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, header.get("angkatan").toString(), database, "1");
                            } else if (currentStatus.equalsIgnoreCase("Mahasiswa Tidak Aktif")) {
                                database = "db_" + kodeProdi + ".rg" + kodeProdi + tahunAkademik + semester;
                                detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, header.get("angkatan").toString(), database, "2");
                            } else if (currentStatus.equalsIgnoreCase("Mahasiswa Cuti")) {
                                database = "db_" + kodeProdi + ".rg" + kodeProdi + tahunAkademik + semester;
                                detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, header.get("angkatan").toString(), database, "3");
                            } else if (currentStatus.equalsIgnoreCase("Mahasiswa DO")) {
                                database = "db_" + kodeProdi + ".do" + kodeProdi;
                                detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, header.get("angkatan").toString(), database, null);
                            } else if (currentStatus.equalsIgnoreCase("Mahasiswa Lulus")) {
                                database = "db_" + kodeProdi + ".ll" + kodeProdi;
                                detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, header.get("angkatan").toString(), database, null);
                            }

                            int no = 1;
                            listboxDetailMahasiswa.getChildren().clear();
                            Listhead listhead = new Listhead();
                            listhead.appendChild(new Listheader("NO", "", "30px"));
                            listhead.appendChild(new Listheader("Nama", "", "400px"));
                            listhead.appendChild(new Listheader("Angkatan", "", "150px"));
                            listhead.appendChild(new Listheader("Program Studi", "", "300px"));
                            listboxDetailMahasiswa.appendChild(listhead);
                            for (Map m : detail) {
                                Listitem item = new Listitem();
                                item.appendChild(new Listcell(no + ""));
                                item.appendChild(new Listcell(m.get("nama_mhs").toString()));
                                item.appendChild(new Listcell(header.get("angkatan").toString()));
                                item.appendChild(new Listcell(m.get("nama_prg").toString()));
                                listboxDetailMahasiswa.appendChild(item);
                                no++;
                            }
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

    private void loadAllDataToListbox() {
        listboxMahasiswa.getChildren().clear();
        Listhead listhead = new Listhead();
        listhead.setSizable(true);
        Listheader listheaderProdi = new Listheader("", "", "150px");
        listhead.appendChild(listheaderProdi);
        Listheader listheaderTotal = new Listheader("Total", "", "60px");
        listheaderTotal.setAlign("center");
        listhead.appendChild(listheaderTotal);

        final List<Map> tahun = statistikMahasiswaDAO.getAngkatan();
        Listheader listheader = new Listheader();

        List<Map> data = statistikMahasiswaDAO.getStatistikMahasiswa();
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
                //anchorDetail.setStyle("color : red");

                anchorDetail.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener() {

                    public void onEvent(Event event) throws Exception {
                        List<Map> detail = new ArrayList<Map>();
                        String database = null;
                        String currentStatus = row.get("status").toString();
                        if (currentStatus.equalsIgnoreCase("Mahasiswa Registrasi")) {
                            database = "db_" + kodeProdi + ".rg" + kodeProdi + tahunAkademik + semester;
                            detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, header.get("angkatan").toString(), database, "1");
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Tidak Aktif")) {
                            database = "db_" + kodeProdi + ".rg" + kodeProdi + tahunAkademik + semester;
                            detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, header.get("angkatan").toString(), database, "2");
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Cuti")) {
                            database = "db_" + kodeProdi + ".rg" + kodeProdi + tahunAkademik + semester;
                            detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, header.get("angkatan").toString(), database, "3");
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa DO")) {
                            database = "db_" + kodeProdi + ".do" + kodeProdi;
                            detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, header.get("angkatan").toString(), database, null);
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Lulus")) {
                            database = "db_" + kodeProdi + ".ll" + kodeProdi;
                            detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, header.get("angkatan").toString(), database, null);
                        }

                        int no = 1;
                        listboxDetailMahasiswa.getChildren().clear();
                        for (Map m : detail) {
                            Listitem item = new Listitem();
                            item.appendChild(new Listcell(no + ""));
                            item.appendChild(new Listcell(m.get("nama_mhs").toString()));
                            item.appendChild(new Listcell(header.get("angkatan").toString()));
                            item.appendChild(new Listcell(m.get("nama_prg").toString()));
                            listboxDetailMahasiswa.appendChild(item);
                            no++;
                        }
                    }
                });
                listcell.appendChild(anchorDetail);
                item.appendChild(listcell);
            }
            listboxMahasiswa.appendChild(item);
        }
        listboxMahasiswa.appendChild(listhead);
    }

    private void generateDataMahasiswa() {
        statistikMahasiswaDAO.createTabelTempo(kodeProdi);
        statistikMahasiswaDAO.getMhsCuti(kodeProdi, tahunAkademik, semester);
        statistikMahasiswaDAO.getMhsReg(kodeProdi, tahunAkademik, semester);
        statistikMahasiswaDAO.getMhsDO(kodeProdi);
        statistikMahasiswaDAO.getMhsLulus(kodeProdi);
        statistikMahasiswaDAO.getMhsTidakReg(kodeProdi, tahunAkademik, semester);
    }

    private void generateAllDataMahasiswa() {
        List<Map> prodis = statistikMahasiswaDAO.getProdi();
        statistikMahasiswaDAO.createTabelStatistik();
        for (Map prodi : prodis) {
            if (statistikMahasiswaDAO.isTabelrgXYZZyyyySAda(prodi.get("Kd_prg").toString(), tahunAkademik, semester)) {
                statistikMahasiswaDAO.createTabelTempo(prodi.get("Kd_prg").toString());
                statistikMahasiswaDAO.getMhsDO(prodi.get("Kd_prg").toString());
                statistikMahasiswaDAO.getMhsCuti(prodi.get("Kd_prg").toString(), tahunAkademik, semester);
                statistikMahasiswaDAO.getMhsReg(prodi.get("Kd_prg").toString(), tahunAkademik, semester);
                statistikMahasiswaDAO.getMhsLulus(prodi.get("Kd_prg").toString());
                statistikMahasiswaDAO.getMhsTidakReg(prodi.get("Kd_prg").toString(), tahunAkademik, semester);
            }

        }
        statistikMahasiswaDAO.createTabelAllStatistik(statistikMahasiswaDAO.getAngkatan());
    }

    public void cmbDataProdiOnSelect() {
        if (cmbboxProdi.getSelectedItem().getLabel().equalsIgnoreCase("--TOTAL--")) {
            System.out.println("OK");
            this.loadAllDataToListbox();
        } else {
            kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
            this.loadDataToListbox();
        }
    }
}
