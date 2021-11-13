package cl.bebt.staffcore;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.MSGChanel.PluginMessage;
import cl.bebt.staffcore.commands.Staff.*;
import cl.bebt.staffcore.commands.Staff.Mysql.FreezeMysql;
import cl.bebt.staffcore.commands.Staff.Mysql.StaffMysql;
import cl.bebt.staffcore.commands.Staff.Mysql.ToggleStaffChatMysql;
import cl.bebt.staffcore.commands.Staff.Mysql.VanishMysql;
import cl.bebt.staffcore.commands.Suicide;
import cl.bebt.staffcore.commands.Time.Day;
import cl.bebt.staffcore.commands.Time.Night;
import cl.bebt.staffcore.commands.Time.Weather;
import cl.bebt.staffcore.commands.staffcore;
import cl.bebt.staffcore.configs.*;
import cl.bebt.staffcore.configs.Lenguajes.EN_NA;
import cl.bebt.staffcore.configs.Lenguajes.ES_CL;
import cl.bebt.staffcore.configs.Lenguajes.FR;
import cl.bebt.staffcore.listeners.*;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.listeners.MenuListener;
import cl.bebt.staffcore.sql.DataExporter;
import cl.bebt.staffcore.sql.Mysql;
import cl.bebt.staffcore.sql.Queries.StaffQuery;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class main extends JavaPlugin {
    
    private static final HashMap < Player, PlayerMenuUtility > playerMenuUtilityMap = new HashMap <>( );
    
    public static main plugin;
    
    public static List < String > staffMembers = new ArrayList <>( );
    
    public static List < String > toggledStaffChat = new ArrayList <>( );
    
    public static HashMap < String, String > playersServerMap = new HashMap <>( );
    
    public static HashMap < String, String > playersServerPingMap = new HashMap <>( );
    
    public static HashMap < String, String > playersServerGamemodesMap = new HashMap <>( );
    
    public static HashMap < String, String > playerSkins = new HashMap <>( );
    
    public static HashMap < Player, Player > invSee = new HashMap <>( );
    
    public static HashMap < Player, Player > enderSee = new HashMap <>( );
    
    public AltsStorage alts;
    
    public BanConfig bans;
    
    public ReportConfig reports;
    
    public WarnConfig warns;
    
    public SQLGetter data;
    
    public EN_NA en_na;
    
    public ES_CL es_cl;
    
    public FR fr;
    
    public StatsConfig stats;
    
    public ItemsConfig items;
    
    public AlertsConfig alerts;
    
    public MenusConfig menus;
    
    public Boolean chatMuted = false;
    
    public String latestVersion;
    
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
        new UpdateChecker( plugin ).getLatestVersion( version -> {
            if ( version != null && !version.isEmpty() && !getDescription( ).getVersion( ).equals( version ) ) {
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&c     Hey, there is a new version out!" ) );
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&b         Staff-Core " + version ) );
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
                if ( utils.getBoolean( "disable_outdated_plugin" ) ) {
                    c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&4&lDISABLING STAFF-CORE. USE THE LATEST VERSION " ) );
                    c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&4&lYou can disable this option (disable_outdated_plugin) in the config file." ) );
                    c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&4&lBut its recommend to use the latest version: &6" + version ) );
                    c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
                    plugin.getPluginLoader( ).disablePlugin( this );
                } else {
                    try{
                        Bukkit.getServer( ).getScheduler( ).scheduleSyncRepeatingTask( this , ( ) -> {
                            for ( Player p : Bukkit.getOnlinePlayers( ) ) {
                                if ( p.hasPermission( "staffcore.staff" ) ) {
                                    utils.tellHover( p , getConfig( ).getString( "server_prefix" ) +
                                                    "&cYou are using an StaffCore older version" ,
                                            "&aClick to download the version: " + latestVersion ,
                                            "https://staffcore.glitch.me/download" );
                                }
                            }
                        } , 0L , 12000L );
                    }catch ( IllegalPluginAccessException ignored ){
                    }
                }
            }
        } );
        loadConfigManager( );
        new StaffCoreAPI( plugin );
        new wipePlayer( plugin );
        new utils( plugin );
        new SetStaffItems( plugin );
        new FreezePlayer( );
        new SetVanish( );
        new ToggleChat( );
        new CountdownManager( );
        new Http( plugin );
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
        Bukkit.getPluginManager( ).registerEvents( new onPlayerJoin( plugin ) , plugin );
        Bukkit.getPluginManager( ).registerEvents( new onChat( ) , plugin );
        if ( !utils.isOlderVersion( ) ) {
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&a    Plugin &5" + getName( ) + "&a&l ACTIVATED" ) );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&a         Staff-Core: &6" + getDescription( ).getVersion( ) ) );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
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
                        if ( CountdownManager.checkMuteCountdown( players ) ) {
                            long remaining = CountdownManager.getMuteCountDown( players );
                            if ( remaining == 0 || remaining == 1 ) {
                                utils.PlaySound( players , "muted_try_to_chat" );
                                utils.tell( players , getConfig( ).getString( "server_prefix" ) + "&aYou were UnMuted!" );
                            }
                        }
                    } catch ( NullPointerException | ArrayIndexOutOfBoundsException | IllegalStateException ignored ) {
                    }
                }
            } , 0L , 20L );
        } else {
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------------------" ) );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&4&l              STAFF-CORE." ) );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&4&lRunning STAFF-CORE in a old version Server." ) );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------------------" ) );
        }
        if ( getConfig( ).getBoolean( "mysql.enabled" ) ) {
            try {
                Mysql.connect( );
                SQLGetter.createTables( );
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "             &a&lMysql: &aTRUE" ) );
                StaffQuery.addToggledPlayersToList( );
            } catch ( SQLException e ) {
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "             &a&lMysql: &cFALSE" ) );
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&a      Disabling Staff-Core &6" + getDescription( ).getVersion( ) ) );
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
                getServer( ).getPluginManager( ).disablePlugin( plugin );
                return;
            }
        } else {
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "             &a&lMysql: &cFALSE" ) );
        }
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
        getServer( ).getMessenger( ).registerOutgoingPluginChannel( plugin , "sc:alerts" );
        getServer( ).getMessenger( ).registerIncomingPluginChannel( plugin , "sc:stafflist" , new PluginMessage( ) );
        getServer( ).getMessenger( ).registerIncomingPluginChannel( plugin , "sc:alerts" , new PluginMessage( ) );
        getServer( ).getMessenger( ).registerOutgoingPluginChannel( plugin , "sc:stafflist" );
        getServer( ).getMessenger( ).registerIncomingPluginChannel( plugin , "BungeeCord" , new PluginMessage( ) );
        getServer( ).getMessenger( ).registerOutgoingPluginChannel( plugin , "BungeeCord" );
        Bukkit.getScheduler( ).runTaskAsynchronously( this , ( ) -> playerSkins = utils.getSavedSkins( ) );
        if ( Mysql.isConnected( ) ) {
            new VanishMysql( plugin );
            new FreezeMysql( plugin );
            new ToggleStaffChatMysql( plugin );
            new StaffMysql( plugin );
        } else {
            new Vanish( plugin );
            new Freeze( plugin );
            new ToggleStaffChat( plugin );
            new Staff( plugin );
        }
        new DataExporter( this );
    }
    
    public void onDisable( ){
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&a    Plugin &5" + plugin.getName( ) + "&4&l DISABLED" ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&a         Staff-Core: &5" + plugin.getDescription( ).getVersion( ) ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
        if ( getConfig( ).getBoolean( "mysql.enabled" ) ) {
            Mysql.disconnect( );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&c      Disconnected from the database!" ) );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" ) );
        }
    }
    
    public void loadConfigManager( ){
        saveDefaultConfig( );
        reloadConfig( );
        this.data = new SQLGetter( this );
        this.reports = new ReportConfig( this );
        this.reports.saveDefaultConfig( );
        this.reports.reloadConfig( );
        this.bans = new BanConfig( plugin );
        this.bans.saveDefaultConfig( );
        this.bans.reloadConfig( );
        this.alts = new AltsStorage( plugin );
        this.alts.saveDefaultConfig( );
        this.alts.reloadConfig( );
        this.warns = new WarnConfig( plugin );
        this.warns.saveDefaultConfig( );
        this.warns.reloadConfig( );
        this.en_na = new EN_NA( plugin );
        this.en_na.saveDefaultConfig( );
        this.en_na.reloadConfig( );
        this.es_cl = new ES_CL( plugin );
        this.es_cl.saveDefaultConfig( );
        this.es_cl.reloadConfig( );
        this.fr = new FR( plugin );
        this.fr.saveDefaultConfig( );
        this.fr.reloadConfig( );
        this.stats = new StatsConfig( plugin );
        this.stats.saveDefaultConfig( );
        this.stats.reloadConfig( );
        this.items = new ItemsConfig( plugin );
        this.items.saveDefaultConfig( );
        this.items.reloadConfig( );
        this.alerts = new AlertsConfig( plugin );
        this.alerts.saveDefaultConfig( );
        this.alerts.reloadConfig( );
        this.menus = new MenusConfig( plugin );
        this.menus.saveDefaultConfig( );
        this.menus.reloadConfig( );
    }
    
}
