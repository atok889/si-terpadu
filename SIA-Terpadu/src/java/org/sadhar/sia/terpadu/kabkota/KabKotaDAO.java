/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.kabkota;

import java.util.List;
import org.sadhar.sia.terpadu.provinsi.Provinsi;

/**
 *
 * @author Hendro Steven
 */
public interface KabKotaDAO {
     public List<KabKota> getKabKota(Provinsi provinsi) throws Exception;

}
