/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffbungee.utils;

import cl.bebt.staffbungee.main;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class utils {
    
    private static final main plugin = main.getPlugin( );
    
    public static String chat( String s ){
        return ChatColor.translateAlternateColorCodes( '&' , s );
    }
    
    public static void tell( CommandSender sender , String message ){
        if ( sender instanceof ProxiedPlayer ) {
            ProxiedPlayer player = ( ProxiedPlayer ) sender;
            player.sendMessage( tc( message ) );
        } else {
            ProxyServer.getInstance( ).getConsole( ).sendMessage( tc( message ) );
        }
    }
    
    public static String getUUID( String urlParaVisitar , String p ){
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
            }
            return head;
        } else {
            return main.playerSkins.get( p );
        }
        
    }
    
    public static void Broadcast(String s){
        for ( ProxiedPlayer p : plugin.getProxy().getPlayers( ) ){
            tell( p, s );
        }
        tell( plugin.getProxy().getConsole(), s );
    }
    
    public static TextComponent tc( String s ){
        return new TextComponent( chat( s ) );
    }
}
