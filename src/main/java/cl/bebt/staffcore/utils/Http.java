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
    
    public static void get( String urlParaVisitar , Player p ){
        Bukkit.getScheduler( ).runTaskAsynchronously( plugin , ( ) -> {
            try {
                // Esto es lo que vamos a devolver
                StringBuilder resultado = new StringBuilder( );
                // Crear un objeto de tipo URL
                URL url = new URL( urlParaVisitar );
                
                // Abrir la conexión e indicar que será de tipo GET
                HttpURLConnection conexion = ( HttpURLConnection ) url.openConnection( );
                conexion.setRequestMethod( "GET" );
                // Búferes para leer
                BufferedReader rd = new BufferedReader( new InputStreamReader( conexion.getInputStream( ) ) );
                String linea;
                // Mientras el BufferedReader se pueda leer, agregar contenido a resultado
                while ((linea = rd.readLine( )) != null) {
                    resultado.append( linea );
                }
                // Cerrar el BufferedReader
                rd.close( );
                // Regresar resultado, pero como cadena, no como StringBuilder
                JSONObject array = new JSONObject( resultado.toString( ) );
                if ( array.get( "type" ).toString( ).equalsIgnoreCase( "error" ) ) {
                    utils.tell( p , utils.getString( "web." + array.get( "msg" ).toString( ) , "lg" , null ) );
                } else {
                    utils.tell( p , utils.getString( "web." + array.get( "msg" ).toString( ) , "lg" , "staff" ) );
                }
            } catch ( IOException error ) {
                error.printStackTrace( );
                utils.tell( p , "&cCould not get a connection with the server" );
            }
        } );
    }
}
