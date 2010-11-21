/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.distribusiumurdosendankaryawan;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface DistribusiUmurDosenDanKaryawanDAO {

    public List<Map> getProdi();

    public List<Map> getAll();

    public List<Map> getAll(String kodeUnit);

    public List<Map> getAll(String kodeProdi, String min, String max);
}
