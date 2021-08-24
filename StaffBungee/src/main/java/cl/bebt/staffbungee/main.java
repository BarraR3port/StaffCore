/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffbungee;

import cl.bebt.staffbungee.listeners.onPlayerJoin;
import cl.bebt.staffbungee.listeners.onPluginMessage;
import cl.bebt.staffbungee.utils.utils;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.EntitiesUtils.ServerSettingsUtils;
import net.md_5.bungee.api.plugin.Plugin;

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
        Api.initializeBungee( this );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1---------------------------------" );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&a    Plugin &5StaffCore Bungee&a&l ACTIVATED" );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1----------------------------------" );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&a         StaffCore Bungee: &5" + this.getDescription( ).getVersion( ) );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1---------------------------------" );
    }
    
    @Override
    public void onDisable( ){
        ServerSettingsUtils.saveServerSettings( );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1---------------------------------" );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&a    Plugin &5StaffCore Bungee&c&l DEACTIVATED" );
        utils.tell( getProxy( ).getConsole( ) , "&8[&a&lSTAFF CORE BUNGEE&r&8]&r " + "&1----------------------------------" );
        
    }
}
