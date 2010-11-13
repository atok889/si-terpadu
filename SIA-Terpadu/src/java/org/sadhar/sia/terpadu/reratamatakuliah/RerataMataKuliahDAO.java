/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.reratamatakuliah;

import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author jasoet
 */
public interface RerataMataKuliahDAO {

    public CategoryDataset getDataset(String prodi, int tahunAwal, int tahunAkhir) throws Exception;
}
