/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.EntitiesUtils;

import cl.bebt.staffcoreapi.Entities.SqlConnection;
import cl.bebt.staffcoreapi.utils.Utils;
import com.google.gson.Gson;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlUtils {
    
    private static Connection connection;
    
    private static SqlConnection sqlConnection;
    
    public static boolean isConnected( ){
        return (connection != null);
    }
    
    public static Connection getConnection( ){
        if ( !isConnected( ) ) {
            try {
                connect( );
            } catch ( SQLException e ) {
                e.printStackTrace( );
            }
        }
        return connection;
    }
    
    public static void disconnect( ){
        if ( isConnected( ) ) {
            try {
                connection.close( );
            } catch ( SQLException ignored ) {
            }
        }
    }
    
    public static SqlConnection getSqlConnection( ){
        return sqlConnection;
    }
    
    public static void connect( ) throws SQLException{
        SqlConnection sql = sqlConnection;
        String host = sql.getHost( );
        String port = sql.getPort( );
        String database = sql.getDataBase( );
        String username = sql.getUserName( );
        String password = sql.getPassword( );
        if ( !isConnected( ) ) {
            connection = DriverManager.getConnection( "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=TRUE&useSSL=FALSE" , username , password );
        }
    }
    
    
    public static void loadSqlConfig( ){
        try {
            Gson gson = new Gson( );
            File file = new File( Utils.getDataFolder( ) + "/mysql.json" );
            if ( file.exists( ) ) {
                Reader reader = new FileReader( file );
                sqlConnection = gson.fromJson( reader , SqlConnection.class );
            } else {
                saveSql( );
            }
        } catch ( IOException error ) {
            error.printStackTrace( );
        }
    }
    
    public static void saveSql( ){
        sqlConnection = new SqlConnection( "localhost" , "3306" , "root" , "staffcore" , "" , false );
        try {
            Gson gson = new Gson( );
            File file = new File( Utils.getDataFolder( ) + "/mysql.json" );
            file.getParentFile( ).mkdir( );
            file.createNewFile( );
            Writer writer = new FileWriter( file , false );
            gson.toJson( sqlConnection , writer );
            writer.flush( );
            writer.close( );
        } catch ( IOException error ) {
            error.printStackTrace( );
        }
    }
    
    
    public static Boolean isEnabled( ){
        return sqlConnection != null && sqlConnection.getEnabled( );
    }
    
    
}
