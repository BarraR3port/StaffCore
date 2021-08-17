/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.EntitysUtils.UserUtils;
import cl.bebt.staffcore.Exeptions.PlayerNotFundException;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.FlyManager;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Fly implements CommandExecutor {
    
    public Fly( main plugin ){
        plugin.getCommand( "fly" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ) {
            try {
                if ( sender instanceof Player ) {
                    if ( sender.hasPermission( "staffcore.fly" ) ) {
                        if ( args.length == 0 ) {
                            Player p = ( Player ) sender;
                            UUID uuid = p.getUniqueId( );
                            if ( UserUtils.getFly( uuid ) ) {
                                FlyManager.disable( uuid );
                                utils.tell( sender , utils.getString( "fly.disabled" , "lg" , "staff" ) );
                            } else {
                                FlyManager.enable( uuid );
                                utils.tell( sender , utils.getString( "fly.enabled" , "lg" , "staff" ) );
                            }
                        } else if ( args.length == 1 ) {
                            if ( utils.isRegistered( args[0] ) ) {
                                UUID uuid = utils.getUUIDFromName( args[0] );
                                if ( UserUtils.getFly( uuid ) ) {
                                    FlyManager.disable( uuid );
                                    utils.tell( sender , utils.getString( "fly.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    utils.tell( uuid , utils.getString( "fly.disabled" , "lg" , "staff" ) );
                                } else {
                                    FlyManager.enable( uuid );
                                    utils.tell( sender , utils.getString( "fly.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                    utils.tell( uuid , utils.getString( "fly.enabled" , "lg" , "staff" ) );
                                }
                            } else {
                                utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                            }
                        } else {
                            utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "fly | fly <player>" ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                    }
                    
                } else {
                    if ( args.length == 1 ) {
                        if ( utils.isRegistered( args[0] ) ) {
                            UUID uuid = utils.getUUIDFromName( args[0] );
                            if ( UserUtils.getFly( uuid ) ) {
                                FlyManager.disable( uuid );
                                utils.tell( sender , utils.getString( "fly.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                utils.tell( uuid , utils.getString( "fly.disabled" , "lg" , "staff" ) );
                            } else {
                                FlyManager.enable( uuid );
                                utils.tell( sender , utils.getString( "fly.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                utils.tell( uuid , utils.getString( "fly.enabled" , "lg" , "staff" ) );
                            }
                        } else {
                            utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "fly | fly <player>" ) );
                    }
                }
            } catch ( PlayerNotFundException ignored ) {
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
}
