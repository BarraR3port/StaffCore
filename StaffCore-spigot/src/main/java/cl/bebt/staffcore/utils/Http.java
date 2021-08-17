/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;


import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.DataExporter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {
    
    private static main plugin;
    
    public Http( main plugin ){
        Http.plugin = plugin;
    }
    
    public static void getLatestVersion( String urlParaVisitar , Player p , String server ){
        Bukkit.getScheduler( ).runTaskAsynchronously( plugin , ( ) -> {
            try {
                StringBuilder resultado = new StringBuilder( );
                URL url = new URL( urlParaVisitar );
                HttpURLConnection connexion = ( HttpURLConnection ) url.openConnection( );
                connexion.setRequestMethod( "GET" );
                BufferedReader rd = new BufferedReader( new InputStreamReader( connexion.getInputStream( ) ) );
                String linea;
                while ((linea = rd.readLine( )) != null) {
                    resultado.append( linea );
                }
                rd.close( );
                JsonObject array = new JsonObject( ).getAsJsonObject( resultado.toString( ) );
                if ( array.get( "type" ).toString( ).equalsIgnoreCase( "error" ) ) {
                    utils.tell( p , utils.getString( "web." + array.get( "msg" ).getAsString( ) , "lg" , null ) );
                } else {
                    utils.tell( p , utils.getString( "web." + array.get( "msg" ).getAsString( ) , "lg" , "staff" ) );
                    if ( array.get( "msg" ).getAsString( ).equalsIgnoreCase( "error_already_registered_by_other" ) ) {
                        utils.tell( p , utils.getString( "web.web" , "lg" , null ) );
                        return;
                    }
                    plugin.getConfig( ).set( "server_name" , server );
                }
            } catch ( IOException error ) {
                error.printStackTrace( );
                utils.tell( p , "&cCould not get a connection with the server" );
            }
        } );
    }
    
    public static String getLatestVersion( String urlParaVisitar , String str ){
        String version = "";
        try {
            StringBuilder resultado = new StringBuilder( );
            URL url = new URL( urlParaVisitar );
            HttpURLConnection connexion = ( HttpURLConnection ) url.openConnection( );
            connexion.setRequestMethod( "GET" );
            BufferedReader rd = new BufferedReader( new InputStreamReader( connexion.getInputStream( ) ) );
            String linea;
            while ((linea = rd.readLine( )) != null) {
                resultado.append( linea );
            }
            rd.close( );
            JsonElement jElement = new JsonParser( ).parse( resultado.toString( ) );
            JsonObject array = jElement.getAsJsonObject( );
            version = array.get( str ).getAsString( );
        } catch ( IOException error ) {
            error.printStackTrace( );
            utils.tell( Bukkit.getConsoleSender( ) , "&cCould not get a connection with the server" );
        }
        return version;
    }
    
    public static boolean getBoolean( String urlParaVisitar , String bool ){
        boolean isRegistered = false;
        try {
            StringBuilder resultado = new StringBuilder( );
            URL url = new URL( urlParaVisitar );
            HttpURLConnection connexion = ( HttpURLConnection ) url.openConnection( );
            connexion.setRequestMethod( "GET" );
            BufferedReader rd = new BufferedReader( new InputStreamReader( connexion.getInputStream( ) ) );
            String linea;
            while ((linea = rd.readLine( )) != null) {
                resultado.append( linea );
            }
            rd.close( );
            JsonElement jElement = new JsonParser( ).parse( resultado.toString( ) );
            JsonObject array = jElement.getAsJsonObject( );
            isRegistered = array.get( bool ).getAsBoolean( );
        } catch ( IOException error ) {
            error.printStackTrace( );
            utils.tell( Bukkit.getConsoleSender( ) , "&cCould not get a connection with the server" );
        }
        return isRegistered;
    }
    
    public static String getHead( String urlParaVisitar , String p ){
        if ( !main.playerSkins.containsKey( p ) ) {
            String head = "";
            try {
                StringBuilder resultado = new StringBuilder( );
                URL url = new URL( urlParaVisitar );
                HttpURLConnection connexion = ( HttpURLConnection ) url.openConnection( );
                connexion.setRequestMethod( "GET" );
                BufferedReader rd = new BufferedReader( new InputStreamReader( connexion.getInputStream( ) ) );
                String linea;
                while ((linea = rd.readLine( )) != null) {
                    resultado.append( linea );
                }
                rd.close( );
                JsonElement jElement = new JsonParser( ).parse( resultado.toString( ) );
                JsonObject array = jElement.getAsJsonObject( );
                if ( array.get( "type" ).getAsString( ).equals( "success" ) ) {
                    head = array.get( "value" ).getAsString( );
                    main.playerSkins.put( p , head );
                }
            } catch ( IOException error ) {
                error.printStackTrace( );
                utils.tell( Bukkit.getConsoleSender( ) , "&cCould not get a connection with the server" );
            }
            return head;
        } else {
            return main.playerSkins.get( p );
        }
        
    }
    
    public static void registerServer( String urlParaVisitar , String uuid ){
        Bukkit.getScheduler( ).runTaskAsynchronously( plugin , ( ) -> {
            try {
                StringBuilder resultado = new StringBuilder( );
                URL url = new URL( urlParaVisitar );
                HttpURLConnection connexion = ( HttpURLConnection ) url.openConnection( );
                connexion.setRequestMethod( "GET" );
                BufferedReader rd = new BufferedReader( new InputStreamReader( connexion.getInputStream( ) ) );
                String linea;
                while ((linea = rd.readLine( )) != null) {
                    resultado.append( linea );
                }
                rd.close( );
                JsonElement jElement = new JsonParser( ).parse( resultado.toString( ) );
                JsonObject array = jElement.getAsJsonObject( );
                if ( array.get( "type" ).getAsString( ).equalsIgnoreCase( "success" ) ) {
                    plugin.stats.reloadConfig( );
                    plugin.stats.getConfig( ).set( "server_uuid" , uuid );
                    plugin.stats.saveConfig( );
                }
            } catch ( IOException ignored ) {
                ignored.printStackTrace( );
            }
        } );
    }
    
    public static void exportNewServerData( String urlParaVisitar ){
        Bukkit.getScheduler( ).runTaskAsynchronously( plugin , ( ) -> {
            try {
                int count = 0;
                while (count < 4) {
                    StringBuilder resultado = new StringBuilder( );
                    URL url = new URL( urlParaVisitar );
                    HttpURLConnection connexion = ( HttpURLConnection ) url.openConnection( );
                    connexion.setRequestMethod( "GET" );
                    BufferedReader rd = new BufferedReader( new InputStreamReader( connexion.getInputStream( ) ) );
                    String linea;
                    while ((linea = rd.readLine( )) != null) {
                        resultado.append( linea );
                    }
                    rd.close( );
                    JsonElement jElement = new JsonParser( ).parse( resultado.toString( ) );
                    JsonObject array = jElement.getAsJsonObject( );
                    if ( array.get( "type" ).getAsString( ).equals( "success" ) ) {
                        break;
                    } else if ( array.get( "type" ).getAsString( ).equals( "error" ) ) {
                        if ( array.get( "msg" ).getAsString( ).equals( "server_not_registered" ) ) {
                            DataExporter.createServerStats( );
                            count++;
                        }
                    }
                }
            } catch ( IOException ignored ) {
            }
        } );
    }
    
}
