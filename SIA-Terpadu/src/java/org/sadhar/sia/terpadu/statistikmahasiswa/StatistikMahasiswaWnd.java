/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.statistikmahasiswa;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.framework.ClassApplicationModule;

/**
 *
 * @author kris
 */
public class StatistikMahasiswaWnd extends ClassApplicationModule{


    private StatistikMahasiswaDAO statistikMahasiswaDAO;

    public StatistikMahasiswaWnd() {
        statistikMahasiswaDAO = new StatistikMahasiswaDAOImpl();
        this.loadData();
    }

    private void loadData(){
        List<Map> maps = statistikMahasiswaDAO.list();
        System.out.println("Run");
        for(Map map : maps){
            System.out.println("------"+map.get("nomor_mhs"));
        }
    }


}
