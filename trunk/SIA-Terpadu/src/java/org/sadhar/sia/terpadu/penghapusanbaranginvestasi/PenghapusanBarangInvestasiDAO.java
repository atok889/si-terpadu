/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.penghapusanbaranginvestasi;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface PenghapusanBarangInvestasiDAO {

    public List<Map> getModelPenghapusanBarang();

    public List<Map> getPenghapusanBarangInvestasi(String tahun, String kodeModel);

}
