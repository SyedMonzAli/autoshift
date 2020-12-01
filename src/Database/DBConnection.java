/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author monzy
 */
import java.sql.*;

public class DBConnection {
   
    private static final String CONN = "jdbc:sqlite:schedulerdata.db";
    
    public static Connection dbConnect() throws ClassNotFoundException{
        
        Connection conn = null;
        
            try { 
                conn = DriverManager.getConnection(CONN);               
            } catch (SQLException e){
                System.out.println(e.getMessage());          
            } 
        return conn;
    }
}
  

