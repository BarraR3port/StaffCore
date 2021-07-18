package cl.bebt.staffcore.utils.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class UUIDGetter {
    
    private static final Gson gson = new GsonBuilder( ).registerTypeAdapter( UUIDGetter.class , new UUIDTypeAdapter( ) ).create( );
    
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";
    
    @SuppressWarnings("deprecation")
    public static UUID getUUID( String name ){
        name = name.toLowerCase( );
        
        try {
            if ( Bukkit.getOnlineMode( ) ) {
                HttpURLConnection connection = ( HttpURLConnection ) new URL(
                        String.format( UUID_URL , name , System.currentTimeMillis( ) / 1000 ) ).openConnection( );
                connection.setReadTimeout( 5000 );
                
                PlayerUUID player = gson.fromJson( new BufferedReader( new InputStreamReader( connection.getInputStream( ) ) ) , PlayerUUID.class );
                
                return player.getId( );
            } else {
                return Bukkit.getOfflinePlayer( name ).getUniqueId( );
            }
        } catch ( Exception e ) {
            Bukkit.getConsoleSender( ).sendMessage( "§cYour server has no connection to the mojang servers or is runnig slow." );
            Bukkit.getConsoleSender( ).sendMessage( "§cTherefore the UUID cannot be parsed." );
            return null;
        }
    }
    
    static class PlayerUUID {
        
        private String name;
        private java.util.UUID id;
        
        public String getName( ){
            return name;
        }
        
        public UUID getId( ){
            return id;
        }
        
    }
    
}
