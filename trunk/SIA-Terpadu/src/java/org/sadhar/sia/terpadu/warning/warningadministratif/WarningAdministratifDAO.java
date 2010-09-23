/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningadministratif;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface WarningAdministratifDAO {

    public List<Map> getProdi();

    public List<Map> getSemesterMahasiswa(String kodeProdi, String akademik, String semester);

    public List<Map> getWarningAdministratif(String kodeProdi, String akademik, String semester);
}
