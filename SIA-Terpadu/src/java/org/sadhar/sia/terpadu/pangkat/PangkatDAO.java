/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pangkat;

import java.util.List;

/**
 *
 * @author jasoet
 */
public interface PangkatDAO {

    public List<Pangkat> gets() throws Exception;

    public Pangkat get(String kode) throws Exception;

    public List<Pangkat> getsByName(String nama) throws Exception;
}
