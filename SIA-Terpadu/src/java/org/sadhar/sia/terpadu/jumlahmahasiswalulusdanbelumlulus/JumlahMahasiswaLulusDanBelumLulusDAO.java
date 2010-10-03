/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahmahasiswalulusdanbelumlulus;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface JumlahMahasiswaLulusDanBelumLulusDAO {

    public List<Map> getProdi();

    public List<Map> getJumlahMahasiswa(String kodeProdi);

    public List<Map> getJumlahMahasiswaLulus(String kodeProdi);

    public boolean isTableMhsExist(String kodeProdi);

    public boolean isTableLulusExist(String kodeProdi);
}
