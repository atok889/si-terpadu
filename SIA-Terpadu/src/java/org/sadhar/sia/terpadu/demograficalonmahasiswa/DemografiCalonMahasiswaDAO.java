/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.demograficalonmahasiswa;

import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.kabkota.KabKota;
import org.sadhar.sia.terpadu.provinsi.Provinsi;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author Hendro Steven
 */
public interface DemografiCalonMahasiswaDAO {

    public CategoryDataset getJenisKelaminDataset(ProgramStudi progdi, String tahun) throws Exception;

    public CategoryDataset getAgamaDataset(ProgramStudi progdi, String tahun) throws Exception;

    public CategoryDataset getAsalDaerahDataset(ProgramStudi progdi, String tahun, Provinsi prov, KabKota kabkota) throws Exception;
}
