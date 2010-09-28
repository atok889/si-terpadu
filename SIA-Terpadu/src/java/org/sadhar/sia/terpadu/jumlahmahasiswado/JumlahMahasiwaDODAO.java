/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.jumlahmahasiswado;

import java.util.List;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author Deny Prasetyo
 */
public interface JumlahMahasiwaDODAO {
    public List<ProgramStudi> getProgramStudi()throws Exception;
    public CategoryDataset getDataset(ProgramStudi progdi, String tahun)throws Exception;
}
