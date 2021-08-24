/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.utils;

import cl.bebt.staffcoreapi.Api;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class UpdateChecker {
    
    public UpdateChecker( ){
    
    }
    
    public void getSpigotLatestVersion( Consumer < String > consumer , JavaPlugin plugin ){
        Bukkit.getScheduler( ).runTaskAsynchronously( plugin , ( ) -> {
            String version = Http.getFromApi( "http://localhost:82/api/version" , "latest" );
            Api.latestVersion = version;
            consumer.accept( version );
        } );
    }
    
    public void getBungeeLatestVersion( Consumer < String > consumer , Plugin plugin ){
        plugin.getProxy( ).getScheduler( ).runAsync( plugin , ( ) -> {
            String version = Http.getFromApi( "http://localhost:82/api/version" , "latest" );
            Api.latestVersion = version;
            consumer.accept( version );
        } );
    }
}
