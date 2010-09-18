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

    public List getMhsDO(String kodeProdi);

    public List getMhsReg(String kodeProdi, String akademik, String semester);

    public List getMhsTidakReg(String kodeProdi, String akademik, String semester);

    public List getMhsCuti(String kodeProdi, String akademik, String semester);

    public List getMhsAllTempo(String kodeProdi);

    public StatistikMahasiswa getTempo(String prodi, String jenis, String tahun);

    public List<StatistikMahasiswa> getAllTempo(String prodi);

    public List<Map> getAngkatan(String prodi);

    public boolean isTabelLulusAda(String kodeProdi);

    public boolean isTabelDOAda(String kodeProdi);

    public boolean isTabelrgXYZZyyyySAda(String kodeProdi, String akademik, String semester);

    public void createTabelTempo(String kodeProdi);

    public void createTabelStatistik(String kodeProdi, List<Map> tahun);

    public void alterTableStatistik(String kodeProdi, List<Map> tahun);

    public int getJumlahMhsReg(String prodi, String tahun);

    public int getJumlahMhsCuti(String prodi, String tahun);

    public int getJumlahMhsDO(String prodi, String tahun);

    public int getJumlahMhsLulus(String prodi, String tahun);

    public int getJumlahMhsTidakAktif(String prodi, String tahun);

    public void deleteTabelTempo(String kodeProdi);

    public void deleteTabelStatistik(String kodeProdi);

    public List<Map> getStatistikMahasiswa(String kodeProdi);

    public LinkedList getFieldStatistikMahasiswa();

    public List<Map> getProdi();

    public Object[][] SetDataToCetak(String kodeProdi) throws Exception;

    public List getListDataStatistik(String kodeProdi);
}
