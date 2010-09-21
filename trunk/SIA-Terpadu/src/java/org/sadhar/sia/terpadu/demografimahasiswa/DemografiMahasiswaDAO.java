/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.demografimahasiswa;

import java.util.List;
import org.jfree.data.category.CategoryDataset;
import org.sadhar.sia.terpadu.jumlahmahasiswa.ProgramStudi;

/**
 *
 * @author Hendro Steven
 */
public interface DemografiMahasiswaDAO {

    public List<ProgramStudi> getProgramStudi() throws Exception;

    public List<Provinsi> getProvinsi() throws Exception;

    public Provinsi getProv(String kode)throws Exception;

    public List<KabKota> getKabKota(Provinsi provinsi) throws Exception;

    public CategoryDataset getJenisKelaminDataset(ProgramStudi progdi, String tahun) throws Exception;

    public CategoryDataset getAgamaDataset(ProgramStudi progdi, String tahun) throws Exception;

    public CategoryDataset getEkonomiOrtuDataset(ProgramStudi progdi, String tahun) throws Exception;

    public CategoryDataset getPekerjaanOrtuDataset(ProgramStudi progdi, String tahun) throws Exception;

    public CategoryDataset getAsalDaerahDataset(ProgramStudi progdi, String tahun,Provinsi prov,KabKota kabkota) throws Exception;
}
