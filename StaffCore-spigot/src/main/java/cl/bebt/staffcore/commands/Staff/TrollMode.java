/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TrollMode implements CommandExecutor {
    
    
    public TrollMode( main plugin ){
        plugin.getCommand( "troll" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command command , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                if ( args.length == 0 ) {
                    Player p = ( Player ) sender;
                    if ( p.hasPermission( "staffcore.troll" ) ) {
                        StaffCoreAPI.setTrollMode( p , !StaffCoreAPI.getTrollStatus( p.getName( ) ) );
                        if ( StaffCoreAPI.getTrollStatus( p.getName( ) ) ) {
                            utils.tell( p , utils.getString( "troll.enabled" , "lg" , "staff" ) );
                        } else {
                            utils.tell( p , utils.getString( "troll.disabled" , "lg" , "staff" ) );
                        }
                    } else {
                        utils.tell( p , utils.getString( "no_permission" , "lg" , "staff" ) );
                    }
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) != null ) {
                        if ( sender.hasPermission( "staffcore.troll" ) ) {
                            Player target = Bukkit.getPlayer( args[0] );
                            StaffCoreAPI.setTrollMode( target , !StaffCoreAPI.getTrollStatus( args[0] ) );
                            if ( StaffCoreAPI.getTrollStatus( args[0] ) ) {
                                if ( target != sender ) {
                                    utils.tell( target , utils.getString( "troll.enabled" , "lg" , "staff" ) );
                                }
                                utils.tell( sender , utils.getString( "troll.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                            } else {
                                if ( target != sender ) {
                                    utils.tell( target , utils.getString( "troll.disabled" , "lg" , "staff" ) );
                                }
                                utils.tell( sender , utils.getString( "troll.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                            }
                        } else {
                            utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                        }
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "troll | troll <player>" ) );
                }
            } else {
                if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) != null ) {
                        Player target = Bukkit.getPlayer( args[0] );
                        StaffCoreAPI.setTrollMode( target , !StaffCoreAPI.getTrollStatus( args[0] ) );
                        if ( StaffCoreAPI.getTrollStatus( args[0] ) ) {
                            if ( target != sender ) {
                                utils.tell( target , utils.getString( "troll.enabled" , "lg" , "staff" ) );
                            }
                            utils.tell( sender , utils.getString( "troll.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                        } else {
                            if ( target != sender ) {
                                utils.tell( target , utils.getString( "troll.disabled" , "lg" , "staff" ) );
                            }
                            utils.tell( sender , utils.getString( "troll.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                        }
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "troll | troll <player>" ) );
                }
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return false;
    }
}
