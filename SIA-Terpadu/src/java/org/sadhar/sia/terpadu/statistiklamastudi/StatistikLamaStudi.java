/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistiklamastudi;

/**
 *
 * @author Deny Prasetyo
 */
public class StatistikLamaStudi {

    private String prodi;
    private int[] semester = new int[35];

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public int[] getSemester() {
        return semester;
    }

    public void setSemester(int[] semester) {
        this.semester = semester;
    }

    public void addSemesterValue(int semester) {
        this.semester[semester] ++;
    }

    public int getSemesterValue(int semester) {
        return this.semester[semester];
    }
}
