/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffbungee.EntitysUtils;

import cl.bebt.staffbungee.Entitys.SqlConnection;
import cl.bebt.staffbungee.main;
import com.google.gson.Gson;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class SqlUtils {
    
    private static Connection connection;
    
    private static ArrayList < SqlConnection > sqlConnection = new ArrayList<>();
    
    public static boolean isConnected( ){
        return (connection != null);
    }
    
    public static Connection getConnection( ){
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
    
    
    public static void connect( ) throws SQLException{
        SqlConnection sql = sqlConnection.get( 0 );
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
            File file = new File( main.plugin.getDataFolder( ).getAbsolutePath( ) + "/mysql.json" );
            if ( file.exists( ) ) {
                Reader reader = new FileReader( file );
                SqlConnection[] sql = gson.fromJson( reader , SqlConnection[].class );
                sqlConnection = new ArrayList <>( Arrays.asList( sql ) );
            }
            createDefaultMysql();
        } catch ( IOException error ) {
            error.printStackTrace( );
        }
    }
    
    public static void saveSql( ){
        try {
            Gson gson = new Gson( );
            File file = new File( main.plugin.getDataFolder( ).getAbsolutePath( ) + "/mysql.json" );
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
    
    public static void createDefaultMysql( ){
        if ( sqlConnection.size( ) == 0 ) {
            SqlConnection sql = new SqlConnection( "localhost" , "3306" , "root" , "staffcore" , "" , false );
            sqlConnection.add( sql );
            saveSql( );
        }
    }
    
    public static Boolean isEnabled( ){
        return sqlConnection.get( 0 ).getEnabled( );
    }
    
    
}
