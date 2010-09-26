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
        Comboitem item = new Comboitem();
        item.setValue("total");
        item.setLabel("--TOTAL--");
        cmbboxProdi.appendChild(item);

        for (Map map : statistikMahasiswaDAO.getProdi()) {
            Comboitem items = new Comboitem();
            items.setValue(map.get("Kd_prg").toString());
            items.setLabel(map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(items);
        }
        cmbboxProdi.setSelectedIndex(0);
        cmbboxProdi.setSelectedItem(item);

    }

    private void resetListboxDetail() {
        listboxDetailMahasiswa.getChildren().clear();
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

            //Generate header
            Listheader listheader = new Listheader();
            List<Map> data = statistikMahasiswaDAO.getStatistikMahasiswa(kodeProdi);
            for (Map header : tahun) {
                listheader = new Listheader();
                listheader.setAlign("center");
                listheader.setLabel(header.get("angkatan").toString());
                listheader.setWidth("60px");
                listhead.appendChild(listheader);
            }

            //Generate list total dan status
            for (final Map row : data) {
                Listcell listcellTotal = new Listcell();
                Listitem item = new Listitem();
                item.appendChild(new Listcell(row.get("status").toString()));
                Toolbarbutton anchorDetailTotal = new Toolbarbutton();
                anchorDetailTotal.setLabel(row.get("total").toString());
                listcellTotal.appendChild(anchorDetailTotal);
                item.appendChild(listcellTotal);

                anchorDetailTotal.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener() {

                    public void onEvent(Event arg0) throws Exception {
                        List<Map> detail = new ArrayList<Map>();
                        String database = null;
                        String currentStatus = row.get("status").toString();

                        if (currentStatus.equalsIgnoreCase("Mahasiswa Registrasi")) {
                            database = "db_" + kodeProdi + ".rg" + kodeProdi + tahunAkademik + semester;
                            detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, null, database, "1");
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Tidak Aktif")) {
                            database = "db_" + kodeProdi + ".rg" + kodeProdi + tahunAkademik + semester;
                            detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, null, database, "2");
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Cuti")) {
                            database = "db_" + kodeProdi + ".rg" + kodeProdi + tahunAkademik + semester;
                            detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, null, database, "3");
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa DO")) {
                            database = "db_" + kodeProdi + ".do" + kodeProdi;
                            detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, null, database, null);
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Lulus")) {
                            database = "db_" + kodeProdi + ".ll" + kodeProdi;
                            detail = statistikMahasiswaDAO.getDetailStatistikMahasiswa(kodeProdi, null, database, null);
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
                            item.appendChild(new Listcell(m.get("angkatan").toString()));
                            item.appendChild(new Listcell(m.get("nama_prg").toString()));
                            listboxDetailMahasiswa.appendChild(item);
                            no++;
                        }
                    }
                });


                //Generate list tahun ke ?
                for (final Map header : tahun) {
                    Listcell listcellDetail = new Listcell();
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
                    listcellDetail.appendChild(anchorDetail);
                    item.appendChild(listcellDetail);
                }
                listboxMahasiswa.appendChild(item);
            }
            listboxMahasiswa.appendChild(listhead);
        } else {
            try {
                Messagebox.show("Data tidak ditemukan !", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
                this.listboxDetailMahasiswa.setVisible(false);
                this.listboxMahasiswa.setVisible(false);
            } catch (InterruptedException ex) {
                Logger.getLogger(StatistikMahasiswaWnd.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void loadAllDataToListbox() {
        this.generateAllDataMahasiswa();
        listboxMahasiswa.getChildren().clear();       
        Listhead listhead = new Listhead();
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
            Listcell listcellTotal = new Listcell();
            item.appendChild(new Listcell(row.get("status").toString()));
            Toolbarbutton anchorDetailTotal = new Toolbarbutton();
            anchorDetailTotal.setLabel(row.get("total").toString());
            listcellTotal.appendChild(anchorDetailTotal);
            item.appendChild(listcellTotal);

            anchorDetailTotal.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener() {

                public void onEvent(Event arg0) throws Exception {
                    List<Map> detail = new ArrayList<Map>();
                    String database = null;
                    String currentStatus = row.get("status").toString();

                    if (currentStatus.equalsIgnoreCase("Mahasiswa Registrasi")) {
                        for (Map prodi : statistikMahasiswaDAO.getProdi()) {
                            if (statistikMahasiswaDAO.isTabelrgXYZZyyyySAda(prodi.get("Kd_prg").toString(), tahunAkademik, semester)) {
                                database = "db_" + prodi.get("Kd_prg").toString() + ".rg" + prodi.get("Kd_prg").toString() + tahunAkademik + semester;
                                List<Map> temp = statistikMahasiswaDAO.getDetailStatistikMahasiswa(prodi.get("Kd_prg").toString(), null, database, "1");
                                detail.addAll(temp);
                            }
                        }
                    } else if (currentStatus.equalsIgnoreCase("Mahasiswa Tidak Aktif")) {
                        for (Map prodi : statistikMahasiswaDAO.getProdi()) {
                            if (statistikMahasiswaDAO.isTabelrgXYZZyyyySAda(prodi.get("Kd_prg").toString(), tahunAkademik, semester)) {
                                database = "db_" + prodi.get("Kd_prg").toString() + ".rg" + prodi.get("Kd_prg").toString() + tahunAkademik + semester;
                                List<Map> temp = statistikMahasiswaDAO.getDetailStatistikMahasiswa(prodi.get("Kd_prg").toString(), null, database, "2");
                                detail.addAll(temp);
                            }
                        }
                    } else if (currentStatus.equalsIgnoreCase("Mahasiswa Cuti")) {
                        for (Map prodi : statistikMahasiswaDAO.getProdi()) {
                            if (statistikMahasiswaDAO.isTabelrgXYZZyyyySAda(prodi.get("Kd_prg").toString(), tahunAkademik, semester)) {
                                database = "db_" + prodi.get("Kd_prg").toString() + ".rg" + prodi.get("Kd_prg").toString() + tahunAkademik + semester;
                                List<Map> temp = statistikMahasiswaDAO.getDetailStatistikMahasiswa(prodi.get("Kd_prg").toString(), null, database, "3");
                                detail.addAll(temp);
                            }
                        }

                    } else if (currentStatus.equalsIgnoreCase("Mahasiswa DO")) {
                        for (Map prodi : statistikMahasiswaDAO.getProdi()) {
                            if (statistikMahasiswaDAO.isTabelLulusAda(prodi.get("Kd_prg").toString())) {
                                database = "db_" + prodi.get("Kd_prg").toString() + ".do" + prodi.get("Kd_prg").toString();
                                List<Map> temp = statistikMahasiswaDAO.getDetailStatistikMahasiswa(prodi.get("Kd_prg").toString(), null, database, null);
                                detail.addAll(temp);
                            }
                        }
                    } else if (currentStatus.equalsIgnoreCase("Mahasiswa Lulus")) {
                        for (Map prodi : statistikMahasiswaDAO.getProdi()) {
                            if (statistikMahasiswaDAO.isTabelLulusAda(prodi.get("Kd_prg").toString())) {
                                database = "db_" + prodi.get("Kd_prg").toString() + ".ll" + prodi.get("Kd_prg").toString();
                                List<Map> temp = statistikMahasiswaDAO.getDetailStatistikMahasiswa(prodi.get("Kd_prg").toString(), null, database, null);
                                detail.addAll(temp);
                            }
                        }
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
                        item.appendChild(new Listcell(m.get("angkatan").toString()));
                        item.appendChild(new Listcell(m.get("nama_prg").toString()));
                        listboxDetailMahasiswa.appendChild(item);
                        no++;
                    }
                }
            });

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
                            for (Map prodi : statistikMahasiswaDAO.getProdi()) {
                                if (statistikMahasiswaDAO.isTabelrgXYZZyyyySAda(prodi.get("Kd_prg").toString(), tahunAkademik, semester)) {
                                    database = "db_" + prodi.get("Kd_prg").toString() + ".rg" + prodi.get("Kd_prg").toString() + tahunAkademik + semester;
                                    List<Map> temp = statistikMahasiswaDAO.getDetailStatistikMahasiswa(prodi.get("Kd_prg").toString(), header.get("angkatan").toString(), database, "1");
                                    detail.addAll(temp);
                                }
                            }
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Tidak Aktif")) {
                            for (Map prodi : statistikMahasiswaDAO.getProdi()) {
                                if (statistikMahasiswaDAO.isTabelrgXYZZyyyySAda(prodi.get("Kd_prg").toString(), tahunAkademik, semester)) {
                                    database = "db_" + prodi.get("Kd_prg").toString() + ".rg" + prodi.get("Kd_prg").toString() + tahunAkademik + semester;
                                    List<Map> temp = statistikMahasiswaDAO.getDetailStatistikMahasiswa(prodi.get("Kd_prg").toString(), header.get("angkatan").toString(), database, "2");
                                    detail.addAll(temp);
                                }
                            }
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Cuti")) {
                            for (Map prodi : statistikMahasiswaDAO.getProdi()) {
                                if (statistikMahasiswaDAO.isTabelrgXYZZyyyySAda(prodi.get("Kd_prg").toString(), tahunAkademik, semester)) {
                                    database = "db_" + prodi.get("Kd_prg").toString() + ".rg" + prodi.get("Kd_prg").toString() + tahunAkademik + semester;
                                    List<Map> temp = statistikMahasiswaDAO.getDetailStatistikMahasiswa(prodi.get("Kd_prg").toString(), header.get("angkatan").toString(), database, "3");
                                    detail.addAll(temp);
                                }
                            }
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa DO")) {
                            for (Map prodi : statistikMahasiswaDAO.getProdi()) {
                                if (statistikMahasiswaDAO.isTabelDOAda(prodi.get("Kd_prg").toString())) {
                                    database = "db_" + prodi.get("Kd_prg").toString() + ".do" + prodi.get("Kd_prg").toString();
                                    List<Map> temp = statistikMahasiswaDAO.getDetailStatistikMahasiswa(prodi.get("Kd_prg").toString(), header.get("angkatan").toString(), database, null);
                                    detail.addAll(temp);
                                }
                            }
                        } else if (currentStatus.equalsIgnoreCase("Mahasiswa Lulus")) {
                            for (Map prodi : statistikMahasiswaDAO.getProdi()) {
                                if (statistikMahasiswaDAO.isTabelLulusAda(prodi.get("Kd_prg").toString())) {
                                    database = "db_" + prodi.get("Kd_prg").toString() + ".ll" + prodi.get("Kd_prg").toString();
                                    List<Map> temp = statistikMahasiswaDAO.getDetailStatistikMahasiswa(prodi.get("Kd_prg").toString(), header.get("angkatan").toString(), database, null);
                                    detail.addAll(temp);
                                }
                            }
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
    }

    private void generateDataMahasiswa() {
        statistikMahasiswaDAO.createTabelStatistik();
        statistikMahasiswaDAO.createTabelTempo(kodeProdi);
        statistikMahasiswaDAO.getMhsCuti(kodeProdi, tahunAkademik, semester);
        statistikMahasiswaDAO.getMhsReg(kodeProdi, tahunAkademik, semester);
        statistikMahasiswaDAO.getMhsDO(kodeProdi);
        statistikMahasiswaDAO.getMhsLulus(kodeProdi);
        statistikMahasiswaDAO.getMhsTidakReg(kodeProdi, tahunAkademik, semester);
    }

    private void generateAllDataMahasiswa() {
        statistikMahasiswaDAO.createTabelStatistik();
        List<Map> prodis = statistikMahasiswaDAO.getProdi();
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
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();

    }

    public void btnShowOnClick() {
        this.listboxMahasiswa.setVisible(true);
        this.listboxDetailMahasiswa.setVisible(true);
        if (cmbboxProdi.getSelectedItem().getValue().equals("total")) {
            this.loadAllDataToListbox();
        } else {
            this.loadDataToListbox();
        }
        this.resetListboxDetail();
    }
}
