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


    public List<Map> getDataMahasiswa(String kodeProdi);

    public List<Map> getDataFromTableTahun(String kodeProdi);

    public List<Map> getJumlahMahasiswaLulusDanBelumLulus(String kodeProdi);

    public void createDatabaseTempo();

    public void createTableTahun(String kodeProdi);

    public void createTableLulus(String kodeProdi);

    public void updateTableLulus(String kodeProde);

    public void dropTableTahun(String kodeProdi);

    public void dropTableLulus(String kodeProdi);

    public List<Map> getProdi();

    public List<Map> getTahunAngkatan(String kodeProdi);
}
