/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.daftarmatakuliah;

/**
 *
 * @author Hendro Steven
 */
public class DaftarMatakuliah {
    private int nomor;
    private String kode;
    private String nama;
    private int sks;
    private String dosen;

    public DaftarMatakuliah(){}

    /**
     * @return the nomor
     */
    public int getNomor() {
        return nomor;
    }

    /**
     * @param nomor the nomor to set
     */
    public void setNomor(int nomor) {
        this.nomor = nomor;
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
     * @return the sks
     */
    public int getSks() {
        return sks;
    }

    /**
     * @param sks the sks to set
     */
    public void setSks(int sks) {
        this.sks = sks;
    }

    /**
     * @return the dosen
     */
    public String getDosen() {
        return dosen;
    }

    /**
     * @param dosen the dosen to set
     */
    public void setDosen(String dosen) {
        this.dosen = dosen;
    }

    
}
