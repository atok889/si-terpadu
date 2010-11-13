/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.matrikborang;

/**
 *
 * @author jasoet
 */
public class MatrikBorang {

    private String tahun;
    private String kodeUnit;
    private int noIsian;
    private String isianMonevinRKA;
    private double skorPascaMonev;
    private double skorPraMonev;

    public String getIsianMonevinRKA() {
        return isianMonevinRKA;
    }

    public void setIsianMonevinRKA(String isianMonevinRKA) {
        this.isianMonevinRKA = isianMonevinRKA;
    }

    public String getKodeUnit() {
        return kodeUnit;
    }

    public void setKodeUnit(String kodeUnit) {
        this.kodeUnit = kodeUnit;
    }

    public int getNoIsian() {
        return noIsian;
    }

    public void setNoIsian(int noIsian) {
        this.noIsian = noIsian;
    }

    public double getSkorPascaMonev() {
        return skorPascaMonev;
    }

    public void setSkorPascaMonev(double skorPascaMonev) {
        this.skorPascaMonev = skorPascaMonev;
    }

    public double getSkorPraMonev() {
        return skorPraMonev;
    }

    public void setSkorPraMonev(double skorPraMonev) {
        this.skorPraMonev = skorPraMonev;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

}
