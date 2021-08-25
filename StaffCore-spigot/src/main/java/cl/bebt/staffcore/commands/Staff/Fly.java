/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.utils.FlyManager;
import cl.bebt.staffcoreapi.utils.Utils;
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
        if ( !Utils.isOlderVersion( ) ) {
            
            if ( sender instanceof Player ) {
                if ( sender.hasPermission( "staffcore.fly" ) ) {
                    if ( args.length == 0 ) {
                        Player p = ( Player ) sender;
                        UUID uuid = p.getUniqueId( );
                        if ( UserUtils.getFly( uuid ) ) {
                            FlyManager.disable( uuid );
                            Utils.tell( sender , Utils.getString( "fly.disabled" , "lg" , "staff" ) );
                        } else {
                            FlyManager.enable( uuid );
                            Utils.tell( sender , Utils.getString( "fly.enabled" , "lg" , "staff" ) );
                        }
                    } else if ( args.length == 1 ) {
                        if ( Utils.isRegistered( args[0] ) ) {
                            UUID uuid = Utils.getUUIDFromName( args[0] );
                            if ( UserUtils.getFly( uuid ) ) {
                                FlyManager.disable( uuid );
                                Utils.tell( sender , Utils.getString( "fly.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                Utils.tell( uuid , Utils.getString( "fly.disabled" , "lg" , "staff" ) );
                            } else {
                                FlyManager.enable( uuid );
                                Utils.tell( sender , Utils.getString( "fly.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                                Utils.tell( uuid , Utils.getString( "fly.enabled" , "lg" , "staff" ) );
                            }
                        } else {
                            Utils.tell( sender , Utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                        }
                    } else {
                        Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "fly | fly <player>" ) );
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "no_permission" , "lg" , "staff" ) );
                }
                
            } else {
                if ( args.length == 1 ) {
                    if ( Utils.isRegistered( args[0] ) ) {
                        UUID uuid = Utils.getUUIDFromName( args[0] );
                        if ( UserUtils.getFly( uuid ) ) {
                            FlyManager.disable( uuid );
                            Utils.tell( sender , Utils.getString( "fly.disabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                            Utils.tell( uuid , Utils.getString( "fly.disabled" , "lg" , "staff" ) );
                        } else {
                            FlyManager.enable( uuid );
                            Utils.tell( sender , Utils.getString( "fly.enabled_to" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                            Utils.tell( uuid , Utils.getString( "fly.enabled" , "lg" , "staff" ) );
                        }
                    } else {
                        Utils.tell( sender , Utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "fly | fly <player>" ) );
                }
            }
        } else {
            Utils.tell( sender , Utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
}
