/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pangkatdosen;

import org.sadhar.sia.terpadu.pangkat.*;
import java.util.List;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.fakultas.Fakultas;

/**
 *
 * @author jasoet
 */
public interface PangkatDosenDAO {

    public List<PangkatDosen> gets() throws Exception;

    public CategoryDataset getCountPangkatByFaculty(Fakultas f) throws Exception;
    public CategoryDataset getCountPangkatAll() throws Exception;
}
