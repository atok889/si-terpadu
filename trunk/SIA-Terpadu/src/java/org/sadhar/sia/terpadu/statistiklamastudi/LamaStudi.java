/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistiklamastudi;

/**
 *
 * @author jasoet
 */
public class LamaStudi {

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

    public void addSemesterValue(int semester, int value) {
        this.semester[semester - 1] += value;
    }

    public int getSemesterValue(int semester) {
        return this.semester[semester - 1];
    }
}
