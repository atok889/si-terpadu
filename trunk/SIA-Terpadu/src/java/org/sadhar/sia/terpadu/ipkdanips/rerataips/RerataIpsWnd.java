/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataips;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

/**
 *
 * @author kris
 */
public class RerataIpsWnd extends ClassApplicationModule {

    private RerataIpsDAO rerataIpsDAO;
    private Listbox listboxMahasiswa;
    private Combobox cmbboxProdi;
    private Intbox intboxTahunAngkatan;
    private String kodeProdi;
    private String tahunAngkatan = "1996";

    public RerataIpsWnd() {
        rerataIpsDAO = new RerataIpsDAOImpl();
    }

    public void onCreate() throws Exception {
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        intboxTahunAngkatan = (Intbox) this.getFellow("intboxTahunAngkatan");
        this.loadDataToComboboxProdi();
    }

    public void loadDataToComboboxProdi() {
        Comboitem item = new Comboitem();
        item.setValue("all");
        item.setLabel("--Pilih Fakultas/Prodi");
        cmbboxProdi.appendChild(item);

        for (Map data : this.rerataIpsDAO.getProdi()) {
            Comboitem comboitem = new Comboitem();
            comboitem.setValue(data.get("Kd_prg"));
            comboitem.setLabel(data.get("Nama_prg").toString());
            cmbboxProdi.appendChild(comboitem);
        }
        cmbboxProdi.setSelectedIndex(0);
    }

    private void loadDataToListbox() {
        listboxMahasiswa.getChildren().clear();
        Listhead listhead = new Listhead();

        Listheader listheaderNo = new Listheader();
        listheaderNo.setLabel("No");
        listheaderNo.setWidth("50px");
        listhead.appendChild(listheaderNo);

        Listheader listheaderFakultas = new Listheader();
        listheaderFakultas.setLabel("Fakultas");
        listheaderFakultas.setWidth("250px");
        listhead.appendChild(listheaderFakultas);

        Listheader listheaderAngkatan = new Listheader();
        listheaderAngkatan.setLabel("Tahun Angkatan");
        listheaderAngkatan.setAlign("center");
        listheaderAngkatan.setWidth("100px");
        listhead.appendChild(listheaderAngkatan);

        Listitem listitem = new Listitem();
        listitem.appendChild(new Listcell("1"));
        listitem.appendChild(new Listcell("Belum jadi"));
        listitem.appendChild(new Listcell(tahunAngkatan));      
        List<Map> datas = rerataIpsDAO.getRerataIps(kodeProdi, tahunAngkatan);

        for (Map data : datas) {
            Listheader listheader = new Listheader();
            listheader.setWidth("100px");
            listheader.setAlign("right");
            listheader.setLabel(data.get("tahun").toString() + "-" + data.get("semester").toString());
            listhead.appendChild(listheader);

            //Isi item          
            listitem.appendChild(new Listcell(data.get("ips").toString().substring(0, 4)));
            listboxMahasiswa.appendChild(listitem);
        }
        listboxMahasiswa.appendChild(listhead);
    }

    public void cmbDataProdiOnSelect() {
        kodeProdi = cmbboxProdi.getSelectedItem().getValue().toString();
    }

    public void btnShowOnClick() {
        listboxMahasiswa.setVisible(true);
        loadDataToListbox();
    }
}
