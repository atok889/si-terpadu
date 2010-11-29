/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.application;

import org.sadhar.sia.common.ClassSession;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Div;

/**
 *
 * @author Hendro Steven
 */
public class IndexWnd extends ClassApplicationModule {
    
    ClassSession classSession;
    Div contentDiv;
    Session session;

    public IndexWnd(){}

    public void onCreate()throws Exception{
        session = Sessions.getCurrent();
        classSession = (ClassSession) session.getAttribute("CURRENT_SESSION");
        contentDiv = (Div)getFellow("contentDiv");
    }



    public void showObject(String objName) {
        contentDiv.getChildren().clear();
        Executions.createComponents(objName, contentDiv, null);
    }
}
