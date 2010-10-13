/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ukprogramstudi;

import java.util.List;

/**
 *
 * @author jasoet
 */
public interface UKProgramStudiDAO {
     public List<UKProgramStudi> gets() throws Exception;

    public UKProgramStudi get(String kodeUnitKerja) throws Exception;

    public List<UKProgramStudi> getsByName(String nama) throws Exception;

}
