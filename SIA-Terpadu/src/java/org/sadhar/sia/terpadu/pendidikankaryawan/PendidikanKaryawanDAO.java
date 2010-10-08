/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pendidikankaryawan;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface PendidikanKaryawanDAO {

    public List<Map> getUnitKerja();

    public List<Map> getPendidikanKaryawan(String unitKerja);
}
