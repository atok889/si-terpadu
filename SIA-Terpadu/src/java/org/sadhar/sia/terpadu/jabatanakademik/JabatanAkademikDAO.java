/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jabatanakademik;

import java.util.List;

/**
 *
 * @author jasoet
 */
public interface JabatanAkademikDAO {

    public List<JabatanAkademik> gets() throws Exception;

    public JabatanAkademik get(String kode) throws Exception;

    public List<JabatanAkademik> getsByName(String nama) throws Exception;
}
