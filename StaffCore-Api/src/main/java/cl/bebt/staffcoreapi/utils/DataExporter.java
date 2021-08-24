/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.utils;

import cl.bebt.staffcoreapi.EntitiesUtils.ServerSettingsUtils;
import cl.bebt.staffcoreapi.EntitiesUtils.SqlUtils;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.SQL.Queries.ServerQuery;
import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

public class DataExporter {
    
    public DataExporter( ){
        if ( !ServerSettingsUtils.settingsExists( ) ) {
            createServerStats( );
        }
    }
    
    public static void updateServerStats( UpdateType type ){
        JSONObject jsonMessage = new JSONObject( );
        String UUID = ServerSettingsUtils.getSettings( ).getServerUUID( ).toString( );
        String Version = Utils.getServerVersion( );
        jsonMessage.put( "type" , "updateServerStats" );
        jsonMessage.put( "UUID" , UUID );
        jsonMessage.put( "ServerName" , ServerSettingsUtils.getSettings( ).getServerName( ) );
        jsonMessage.put( "Version" , Version );
        switch (type) {
            case BAN -> jsonMessage.put( "updateType" , "Bans" );
            case MUTE -> jsonMessage.put( "updateType" , "Mutes" );
            case WARN -> jsonMessage.put( "updateType" , "Warns" );
            case FREEZE -> jsonMessage.put( "updateType" , "Frozen" );
            case STAFF -> jsonMessage.put( "updateType" , "Staff" );
            case WIPE -> jsonMessage.put( "updateType" , "Wipes" );
            case PLAYER -> jsonMessage.put( "updateType" , "Players" );
            case REPORT -> jsonMessage.put( "updateType" , "Reports" );
            case VANISH -> jsonMessage.put( "updateType" , "Vanish" );
            case INITIALIZATION -> jsonMessage.put( "updateType" , "Initialization" );
        }
        String convertedString1 = Base64.getEncoder( ).withoutPadding( ).encodeToString( jsonMessage.toString( ).getBytes( ) );
        String convertedString2 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString1.getBytes( ) );
        String convertedString3 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString2.getBytes( ) );
        Http.exportNewServerData( "http://localhost:82/api/stats/" + convertedString3 );
    }
    
    
    public static void createServerStats( ){
        String uuid = UUID.randomUUID( ).toString( );
        JSONObject jsonMessage = new JSONObject( );
        String Version = Utils.getPluginVersion( );
        String DefaultName = "DefaultName";
        int CurrentBans = 0;
        int CurrentReports = 0;
        int CurrentWarns = 0;
        int CurrentPlayers = 0;
        int CurrentFrozen = 0;
        int CurrentStaff = 0;
        int CurrentVanished = 0;
        int CurrentWipes = 0;
        int CurrentMutes = 0;
        if ( SqlUtils.isEnabled( ) ) {
            HashMap < String, Integer > serverStatus = ServerQuery.getServerStatus( );
            CurrentBans = serverStatus.get( "CurrentBans" );
            CurrentReports = serverStatus.get( "CurrentReports" );
            CurrentWarns = serverStatus.get( "CurrentWarns" );
            CurrentPlayers = serverStatus.get( "CurrentPlayers" );
            CurrentFrozen = serverStatus.get( "CurrentFrozen" );
            CurrentStaff = serverStatus.get( "CurrentStaff" );
            CurrentVanished = serverStatus.get( "CurrentVanished" );
        } else {
            CurrentBans = Utils.count( UpdateType.BAN );
            CurrentReports = Utils.count( UpdateType.REPORT );
            CurrentWarns = Utils.count( UpdateType.WARN );
            CurrentPlayers = Utils.getUsers( ).size( );
        }
        jsonMessage.put( "UUID" , uuid );
        jsonMessage.put( "Version" , Version );
        jsonMessage.put( "ServerName" , DefaultName );
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
        ServerSettingsUtils.createServerSettings( "DefaultName" , UUID.fromString( uuid ) );
    }
}
