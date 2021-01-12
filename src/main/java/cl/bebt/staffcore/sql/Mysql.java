package cl.bebt.staffcore.sql;


import cl.bebt.staffcore.main;

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
        String host = main.plugin.getConfig( ).getString( "mysql.host" );
        String port = main.plugin.getConfig( ).getString( "mysql.port" );
        String database = main.plugin.getConfig( ).getString( "mysql.database" );
        String username = main.plugin.getConfig( ).getString( "mysql.username" );
        String password = main.plugin.getConfig( ).getString( "mysql.password" );
        if ( !isConnected( ) ) {
            connection = DriverManager.getConnection( "jdbc:mysql://" +
                            host + ":" + port + "/" + database + "?useSSL=false" ,
                    username , password );
        }
    }
}
