/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.OpenEnderSee;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class invSeeEnder implements CommandExecutor {
    
    public invSeeEnder( main plugin ){
        plugin.getCommand( "endersee" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !Utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        if ( sender.hasPermission( "staffcore.endersee" ) ) {
                            Player p = ( Player ) sender;
                            Player p2 = Bukkit.getPlayer( args[0] );
                            Utils.tell( p , Utils.getString( "invsee.ender_chest" , "lg" , "staff" ).replace( "%player%" , p2.getName( ) ) );
                            new OpenEnderSee( p , p2 );
                        } else {
                            Utils.tell( sender , Utils.getString( "no_permission" , "lg" , "staff" ) );
                        }
                    } else {
                        Utils.tell( sender , Utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "endersee <player>" ) );
                }
            } else {
                Utils.tell( sender , Utils.getString( "only_players" , "lg" , "sv" ) );
            }
        } else {
            Utils.tell( sender , Utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return false;
    }
}
