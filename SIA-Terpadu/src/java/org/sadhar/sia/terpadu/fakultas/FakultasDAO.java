/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.fakultas;

import java.util.List;

/**
 *
 * @author jasoet
 */
public interface FakultasDAO {

    public List<Fakultas> gets() throws Exception;

    public Fakultas getByPrefix(String prefix) throws Exception;
    public Fakultas get(String kodeUnitKerja) throws Exception;

    public List<Fakultas> getsByName(String nama) throws Exception;
}
