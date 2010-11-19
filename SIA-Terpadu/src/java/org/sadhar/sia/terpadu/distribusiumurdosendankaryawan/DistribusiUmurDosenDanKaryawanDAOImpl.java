/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.distribusiumurdosendankaryawan;

import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class DistribusiUmurDosenDanKaryawanDAOImpl implements DistribusiUmurDosenDanKaryawanDAO {

    public DistribusiUmurDosenDanKaryawanDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }
}
