/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.ipkdanips.rerataips;

import org.sadhar.sia.framework.ClassApplicationModule;

/**
 *
 * @author kris
 */
public class RerataIpsWnd extends ClassApplicationModule {

    private RerataIpsDAO rerataIpsDAO;

    public RerataIpsWnd() {
        rerataIpsDAO = new RerataIpsDAOImpl();
    }
}
