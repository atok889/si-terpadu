/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.reratamatakuliah;

/**
 *
 * @author jasoet
 */
public class RerataMataKuliah {

    private String semester;
    private String mataKuliah;
    private double rataNilaiMK;

    public String getMataKuliah() {
        return mataKuliah;
    }

    public void setMataKuliah(String mataKuliah) {
        this.mataKuliah = mataKuliah;
    }

    public double getRataNilaiMK() {
        return rataNilaiMK;
    }

    public void setRataNilaiMK(double rataNilaiMK) {
        this.rataNilaiMK = rataNilaiMK;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
