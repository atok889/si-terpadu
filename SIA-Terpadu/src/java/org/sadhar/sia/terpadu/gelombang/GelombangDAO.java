/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.gelombang;

import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public interface GelombangDAO {
    public Gelombang get(int kode)throws Exception;
    public List<Gelombang> gets()throws Exception;
    public List<Gelombang> gets(String except)throws Exception;
}
