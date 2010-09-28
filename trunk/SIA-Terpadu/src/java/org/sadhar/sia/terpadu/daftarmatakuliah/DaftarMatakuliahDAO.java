/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.daftarmatakuliah;

import java.util.List;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author Hendro Steven
 */
public interface DaftarMatakuliahDAO {
public List<DaftarMatakuliah> getBeanCollection(ProgramStudi prodi) throws Exception;
}
