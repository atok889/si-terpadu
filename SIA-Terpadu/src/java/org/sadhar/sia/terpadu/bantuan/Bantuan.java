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
    private String jenisSIA;
    private String jenisMenu;
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
    public String getJenisSIA() {
        return jenisSIA;
    }

    /**
     * @param jenis the jenis to set
     */
    public void setJenisSIA(String jenisSIA) {
        this.jenisSIA = jenisSIA;
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

    /**
     * @return the jenisMenu
     */
    public String getJenisMenu() {
        return jenisMenu;
    }

    /**
     * @param jenisMenu the jenisMenu to set
     */
    public void setJenisMenu(String jenisMenu) {
        this.jenisMenu = jenisMenu;
    }
    
}
