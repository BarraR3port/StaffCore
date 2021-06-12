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
        String host = utils.getString( "mysql.host" );
        String port = utils.getString( "mysql.port" );
        String database = utils.getString( "mysql.database" );
        String username = utils.getString( "mysql.username" );
        String password = utils.getString( "mysql.password" );
        if ( !isConnected( ) ) {
            connection = DriverManager.getConnection( "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=TRUE&useSSL=FALSE" , username , password );
        }
    }
}
