/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.jumlahmahasiswa;

import java.util.List;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author Hendro Steven
 */
public interface JumlahMahasiwaDAO {
    public List<ProgramStudi> getProgramStudi()throws Exception;
    public CategoryDataset getDataset(ProgramStudi progdi, String tahun)throws Exception;
}
