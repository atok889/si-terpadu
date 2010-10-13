/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.bebansksdosen;

import java.util.List;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author Hendro Steven
 */
public interface BebanSKSDosenDAO {
    public List<BebanSKSDosen> getBebanSKSDosen(ProgramStudi ps,String tahun, int semester)throws Exception;
}
