package cl.bebt.staffcore.sql;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Queries.ServerQuery;
import cl.bebt.staffcore.utils.Http;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class DataExporter {
    
    public DataExporter( main plugin ){
        Bukkit.getScheduler( ).scheduleAsyncRepeatingTask( plugin, ()->{
            if ( Objects.equals( plugin.stats.getConfig( ).getString( "server_uuid" ) , "" ) ){
                createServerStats();
            }
        }, 0L, 6000L );
    }
    
    public static void updateServerStats( String type ){
        JSONObject jsonMessage = new JSONObject( );
        String UUID = main.plugin.stats.getConfig().getString("server_uuid");
        jsonMessage.put( "type" , "updateServerStats" );
        jsonMessage.put( "UUID" , UUID );
        if (type.equalsIgnoreCase( "ban" ) ){
            jsonMessage.put( "updateType" , "Bans" );
        } else if (type.equalsIgnoreCase( "report" ) ){
            jsonMessage.put( "updateType" , "Reports" );
        } else if (type.equalsIgnoreCase( "warn" ) ){
            jsonMessage.put( "updateType" , "Warns" );
        } else if (type.equalsIgnoreCase( "wipe" ) ){
            jsonMessage.put( "updateType" , "Wipes" );
        } else if (type.equalsIgnoreCase( "player" ) ){
            jsonMessage.put( "updateType" , "Players" );
        } else if (type.equalsIgnoreCase( "mute" ) ){
            jsonMessage.put( "updateType" , "Mutes" );
        } else if (type.equalsIgnoreCase( "frozen" ) ){
            jsonMessage.put( "updateType" , "Frozen" );
        } else if (type.equalsIgnoreCase( "staff" ) ){
            jsonMessage.put( "updateType" , "Staff" );
        } else if (type.equalsIgnoreCase( "vanish" ) ){
            jsonMessage.put( "updateType" , "Vanish" );
        }
        String convertedString1 = Base64.getEncoder( ).withoutPadding( ).encodeToString( jsonMessage.toString( ).getBytes( ) );
        String convertedString2 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString1.getBytes( ) );
        String convertedString3 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString2.getBytes( ) );
        Http.exportNewServerData( "https://staffcore.glitch.me/api/stats/"+ convertedString3);
    }
    
    
    private void createServerStats(){
        if ( utils.mysqlEnabled( ) ){
            String uuid = UUID.randomUUID( ).toString();
            HashMap < String, Integer > serverStatus = ServerQuery.getServerStatus();
            int CurrentBans = serverStatus.get( "CurrentBans" );
            int CurrentReports = serverStatus.get( "CurrentReports" );
            int CurrentWarns = serverStatus.get( "CurrentWarns" );
            int CurrentPlayers = serverStatus.get( "CurrentPlayers" );
            int CurrentFrozen = serverStatus.get( "CurrentFrozen" );
            int CurrentStaff = serverStatus.get( "CurrentStaff" );
            int CurrentVanished = serverStatus.get( "CurrentVanished" );
            int CurrentWipes = 0;
            int CurrentMutes = 0;
            
            JSONObject jsonMessage = new JSONObject( );
            jsonMessage.put( "UUID" , uuid );
            jsonMessage.put( "ServerName" , utils.getWebServer() );
            jsonMessage.put( "type" , "createServerStats" );
            jsonMessage.put( "CurrentBans" , CurrentBans );
            jsonMessage.put( "CurrentReports" , CurrentReports );
            jsonMessage.put( "CurrentWarns" , CurrentWarns );
            jsonMessage.put( "CurrentPlayers" , CurrentPlayers );
            jsonMessage.put( "CurrentFrozen" , CurrentFrozen );
            jsonMessage.put( "CurrentStaff" , CurrentStaff );
            jsonMessage.put( "CurrentVanished" , CurrentVanished );
            jsonMessage.put( "CurrentWipes" , CurrentWipes );
            jsonMessage.put( "CurrentMutes" , CurrentMutes );
            String convertedString1 = Base64.getEncoder( ).withoutPadding( ).encodeToString( jsonMessage.toString( ).getBytes( ) );
            String convertedString2 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString1.getBytes( ) );
            String convertedString3 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString2.getBytes( ) );
            Http.registerServer( "https://staffcore.glitch.me/api/stats/"+ convertedString3, uuid);
        }
    }
}
