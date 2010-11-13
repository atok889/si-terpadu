/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.matrikborang;

import java.util.List;

/**
 *
 * @author jasoet
 */
public interface MatrikBorangDAO {

    public List<MatrikBorang> getByKodeUnit(String kodeUnit) throws Exception;

    public List<MatrikBorang> getByTahunBetween(String awal, String akhir) throws Exception;

    public List<MatrikBorang> getByKodeUnitDanTahun(String KodeUnit, String akhir) throws Exception;
}
