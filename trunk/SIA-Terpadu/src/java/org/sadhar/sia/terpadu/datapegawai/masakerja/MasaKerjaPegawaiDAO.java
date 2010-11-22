/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.datapegawai.masakerja;

import org.jfree.data.general.PieDataset;
import org.sadhar.sia.terpadu.unkerja.UnitKerja;

/**
 *
 * @author Hendro Steven
 */
public interface MasaKerjaPegawaiDAO {
    public PieDataset getMasaKerja(UnitKerja unKerja)throws Exception;
}
