/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.dosen;

import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public interface DosenDAO {
    public List<Dosen> gets()throws Exception;
    public Dosen get(String kdPegawai)throws Exception;
    public List<Dosen> getsByName(String nama)throws Exception;
}
