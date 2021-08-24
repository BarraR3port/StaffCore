/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.MSGChanel;

import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.utils.DataExporter;
import cl.bebt.staffcoreapi.utils.Utils;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

public class SendMsg {
    private static final main plugin = main.plugin;
    
    public static void sendReportAlert( int id , String sender , String target , String reason , String date , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "Report" );
            out.writeInt( id );
            out.writeUTF( sender );
            out.writeUTF( target );
            out.writeUTF( reason );
            out.writeUTF( date );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
        ArrayList < String > dc = new ArrayList <>( );
        dc.add( "**Reporter:** " + sender );
        dc.add( "**Reported:** " + target );
        dc.add( "**Reason:** " + reason );
        dc.add( "**Report Id:** " + id );
        dc.add( "**Server:** " + server );
        Utils.sendDiscordAlertMsg( "⚠ Report Alert ⚠" , dc );
        DataExporter.updateServerStats( UpdateType.REPORT );
    }
    
    public static void sendReportChangeAlert( int id , String changer , String sender , String target , String reason , String date , String status , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "ReportChange" );
            out.writeInt( id );
            out.writeUTF( changer );
            out.writeUTF( sender );
            out.writeUTF( target );
            out.writeUTF( reason );
            out.writeUTF( date );
            out.writeUTF( status );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
        ArrayList < String > dc = new ArrayList <>( );
        dc.add( "**Changer:** " + changer );
        dc.add( "**Reported:** " + target );
        dc.add( "**Reason:** " + reason );
        dc.add( "**Changed to:** " + status );
        dc.add( "**Report Id:** " + id );
        dc.add( "**Server:** " + server );
        Utils.sendDiscordAlertMsg( "⚠ Report Change Alert ⚠" , dc );
    }
    
    public static void sendBanAlert( String sender , String target , String reason , Boolean permanent , Boolean Ip , Date ExpDate , Date CreateDate , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "Ban" );
            out.writeUTF( sender );
            out.writeUTF( target );
            out.writeUTF( reason );
            out.writeBoolean( permanent );
            out.writeBoolean( Ip );
            out.writeUTF( ExpDate.toString( ) );
            out.writeUTF( CreateDate.toString( ) );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
        ArrayList < String > dc = new ArrayList <>( );
        dc.add( "**Banner:** " + sender );
        dc.add( "**Banned:** " + target );
        dc.add( "**Reason:** " + reason );
        dc.add( "**Permanent:** " + permanent );
        dc.add( "**Period:** " + Utils.getTimeLeft( new Date( ) , ExpDate ) );
        dc.add( "**Exp Date:** " + ExpDate );
        dc.add( "**Server:** " + server );
        Utils.sendDiscordAlertMsg( "⚠ Ban Alert ⚠" , dc );
        DataExporter.updateServerStats( UpdateType.BAN );
    }
    
    public static void sendBanChangeAlert( int id , String changer , String sender , String target , String reason , String ExpDate , String date , String status , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "BanChange" );
            out.writeInt( id );
            out.writeUTF( changer );
            out.writeUTF( sender );
            out.writeUTF( target );
            out.writeUTF( reason );
            out.writeUTF( ExpDate );
            out.writeUTF( date );
            out.writeUTF( status );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
        ArrayList < String > dc = new ArrayList <>( );
        dc.add( "**Changer:** " + changer );
        dc.add( "**Banned:** " + target );
        dc.add( "**Reason:** " + reason );
        dc.add( "**Exp Date:** " + ExpDate );
        dc.add( "**Status:** " + status );
        dc.add( "**Ban Id:** " + id );
        dc.add( "**Server:** " + server );
        Utils.sendDiscordAlertMsg( "⚠Ban Change Alert ⚠" , dc );
    }
    
    public static void sendWarnAlert( String sender , String target , String reason , Integer amount , String time , String ExpDate , String date , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "Warn" );
            out.writeUTF( sender );
            out.writeUTF( target );
            out.writeUTF( reason );
            out.writeLong( amount );
            out.writeUTF( time );
            out.writeUTF( ExpDate );
            out.writeUTF( date );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
        ArrayList < String > dc = new ArrayList <>( );
        dc.add( "**Warner:** " + sender );
        dc.add( "**Warned:** " + target );
        dc.add( "**Reason:** " + reason );
        dc.add( "**Period:** " + amount + time );
        dc.add( "**Exp Date:** " + ExpDate );
        dc.add( "**Server:** " + server );
        Utils.sendDiscordAlertMsg( "⚠ Warn Alert ⚠" , dc );
        DataExporter.updateServerStats( UpdateType.WARN );
    }
    
    public static void sendWarnChangeAlert( int id , String changer , String sender , String target , String reason , String ExpDate , String date , String status , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "WarnChange" );
            out.writeInt( id );
            out.writeUTF( changer );
            out.writeUTF( sender );
            out.writeUTF( target );
            out.writeUTF( reason );
            out.writeUTF( ExpDate );
            out.writeUTF( date );
            out.writeUTF( status );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
        ArrayList < String > dc = new ArrayList <>( );
        dc.add( "**Changer:** " + changer );
        dc.add( "**Banned:** " + target );
        dc.add( "**Reason:** " + reason );
        dc.add( "**Exp Date:** " + ExpDate );
        dc.add( "**Status:** " + status );
        dc.add( "**Id:** " + id );
        dc.add( "**Server:** " + server );
        Utils.sendDiscordAlertMsg( "⚠ Warn Change Alert ⚠" , dc );
        DataExporter.updateServerStats( UpdateType.WARN );
    }
    
    public static void sendFreezeAlert( String sender , String target , Boolean bool , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "Freeze" );
            out.writeUTF( sender );
            out.writeUTF( target );
            out.writeBoolean( bool );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
        ArrayList < String > dc = new ArrayList <>( );
        dc.add( "**Freezer:** " + sender );
        dc.add( "**Frozen:** " + target );
        dc.add( "**Set to:** " + bool );
        dc.add( "**Server:** " + server );
        Utils.sendDiscordAlertMsg( "⚠ Freeze Alert ⚠" , dc );
        DataExporter.updateServerStats( UpdateType.FREEZE );
    }
    
    public static void sendWipeAlert( String sender , String target , int bans , int reports , int warns , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "Wipe" );
            out.writeUTF( sender );
            out.writeUTF( target );
            out.writeInt( bans );
            out.writeInt( reports );
            out.writeInt( warns );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
        ArrayList < String > dc = new ArrayList <>( );
        dc.add( "**Wiper:** " + sender );
        dc.add( "**Wiped:** " + target );
        dc.add( "**Bans Wiped:** " + bans );
        dc.add( "**Reports Wiped:** " + reports );
        dc.add( "**WarnsQuery Wiped:** " + warns );
        dc.add( "**Server:** " + server );
        Utils.sendDiscordAlertMsg( "⚠ Wipe Alert ⚠" , dc );
        DataExporter.updateServerStats( UpdateType.WIPE );
    }
    
    public static void sendStaffChatMSG( String sender , String msg , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "StaffChat" );
            out.writeUTF( sender );
            out.writeUTF( msg );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
        ArrayList < String > dc = new ArrayList <>( );
        dc.add( "**[SC][" + server + "]**" + sender + ": " + msg );
        Utils.sendDiscordAlertMsg( "⚠ Staff Chat ⚠" , dc );
    }
    
    public static void sendStaffListRequest( String sender , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "SendSLRequest" );
            out.writeUTF( sender );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:stafflist" , out.toByteArray( ) );
        }
    }
    
    public static void sendSLPlayersData( String sender , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            ArrayList < String > players = new ArrayList <>( );
            HashMap < String, String > player_server = new HashMap <>( );
            HashMap < String, String > ping = new HashMap <>( );
            HashMap < String, String > gamemodes = new HashMap <>( );
            for ( Player player : Bukkit.getServer( ).getOnlinePlayers( ) ) {
                if ( player.hasPermission( "staffcore.staff" ) ) {
                    players.add( player.getName( ) );
                    player_server.put( player.getName( ) , Utils.getString( "bungeecord.server" ) );
                    ping.put( player.getName( ) , String.valueOf( Utils.getPing( player ) ) );
                    gamemodes.put( player.getName( ) , player.getGameMode( ).toString( ) );
                }
            }
            String staffMembers = players.toString( );
            String staffMembersServer = player_server.toString( );
            String staffMembersPing = ping.toString( );
            String staffMembersGamemode = gamemodes.toString( );
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "SendData" );
            out.writeUTF( sender );
            out.writeUTF( server );
            out.writeUTF( staffMembers );
            out.writeUTF( staffMembersServer );
            out.writeUTF( staffMembersPing );
            out.writeUTF( staffMembersGamemode );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:stafflist" , out.toByteArray( ) );
        }
    }
    
    public static void connectPlayerToServer( String player , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "ConnectOther" );
            out.writeUTF( player );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "BungeeCord" , out.toByteArray( ) );
        }
        ArrayList < String > dc = new ArrayList <>( );
        dc.add( "**The player:** " + player + " has connected to " + server );
        Utils.sendDiscordAlertMsg( "⚠ Player Changed Server ⚠" , dc );
    }
    
    public static void helpOp( String player , String reason , String server ){
        if ( Utils.getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) )
                return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "HelpOp" );
            out.writeUTF( player );
            out.writeUTF( reason );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
        ArrayList < String > dc = new ArrayList <>( );
        dc.add( "**Player:** " + player );
        dc.add( "**Reason:** " + reason );
        dc.add( "**Server:** " + server );
        Utils.sendDiscordAlertMsg( "⚠ HelpOp Alert ⚠" , dc );
    }
}
