/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.SQL.Queries.StaffQuery;
import cl.bebt.staffcoreapi.utils.StaffManager;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;


public class Staff implements CommandExecutor {
    
    public Staff( main plugin ){
        plugin.getCommand( "staff" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !Utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                if ( sender.hasPermission( "staffcore.staff" ) ) {
                    Player p = ( Player ) sender;
                    if ( args.length == 0 ) {
                        UUID uuid = p.getUniqueId( );
                        if ( Utils.mysqlEnabled( ) ) {
                            String is = StaffQuery.isStaff( p.getName( ) );
                            if ( is.equals( "true" ) ) {
                                StaffManager.disable( uuid );
                                Utils.tell( sender , Utils.getString( "staff.disabled" , "lg" , "staff" ) );
                            } else {
                                StaffManager.enable( uuid );
                                Utils.tell( sender , Utils.getString( "staff.enabled" , "lg" , "staff" ) );
                            }
                        } else {
                            if ( UserUtils.getStaff( uuid ) ) {
                                StaffManager.disable( uuid );
                                Utils.tell( sender , Utils.getString( "staff.disabled" , "lg" , "staff" ) );
                            } else {
                                StaffManager.enable( uuid );
                                Utils.tell( sender , Utils.getString( "staff.enabled" , "lg" , "staff" ) );
                            }
                        }
                    } else if ( args.length == 1 ) {
                        if ( Utils.isRegistered( args[0] ) ) {
                            UUID uuid = Utils.getUUIDFromName( args[0] );
                            if ( Utils.mysqlEnabled( ) ) {
                                String is = StaffQuery.isStaff( args[0] );
                                if ( is.equals( "true" ) ) {
                                    StaffManager.disable( uuid );
                                    Utils.tell( p , Utils.getString( "staff.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    Utils.tell( uuid , Utils.getString( "staff.disabled" , "lg" , "staff" ) );
                                } else {
                                    StaffManager.enable( uuid );
                                    Utils.tell( p , Utils.getString( "staff.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    Utils.tell( uuid , Utils.getString( "staff.enabled" , "lg" , "staff" ) );
                                }
                            } else {
                                if ( UserUtils.getStaff( uuid ) ) {
                                    StaffManager.disable( uuid );
                                    Utils.tell( p , Utils.getString( "staff.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    Utils.tell( uuid , Utils.getString( "staff.disabled" , "lg" , "staff" ) );
                                } else {
                                    StaffManager.enable( uuid );
                                    Utils.tell( p , Utils.getString( "staff.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    Utils.tell( uuid , Utils.getString( "staff.enabled" , "lg" , "staff" ) );
                                }
                            }
                        } else {
                            Utils.tell( sender , Utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                        }
                        
                    } else {
                        Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staff | staff <player>" ) );
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            } else {
                if ( args.length == 1 ) {
                    if ( Utils.isRegistered( args[0] ) ) {
                        UUID uuid = Utils.getUUIDFromName( args[0] );
                        if ( Utils.mysqlEnabled( ) ) {
                            String is = StaffQuery.isStaff( args[0] );
                            if ( is.equals( "true" ) ) {
                                StaffManager.disable( uuid );
                                Utils.tell( Bukkit.getConsoleSender( ) , Utils.getString( "staff.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                Utils.tell( uuid , Utils.getString( "staff.disabled" , "lg" , "staff" ) );
                            } else {
                                StaffManager.enable( uuid );
                                Utils.tell( Bukkit.getConsoleSender( ) , Utils.getString( "staff.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                Utils.tell( uuid , Utils.getString( "staff.enabled" , "lg" , "staff" ) );
                            }
                        } else {
                            if ( UserUtils.getStaff( uuid ) ) {
                                StaffManager.disable( uuid );
                                Utils.tell( Bukkit.getConsoleSender( ) , Utils.getString( "staff.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                Utils.tell( uuid , Utils.getString( "staff.disabled" , "lg" , "staff" ) );
                            } else {
                                StaffManager.enable( uuid );
                                Utils.tell( Bukkit.getConsoleSender( ) , Utils.getString( "staff.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                Utils.tell( uuid , Utils.getString( "staff.enabled" , "lg" , "staff" ) );
                            }
                        }
                    } else {
                        Utils.tell( sender , Utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staff <player>" ) );
                }
            }
        } else {
            Utils.tell( sender , Utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
    
}