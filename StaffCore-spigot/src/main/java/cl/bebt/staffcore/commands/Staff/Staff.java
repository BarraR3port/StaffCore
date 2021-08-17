/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.EntitysUtils.UserUtils;
import cl.bebt.staffcore.Exeptions.PlayerNotFundException;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Queries.StaffQuery;
import cl.bebt.staffcore.utils.StaffManager;
import cl.bebt.staffcore.utils.utils;
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
        if ( !utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                if ( sender.hasPermission( "staffcore.staff" ) ) {
                    Player p = ( Player ) sender;
                    if ( args.length == 0 ) {
                        UUID uuid = p.getUniqueId( );
                        if ( utils.mysqlEnabled( ) ) {
                            String is = StaffQuery.isStaff( p.getName( ) );
                            if ( is.equals( "true" ) ) {
                                StaffManager.disable( uuid );
                                utils.tell( sender , utils.getString( "staff.disabled" , "lg" , "staff" ) );
                            } else {
                                StaffManager.enable( uuid );
                                utils.tell( sender , utils.getString( "staff.enabled" , "lg" , "staff" ) );
                            }
                        } else {
                            try {
                                if ( UserUtils.getStaff( uuid ) ) {
                                    StaffManager.disable( uuid );
                                    utils.tell( sender , utils.getString( "staff.disabled" , "lg" , "staff" ) );
                                } else {
                                    StaffManager.enable( uuid );
                                    utils.tell( sender , utils.getString( "staff.enabled" , "lg" , "staff" ) );
                                }
                            } catch ( PlayerNotFundException e ) {
                                e.printStackTrace( );
                            }
                        }
                    } else if ( args.length == 1 ) {
                        if ( utils.isRegistered( args[0] ) ) {
                            UUID uuid = utils.getUUIDFromName( args[0] );
                            if ( utils.mysqlEnabled( ) ) {
                                String is = StaffQuery.isStaff( args[0] );
                                if ( is.equals( "true" ) ) {
                                    StaffManager.disable( uuid );
                                    utils.tell( p , utils.getString( "staff.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    utils.tell( uuid , utils.getString( "staff.disabled" , "lg" , "staff" ) );
                                } else {
                                    StaffManager.enable( uuid );
                                    utils.tell( p , utils.getString( "staff.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    utils.tell( uuid , utils.getString( "staff.enabled" , "lg" , "staff" ) );
                                }
                            } else {
                                try {
                                    if ( UserUtils.getStaff( uuid ) ) {
                                        StaffManager.disable( uuid );
                                        utils.tell( p , utils.getString( "staff.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                        utils.tell( uuid , utils.getString( "staff.disabled" , "lg" , "staff" ) );
                                    } else {
                                        StaffManager.enable( uuid );
                                        utils.tell( p , utils.getString( "staff.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                        utils.tell( uuid , utils.getString( "staff.enabled" , "lg" , "staff" ) );
                                    }
                                } catch ( PlayerNotFundException e ) {
                                    e.printStackTrace( );
                                }
                            }
                        } else {
                            utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                        }
                        
                    } else {
                        utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staff | staff <player>" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            } else {
                if ( args.length == 1 ) {
                    if ( utils.isRegistered( args[0] ) ) {
                        UUID uuid = utils.getUUIDFromName( args[0] );
                        if ( utils.mysqlEnabled( ) ) {
                            String is = StaffQuery.isStaff( args[0] );
                            if ( is.equals( "true" ) ) {
                                StaffManager.disable( uuid );
                                utils.tell( Bukkit.getConsoleSender( ) , utils.getString( "staff.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                utils.tell( uuid , utils.getString( "staff.disabled" , "lg" , "staff" ) );
                            } else {
                                StaffManager.enable( uuid );
                                utils.tell( Bukkit.getConsoleSender( ) , utils.getString( "staff.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                utils.tell( uuid , utils.getString( "staff.enabled" , "lg" , "staff" ) );
                            }
                        } else {
                            try {
                                if ( UserUtils.getStaff( uuid ) ) {
                                    StaffManager.disable( uuid );
                                    utils.tell( Bukkit.getConsoleSender( ) , utils.getString( "staff.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    utils.tell( uuid , utils.getString( "staff.disabled" , "lg" , "staff" ) );
                                } else {
                                    StaffManager.enable( uuid );
                                    utils.tell( Bukkit.getConsoleSender( ) , utils.getString( "staff.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    utils.tell( uuid , utils.getString( "staff.enabled" , "lg" , "staff" ) );
                                }
                            } catch ( PlayerNotFundException e ) {
                                e.printStackTrace( );
                            }
                        }
                    } else {
                        utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staff <player>" ) );
                }
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
    
}