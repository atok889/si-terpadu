/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.dosensedangmenempuhstudi;

import java.util.List;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.jenjangstudi.JenjangStudi;
import org.sadhar.sia.terpadu.ukprogramstudi.UKProgramStudi;

/**
 *
 * @author jasoet
 */
public interface DosenSedangMenempuhStudiDAO {

    public List<DosenSedangMenempuhStudi> getData(UKProgramStudi progdi, JenjangStudi jenjangStudi) throws Exception;

    public CategoryDataset getDataset(UKProgramStudi progdi, JenjangStudi jenjangStudi) throws Exception;
}
