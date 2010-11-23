/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.datapegawai.demografi;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Hendro Steven
 */
public interface DemografiPegawaiDAO {
    public CategoryDataset getBarDemografiBySex()throws Exception;
    public CategoryDataset getBarDemografiByAgama()throws Exception;
    public CategoryDataset getBarDemografiByPendidikan()throws Exception;

    public PieDataset getPieDemografiBySex()throws Exception;
    public PieDataset getPieDemografiByAgama()throws Exception;
    public PieDataset getPieDemografiByPendidikan()throws Exception;
}
