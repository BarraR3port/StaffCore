package cl.bebt.staffcore.utils;


import cl.bebt.staffcore.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

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
                JSONObject array = new JSONObject( resultado.toString( ) );
                if ( array.get( "type" ).toString( ).equalsIgnoreCase( "error" ) ) {
                    utils.tell( p , utils.getString( "web." + array.getString( "msg" ) , "lg" , null ) );
                } else {
                    utils.tell( p , utils.getString( "web." + array.getString( "msg" ) , "lg" , "staff" ) );
                    if ( array.getString( "msg" ).equalsIgnoreCase( "error_already_registered_by_other" ) ) {
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
            JSONObject array = new JSONObject( resultado.toString( ) );
            version = array.getString( str );
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
            JSONObject array = new JSONObject( resultado.toString( ) );
            isRegistered = array.getBoolean( bool );
        } catch ( IOException error ) {
            error.printStackTrace( );
            utils.tell( Bukkit.getConsoleSender( ) , "&cCould not get a connection with the server" );
        }
        return isRegistered;
    }
    
    public static String getHead( String urlParaVisitar , String p ){
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
            JSONObject array = new JSONObject( resultado.toString( ) );
            if ( array.getString( "type" ).equals( "success" ) ) {
                head = array.getString( "value" );
                main.playerSkins.put( p , head );
            }
        } catch ( IOException error ) {
            error.printStackTrace( );
            utils.tell( Bukkit.getConsoleSender( ) , "&cCould not get a connection with the server" );
        }
        return head;
    }
    
    public static void registerServer( String urlParaVisitar , String uuid){
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
                JSONObject array = new JSONObject( resultado.toString( ) );
                if ( array.get( "type" ).toString( ).equalsIgnoreCase( "success" ) ) {
                    plugin.stats.getConfig( ).set( "server_uuid" , uuid );
                    plugin.stats.saveConfig( );
                }
            } catch ( IOException ignored ){
                ignored.printStackTrace();
            }
        } );
    }
    
    public static void exportNewServerData( String urlParaVisitar){
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
                JSONObject array = new JSONObject( resultado.toString( ) );
                if ( array.get( "type" ).toString( ).equalsIgnoreCase( "success" ) ) {
                
                }
            } catch ( IOException ignored ){
            }
        } );
    }
    
}
