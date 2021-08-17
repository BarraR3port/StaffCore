/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffbungee;

import cl.bebt.staffbungee.EntitysUtils.SqlUtils;
import cl.bebt.staffbungee.EntitysUtils.UserUtils;
import cl.bebt.staffbungee.listeners.onPlayerJoin;
import cl.bebt.staffbungee.listeners.onPluginMessage;
import cl.bebt.staffbungee.utils.utils;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;
import java.util.HashMap;


public final class main extends Plugin {
    
    public static main plugin;
    
    public static HashMap < String, String > playerSkins = new HashMap <>( );
    
    public static main getPlugin( ){
        return plugin;
    }
    
    @Override
    public void onEnable( ){
        plugin = this;
        getProxy( ).getPluginManager( ).registerListener( this , new onPluginMessage( ) );
        getProxy( ).getPluginManager( ).registerListener( this , new onPlayerJoin( ) );
        getProxy( ).registerChannel( "sc:alerts" );
        getProxy( ).registerChannel( "sc:stafflist" );
        getProxy( ).registerChannel( "hc:msg" );
        SqlUtils.loadSqlConfig( );
        if ( SqlUtils.isEnabled( ) ) {
            try {
                SqlUtils.connect( );
            } catch ( SQLException e ) {
                utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1-----------------------------------------------------------------------------------------" );
                utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " +"&c     There has been an error while trying to connect to the mysql, check your config!" );
                utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&a                      Plugin &5StaffCore Bungee&c&l DEACTIVATED" );
                utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1-----------------------------------------------------------------------------------------" );
                this.getProxy().stop();
                return;
            }
        }
        UserUtils.loadUsers();
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1---------------------------------" );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&a    Plugin &5StaffCore Bungee&a&l ACTIVATED" );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1----------------------------------" );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&a         StaffCore Bungee: &5" + this.getDescription( ).getVersion( ) );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1---------------------------------" );
    }
    
    @Override
    public void onDisable( ){
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1---------------------------------" );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&a    Plugin &5StaffCore Bungee&c&l DEACTIVATED" );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1----------------------------------" );
        SqlUtils.disconnect();
        
    }
}
