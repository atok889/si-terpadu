/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.pembelianbaranginvestasi;

import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public interface PembelianBarangInvestasiDAO {
    public List<PembelianBarangInvestasi> loadData()throws Exception;
}
