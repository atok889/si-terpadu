/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.reratalamapengerjaanta;

import java.util.List;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author Deny Prasetyo
 */
public interface RerataLamaPengerjaanTADAO {
    public List<ProgramStudi> getProgramStudi()throws Exception;
    public CategoryDataset getDataset()throws Exception;
    public List<RerataLamaPengerjaanTA> getRecord() throws Exception;
    public double getAvBulanByProdi(String prodi) throws Exception;
    public double getAvBulanByTahun(String tahun) throws Exception;
    public double getAvTotal() throws Exception;
}
