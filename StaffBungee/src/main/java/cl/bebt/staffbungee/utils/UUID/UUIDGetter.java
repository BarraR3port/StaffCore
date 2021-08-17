/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffbungee.utils.UUID;

import cl.bebt.staffbungee.main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class UUIDGetter {
    
    private static final Gson gson = new GsonBuilder( ).registerTypeAdapter( UUIDGetter.class , new UUIDTypeAdapter( ) ).create( );
    
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";
    
    public static UUID getUUID( String name ){
        name = name.toLowerCase( );
        try {
            for ( ProxiedPlayer p : main.plugin.getProxy( ).getPlayers( ) ) {
                if ( name.equalsIgnoreCase( p.getName( ) ) ) {
                    return p.getUniqueId( );
                }
            }
            
            HttpURLConnection connection = ( HttpURLConnection ) new URL(
                    String.format( UUID_URL , name , System.currentTimeMillis( ) / 1000 ) ).openConnection( );
            connection.setReadTimeout( 5000 );
            
            PlayerUUID player = gson.fromJson( new BufferedReader( new InputStreamReader( connection.getInputStream( ) ) ) , PlayerUUID.class );
            
            return player.getId( );
            
        } catch ( Exception e ) {
            return null;
        }
    }
    
    static class PlayerUUID {
        
        private String name;
        private UUID id;
        
        public String getName( ){
            return name;
        }
        
        public UUID getId( ){
            return id;
        }
        
    }
    
}
