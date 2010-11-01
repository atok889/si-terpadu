/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.unkerja;

import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public interface UnitKerjaDAO {
    public UnitKerja get(String kode)throws Exception;
    public List<UnitKerja> gets()throws Exception;
}
