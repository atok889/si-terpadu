/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.nepdosen;

import java.util.List;

/**
 *
 * @author jasoet
 */
public interface NEPDosenDAO {

    public List<NEPDosen> getByKodeUnit(String tahun,String kodeUnit) throws Exception;

    public List<NEPDosen> getAll(String tahun) throws Exception;
}
