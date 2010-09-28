/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataips;

import java.util.List;
import java.util.Map;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
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
        cmbboxProdi.setReadonly(true);
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
            comboitem.setLabel(data.get("Kd_prg").toString()+" "+data.get("Nama_prg").toString());
            cmbboxProdi.appendChild(comboitem);
        }
        cmbboxProdi.setSelectedIndex(0);
    }

    private void loadDataToListbox() {
        listboxMahasiswa.getChildren().clear();

        Listhead listhead = new Listhead();
        Auxhead auxhead = new Auxhead();

        Listheader listheaderNo = new Listheader();
        listheaderNo.setWidth("40px");
        listhead.appendChild(listheaderNo);
        Auxheader auxheaderNo = new Auxheader();
        auxheaderNo.setLabel("No");
        //auxheaderNo.setRowspan(2);
        auxheaderNo.setAlign("center");
        auxhead.appendChild(auxheaderNo);

        Listheader listheaderFakultas = new Listheader();
        listheaderFakultas.setWidth("250px");
        listhead.appendChild(listheaderFakultas);
        Auxheader auxheaderFakultas = new Auxheader();
        auxheaderFakultas.setLabel("Fakultas");
        //auxheaderFakultas.setRowspan(2);
        auxheaderFakultas.setAlign("center");
        auxhead.appendChild(auxheaderFakultas);

        Listheader listheaderAngkatan = new Listheader();
        listheaderAngkatan.setWidth("120px");
        listhead.appendChild(listheaderAngkatan);
        Auxheader auxheaderAngkatan = new Auxheader();
        auxheaderAngkatan.setLabel("Tahun Angkatan");
        auxheaderAngkatan.setWidth("120px");
        //auxheaderAngkatan.setRowspan(2);
        auxhead.appendChild(auxheaderAngkatan);

        Auxheader auxheaderIPS = new Auxheader();
        auxheaderIPS.setLabel("IPS");
        auxheaderIPS.setColspan(100);
        auxheaderIPS.setAlign("center");
        auxhead.appendChild(auxheaderIPS);
        listboxMahasiswa.appendChild(auxhead);

        //Export data to dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (kodeProdi == null && intboxTahunAngkatan.getValue() == null) {
            System.out.println("dijalankan");
            for (Map data : rerataIpsDAO.getRerataIps()) {
                dataset.addValue(Double.parseDouble(data.get("ips").toString().substring(0, 4)), data.get("tahun").toString() + "-" + data.get("semester").toString(), data.get("Nama_fak").toString());
            }
        } else {
            for (Map data : rerataIpsDAO.getRerataIps(kodeProdi, intboxTahunAngkatan.getValue().toString())) {
                dataset.addValue(Double.parseDouble(data.get("ips").toString().substring(0, 4)), data.get("tahun").toString() + "-" + data.get("semester").toString(), data.get("Nama_fak").toString());
            }
        }
        int no = 1;

        for (Object row : dataset.getRowKeys()) {
            Listheader listheader = new Listheader();
            listheader.setWidth("70px");
            listheader.setAlign("right");
            listheader.setLabel(row.toString());
            listhead.appendChild(listheader);
        }
        for (Object column : dataset.getColumnKeys()) {
            Listitem listitem = new Listitem();
            listitem.appendChild(new Listcell(no + ""));
            listitem.appendChild(new Listcell(column.toString()));
            listitem.appendChild(new Listcell(""));
            for (Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                Double data = null;
                if (number != null) {
                    data = number.doubleValue();
                } else {
                    data = 0d;
                }
                listitem.appendChild(new Listcell(data + ""));
                listboxMahasiswa.appendChild(listitem);
            }
            no++;
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
