/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.datapegawai.jumlahpegawai;

import java.util.Date;

/**
 *
 * @author Hendro Steven
 */
public class JumlahPegawaiGolongan {

    private String kode;
    private Date tanggal_mulai;
    private Date tanggal_selesai;
    private String golongan;

    public JumlahPegawaiGolongan(){}

    /**
     * @return the kode
     */
    public String getKode() {
        return kode;
    }

    /**
     * @param kode the kode to set
     */
    public void setKode(String kode) {
        this.kode = kode;
    }

    /**
     * @return the tanggal_mulai
     */
    public Date getTanggal_mulai() {
        return tanggal_mulai;
    }

    /**
     * @param tanggal_mulai the tanggal_mulai to set
     */
    public void setTanggal_mulai(Date tanggal_mulai) {
        this.tanggal_mulai = tanggal_mulai;
    }

    /**
     * @return the tanggal_selesai
     */
    public Date getTanggal_selesai() {
        return tanggal_selesai;
    }

    /**
     * @param tanggal_selesai the tanggal_selesai to set
     */
    public void setTanggal_selesai(Date tanggal_selesai) {
        this.tanggal_selesai = tanggal_selesai;
    }

    /**
     * @return the golongan
     */
    public String getGolongan() {
        return golongan;
    }

    /**
     * @param golongan the golongan to set
     */
    public void setGolongan(String golongan) {
        this.golongan = golongan;
    }

    
}
