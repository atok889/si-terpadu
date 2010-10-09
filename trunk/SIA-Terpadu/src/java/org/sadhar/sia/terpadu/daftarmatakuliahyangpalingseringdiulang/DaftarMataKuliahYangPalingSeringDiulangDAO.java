/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftarmatakuliahyangpalingseringdiulang;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface DaftarMataKuliahYangPalingSeringDiulangDAO {

    public List<Map> getProdi();

    public List<Map> getMataKuliah(String kodeProdi);

    public List<Map> getDaftarMataKuliahYangPalingSeringDiulang(String kodeProdi, String tahunSemester);
}
