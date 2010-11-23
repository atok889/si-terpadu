/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.rekapAll;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.sadhar.sia.common.ClassSession;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Dian
 */
public class RekapAllWnd extends Window {

    private RekapAllDAO dao;
    //
    private Window windowDaftarUnit;
    private Textbox textKodeUnit;
    private Textbox textNamaUnit;
    private Label lblTahunAnggaran;
    private Button btnUnit;
    private List lstUnit;
    //==========================================================================
    // windowDaftarUnit
    //==========================================================================
    private Listbox lstboxDaftarUnit;
    private Button btnTutupWindowDaftarUnit;
    private List lstDaftarUnit;
    private String xTahunAnggaran = "";
    private Connection conn;
    //=========//
    private Button btnCetak;
    private Button btnTutupCetak;
    //
    private Jasperreport JasperCetak;
    //
    private Groupbox grouptutup;

    public RekapAllWnd() {
        dao = new RekapAllDAOImpl();
    }

    public void onCreate() {
        windowDaftarUnit = (Window) getFellow("windowDaftarUnit");
        xTahunAnggaran = ClassSession.getInstance().getThnAnggaran();
        //
        textKodeUnit = (Textbox) getFellow("textKodeUnit");
        textNamaUnit = (Textbox) getFellow("textNamaUnit");
        //
        lblTahunAnggaran = (Label) getFellow("lblTahunAnggaran");
        //
        //
        btnUnit = (Button) getFellow("btnUnit");
        //
        grouptutup = (Groupbox) getFellow("grouptutup");
        //
        lstboxDaftarUnit = (Listbox) windowDaftarUnit.getFellow("lstboxDaftarUnit");
        btnTutupWindowDaftarUnit = (Button) windowDaftarUnit.getFellow("btnTutupWindowDaftarUnit");
        JasperCetak = (Jasperreport) getFellow("JasperCetak");
        lblTahunAnggaran.setValue(xTahunAnggaran);

        //filter untuk hak akses
        String kd = ClassSession.getInstance().getKdUnitKerja().toString();
        String namaunit = ClassSession.getInstance().getNmUnitKerja().toString();
        if (kd.compareTo("08000000") == 0) {
        } else {
            textKodeUnit.setValue(kd);
            textNamaUnit.setValue(namaunit);
            textNamaUnit.setReadonly(true);
            btnUnit.setDisabled(true);
        }
    }

    /*
     * @method untuk menampilkan daftar unit
     */
    public void LoadUnit() throws InterruptedException {
        lstboxDaftarUnit.getItems().clear();
        String kata = textNamaUnit.getValue();
        lstDaftarUnit = dao.getDaftarUnit(kata);
        int i = 1;
        int a = 0;
        int p = 0;
        if (!lstDaftarUnit.isEmpty()) {
            for (Iterator it = lstDaftarUnit.iterator(); it.hasNext();) {
                final RekapAll obj = (RekapAll) it.next();
                Listitem listRow = new Listitem();
                listRow.setValue(obj);
                listRow.appendChild(new Listcell("" + (i++)));
                listRow.appendChild(new Listcell(obj.getNamaUnit()));
                lstboxDaftarUnit.appendChild(listRow);
            }
            windowDaftarUnit.doModal();
        } else {
            Messagebox.show("Data Unit Tidak Ada", "INFORMASI", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    /*
     * @Method jika popup daftar unit dipilih
     */
    public void DaftarUnitOnSelect() {
        RekapAll obj = (RekapAll) lstDaftarUnit.get(this.lstboxDaftarUnit.getSelectedIndex());
        textKodeUnit.setValue(obj.getKodeUnit());
        textNamaUnit.setValue(obj.getNamaUnit());
        windowDaftarUnit.setVisible(false);
    }

    /*
     * @Method untuk menutup WindowDaftarUnit
     */
    public void Close() throws InterruptedException {
        windowDaftarUnit.setVisible(false);
    }

    /*
     * @Method untuk menghapus kodeunit
     */
    public void UnitOnChange() throws InterruptedException {
        textKodeUnit.setValue("");
    }

    /*
     * Method untuk menampilkan daftar Pengajuan Pencairan Dana dari tabel ppmk.pengajuanPengambilan di window1
     */
    public class lpjCustomDataSource implements JRDataSource {

        /**
         *
         */
        private Object[][] data;
        private int index = -1;

        /**
         *
         */
        public lpjCustomDataSource(String kdUnker, String thn) throws Exception {

            data = dao.DataLpjToCetak(kdUnker, thn);
        }

        /**
         *
         */
        public boolean next() throws JRException {
            index++;

            return (index < data.length);
        }

        /**
         *
         */
        public Object getFieldValue(JRField field) throws JRException {
            Object value = null;

            String fieldName = field.getName();
            LinkedList xLs;
            try {
                xLs = dao.getFieldLpj();
                for (int i = 0; i < xLs.size(); i++) {
                    if (xLs.get(i).equals(fieldName)) {
                        value = data[index][i];
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(RekapAllWnd.class.getName()).log(Level.SEVERE, null, ex);
            }
            return value;
        }
    }

    /*
     * @Method untuk mencetak
     */
    public void Cetak() throws SQLException, Exception {
        String unit = textNamaUnit.getValue();
        if (unit.compareTo("") == 0) {
            Messagebox.show("Pilih nama unit terlebih dahulu!", "PESAN", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            JasperCetak.setVisible(true);
            grouptutup.setVisible(true);
            Map parameters = new HashMap();
            parameters.put("thnAnggaran", lblTahunAnggaran.getValue());
            parameters.put("nmUnker", textNamaUnit.getValue());
            parameters.put("kdUnker", textKodeUnit.getValue());
            JasperCetak.setParameters(parameters);
            Listbox format = (Listbox) getFellow("format");
            JasperCetak.setType((String) format.getSelectedItem().getValue());
            JasperCetak.setSrc("reports/rekapAll/rpt_all.jasper");
            JasperCetak.setDatasource(new lpjCustomDataSource(textKodeUnit.getValue(), lblTahunAnggaran.getValue()));
        }
    }

    /*
     * @Method untuk menutup windowCetak
     */
    public void CloseCetak() throws InterruptedException {
        JasperCetak.setVisible(false);
        grouptutup.setVisible(false);
    }
}
