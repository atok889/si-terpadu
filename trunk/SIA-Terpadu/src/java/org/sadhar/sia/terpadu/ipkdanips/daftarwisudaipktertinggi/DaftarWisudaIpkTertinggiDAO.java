/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.daftarwisudaipktertinggi;

import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

/**
 *
 * @author kris
 */
public interface DaftarWisudaIpkTertinggiDAO {

    public List<Map> getProdi();

    public List<Map> getDaftarWisudaIpkTertinggi(String tanggalWisuda);
}
