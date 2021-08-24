/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.FreezeManager;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.SQL.Queries.StaffQuery;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Freeze implements CommandExecutor {
    
    
    public Freeze( main plugin ){
        plugin.getCommand( "freeze" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !Utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                Player player = ( Player ) sender;
                if ( player.hasPermission( "staffcore.freeze" ) ) {
                    if ( args.length == 1 ) {
                        if ( Utils.isRegistered( args[0] ) ) {
                            Player p = Bukkit.getPlayer( args[0] );
                            UUID uuid = Utils.getUUIDFromName( args[0] );
                            if ( p == player ) {
                                if ( Utils.mysqlEnabled( ) ) {
                                    String is = StaffQuery.isStaff( args[0] );
                                    if ( is.equals( "true" ) ) {
                                        if ( p.hasPermission( "staffcore.unfreeze.himself" ) ) {
                                            FreezeManager.disable( uuid , player.getName( ) );
                                            Utils.tell( player , Utils.getString( "freeze.unfreeze_ur_self" , "lg" , "staff" ) );
                                        } else {
                                            Utils.tell( player , Utils.chat( Utils.getString( "no_permission" , "lg" , "staff" ) ) );
                                        }
                                    } else {
                                        if ( !player.hasPermission( "staffcore.freeze.bypas" ) ) {
                                            FreezeManager.enable( uuid , player.getName( ) );
                                        } else {
                                            Utils.tell( player , Utils.getString( "freeze.freeze_bypass" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                            return false;
                                        }
                                    }
                                } else {
                                    if ( player.hasPermission( "staffcore.unfreeze.himself" ) ) {
                                        if ( UserUtils.getFrozen( uuid ) ) {
                                            FreezeManager.disable( uuid , player.getName( ) );
                                            Utils.tell( player , Utils.getString( "freeze.unfreeze_ur_self" , "lg" , "staff" ) );
                                        } else {
                                            if ( !player.hasPermission( "staffcore.freeze.bypas" ) ) {
                                                FreezeManager.enable( uuid , player.getName( ) );
                                            } else {
                                                Utils.tell( player , Utils.getString( "freeze.freeze_bypass" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                                return false;
                                            }
                                        }
                                    } else {
                                        Utils.tell( p , Utils.chat( Utils.getString( "no_permission" , "lg" , "staff" ) ) );
                                    }
                                }
                            } else {
                                if ( Utils.mysqlEnabled( ) ) {
                                    String is = StaffQuery.isStaff( args[0] );
                                    if ( is.equals( "true" ) ) {
                                        FreezeManager.disable( uuid , player.getName( ) );
                                    } else {
                                        if ( !p.hasPermission( "staffcore.freeze.bypas" ) ) {
                                            FreezeManager.enable( uuid , player.getName( ) );
                                        } else {
                                            Utils.tell( player , Utils.getString( "freeze.freeze_bypass" , "lg" , "staff" ) );
                                            return false;
                                        }
                                    }
                                } else {
                                    if ( UserUtils.getFrozen( uuid ) ) {
                                        FreezeManager.disable( uuid , player.getName( ) );
                                    } else {
                                        if ( !p.hasPermission( "staffcore.freeze.bypas" ) ) {
                                            FreezeManager.enable( uuid , player.getName( ) );
                                        } else {
                                            Utils.tell( player , Utils.getString( "freeze.freeze_bypass" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                            return false;
                                        }
                                    }
                                }
                            }
                        } else {
                            Utils.tell( sender , Utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                        }
                    } else {
                        Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "freeze <player>" ) );
                    }
                } else if ( !(player.hasPermission( "staffcore.freeze" )) ) {
                    Utils.tell( sender , Utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            } else {
                if ( args.length == 1 ) {
                    if ( Utils.isRegistered( args[0] ) ) {
                        UUID uuid = Utils.getUUIDFromName( args[0] );
                        if ( Utils.mysqlEnabled( ) ) {
                            String is = StaffQuery.isStaff( args[0] );
                            if ( is.equals( "true" ) ) {
                                FreezeManager.disable( uuid , Utils.getConsoleName( ) );
                            } else {
                                FreezeManager.enable( uuid , Utils.getConsoleName( ) );
                            }
                        } else {
                            if ( UserUtils.getFrozen( uuid ) ) {
                                FreezeManager.disable( uuid , Utils.getConsoleName( ) );
                            } else {
                                FreezeManager.enable( uuid , Utils.getConsoleName( ) );
                            }
                        }
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "freeze <player>" ) );
                }
            }
        } else {
            Utils.tell( sender , Utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
}
