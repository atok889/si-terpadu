/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.pangkatdosen;

/**
 *
 * @author jasoet
 */
public class PangkatDosen {
    private String npp;
    private String nama;
    private String tanggalSK;
    private String kodeUnitKerja;
    private String namaUnitKerja;
    private String kodePangkat;
    private int umur;
    private String namaPangkat;

    public String getKodePangkat() {
        return kodePangkat;
    }

    public void setKodePangkat(String kodePangkat) {
        this.kodePangkat = kodePangkat;
    }

    public String getKodeUnitKerja() {
        return kodeUnitKerja;
    }

    public void setKodeUnitKerja(String kodeUnitKerja) {
        this.kodeUnitKerja = kodeUnitKerja;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNamaPangkat() {
        return namaPangkat;
    }

    public void setNamaPangkat(String namaPangkat) {
        this.namaPangkat = namaPangkat;
    }

    public String getNamaUnitKerja() {
        return namaUnitKerja;
    }

    public void setNamaUnitKerja(String namaUnitKerja) {
        this.namaUnitKerja = namaUnitKerja;
    }

    public String getNpp() {
        return npp;
    }

    public void setNpp(String npp) {
        this.npp = npp;
    }

    public String getTanggalSK() {
        return tanggalSK;
    }

    public void setTanggalSK(String tanggalSK) {
        this.tanggalSK = tanggalSK;
    }

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    
}
