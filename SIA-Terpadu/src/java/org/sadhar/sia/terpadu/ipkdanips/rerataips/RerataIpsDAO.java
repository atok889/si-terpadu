/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataips;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface RerataIpsDAO {

    public List<Map> getProdi();

    public List<Map> getRerataIps(String kodeProdi, String tahunAngkatan);

    public List<Map> getRerataIps();

    public boolean isTabelKTExist(String kodeProdi, String tahun);
}
