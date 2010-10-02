/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistikmahasiswa;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface StatistikMahasiswaDAO {

    public List<Map> getAngkatan();

    public List<Map> getProdi();

    public List<Map> getMahasiswaDO(String kodeProdi);

    public List<Map> getMahasiswaLulus(String kodeProdi);

    public List<Map> getMahasiswaCuti(String kodeProdi);

    public List<Map> getMahasiswaRegistrasi(String kodeProdi);

    public List<Map> getMahasiswaTidakRegistrasi(String kodeProdi);

    public List<Map> getDetailMahasiswaDO(String kodeProdi, String tahun);

    public List<Map> getDetailAllMahasiswaDO(String kodeProdi, String tahun);

    public List<Map> getDetailMahasiswaRegistrasi(String kodeProdi, String tahun);

    public List<Map> getDetailAllMahasiswaRegistrasi(String kodeProdi, String tahun);

    public List<Map> getDetailMahasiswaTidakRegistrasi(String kodeProdi, String tahun);

    public List<Map> getDetailAllMahasiswaTidakRegistrasi(String kodeProdi, String tahun);

    public List<Map> getDetailMahasiswaCuti(String kodeProdi, String tahun);

    public List<Map> getDetailAllMahasiswaCuti(String kodeProdi, String tahun);

    public List<Map> getDetailMahasiswaLulus(String kodeProdi, String tahun);

    public List<Map> getDetailAllMahasiswaLulus(String kodeProdi, String tahun);

    public List<Map> getDetailMahasiswa(String kodeProdi, String tahun);

    public List<Map> getDetailAllMahasiswa(String kodeProdi, String tahun);

    public boolean isTableMhsExist(String kodeProdi);

    public boolean isTableLulusExist(String kodeProdi);

    public boolean isTableDOExist(String kodeProdi);
}
