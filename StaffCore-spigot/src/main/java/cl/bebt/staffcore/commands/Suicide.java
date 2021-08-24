/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands;

import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Suicide implements CommandExecutor {
    
    private final main plugin;
    
    public Suicide( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "suicide" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( sender instanceof Player ) {
            Player p = ( Player ) sender;
            if ( p.hasPermission( "staffcore.suicide" ) ) {
                p.setHealth( 0 );
            } else {
                Utils.tell( sender , Utils.getString( "no_permission" , "lg" , "sv" ) );
            }
        } else {
            Utils.tell( sender , Utils.getString( "only_players" , "lg" , "sv" ) );
        }
        
        return true;
    }
}