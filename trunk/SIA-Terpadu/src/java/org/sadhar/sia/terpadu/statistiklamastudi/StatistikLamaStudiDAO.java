/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.statistiklamastudi;

import java.util.List;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.jumlahmahasiswa.ProgramStudi;

/**
 *
 * @author Hendro Steven
 */
public interface StatistikLamaStudiDAO {
    public List<ProgramStudi> getProgramStudi()throws Exception;
    public CategoryDataset getDataset(ProgramStudi progdi, int semester)throws Exception;
}
