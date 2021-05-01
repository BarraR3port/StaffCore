package cl.bebt.staffcore.sql;


import cl.bebt.staffcore.utils.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mysql {
    
    private static Connection connection;
    
    public static boolean isConnected( ){
        return (connection != null);
    }
    
    public static void disconnect( ){
        if ( isConnected( ) ) {
            try {
                connection.close( );
            } catch ( SQLException ignored ) {
            }
        }
    }
    
    public static Connection getConnection( ){
        return connection;
    }
    
    public static void connect( ) throws SQLException{
        String host = utils.getString( "mysql.host" , null , null );
        String port = utils.getString( "mysql.port" , null , null );
        String database = utils.getString( "mysql.database" , null , null );
        String username = utils.getString( "mysql.username" , null , null );
        String password = utils.getString( "mysql.password" , null , null );
        if ( !isConnected( ) ) {
            connection = DriverManager.getConnection( "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false" , username , password );
        }
    }
}
