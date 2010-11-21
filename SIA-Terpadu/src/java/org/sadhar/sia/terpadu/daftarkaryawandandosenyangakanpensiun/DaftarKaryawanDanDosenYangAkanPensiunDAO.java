/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftarkaryawandandosenyangakanpensiun;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface DaftarKaryawanDanDosenYangAkanPensiunDAO {

    public List<Map> getDaftarDosenDanKaryawanYangAkanPensiun(String tahun);
}
