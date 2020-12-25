package cl.bebt.staffcore;

import cl.bebt.staffcore.API.API;
import cl.bebt.staffcore.API.Server.Metrics;
import cl.bebt.staffcore.API.Server.staffcore;
import cl.bebt.staffcore.MSGChanel.PluginMessage;
import cl.bebt.staffcore.commands.Head;
import cl.bebt.staffcore.commands.Staff.*;
import cl.bebt.staffcore.commands.Staff.Mysql.*;
import cl.bebt.staffcore.commands.Suicide;
import cl.bebt.staffcore.commands.Time.Day;
import cl.bebt.staffcore.commands.Time.Night;
import cl.bebt.staffcore.commands.Time.Weather;
import cl.bebt.staffcore.commands.WarningsCommand;
import cl.bebt.staffcore.configs.AltsStorage;
import cl.bebt.staffcore.configs.BanConfig;
import cl.bebt.staffcore.configs.ReportConfig;
import cl.bebt.staffcore.configs.WarnConfig;
import cl.bebt.staffcore.listeners.*;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.listeners.MenuListener;
import cl.bebt.staffcore.sql.Mysql;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.*;
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

    public AltsStorage alts;

    public BanConfig bans;

    public ReportConfig reports;

    public WarnConfig warns;

    public SQLGetter data;

    public Boolean chatMuted = Boolean.valueOf( false );

    public static PlayerMenuUtility getPlayerMenuUtility( Player p ){
        if ( playerMenuUtilityMap.containsKey( p ) )
            return playerMenuUtilityMap.get( p );
        PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility( p );
        playerMenuUtilityMap.put( p , playerMenuUtility );
        return playerMenuUtility;
    }

    public void onEnable( ){
        plugin = this;
        saveDefaultConfig( );
        reloadConfig( );
        this.reports = new ReportConfig( this );
        this.data = new SQLGetter( this );
        this.bans = new BanConfig( this );
        this.alts = new AltsStorage( this );
        this.warns = new WarnConfig( this );
        loadConfigManager( );
        Mysql connection = new Mysql( );
        ConsoleCommandSender c = Bukkit.getConsoleSender( );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------" ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&a    Plugin &5" + getName( ) + "&a&l ACTIVATED" ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1----------------------------------" ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&a         Staff-Core: &5" + getDescription( ).getVersion( ) ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------" ) );
        if ( getConfig( ).getBoolean( "mysql.enabled" ) ) {
            try {
                connection.connect( );
            } catch ( SQLException e ) {
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&4The plugin did not connect to any database." ) );
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------" ) );
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&4      Configure your Mysql" ) );
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&a  Disabling Staff-Core &5" + getDescription( ).getVersion( ) ) );
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------" ) );
                getServer( ).getPluginManager( ).disablePlugin( plugin );
                return;
            }
            if ( Mysql.isConnected( ) ) {
                SQLGetter.createTable( "vanish" );
                SQLGetter.createTable( "staff" );
                SQLGetter.createTable( "frozen" );
                SQLGetter.createTable( "flying" );
                SQLGetter.createTable( "staffchat" );
                SQLGetter.createAltsTable( );
                SQLGetter.createReportTable( );
                SQLGetter.createBansTable( );
                SQLGetter.createWarnsTable( );
                new VanishMysql( plugin );
                new FreezeMysql( plugin );
                new StaffChatToggleMysql( plugin );
                new FlyMysql( plugin );
                new StaffMysql( plugin );
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&a&lThe plugin has connect to a database!" ) );
            }
        } else {
            new Vanish( plugin );
            new Fly( plugin );
            new Freeze( plugin );
            new StaffChatToggle( plugin );
            new Staff( plugin );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&4The plugin did not connect to any database." ) );
        }
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------" ) );
        new unBan( plugin );
        new Ban( plugin );
        new MuteChat( plugin );
        new unMute( plugin );
        new Metrics( plugin , 8871 );
        new invSeeChest( plugin );
        new invSeeEnder( plugin );
        new ReportList( plugin );
        new Teleport( plugin );
        new TpAll( plugin );
        new GmCreative( plugin );
        new GmSurvival( plugin );
        new Day( plugin );
        new Night( plugin );
        new staffcore( plugin );
        new ClearChat( plugin );
        new Heal( plugin );
        new Ip( plugin );
        new Ping( plugin );
        new Weather( plugin );
        new Head( plugin );
        new Suicide( plugin );
        new StaffChat( plugin );
        new Report( plugin );
        new CheckAlts( plugin );
        new wipe( plugin );
        new ReportPlayer( plugin );
        new StaffList( plugin );
        new Warn( plugin );
        new WarningsCommand( plugin );
        new SetStaffItems( plugin );
        new FreezePlayer( );
        new SetVanish( );
        new utils( plugin );
        new ToggleChat( );
        new CountdownManager( );
        new API( plugin );
        getServer( ).getMessenger( ).registerOutgoingPluginChannel( plugin , "sc:alerts" );
        getServer( ).getMessenger( ).registerIncomingPluginChannel( plugin , "sc:stafflist" , new PluginMessage( ) );
        getServer( ).getMessenger( ).registerIncomingPluginChannel( plugin , "sc:alerts" , new PluginMessage( ) );
        getServer( ).getMessenger( ).registerOutgoingPluginChannel( plugin , "sc:stafflist" );
        getServer( ).getMessenger( ).registerIncomingPluginChannel( plugin , "BungeeCord" , new PluginMessage( ) );
        getServer( ).getMessenger( ).registerOutgoingPluginChannel( plugin , "BungeeCord" );
        Bukkit.getPluginManager( ).registerEvents( new GetReportMessage( plugin ) , plugin );
        Bukkit.getPluginManager( ).registerEvents( new MenuListener( ) , plugin );
        Bukkit.getPluginManager( ).registerEvents( new FreezeListeners( plugin ) , plugin );
        Bukkit.getPluginManager( ).registerEvents( new onPlayerJoin( plugin ) , plugin );
        Bukkit.getPluginManager( ).registerEvents( new onPLayerLeave( plugin ) , plugin );
        Bukkit.getPluginManager( ).registerEvents( new onChat( ) , plugin );
        Bukkit.getPluginManager( ).registerEvents( new InventoryListeners( plugin ) , plugin );
        (new UpdateChecker( plugin , 82324 )).getLatestVersion( version -> {
            if ( getDescription( ).getVersion( ).equals( version ) ) {
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&aYou are using &bStaff-Core!" + getDescription( ).getVersion( ) ) );
            } else {
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&cHey, there is a new version out!" ) );
                c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&b      Staff-Core " + version ) );
            }
        } );
        Bukkit.getServer( ).getScheduler( ).scheduleSyncRepeatingTask( this , new TPS( ) , 100L , 1L );
        Bukkit.getServer( ).getScheduler( ).scheduleSyncRepeatingTask( this , ( ) -> {
            for ( Player players : Bukkit.getOnlinePlayers( ) ) {
                try {
                    if ( players.getOpenInventory( ).getTopInventory( ).getItem( 31 ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "server" ) , PersistentDataType.STRING ) ) {
                        players.getOpenInventory( ).getTopInventory( ).setItem( 31 , Items.ServerStatus( ) );
                        players.getOpenInventory( ).getTopInventory( ).setItem( 13 , Items.PlayerStatus( players ) );
                    }
                } catch ( NullPointerException | ArrayIndexOutOfBoundsException ignored ) { }
            }
        } ,10L, 10L);
    }

    public void onDisable( ){
        ConsoleCommandSender c = Bukkit.getConsoleSender( );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------" ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&a    Plugin &5" + plugin.getName( ) + "&4&l DISABLED" ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------" ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&a         Staff-Core: &5" + plugin.getDescription( ).getVersion( ) ) );
        c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------" ) );
        if ( getConfig( ).getBoolean( "mysql.enabled" ) ) {
            Mysql.disconnect( );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&cDisconnected from the database!" ) );
            c.sendMessage( utils.chat( getConfig( ).getString( "server_prefix" ) + "&1---------------------------------" ) );
        }
    }

    public void loadConfigManager( ){
        this.bans = new BanConfig( plugin );
        this.bans.reloadConfig( );
        this.alts = new AltsStorage( plugin );
        this.alts.reloadConfig( );
        this.warns = new WarnConfig( plugin );
        this.warns.reloadConfig( );
    }
}
