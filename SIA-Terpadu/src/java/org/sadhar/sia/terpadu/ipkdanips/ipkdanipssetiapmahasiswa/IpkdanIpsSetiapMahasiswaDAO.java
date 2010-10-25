/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.ipkdanipssetiapmahasiswa;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface IpkdanIpsSetiapMahasiswaDAO {

    public List<Map> getProdi();

    public List<Map> getIpkDanIpsSetiapMahasiswa(String kodeProdi, String angkatan, String tahun, String semester, String param1);
}
