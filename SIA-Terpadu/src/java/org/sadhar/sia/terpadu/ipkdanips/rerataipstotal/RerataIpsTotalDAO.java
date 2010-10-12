/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataipstotal;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface RerataIpsTotalDAO {

    public List<Map> getProdi();

    public List<Map> getRerataIpsTotal(String kodeProdi);
}
