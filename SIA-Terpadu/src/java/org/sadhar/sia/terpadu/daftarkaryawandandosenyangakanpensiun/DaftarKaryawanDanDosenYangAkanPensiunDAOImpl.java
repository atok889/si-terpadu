/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftarkaryawandandosenyangakanpensiun;

import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class DaftarKaryawanDanDosenYangAkanPensiunDAOImpl implements DaftarKaryawanDanDosenYangAkanPensiunDAO {

    public DaftarKaryawanDanDosenYangAkanPensiunDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }
}
