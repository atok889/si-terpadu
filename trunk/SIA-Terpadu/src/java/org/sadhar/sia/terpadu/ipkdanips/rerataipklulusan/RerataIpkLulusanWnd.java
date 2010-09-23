/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataipklulusan;

import org.sadhar.sia.framework.ClassApplicationModule;

/**
 *
 * @author kris
 */
public class RerataIpkLulusanWnd extends ClassApplicationModule {

    private RerataIpkLulusanDAO rerataIpkLulusanDAO;

    public RerataIpkLulusanWnd() {
        rerataIpkLulusanDAO = new RerataIpkLulusanDAOImpl();
    }

    public void onCreate() throws Exception {
    }
}
