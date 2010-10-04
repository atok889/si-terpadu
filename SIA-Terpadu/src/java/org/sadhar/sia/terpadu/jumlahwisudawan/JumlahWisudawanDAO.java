/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahwisudawan;

import java.util.List;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author jasoet
 */
public interface JumlahWisudawanDAO {

    public List<ProgramStudi> getProgramStudi() throws Exception;

    public CategoryDataset getDataset() throws Exception;

    public List<JumlahWisudawan> getRecord() throws Exception;
}
