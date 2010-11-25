/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.rasiodosenmahasiswa;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface RasioDosenMahasiswaDAO {

    public List<Map> getProdi();

    public List<Map> getNamaDosen(String kodeProdi, String tahun, String semester);

    public List<Map> getRasioDosenMahasiswa(String kodeProdi, String tahun, String semester);

    public List<Map> getRasioDosenMahasiswa();
}
