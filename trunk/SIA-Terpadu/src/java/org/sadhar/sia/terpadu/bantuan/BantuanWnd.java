/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.bantuan;

import java.util.Date;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author Hendro Steven
 */
public class BantuanWnd extends ClassApplicationModule{
    Textbox txtNama;
    //Combobox cmbJenis;
    Textbox txtPesan;
    Textbox txtJenisMenu;

    public void onCreate()throws Exception{
        txtNama = (Textbox)getFellow("txtNama");
       // cmbJenis = (Combobox)getFellow("cmbJenis");
        txtPesan = (Textbox)getFellow("txtPesan");
        txtJenisMenu = (Textbox)getFellow("txtJenisMenu");
        //loadJenis();
        //cmbJenis.setSelectedIndex(0);
    }

//    private void loadJenis(){
//        //cmbJenis.getItems().clear();
//        Comboitem item1 = new Comboitem();
//        item1.setValue("Kritik");
//        item1.setLabel("Kritik");
//        //cmbJenis.appendChild(item1);
//        Comboitem item2 = new Comboitem();
//        item2.setValue("Saran");
//        item2.setLabel("Saran");
//        //cmbJenis.appendChild(item2);
//    }

    public void kirim()throws Exception{
        try{
            if(!txtNama.getValue().isEmpty() && ! txtPesan.getValue().isEmpty() && !txtJenisMenu.getValue().isEmpty()){
                Bantuan bantuan = new Bantuan();
                bantuan.setNama(txtNama.getValue());
                bantuan.setJenisSIA("99");
                bantuan.setJenisMenu(txtJenisMenu.getValue());
                bantuan.setPesan(txtPesan.getValue());
                bantuan.setTanggal(new Date());
                BantuanDAO dao = new BantuanDAOImpl();
                dao.insert(bantuan);
                Messagebox.show("Pesan terkirim...");
                txtNama.setValue("");
                txtPesan.setValue("");
                txtJenisMenu.setValue("");
            }else{
                Messagebox.show("Silahkan input data dengan lengkap");
            }
        }catch(Exception ex){
            ex.printStackTrace();
            Messagebox.show("Gagal menyimpan data");
        }
    }
}
