/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.demografimahasiswa;

import org.sadhar.sia.terpadu.kabkota.KabKota;
import org.sadhar.sia.terpadu.provinsi.Provinsi;
import java.util.List;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author Hendro Steven
 */
public interface DemografiMahasiswaDAO {
    public CategoryDataset getJenisKelaminDataset(ProgramStudi progdi, String tahun) throws Exception;

    public CategoryDataset getAgamaDataset(ProgramStudi progdi, String tahun) throws Exception;

    public CategoryDataset getEkonomiOrtuDataset(ProgramStudi progdi, String tahun) throws Exception;

    public CategoryDataset getPekerjaanOrtuDataset(ProgramStudi progdi, String tahun) throws Exception;

    public CategoryDataset getAsalDaerahDataset(ProgramStudi progdi, String tahun,Provinsi prov,KabKota kabkota) throws Exception;
}
