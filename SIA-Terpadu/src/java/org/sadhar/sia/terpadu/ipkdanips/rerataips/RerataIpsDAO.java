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

    public Map getNamaFakultas(String kodeProdi);

    public List<Map> getRerataIps(String kodeProdi, String tahunAngkatan);

    public boolean isTabelKTExist(String kodeProdi, String tahun);   

}
