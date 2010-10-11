/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.jadwaldosen;

import java.util.List;
import org.sadhar.sia.terpadu.dosen.Dosen;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author Hendro Steven
 */
public interface JadwalDosenDAO {
    public List<JadwalDosen> getJadwalDosen(Dosen dosen,ProgramStudi progdi,String tahun,int semester)throws Exception;
}
