/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.bantuan;

import java.util.Date;

/**
 *
 * @author Hendro Steven
 */
public class Bantuan {
    
    private String nama;
    private String jenis;
    private String pesan;
    private Date tanggal;

    public Bantuan(){}

    /**
     * @return the nama
     */
    public String getNama() {
        return nama;
    }
    

    /**
     * @param nama the nama to set
     */
    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     * @return the jenis
     */
    public String getJenis() {
        return jenis;
    }

    /**
     * @param jenis the jenis to set
     */
    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    /**
     * @return the pesan
     */
    public String getPesan() {
        return pesan;
    }

    /**
     * @param pesan the pesan to set
     */
    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    /**
     * @return the tanggal
     */
    public Date getTanggal() {
        return tanggal;
    }

    /**
     * @param tanggal the tanggal to set
     */
    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }
    
}
