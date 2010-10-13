/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jenjangstudi;

import java.util.List;

/**
 *
 * @author jasoet
 */
public interface JenjangStudiDAO {

    public List<JenjangStudi> gets() throws Exception;

    public JenjangStudi get(String kode) throws Exception;

    public List<JenjangStudi> getsByName(String nama) throws Exception;
}
