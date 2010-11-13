/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.dptiga;

/**
 *
 * @author jasoet
 */
public class DPTiga {

    private String kodePegawai;
    private String namaPegawai;
    private double nilaiDP3;
    private String kodeUnit;
    private String namaUnit;
    private String tahunPenilaian;

    public String getKodePegawai() {
        return kodePegawai;
    }

    public void setKodePegawai(String kodePegawai) {
        this.kodePegawai = kodePegawai;
    }

    public String getKodeUnit() {
        return kodeUnit;
    }

    public void setKodeUnit(String kodeUnit) {
        this.kodeUnit = kodeUnit;
    }

    public String getNamaPegawai() {
        return namaPegawai;
    }

    public void setNamaPegawai(String namaPegawai) {
        this.namaPegawai = namaPegawai;
    }

    public String getNamaUnit() {
        return namaUnit;
    }

    public void setNamaUnit(String namaUnit) {
        this.namaUnit = namaUnit;
    }

    public double getNilaiDP3() {
        return nilaiDP3;
    }

    public void setNilaiDP3(double nilaiDP3) {
        this.nilaiDP3 = nilaiDP3;
    }

    public String getTahunPenilaian() {
        return tahunPenilaian;
    }

    public void setTahunPenilaian(String tahunPenilaian) {
        this.tahunPenilaian = tahunPenilaian;
    }
}
