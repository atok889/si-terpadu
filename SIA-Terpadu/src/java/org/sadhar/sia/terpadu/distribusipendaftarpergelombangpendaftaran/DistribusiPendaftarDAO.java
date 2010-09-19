/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.distribusipendaftarpergelombangpendaftaran;

import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public interface DistribusiPendaftarDAO {

    public List<DistribusiPendaftar> getBeanCollection(List<String> tahuns) throws Exception;
}
