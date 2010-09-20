/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswalulusdanbelumlulus;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

/**
 *
 * @author kris
 */
public class JumlahMahasiswaLulusDanBelumLulusWnd extends ClassApplicationModule {

    private JumlahMahasiswaLulusDanBelumLulusDAO jumlahMahasiswaLulusDanBelumLulusDAO;
    private Combobox cmbboxProdi;
    private String kodeProdi;
    private Listbox listboxMahasiswa;
    private String tahunAkademik = "2000";
    private String semester = "2";

    public JumlahMahasiswaLulusDanBelumLulusWnd() {
        jumlahMahasiswaLulusDanBelumLulusDAO = new JumlahMahasiswaLulusDanBelumLulusDAOImpl();
    }

    public void onCreate() throws Exception {
        cmbboxProdi = (Combobox) this.getFellow("cmbboxProdi");
        listboxMahasiswa = (Listbox) this.getFellow("listboxMahasiswa");
        this.loadDataProdiToCombo();

    }

    private void loadDataProdiToCombo() {
        for (Map map : jumlahMahasiswaLulusDanBelumLulusDAO.getProdi()) {
            Comboitem item = new Comboitem();
            item.setValue(map.get("Kd_prg").toString());
            item.setLabel(map.get("Nama_prg").toString());
            cmbboxProdi.appendChild(item);
        }
    }

    private void loadDataToListbox() {
        this.listboxMahasiswa.getChildren().clear();
        Listhead listhead = new Listhead();
        Listheader listheaderAngkatan = new Listheader();
        listheaderAngkatan.setLabel("Tahun Angkatan");
        listheaderAngkatan.setAlign("center");
        listheaderAngkatan.setWidth("130px");
        listhead.appendChild(listheaderAngkatan);
        Listheader listheaderJumlahMahasiswa = new Listheader();
        listheaderJumlahMahasiswa.setLabel("Jumlah Mahasiswa");
        listheaderJumlahMahasiswa.setAlign("center");
        listheaderJumlahMahasiswa.setWidth("130px");
        listhead.appendChild(listheaderJumlahMahasiswa);

        Listheader listheaderMhsLulus = new Listheader();
        listheaderMhsLulus.setLabel("Jumlah Lulus");
        listheaderMhsLulus.setAlign("center");
        listheaderMhsLulus.setWidth("130px");
        listhead.appendChild(listheaderMhsLulus);
        Listheader listheaderPersenMhsLulus = new Listheader();
       
        listhead.appendChild(listheaderPersenMhsLulus);

        Listheader listheaderMhsBelumLulus = new Listheader();
        listheaderMhsBelumLulus.setLabel("Jumlah Belum Lulus");
        listheaderMhsBelumLulus.setAlign("center");
        listheaderMhsBelumLulus.setWidth("130px");
        listhead.appendChild(listheaderMhsBelumLulus);
        Listheader listheaderPersenMhsBelumLulus = new Listheader();
    
        listhead.appendChild(listheaderPersenMhsBelumLulus);

        List<Map> mahasiswa = jumlahMahasiswaLulusDanBelumLulusDAO.getJumlahMahasiswaLulusDanBelumLulus(kodeProdi);
        DecimalFormat df = new DecimalFormat("# 0.00");
        for (Map data : mahasiswa) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(data.get("angkatan").toString()));
            listitem.appendChild(new Listcell(data.get("jumlah_total").toString()));
            listitem.appendChild(new Listcell(data.get("jumlah_lulus").toString()));
            System.out.println("---------------" + data.get("prosentase_lulus").toString());
            if (!data.get("prosentase_lulus").toString().equals("0.0000")) {
                listitem.appendChild(new Listcell(df.format(Double.parseDouble(data.get("prosentase_lulus").toString()))+"%"));
            } else {
                listitem.appendChild(new Listcell("0"+"%"));
            }
            listitem.appendChild(new Listcell(data.get("jumlah_belum_lulus").toString()));
            if (!data.get("prosentase_belum_lulus").toString().equals("0.0000")) {
                listitem.appendChild(new Listcell(df.format(Double.parseDouble(data.get("prosentase_belum_lulus").toString()))+"%"));
            } else {
                listitem.appendChild(new Listcell("0"+"%"));
            }
            listboxMahasiswa.appendChild(listitem);         
        }
        listboxMahasiswa.appendChild(listhead);
    }

    public void cmbDataProdiOnSelect() {
        kodeProdi = (String) cmbboxProdi.getSelectedItem().getValue();
        System.out.println("Kode Prodi" + kodeProdi);
        this.loadDataToListbox();

    }
}
