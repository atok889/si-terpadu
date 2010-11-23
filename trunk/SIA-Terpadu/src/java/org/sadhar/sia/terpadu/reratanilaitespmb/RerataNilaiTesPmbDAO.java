/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.reratanilaitespmb;

import java.util.List;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author Hendro Steven
 */
public interface RerataNilaiTesPmbDAO {
    public RerataNilaiTesPmb getRerataNilaiTesPmb(String tahun,ProgramStudi prodi)throws Exception;
    public List<RerataNilaiTesPmb> getAllRerataNilaiTesPmb(String tahun)throws Exception;
}
