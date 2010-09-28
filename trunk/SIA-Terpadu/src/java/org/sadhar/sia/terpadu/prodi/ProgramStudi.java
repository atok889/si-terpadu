/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.prodi;

/**
 *
 * @author Hendro Steven
 */
public class ProgramStudi {

    private String kode;
    private String nama;

    public ProgramStudi(){}

    public ProgramStudi(String kode, String nama) {
        this.kode = kode;
        this.nama = nama;
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
}
