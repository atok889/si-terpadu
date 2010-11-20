/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.matrikborang;

import java.util.List;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author jasoet
 */
public interface MatrikBorangDAO {

    public List<MatrikBorang> getByKodeUnit(String kodeUnit) throws Exception;

    public List<MatrikBorang> getByTahunBetween(String kodeUnit) throws Exception;

    public List<MatrikBorang> getByKodeUnitDanTahun(String KodeUnit, String akhir) throws Exception;

    public CategoryDataset getDatasetSkor(String kodeUnit) throws Exception;

    public CategoryDataset getDatasetSkorAll() throws Exception;
}
