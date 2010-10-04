/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahwisudawan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jasoet
 */
public class JumlahWisudawan implements Comparable<JumlahWisudawan>{

    private String tahun;
    private String prodi;
    private String tanggal;
    private long jumlah;

    public long getJumlah() {
        return jumlah;
    }

    public void setJumlah(long jumlah) {
        this.jumlah = jumlah;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int compareTo(JumlahWisudawan o) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            Date thisDate = sdf.parse(tanggal);
            Date oDate = sdf.parse(o.getTanggal());

            return thisDate.compareTo(oDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
