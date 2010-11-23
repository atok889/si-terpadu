/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pendapatan;

/**
 *
 * @author jasoet
 */
public class PendapatanRencana {

    private String idPosAnggaranPendapatanUnit;
    private String jenisPendapatan;
    private double rencana;
    private String tahun;

    public String getIdPosAnggaranPendapatanUnit() {
        return idPosAnggaranPendapatanUnit;
    }

    public void setIdPosAnggaranPendapatanUnit(String idPosAnggaranPendapatanUnit) {
        this.idPosAnggaranPendapatanUnit = idPosAnggaranPendapatanUnit;
    }

    public String getJenisPendapatan() {
        return jenisPendapatan;
    }

    public void setJenisPendapatan(String jenisPendapatan) {
        this.jenisPendapatan = jenisPendapatan;
    }

    public double getRencana() {
        return rencana;
    }

    public void setRencana(double rencana) {
        this.rencana = rencana;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }
}
