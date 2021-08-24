/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore;

import cl.bebt.staffcore.Items.Items;
import cl.bebt.staffcore.MSGChanel.PluginMessage;
import cl.bebt.staffcore.commands.Staff.*;
import cl.bebt.staffcore.commands.Suicide;
import cl.bebt.staffcore.commands.Time.Day;
import cl.bebt.staffcore.commands.Time.Night;
import cl.bebt.staffcore.commands.Time.Weather;
import cl.bebt.staffcore.commands.staffcore;
import cl.bebt.staffcore.listeners.*;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.listeners.MenuListener;
import cl.bebt.staffcore.utils.*;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.EntitiesUtils.SqlUtils;
import cl.bebt.staffcoreapi.SQL.SqlManager;
import cl.bebt.staffcoreapi.utils.TPS;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class main extends JavaPlugin {
    
    private static final HashMap < Player, PlayerMenuUtility > playerMenuUtilityMap = new HashMap <>( );
    
    public static main plugin;
    
    public static List < String > staffMembers = new ArrayList <>( );
    
    public static HashMap < String, String > playersServerMap = new HashMap <>( );
    
    public static HashMap < String, String > playersServerPingMap = new HashMap <>( );
    
    public static HashMap < String, String > playersServerGamemodesMap = new HashMap <>( );
    
    public static HashMap < Player, Player > invSee = new HashMap <>( );
    
    public static HashMap < Player, Player > enderSee = new HashMap <>( );
    
    public Boolean chatMuted = false;
    
    ConsoleCommandSender c = Bukkit.getConsoleSender( );
    
    public static PlayerMenuUtility getPlayerMenuUtility( Player p ){
        if ( playerMenuUtilityMap.containsKey( p ) )
            return playerMenuUtilityMap.get( p );
        PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility( p );
        playerMenuUtilityMap.put( p , playerMenuUtility );
        return playerMenuUtility;
    }
    
    public void onEnable( ){
        plugin = this;
        Api.initializeSpigot( this );
        new wipePlayer( plugin );
        new FreezeManager( plugin );
        new ToggleChat( );
        new CountdownManager( );
        new StaffManager( plugin );
        new VanishManager( plugin );
        new FlyManager( plugin );
        new Items( plugin );
        new Ip( plugin );
        new Teleport( plugin );
        new TpAll( plugin );
        new GmCreative( plugin );
        new GmSurvival( plugin );
        new Heal( plugin );
        new Ping( plugin );
        new Day( plugin );
        new Night( plugin );
        new ClearChat( plugin );
        new Weather( plugin );
        new Suicide( plugin );
        new Metrics( plugin , 8871 );
        new wipe( plugin );
        new unBan( plugin );
        new Ban( plugin );
        new Bans( plugin );
        new MutePlayer( plugin );
        new MuteChat( plugin );
        new unMute( plugin );
        new invSeeChest( plugin );
        new invSeeEnder( plugin );
        new ReportList( plugin );
        new staffcore( plugin );
        new StaffChat( plugin );
        new Report( plugin );
        new CheckAlts( plugin );
        new ReportPlayer( plugin );
        new StaffList( plugin );
        new Warn( plugin );
        new Warnings( plugin );
        new HelpOp( plugin );
        new TrollMode( plugin );
        new Fly( plugin );
        new Staff( plugin );
        new Vanish( plugin );
        new Freeze( plugin );
        new ToggleStaffChat( plugin );
        Bukkit.getPluginManager( ).registerEvents( new onPlayerJoin( plugin ) , plugin );
        Bukkit.getPluginManager( ).registerEvents( new onChat( ) , plugin );
        if ( !Utils.isOlderVersion( ) ) {
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&a    Plugin &5" + getName( ) + "&a&l ACTIVATED" ) );
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&a         Staff-Core: &6" + getDescription( ).getVersion( ) ) );
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
            Bukkit.getPluginManager( ).registerEvents( new GetReportMessage( plugin ) , plugin );
            Bukkit.getPluginManager( ).registerEvents( new MenuListener( ) , plugin );
            Bukkit.getPluginManager( ).registerEvents( new FreezeListeners( plugin ) , plugin );
            Bukkit.getPluginManager( ).registerEvents( new InventoryListeners( plugin ) , plugin );
            Bukkit.getPluginManager( ).registerEvents( new onPLayerLeave( plugin ) , plugin );
            Bukkit.getServer( ).getScheduler( ).scheduleSyncRepeatingTask( this , new TPS( ) , 100L , 1L );
            Bukkit.getServer( ).getScheduler( ).scheduleAsyncRepeatingTask( this , ( ) -> {
                for ( Player players : Bukkit.getOnlinePlayers( ) ) {
                    try {
                        if ( players.getOpenInventory( ).getTopInventory( ).getItem( 31 ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "server" ) , PersistentDataType.STRING ) ) {
                            players.getOpenInventory( ).getTopInventory( ).setItem( 31 , Items.ServerStatus( ) );
                            players.getOpenInventory( ).getTopInventory( ).setItem( 13 , Items.PlayerStats( players ) );
                        }
                        if ( CountdownManager.checkMuteCountdown( players.getUniqueId( ) ) ) {
                            long remaining = CountdownManager.getMuteCountDown( players.getUniqueId( ) );
                            if ( remaining == 0 || remaining == 1 ) {
                                Utils.PlaySound( players , "muted_try_to_chat" );
                                Utils.tell( players , getConfig( ).getString( "server_prefix" ) + "&aYou were UnMuted!" );
                            }
                        }
                    } catch ( NullPointerException | ArrayIndexOutOfBoundsException | IllegalStateException ignored ) {
                    }
                }
            } , 0L , 20L );
        } else {
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------------------" ) );
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&4&l              STAFF-CORE." ) );
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&4&lRunning STAFF-CORE in a old version Server." ) );
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------------------" ) );
        }
        if ( getConfig( ).getBoolean( "mysql.enabled" ) ) {
            try {
                SqlUtils.connect( );
                SqlManager.createTables( );
                c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "             &a&lMysql: &aTRUE" ) );
                //StaffQuery.addToggledPlayersToList( );
            } catch ( SQLException e ) {
                c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "             &a&lMysql: &cFALSE" ) );
                c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&a      Disabling Staff-Core &6" + getDescription( ).getVersion( ) ) );
                c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
                getServer( ).getPluginManager( ).disablePlugin( plugin );
                return;
            }
        } else {
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "             &a&lMysql: &cFALSE" ) );
        }
        c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
        getServer( ).getMessenger( ).registerOutgoingPluginChannel( plugin , "sc:alerts" );
        getServer( ).getMessenger( ).registerIncomingPluginChannel( plugin , "sc:stafflist" , new PluginMessage( ) );
        getServer( ).getMessenger( ).registerIncomingPluginChannel( plugin , "sc:alerts" , new PluginMessage( ) );
        getServer( ).getMessenger( ).registerOutgoingPluginChannel( plugin , "sc:stafflist" );
        getServer( ).getMessenger( ).registerIncomingPluginChannel( plugin , "BungeeCord" , new PluginMessage( ) );
        getServer( ).getMessenger( ).registerOutgoingPluginChannel( plugin , "BungeeCord" );
    }
    
    public void onDisable( ){
        c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
        c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&a    Plugin &5" + plugin.getName( ) + "&4&l DISABLED" ) );
        c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
        c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&a         Staff-Core: &5" + plugin.getDescription( ).getVersion( ) ) );
        c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
        if ( getConfig( ).getBoolean( "mysql.enabled" ) ) {
            SqlUtils.disconnect( );
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&c      Disconnected from the database!" ) );
            c.sendMessage( Utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
        }
    }
    
}
