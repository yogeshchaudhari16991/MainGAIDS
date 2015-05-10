/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FTPServer;

import java.net.InetAddress;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kp
 */
public class DatabaseConnection {

    String db = "gaids";
    String username = "root";
    String password = "2356";
    Connection con = null;
    Statement stmt;
    ResultSet rs = null;
    public static String ip = "";

    public DatabaseConnection() {
    }

    public Connection dbconnection() {

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
           // System.out.println(ip);
            String url = "jdbc:mysql://localhost:3306/" + db;
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            if(con == null || con.isClosed())
            {
            con = DriverManager.getConnection(url, username, password);
            }


        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    public ResultSet getResultSet(String query) {
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public int getUpdate(String query) {
        int i = 0;
        try {
            stmt = con.createStatement();
            i = stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
