/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.ipkdanipssetiapmahasiswa;

import org.sadhar.sia.framework.ClassApplicationModule;

/**
 *
 * @author kris
 */
public class IpkdanIpsSetiapMahasiswaWnd extends ClassApplicationModule {

    private IpkdanIpsSetiapMahasiswaDAO ipkDanIpsSetiapMahasiswaDAO;

    public IpkdanIpsSetiapMahasiswaWnd() {
        ipkDanIpsSetiapMahasiswaDAO = new IpkdanIpsSetiapMahasiswaDAOImpl();
    }

    public void onCreate() throws Exception {
    }
}
