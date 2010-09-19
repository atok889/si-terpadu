/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.pdfpreview;

import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zkex.zul.Jasperreport;

/**
 *
 * @author Hendro Steven
 */
public class PdfPreviewWnd extends ClassApplicationModule{
    Jasperreport report;

    public PdfPreviewWnd(){}

    public void onCreate()throws Exception{
        report = (Jasperreport)getFellow("report");
    }
}
