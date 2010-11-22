/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.datapegawai.jumlahpegawai;

import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.unkerja.UnitKerja;

/**
 *
 * @author Hendro Steven
 */
public interface JumlahPegawaiDAO {
    public CategoryDataset getJumlahPegawaiByTotal(UnitKerja unKerja)throws Exception;
    public CategoryDataset getJumlahPegawaiByStatus(UnitKerja unKerja)throws Exception;
    public CategoryDataset getJumlahPegawaiByPangkat(UnitKerja unKerja)throws Exception;
    public CategoryDataset getJumlahPegawaiByGolongan(UnitKerja unKerja)throws Exception;
    public CategoryDataset getJumlahPegawaiByJabatanAkademik(UnitKerja unKerja)throws Exception;
}
