/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pendapatan;

import java.util.List;

/**
 *
 * @author jasoet
 */
public interface PendapatanDAO {

    public List<PendapatanRencana> getAllRencana(String tahun);

    public List<PendapatanRealisasi> getAllRealisasi(String kodeFakultas, String tahun);
}
