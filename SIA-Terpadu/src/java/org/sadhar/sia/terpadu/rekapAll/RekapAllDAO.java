/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.rekapAll;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dian
 */
public interface RekapAllDAO {

    public List getDaftarUnit(String kata);

    public List getLpj(String kdUnker, String thn) throws Exception; 

    public LinkedList getFieldLpj() throws Exception;

    public Object[][] DataLpjToCetak(String kdUnker, String thn) throws Exception;

    
}
