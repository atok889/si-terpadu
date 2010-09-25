/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningipkrendah;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface WarningIPKRendahDAO {

    public List<Map> getProdi();

    public List<Map> getAngkatan();

    public List<Map> getWarningIPKRendah(String kodeProdi);

    public List<Map> getWarningIPKRendah();

    public boolean isTabelKHExist(String kodeProdi, String tahun);
}
