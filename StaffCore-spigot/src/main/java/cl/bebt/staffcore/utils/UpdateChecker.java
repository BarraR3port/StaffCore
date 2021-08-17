/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public class UpdateChecker {
    private final main plugin;
    
    public UpdateChecker( main plugin ){
        this.plugin = plugin;
    }
    
    public void getLatestVersion( Consumer < String > consumer ){
        Bukkit.getScheduler( ).runTaskAsynchronously( plugin , ( ) -> {
            String version = Http.getLatestVersion( "http://localhost:82/api/version" , "latest" );
            plugin.latestVersion = version;
            consumer.accept( version );
        } );
    }
}
