/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi;

import cl.bebt.staffcoreapi.EntitiesUtils.*;
import cl.bebt.staffcoreapi.Enums.ApiType;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.SQL.SqlManager;
import cl.bebt.staffcoreapi.configs.*;
import cl.bebt.staffcoreapi.configs.Lenguajes.EN_NA;
import cl.bebt.staffcoreapi.configs.Lenguajes.ES_CL;
import cl.bebt.staffcoreapi.configs.Lenguajes.FR;
import cl.bebt.staffcoreapi.utils.DataExporter;
import cl.bebt.staffcoreapi.utils.UpdateChecker;
import cl.bebt.staffcoreapi.utils.Utils;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;

public class Api {
    
    public static HashMap < String, String > playerSkins = new HashMap <>( );
    
    public static String latestVersion;
    
    public static Api api;
    
    public static AltsStorage alts;
    
    public static BanConfig bans;
    
    public static ReportConfig reports;
    
    public static WarnConfig warns;
    
    public static SqlManager data;
    
    public static EN_NA en_na;
    
    public static ES_CL es_cl;
    
    public static FR fr;
    
    public static StatsConfig stats;
    
    public static ItemsConfig items;
    
    public static AlertsConfig alerts;
    
    public static MenusConfig menus;
    
    public static ApiType currentApiType;
    
    
    public static void initializeSpigot( JavaPlugin plugin ){
        currentApiType = ApiType.SPIGOT;
        new Utils( ApiType.SPIGOT , plugin );
        new UpdateChecker( ).getSpigotLatestVersion( version -> {
            latestVersion = version;
            if ( !plugin.getDescription( ).getVersion( ).equals( version ) ) {
                Utils.tellConsole( plugin.getConfig( ).getString( "server_prefix" ) + "&c     Hey, there is a new version out!" );
                Utils.tellConsole( plugin.getConfig( ).getString( "server_prefix" ) + "&b         Staff-Core " + version );
                Utils.tellConsole( plugin.getConfig( ).getString( "server_prefix" ) + "&4&lDISABLING STAFF-CORE. USE THE LATEST VERSION" );
                Utils.tellConsole( plugin.getConfig( ).getString( "server_prefix" ) + "&1---------------------------------------------" );
                plugin.getPluginLoader( ).disablePlugin( plugin );
            }
        } , plugin );
        loadConfigManager( plugin );
        PersistentDataUtils.loadItems( );
        loadCommonStuff( );
    }
    
    public static void initializeBungee( Plugin plugin ){
        currentApiType = ApiType.BUNGEECORD;
        new Utils( ApiType.BUNGEECORD , plugin );
        new UpdateChecker( ).getBungeeLatestVersion( version -> {
            if ( !plugin.getDescription( ).getVersion( ).equals( version ) ) {
                Utils.tellConsole( "&c     Hey, there is a new version out!" );
                Utils.tellConsole( "&b         Staff-Core " + version );
                Utils.tellConsole( "&4&lDISABLING STAFF-CORE. USE THE LATEST VERSION" );
                Utils.tellConsole( "&1---------------------------------------------" );
                plugin.getProxy( ).stop( );
            }
        } , plugin );
        loadCommonStuff( );
    }
    
    
    public static void loadConfigManager( JavaPlugin plugin ){
        plugin.saveDefaultConfig( );
        plugin.reloadConfig( );
        data = new SqlManager( );
        reports = new ReportConfig( plugin );
        reports.saveDefaultConfig( );
        reports.reloadConfig( );
        bans = new BanConfig( plugin );
        bans.saveDefaultConfig( );
        bans.reloadConfig( );
        alts = new AltsStorage( plugin );
        alts.saveDefaultConfig( );
        alts.reloadConfig( );
        warns = new WarnConfig( plugin );
        warns.saveDefaultConfig( );
        warns.reloadConfig( );
        en_na = new EN_NA( plugin );
        en_na.saveDefaultConfig( );
        en_na.reloadConfig( );
        es_cl = new ES_CL( plugin );
        es_cl.saveDefaultConfig( );
        es_cl.reloadConfig( );
        fr = new FR( plugin );
        fr.saveDefaultConfig( );
        fr.reloadConfig( );
        stats = new StatsConfig( plugin );
        stats.saveDefaultConfig( );
        stats.reloadConfig( );
        items = new ItemsConfig( plugin );
        items.saveDefaultConfig( );
        items.reloadConfig( );
        alerts = new AlertsConfig( plugin );
        alerts.saveDefaultConfig( );
        alerts.reloadConfig( );
        menus = new MenusConfig( plugin );
        menus.saveDefaultConfig( );
        menus.reloadConfig( );
    }
    
    private static void loadCommonStuff( ){
        UserUtils.loadUsers( );
        BanUtils.loadBans( );
        SqlUtils.loadSqlConfig( );
        ServerSettingsUtils.loadServerSettings( );
        DataExporter.updateServerStats( UpdateType.INITIALIZATION );
        Utils.runAsync( ( ) -> playerSkins = Utils.getSavedSkins( ) );
        if ( SqlUtils.isEnabled( ) ) {
            try {
                SqlUtils.connect( );
            } catch ( SQLException e ) {
                Utils.tellConsole( "&8[&a&lSTAFF CORE API&r&8]&r " + "&1-----------------------------------------------------------------------------------------" );
                Utils.tellConsole( "&8[&a&lSTAFF CORE API&r&8]&r " + "&c     There has been an error while trying to connect to the mysql, check your config!" );
                Utils.tellConsole( "&8[&a&lSTAFF CORE API&r&8]&r " + "&a                      Plugin &5StaffCore &c&l DEACTIVATED" );
                Utils.tellConsole( "&8[&a&lSTAFF CORE API&r&8]&r " + "&1-----------------------------------------------------------------------------------------" );
                Utils.StopServer( );
                return;
            }
        }
    }
    
}
