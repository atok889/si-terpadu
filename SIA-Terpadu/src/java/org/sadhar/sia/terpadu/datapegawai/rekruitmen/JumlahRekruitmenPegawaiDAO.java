/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.datapegawai.rekruitmen;

import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.unkerja.UnitKerja;

/**
 *
 * @author Hendro Steven
 */
public interface JumlahRekruitmenPegawaiDAO {
    public CategoryDataset getJumlahRekruitmen(UnitKerja unKerja)throws Exception;
}
