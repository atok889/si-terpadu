/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.dptiga;

import java.util.List;

/**
 *
 * @author jasoet
 */
public interface DPTigaDAO {
    public List<DPTiga> getByKodeUnit(String kodeUnit) throws Exception;
}
