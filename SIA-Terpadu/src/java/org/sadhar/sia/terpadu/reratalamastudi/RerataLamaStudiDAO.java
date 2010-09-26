/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.reratalamastudi;

import java.util.List;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.jumlahmahasiswa.ProgramStudi;

/**
 *
 * @author Deny Prasetyo
 */
public interface RerataLamaStudiDAO {
    public List<ProgramStudi> getProgramStudi()throws Exception;
    public CategoryDataset getDataset()throws Exception;
}
