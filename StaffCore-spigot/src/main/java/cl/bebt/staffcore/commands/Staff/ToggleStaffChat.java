/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.EntitysUtils.UserUtils;
import cl.bebt.staffcore.Exeptions.PlayerNotFundException;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Queries.StaffChatQuery;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;

public class ToggleStaffChat implements CommandExecutor, Listener {
    
    
    private final main plugin;
    
    public ToggleStaffChat( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "togglestaffchat" ).setExecutor( this );
    }
    
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 1 ) {
                if ( utils.isRegistered( args[0] ) ) {
                    UUID uuid = utils.getUUIDFromName( args[0] );
                    try {
                        if ( utils.mysqlEnabled( ) ) {
                            String is = StaffChatQuery.isStaffChat( args[0] );
                            if ( is.equals( "true" ) ) {
                                utils.tell( sender , utils.getString( "staff_chat.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                utils.tell( uuid , utils.getString( "staff_chat.disabled" , "lg" , "staff" ) );
                                StaffChatQuery.disable( args[0] );
                                UserUtils.setStaffChat( uuid , false );
                            } else {
                                utils.tell( sender , utils.getString( "staff_chat.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                utils.tell( uuid , utils.getString( "staff_chat.enabled" , "lg" , "staff" ) );
                                StaffChatQuery.enable( args[0] );
                                UserUtils.setStaffChat( uuid , true );
                            }
                        } else {
                            if ( UserUtils.getStaffChat( uuid ) ) {
                                utils.tell( sender , utils.getString( "staff_chat.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                utils.tell( uuid , utils.getString( "staff_chat.disabled" , "lg" , "staff" ) );
                                UserUtils.setStaffChat( uuid , false );
                            } else {
                                utils.tell( sender , utils.getString( "staff_chat.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                utils.tell( uuid , utils.getString( "staff_chat.enabled" , "lg" , "staff" ) );
                                UserUtils.setStaffChat( uuid , true );
                            }
                        }
                    } catch ( PlayerNotFundException ignored ) {
                    }
                } else {
                    utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "tsc | tsc <player>" ) );
            }
        } else {
            if ( args.length == 0 ) {
                Player p = ( Player ) sender;
                if ( p.hasPermission( "staffcore.tsc" ) ) {
                    UUID uuid = p.getUniqueId( );
                    try {
                        if ( utils.mysqlEnabled( ) ) {
                            String is = StaffChatQuery.isStaffChat( p.getName( ) );
                            if ( is.equals( "true" ) ) {
                                utils.tell( p , utils.getString( "staff_chat.disabled" , "lg" , "staff" ) );
                                StaffChatQuery.disable( p.getName( ) );
                                UserUtils.setStaffChat( uuid , false );
                            } else {
                                utils.tell( p , "&8[&3&lSC&r&8]&r &aOn" );
                                StaffChatQuery.enable( p.getName( ) );
                                UserUtils.setStaffChat( uuid , true );
                            }
                        } else {
                            if ( UserUtils.getStaffChat( uuid ) ) {
                                utils.tell( p , utils.getString( "staff_chat.disabled" , "lg" , "staff" ) );
                                UserUtils.setStaffChat( uuid , false );
                            } else {
                                utils.tell( p , "&8[&3&lSC&r&8]&r &aOn" );
                                UserUtils.setStaffChat( uuid , true );
                            }
                        }
                    } catch ( PlayerNotFundException ignored ) {
                    }
                } else {
                    utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            } else if ( args.length == 1 ) {
                if ( utils.isRegistered( args[0] ) ) {
                    if ( sender.hasPermission( "staffcore.tsc" ) ) {
                        UUID uuid = utils.getUUIDFromName( args[0] );
                        try {
                            if ( utils.mysqlEnabled( ) ) {
                                String is = StaffChatQuery.isStaffChat( args[0] );
                                if ( is.equals( "true" ) ) {
                                    utils.tell( sender , utils.getString( "staff_chat.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    utils.tell( uuid , utils.getString( "staff_chat.disabled" , "lg" , "staff" ) );
                                    StaffChatQuery.disable( args[0] );
                                    UserUtils.setStaffChat( uuid , false );
                                } else {
                                    utils.tell( sender , utils.getString( "staff_chat.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    utils.tell( uuid , utils.getString( "staff_chat.enabled" , "lg" , "staff" ) );
                                    StaffChatQuery.enable( args[0] );
                                    UserUtils.setStaffChat( uuid , true );
                                }
                            } else {
                                if ( UserUtils.getStaffChat( uuid ) ) {
                                    utils.tell( sender , utils.getString( "staff_chat.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    utils.tell( uuid , utils.getString( "staff_chat.disabled" , "lg" , "staff" ) );
                                    UserUtils.setStaffChat( uuid , false );
                                } else {
                                    utils.tell( sender , utils.getString( "staff_chat.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    utils.tell( uuid , utils.getString( "staff_chat.enabled" , "lg" , "staff" ) );
                                    UserUtils.setStaffChat( uuid , true );
                                }
                            }
                        } catch ( PlayerNotFundException ignored ) {
                        }
                    } else {
                        utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "tsc | tsc <player>" ) );
            }
        }
        return true;
    }
}
