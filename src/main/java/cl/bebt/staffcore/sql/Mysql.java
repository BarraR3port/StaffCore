package cl.bebt.staffcore.sql;


import cl.bebt.staffcore.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mysql {
    
    private static Connection connection;
    private final String host = main.plugin.getConfig( ).getString( "mysql.host" );
    private final String port = main.plugin.getConfig( ).getString( "mysql.port" );
    private final String database = main.plugin.getConfig( ).getString( "mysql.database" );
    private final String username = main.plugin.getConfig( ).getString( "mysql.username" );
    private final String password = main.plugin.getConfig( ).getString( "mysql.password" );
    
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
    
    public void connect( ) throws SQLException{
        if ( !isConnected( ) ) {
            connection = DriverManager.getConnection( "jdbc:mysql://" +
                            host + ":" + port + "/" + database + "?useSSL=false" ,
                    username , password );
        }
    }
}
