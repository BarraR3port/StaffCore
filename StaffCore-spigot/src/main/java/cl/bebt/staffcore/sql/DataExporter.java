/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

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
        Bukkit.getScheduler( ).runTaskAsynchronously( plugin , ( ) -> {
            if ( Objects.equals( plugin.stats.getConfig( ).getString( "server_uuid" ) , "" ) ) {
                createServerStats( );
            }
        } );
    }
    
    public static void updateServerStats( String type ){
        JSONObject jsonMessage = new JSONObject( );
        String UUID = main.plugin.stats.getConfig( ).getString( "server_uuid" );
        String Version = main.plugin.getDescription( ).getVersion( );
        jsonMessage.put( "type" , "updateServerStats" );
        jsonMessage.put( "UUID" , UUID );
        jsonMessage.put( "ServerName" , utils.getWebServer( ) );
        jsonMessage.put( "Version" , Version );
        if ( type.equalsIgnoreCase( "ban" ) ) {
            jsonMessage.put( "updateType" , "Bans" );
        } else if ( type.equalsIgnoreCase( "report" ) ) {
            jsonMessage.put( "updateType" , "Reports" );
        } else if ( type.equalsIgnoreCase( "warn" ) ) {
            jsonMessage.put( "updateType" , "Warns" );
        } else if ( type.equalsIgnoreCase( "wipe" ) ) {
            jsonMessage.put( "updateType" , "Wipes" );
        } else if ( type.equalsIgnoreCase( "player" ) ) {
            jsonMessage.put( "updateType" , "Players" );
        } else if ( type.equalsIgnoreCase( "mute" ) ) {
            jsonMessage.put( "updateType" , "Mutes" );
        } else if ( type.equalsIgnoreCase( "frozen" ) ) {
            jsonMessage.put( "updateType" , "Frozen" );
        } else if ( type.equalsIgnoreCase( "staff" ) ) {
            jsonMessage.put( "updateType" , "Staff" );
        } else if ( type.equalsIgnoreCase( "vanish" ) ) {
            jsonMessage.put( "updateType" , "Vanish" );
        }
        String convertedString1 = Base64.getEncoder( ).withoutPadding( ).encodeToString( jsonMessage.toString( ).getBytes( ) );
        String convertedString2 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString1.getBytes( ) );
        String convertedString3 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString2.getBytes( ) );
        Http.exportNewServerData( "http://localhost:82/api/stats/" + convertedString3 );
    }
    
    
    public static void createServerStats( ){
        String uuid = UUID.randomUUID( ).toString( );
        JSONObject jsonMessage = new JSONObject( );
        String Version = main.plugin.getDescription( ).getVersion( );
        int CurrentBans = 0;
        int CurrentReports = 0;
        int CurrentWarns = 0;
        int CurrentPlayers = 0;
        int CurrentFrozen = 0;
        int CurrentStaff = 0;
        int CurrentVanished = 0;
        int CurrentWipes = 0;
        int CurrentMutes = 0;
        if ( utils.mysqlEnabled( ) ) {
            HashMap < String, Integer > serverStatus = ServerQuery.getServerStatus( );
            CurrentBans = serverStatus.get( "CurrentBans" );
            CurrentReports = serverStatus.get( "CurrentReports" );
            CurrentWarns = serverStatus.get( "CurrentWarns" );
            CurrentPlayers = serverStatus.get( "CurrentPlayers" );
            CurrentFrozen = serverStatus.get( "CurrentFrozen" );
            CurrentStaff = serverStatus.get( "CurrentStaff" );
            CurrentVanished = serverStatus.get( "CurrentVanished" );
        } else {
            CurrentBans = utils.count( "bans" );
            CurrentReports = utils.count( "reports" );
            CurrentWarns = utils.count( "warns" );
            CurrentPlayers = utils.getUsers( ).size( );
        }
        
        jsonMessage.put( "UUID" , uuid );
        jsonMessage.put( "Version" , Version );
        jsonMessage.put( "ServerName" , utils.getWebServer( ) );
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
        Http.registerServer( "http://localhost:82/api/stats/" + convertedString3 , uuid );
    }
}
