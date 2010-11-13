/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.laporankinerjapegawai;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface LaporanKinerjaPegawaiDAO {

    public List<Map> getUnitKerja();

    public List<Map> getLaporanKinerjaPegawaiAdministratif();
}
