/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.VanishManager;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.SQL.Queries.VanishQuery;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;


public class Vanish implements CommandExecutor {
    
    public Vanish( main plugin ){
        plugin.getCommand( "vanish" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !Utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                if ( sender.hasPermission( "staffcore.vanish" ) ) {
                    if ( args.length == 0 ) {
                        Player p = ( Player ) sender;
                        UUID uuid = p.getUniqueId( );
                        if ( Utils.mysqlEnabled( ) ) {
                            String is = VanishQuery.isVanished( p.getName( ) );
                            if ( is.equals( "true" ) ) {
                                VanishManager.disable( uuid );
                                Utils.tell( p , Utils.getString( "vanish.disabled" , "lg" , "staff" ) );
                            } else if ( is.equals( "false" ) ) {
                                VanishManager.enable( uuid );
                                Utils.tell( p , Utils.getString( "vanish.enabled" , "lg" , "staff" ) );
                            }
                        } else {
                            if ( UserUtils.getVanish( uuid ) ) {
                                VanishManager.disable( uuid );
                                Utils.tell( sender , Utils.getString( "staff.disabled" , "lg" , "staff" ) );
                            } else {
                                VanishManager.enable( uuid );
                                Utils.tell( sender , Utils.getString( "staff.enabled" , "lg" , "staff" ) );
                            }
                        }
                    } else if ( args.length == 1 ) {
                        if ( Utils.isRegistered( args[0] ) ) {
                            UUID uuid = Utils.getUUIDFromName( args[0] );
                            Player p = ( Player ) sender;
                            if ( Utils.mysqlEnabled( ) ) {
                                String is = VanishQuery.isVanished( args[0] );
                                if ( is.equals( "true" ) ) {
                                    VanishManager.disable( uuid );
                                    Utils.tell( p , Utils.getString( "vanish.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    Utils.tell( uuid , Utils.getString( "vanish.disabled" , "lg" , "staff" ) );
                                    
                                } else {
                                    VanishManager.enable( uuid );
                                    Utils.tell( p , Utils.getString( "vanish.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    Utils.tell( uuid , Utils.getString( "vanish.enabled" , "lg" , "staff" ) );
                                }
                            } else {
                                if ( UserUtils.getVanish( uuid ) ) {
                                    VanishManager.disable( uuid );
                                    Utils.tell( p , Utils.getString( "vanish.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    Utils.tell( uuid , Utils.getString( "vanish.disabled" , "lg" , "staff" ) );
                                } else {
                                    VanishManager.enable( uuid );
                                    Utils.tell( p , Utils.getString( "vanish.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    Utils.tell( uuid , Utils.getString( "vanish.enabled" , "lg" , "staff" ) );
                                }
                            }
                        } else {
                            Utils.tell( sender , Utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                        }
                    } else {
                        Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "vanish | vanish <player>" ) );
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            } else {
                if ( args.length == 1 ) {
                    if ( Utils.isRegistered( args[0] ) ) {
                        UUID uuid = Utils.getUUIDFromName( args[0] );
                        if ( Utils.mysqlEnabled( ) ) {
                            String is = VanishQuery.isVanished( args[0] );
                            if ( is.equals( "true" ) ) {
                                VanishManager.disable( uuid );
                                Utils.tell( Bukkit.getConsoleSender( ) , Utils.getString( "vanish.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                Utils.tell( uuid , Utils.getString( "vanish.disabled" , "lg" , "staff" ) );
                                
                            } else {
                                VanishManager.enable( uuid );
                                Utils.tell( Bukkit.getConsoleSender( ) , Utils.getString( "vanish.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                Utils.tell( uuid , Utils.getString( "vanish.enabled" , "lg" , "staff" ) );
                            }
                        } else {
                            if ( UserUtils.getVanish( uuid ) ) {
                                VanishManager.disable( uuid );
                                Utils.tell( Bukkit.getConsoleSender( ) , Utils.getString( "vanish.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                Utils.tell( uuid , Utils.getString( "vanish.disabled" , "lg" , "staff" ) );
                            } else {
                                VanishManager.enable( uuid );
                                Utils.tell( Bukkit.getConsoleSender( ) , Utils.getString( "vanish.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                Utils.tell( uuid , Utils.getString( "vanish.enabled" , "lg" , "staff" ) );
                            }
                        }
                    } else {
                        Utils.tell( sender , Utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "vanish | vanish <player>" ) );
                }
            }
        } else {
            Utils.tell( sender , Utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
}


