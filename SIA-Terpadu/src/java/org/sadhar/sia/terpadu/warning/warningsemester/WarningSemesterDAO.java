/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.warning.warningsemester;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface WarningSemesterDAO {

    public List<Map> getProdi();

    public List<Map> getSemesterMahasiswa(String kodeProdi, String akademik, String semester);

    public List<Map> getWarningSemester(String kodeProdi, String akademik, String semester);
}
