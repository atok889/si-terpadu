/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jabatanakademikdosen;

import org.sadhar.sia.terpadu.jabatanakademik.*;
import java.util.List;
import org.sadhar.sia.terpadu.fakultas.Fakultas;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author jasoet
 */
public interface JabatanAkademikDosenDAO {

    public List<JabatanAkademikDosen> gets() throws Exception;
    public List<JabatanAkademikDosen> getByFaculty(Fakultas f) throws Exception;

    public CategoryDataset getCountJabatanByFaculty(Fakultas f) throws Exception;

    public CategoryDataset getCountJabatanAll() throws Exception;
}
