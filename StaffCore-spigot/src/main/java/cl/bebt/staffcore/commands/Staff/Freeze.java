/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.EntitysUtils.UserUtils;
import cl.bebt.staffcore.Exeptions.PlayerNotFundException;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Queries.StaffQuery;
import cl.bebt.staffcore.utils.FreezeManager;
import cl.bebt.staffcore.utils.utils;
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
        if ( !utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                Player player = ( Player ) sender;
                if ( player.hasPermission( "staffcore.freeze" ) ) {
                    if ( args.length == 1 ) {
                        if ( utils.isRegistered( args[0] ) ) {
                            Player p = Bukkit.getPlayer( args[0] );
                            UUID uuid = utils.getUUIDFromName( args[0] );
                            if ( p == player ) {
                                if ( utils.mysqlEnabled( ) ) {
                                    String is = StaffQuery.isStaff( args[0] );
                                    if ( is.equals( "true" ) ) {
                                        if ( p.hasPermission( "staffcore.unfreeze.himself" ) ) {
                                            FreezeManager.disable( uuid , player.getName( ) );
                                            utils.tell( player , utils.getString( "freeze.unfreeze_ur_self" , "lg" , "staff" ) );
                                        } else {
                                            utils.tell( player , utils.chat( utils.getString( "no_permission" , "lg" , "staff" ) ) );
                                        }
                                    } else {
                                        if ( !player.hasPermission( "staffcore.freeze.bypas" ) ) {
                                            FreezeManager.enable( uuid , player.getName( ) );
                                        } else {
                                            utils.tell( player , utils.getString( "freeze.freeze_bypass" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                            return false;
                                        }
                                    }
                                } else {
                                    if ( player.hasPermission( "staffcore.unfreeze.himself" ) ) {
                                        try {
                                            if ( UserUtils.getFrozen( uuid ) ) {
                                                FreezeManager.disable( uuid , player.getName( ) );
                                                utils.tell( player , utils.getString( "freeze.unfreeze_ur_self" , "lg" , "staff" ) );
                                            } else {
                                                if ( !player.hasPermission( "staffcore.freeze.bypas" ) ) {
                                                    FreezeManager.enable( uuid , player.getName( ) );
                                                } else {
                                                    utils.tell( player , utils.getString( "freeze.freeze_bypass" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                                    return false;
                                                }
                                            }
                                        } catch ( PlayerNotFundException e ) {
                                            e.printStackTrace( );
                                        }
                                    } else {
                                        utils.tell( p , utils.chat( utils.getString( "no_permission" , "lg" , "staff" ) ) );
                                    }
                                }
                            } else {
                                if ( utils.mysqlEnabled( ) ) {
                                    String is = StaffQuery.isStaff( args[0] );
                                    if ( is.equals( "true" ) ) {
                                        FreezeManager.disable( uuid , player.getName( ) );
                                    } else {
                                        if ( !p.hasPermission( "staffcore.freeze.bypas" ) ) {
                                            FreezeManager.enable( uuid , player.getName( ) );
                                        } else {
                                            utils.tell( player , utils.getString( "freeze.freeze_bypass" , "lg" , "staff" ) );
                                            return false;
                                        }
                                    }
                                } else {
                                    try {
                                        if ( UserUtils.getFrozen( uuid ) ) {
                                            FreezeManager.disable( uuid , player.getName( ) );
                                        } else {
                                            if ( !p.hasPermission( "staffcore.freeze.bypas" ) ) {
                                                FreezeManager.enable( uuid , player.getName( ) );
                                            } else {
                                                utils.tell( player , utils.getString( "freeze.freeze_bypass" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                                return false;
                                            }
                                        }
                                    } catch ( PlayerNotFundException e ) {
                                        e.printStackTrace( );
                                    }
                                }
                            }
                        } else {
                            utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "freeze <player>" ) );
                    }
                } else if ( !(player.hasPermission( "staffcore.freeze" )) ) {
                    utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            } else {
                if ( args.length == 1 ) {
                    if ( utils.isRegistered( args[0] ) ) {
                        UUID uuid = utils.getUUIDFromName( args[0] );
                        if ( utils.mysqlEnabled( ) ) {
                            String is = StaffQuery.isStaff( args[0] );
                            if ( is.equals( "true" ) ) {
                                FreezeManager.disable( uuid , utils.getConsoleName( ) );
                            } else {
                                FreezeManager.enable( uuid , utils.getConsoleName( ) );
                            }
                        } else {
                            try {
                                if ( UserUtils.getFrozen( uuid ) ) {
                                    FreezeManager.disable( uuid , utils.getConsoleName( ) );
                                } else {
                                    FreezeManager.enable( uuid , utils.getConsoleName( ) );
                                }
                            } catch ( PlayerNotFundException e ) {
                                e.printStackTrace( );
                            }
                        }
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "freeze <player>" ) );
                }
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
}
