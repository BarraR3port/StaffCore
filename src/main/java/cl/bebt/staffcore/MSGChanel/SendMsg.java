package cl.bebt.staffcore.MSGChanel;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SendMsg{
    private static final main plugin = main.plugin;

    public static void sendReportAlert( int id , String sender , String target , String reason , String date , String server ){
        if ( plugin.getConfig( ).getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
                return;
            }
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
    }

    public static void sendReportChangeAlert( int id , String changer , String sender , String target , String reason , String date , String status , String server ){
        if ( plugin.getConfig( ).getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
                return;
            }
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
    }

    public static void sendBanAlert( String sender , String target , String reason , Boolean permanent , Boolean Ip , Long amount , String time , String ExpDate , String date , String server ){
        if ( plugin.getConfig( ).getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
                return;
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "Ban" );
            out.writeUTF( sender );
            out.writeUTF( target );
            out.writeUTF( reason );
            out.writeBoolean( permanent );
            out.writeBoolean( Ip );
            out.writeLong( amount );
            out.writeUTF( time );
            out.writeUTF( ExpDate );
            out.writeUTF( date );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
    }

    public static void sendBanChangeAlert( int id , String changer , String sender , String target , String reason , String ExpDate , String date , String status , String server ){
        if ( plugin.getConfig( ).getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
                return;
            }
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
    }

    public static void sendWarnChangeAlert( int id , String changer , String sender , String target , String reason , String ExpDate , String date , String status , String server ){
        if ( plugin.getConfig( ).getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
                return;
            }
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
    }

    public static void sendWarnAlert( String sender , String target , String reason , Long amount , String time , String ExpDate , String date , String server ){
        if ( plugin.getConfig( ).getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
                return;
            }
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
    }

    public static void sendFreezeAlert( String sender , String target , Boolean bool , String server ){
        if ( plugin.getConfig( ).getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
                return;
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "Freeze" );
            out.writeUTF( sender );
            out.writeUTF( target );
            out.writeBoolean( bool );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
    }

    public static void sendWipeAlert( String sender , String target , int bans , int reports , int warns , String server ){
        if ( plugin.getConfig( ).getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
                return;
            }
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
    }

    public static void sendStaffChatMSG( String sender , String msg , String server ){
        if ( plugin.getConfig( ).getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
                return;
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "StaffChat" );
            out.writeUTF( sender );
            out.writeUTF( msg );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:alerts" , out.toByteArray( ) );
        }
    }

    public static void sendStaffListRequest( String sender , String server ){
        if ( plugin.getConfig( ).getBoolean( "bungeecord.enabled" ) ) {
            Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
                return;
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "SendSLRequest" );
            out.writeUTF( sender );
            out.writeUTF( server );
            Bukkit.getServer( ).sendPluginMessage( plugin , "sc:stafflist" , out.toByteArray( ) );
        }
    }

    public static void sendSLPlayersData( String sender , String server ){
        ArrayList < String > players = new ArrayList <>( );
        HashMap < String, String > player_server = new HashMap <>( );
        HashMap < String, String > ping = new HashMap <>( );
        HashMap < String, String > gamemodes = new HashMap <>( );
        for ( Player player : Bukkit.getServer( ).getOnlinePlayers( ) ) {
            if ( player.hasPermission( "staffcore.staff" ) ) {
                players.add( player.getName( ) );
                player_server.put( player.getName( ) , utils.getString( "bungeecord.server" ) );
                ping.put( player.getName( ) , String.valueOf( utils.getPing( player ) ) );
                gamemodes.put( player.getName( ) , player.getGameMode( ).toString( ) );
            }
        }
        String staffMembers = players.toString( );
        String staffMembersServer = player_server.toString( );
        String staffMembersPing = ping.toString( );
        String staffMembersGamemode = gamemodes.toString( );

        Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
        if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
            return;
        }
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

    public static void connectPlayerToServer( String player , String server ){
        Collection < Player > networkPlayers = ( Collection < Player > ) Bukkit.getServer( ).getOnlinePlayers( );
        if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput( );
        out.writeUTF( "ConnectOther" );
        out.writeUTF( player );
        out.writeUTF( server );
        Bukkit.getServer( ).sendPluginMessage( plugin , "BungeeCord" , out.toByteArray( ) );
    }
}
