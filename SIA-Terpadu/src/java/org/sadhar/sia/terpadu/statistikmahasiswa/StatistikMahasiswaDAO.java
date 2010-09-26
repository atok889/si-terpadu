/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistikmahasiswa;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface StatistikMahasiswaDAO {

    public List getMhsLulus(String kodeProdi);

    public List<Map> getMhsDO(String kodeProdi);

    public List<Map> getMhsReg(String kodeProdi, String akademik, String semester);

    public List<Map> getMhsTidakReg(String kodeProdi, String akademik, String semester);

    public List<Map> getMhsCuti(String kodeProdi, String akademik, String semester);

    public List<Map> getMhsAllTempo(String kodeProdi);

    public StatistikMahasiswa getTempo(String prodi, String jenis, String tahun);

    public List<Map> getAllTempo(String prodi);

    public List<Map> getAngkatan(String prodi);

    public boolean isTabelLulusAda(String kodeProdi);

    public boolean isTabelDOAda(String kodeProdi);

    public boolean isTabelrgXYZZyyyySAda(String kodeProdi, String akademik, String semester);

    public void createTabelTempo(String kodeProdi);

    public void createTabelAllStatistik(List<Map> tahun);

    public void createTabelStatistik(String kodeProdi, List<Map> tahun);

    public void createTabelStatistik();

    public void deleteTabelTempo(String kodeProdi);

    public void deleteTabelStatistik(String kodeProdi);

    public void createDatabaseTempo();

    public void deleteTabelStatistik();

    public void deleteTabelAllStatistik();

    public void alterTableStatistik(String kodeProdi, List<Map> tahun);

    public void alterTableStatistik(List<Map> tahun);

    public int getJumlahMhsReg(String prodi, String tahun);

    public int getJumlahMhsCuti(String prodi, String tahun);

    public int getJumlahMhsDO(String prodi, String tahun);

    public int getJumlahMhsLulus(String prodi, String tahun);

    public int getJumlahMhsTidakAktif(String prodi, String tahun);

    public int getJumlahMhsReg(String tahun);

    public int getJumlahMhsCuti(String tahun);

    public int getJumlahMhsDO(String tahun);

    public int getJumlahMhsLulus(String tahun);

    public int getJumlahMhsTidakAktif(String tahun);

    public List<Map> getStatistikMahasiswa(String kodeProdi);

    public List<Map> getStatistikMahasiswa();

    public List<Map> getDetailStatistikMahasiswa(String kodeProdi, String tahunAngkatan, String database, String status);
    
    public LinkedList getFieldStatistikMahasiswa();

    public List<Map> getProdi();

    public List<Map> getAngkatan();

    public Object[][] SetDataToCetak(String kodeProdi) throws Exception;

    public List getListDataStatistik(String kodeProdi);
}
