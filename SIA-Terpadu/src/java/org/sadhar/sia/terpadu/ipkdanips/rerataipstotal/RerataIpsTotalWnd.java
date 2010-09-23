/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataipstotal;

import org.sadhar.sia.framework.ClassApplicationModule;

/**
 *
 * @author kris
 */
public class RerataIpsTotalWnd extends ClassApplicationModule {

    private RerataIpsTotalDAO rerataIpsTotalDAO;

    public RerataIpsTotalWnd() {
        rerataIpsTotalDAO = new RerataIpsTotalDAOImpl();
    }

    public void onCreate() throws Exception {
    }
}
