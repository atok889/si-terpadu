/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.pendapatan;

import java.util.Date;

/**
 *
 * @author jasoet
 */
public class PendapatanRealisasi {
    private String kodeTagih;
    private String namaTagih;
    private double realisasi;
    private Date tanggalBayar;

    public String getKodeTagih() {
        return kodeTagih;
    }

    public void setKodeTagih(String kodeTagih) {
        this.kodeTagih = kodeTagih;
    }

    public String getNamaTagih() {
        return namaTagih;
    }

    public void setNamaTagih(String namaTagih) {
        this.namaTagih = namaTagih;
    }

    public double getRealisasi() {
        return realisasi;
    }

    public void setRealisasi(double realisasi) {
        this.realisasi = realisasi;
    }

    public Date getTanggalBayar() {
        return tanggalBayar;
    }

    public void setTanggalBayar(Date tanggalBayar) {
        this.tanggalBayar = tanggalBayar;
    }
    
}
