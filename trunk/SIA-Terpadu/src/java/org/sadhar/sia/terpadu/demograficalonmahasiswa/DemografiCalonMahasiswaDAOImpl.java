/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sadhar.sia.terpadu.demograficalonmahasiswa;

import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author Hendro Steven
 */
public class DemografiCalonMahasiswaDAOImpl implements DemografiCalonMahasiswaDAO {
    public DemografiCalonMahasiswaDAOImpl(){
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }
}
