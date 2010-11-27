/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.statistiklamastudi;

import java.util.List;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author Deny Prasetyo
 */
public interface StatistikLamaStudiDAO {
    //public List<ProgramStudi> getProgramStudi()throws Exception;
    //public CategoryDataset getDataset(ProgramStudi progdi, int semester)throws Exception;

    public List<Map> getProdi();

    public List<Map> getStatistikLamaStudi(String kodeProdi, String semester);

    public boolean isTabelYudExist(String kodeProdi);
    // public List<Map> getStatistikLamaStudi(String kodeProdi);
    //public StatistikLamaStudi getStatistikLamaStudi(String kodeProdi);
    //public List<StatistikLamaStudi> getStatistikLamaStudi();
}
