/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.distribusipendaftarpergelombangpendaftaran;

/**
 *
 * @author Hendro Steven
 */
public class DistribusiPendaftar {
    private String tahun;
    private int gelombang1;
    private int gelombang2;
    private int gelombang3;
    private int jalurKerjaSama;
    private int jalurPrestasi;

    public DistribusiPendaftar(){
        this.gelombang1 = 0;
        this.gelombang2 = 0;
        this.gelombang3 = 0;
        this.jalurKerjaSama = 0;
        this.jalurPrestasi = 0;
    }

    /**
     * @return the tahun
     */
    public String getTahun() {
        return tahun;
    }

    /**
     * @param tahun the tahun to set
     */
    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    /**
     * @return the gelombang1
     */
    public int getGelombang1() {
        return gelombang1;
    }

    /**
     * @param gelombang1 the gelombang1 to set
     */
    public void setGelombang1(int gelombang1) {
        this.gelombang1 = gelombang1;
    }

    /**
     * @return the gelombang2
     */
    public int getGelombang2() {
        return gelombang2;
    }

    /**
     * @param gelombang2 the gelombang2 to set
     */
    public void setGelombang2(int gelombang2) {
        this.gelombang2 = gelombang2;
    }

    /**
     * @return the gelombang3
     */
    public int getGelombang3() {
        return gelombang3;
    }

    /**
     * @param gelombang3 the gelombang3 to set
     */
    public void setGelombang3(int gelombang3) {
        this.gelombang3 = gelombang3;
    }

    /**
     * @return the jalurKerjaSama
     */
    public int getJalurKerjaSama() {
        return jalurKerjaSama;
    }

    /**
     * @param jalurKerjaSama the jalurKerjaSama to set
     */
    public void setJalurKerjaSama(int jalurKerjaSama) {
        this.jalurKerjaSama = jalurKerjaSama;
    }

    /**
     * @return the jalurPrestasi
     */
    public int getJalurPrestasi() {
        return jalurPrestasi;
    }

    /**
     * @param jalurPrestasi the jalurPrestasi to set
     */
    public void setJalurPrestasi(int jalurPrestasi) {
        this.jalurPrestasi = jalurPrestasi;
    }

    
}
