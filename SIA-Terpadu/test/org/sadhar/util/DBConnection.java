/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author kris
 */
public class DBConnection {

    private DBConnection() {
    }

    public static void initDataSource() {
        try {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
            Context context = new InitialContext();
            context.createSubcontext("java:");
            context.createSubcontext("java:comp");
            context.createSubcontext("java:comp/env");
            context.createSubcontext("java:comp/env/jdbc");
            DataSource ds = new DriverManagerDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/kamus?zeroDateTimeBehavior=convertToNull", "root", "root");
            context.bind("java:comp/env/jdbc/kamus", ds);
            System.out.println("------" + context.lookup("java:comp/env/jdbc/kamus"));
        } catch (NamingException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
