 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.riwayatdosen;

import java.util.List;

/**
 *
 * @author jasoet
 */
public interface RiwayatDosenDAO {

    public List<DataDosen> getDataDosenByUnitKerja(String unitKerja);

    public List<RiwayatDosen> getRiwayatDosenByKodeDosen(String kodeDosen, String unitKerja);
}
