/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.provinsi;

import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public interface ProvinsiDAO {
    public List<Provinsi> getProvinsi() throws Exception;

    public Provinsi getProv(String kode)throws Exception;

}
