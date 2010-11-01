/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.assetbaranginvestasi;

/**
 *
 * @author jasoet
 */
public class AssetBarangInvestasi {

    private int index;
    private String kode;
    private String nomor;
    private String kodeKelompok;
    private String nama;
    private int jumlah;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getKodeKelompok() {
        return kodeKelompok;
    }

    public void setKodeKelompok(String kodeKelompok) {
        this.kodeKelompok = kodeKelompok;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }
}
