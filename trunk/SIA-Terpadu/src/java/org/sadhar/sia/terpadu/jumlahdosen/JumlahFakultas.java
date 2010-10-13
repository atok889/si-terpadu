/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.jumlahdosen;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public class JumlahFakultas {
    private String nama;
    private int jumlah;
    private List<JumlahProdi> prodis = new ArrayList<JumlahProdi>();

    public JumlahFakultas(){}

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
     * @return the jumlah
     */
    public int getJumlah() {
        return jumlah;
    }

    /**
     * @param jumlah the jumlah to set
     */
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    /**
     * @return the prodis
     */
    public List<JumlahProdi> getProdis() {
        return prodis;
    }

    /**
     * @param prodis the prodis to set
     */
    public void setProdis(List<JumlahProdi> prodis) {
        this.prodis = prodis;
    }

    
}
