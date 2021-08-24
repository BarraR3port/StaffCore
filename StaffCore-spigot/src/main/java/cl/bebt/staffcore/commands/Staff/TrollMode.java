/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.utils.Utils;
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
        if ( !Utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                if ( args.length == 0 ) {
                    Player p = ( Player ) sender;
                    if ( p.hasPermission( "staffcore.troll" ) ) {
                        Utils.setTrollMode( p , !Utils.getTrollStatus( p.getName( ) ) );
                        if ( Utils.getTrollStatus( p.getName( ) ) ) {
                            Utils.tell( p , Utils.getString( "troll.enabled" , "lg" , "staff" ) );
                        } else {
                            Utils.tell( p , Utils.getString( "troll.disabled" , "lg" , "staff" ) );
                        }
                    } else {
                        Utils.tell( p , Utils.getString( "no_permission" , "lg" , "staff" ) );
                    }
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) != null ) {
                        if ( sender.hasPermission( "staffcore.troll" ) ) {
                            Player target = Bukkit.getPlayer( args[0] );
                            Utils.setTrollMode( target , !Utils.getTrollStatus( args[0] ) );
                            if ( Utils.getTrollStatus( args[0] ) ) {
                                if ( target != sender ) {
                                    Utils.tell( target , Utils.getString( "troll.enabled" , "lg" , "staff" ) );
                                }
                                Utils.tell( sender , Utils.getString( "troll.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                            } else {
                                if ( target != sender ) {
                                    Utils.tell( target , Utils.getString( "troll.disabled" , "lg" , "staff" ) );
                                }
                                Utils.tell( sender , Utils.getString( "troll.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                            }
                        } else {
                            Utils.tell( sender , Utils.getString( "no_permission" , "lg" , "staff" ) );
                        }
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "troll | troll <player>" ) );
                }
            } else {
                if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) != null ) {
                        Player target = Bukkit.getPlayer( args[0] );
                        Utils.setTrollMode( target , !Utils.getTrollStatus( args[0] ) );
                        if ( Utils.getTrollStatus( args[0] ) ) {
                            if ( target != sender ) {
                                Utils.tell( target , Utils.getString( "troll.enabled" , "lg" , "staff" ) );
                            }
                            Utils.tell( sender , Utils.getString( "troll.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                        } else {
                            if ( target != sender ) {
                                Utils.tell( target , Utils.getString( "troll.disabled" , "lg" , "staff" ) );
                            }
                            Utils.tell( sender , Utils.getString( "troll.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                        }
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "troll | troll <player>" ) );
                }
            }
        } else {
            Utils.tell( sender , Utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return false;
    }
}
