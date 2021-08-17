/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Bangui.BansPerPlayer;
import cl.bebt.staffcore.menu.menu.WarnManager.WarnManager;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Bans implements TabExecutor {
    private final main plugin;
    
    public Bans( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "bans" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                Player p = ( Player ) sender;
                if ( sender.hasPermission( "staffcore.bans" ) ) {
                    if ( args.length == 0 ) {
                        new WarnManager( new PlayerMenuUtility( p ) , plugin ).open( );
                    } else if ( args.length == 1 ) {
                        try {
                            new BansPerPlayer( new PlayerMenuUtility( p ) , plugin , p , args[0] ).open( );
                        } catch ( NullPointerException ignored ) {
                            utils.tell( sender , utils.getString( "offline" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "bans <player>" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "only_players" , "lg" , "staff" ) );
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "staff" ) );
        }
        return true;
    }
    
    @Override
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        List < String > version = new ArrayList <>( );
        if ( args.length == 1 ) {
            ArrayList < String > Players = utils.getUsers( );
            if ( !Players.isEmpty( ) ) {
                Players.remove( sender.getName( ) );
                version.addAll( Players );
            }
        }
        return version;
    }
}