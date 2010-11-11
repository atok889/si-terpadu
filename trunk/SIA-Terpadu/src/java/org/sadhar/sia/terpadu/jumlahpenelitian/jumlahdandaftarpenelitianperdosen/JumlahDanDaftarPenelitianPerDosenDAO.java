/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahpenelitian.jumlahdandaftarpenelitianperdosen;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface JumlahDanDaftarPenelitianPerDosenDAO {

    public List<Map> getProdi();

    public List<Map> getJumlahDanDaftarPenelitianPerDosen(String prodi);

    public List<Map> getDetailJumlahDanDaftarPenelitianPerDosen(String kodePegawai);
}
