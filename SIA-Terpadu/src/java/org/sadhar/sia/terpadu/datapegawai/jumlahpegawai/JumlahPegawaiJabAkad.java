/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.datapegawai.jumlahpegawai;

import java.util.Date;
import org.joda.time.DateTime;

/**
 *
 * @author Hendro Steven
 */
public class JumlahPegawaiJabAkad {

    private String kode;
    private Date tanggal_mulai;
    private Date tanggal_selesai;
    private String jabatan;

    public JumlahPegawaiJabAkad() {
    }

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
     * @return the jabatan
     */
    public String getJabatan() {
        return jabatan;
    }

    /**
     * @param jabatan the jabatan to set
     */
    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }
}
