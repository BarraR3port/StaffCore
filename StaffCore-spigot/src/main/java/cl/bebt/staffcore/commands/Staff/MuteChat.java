/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.ToggleChat;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MuteChat implements TabExecutor {
    private final main plugin;
    
    public MuteChat( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "mutechat" ).setExecutor( this );
    }
    
    @Override
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        if ( args.length == 2 ) {
            String current = args[1];
            List < String > version = new ArrayList <>( );
            version.add( current + "s" );
            version.add( current + "m" );
            version.add( current + "h" );
            version.add( current + "d" );
            return version;
        }
        return null;
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( (args.length == 1 || args.length == 2) && Utils.isOlderVersion( ) ) {
                Utils.tell( sender , Utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
                return true;
            }
            if ( args.length == 0 ) {
                if ( !plugin.chatMuted ) {
                    Bukkit.broadcastMessage( Utils.chat( Utils.getString( "toggle_chat.mute_by_console" , "lg" , "staff" ) ) );
                    ToggleChat.Mute( true );
                } else {
                    Bukkit.broadcastMessage( Utils.chat( Utils.getString( "toggle_chat.un_mute_by_console" , "lg" , "staff" ) ) );
                    ToggleChat.Mute( false );
                }
            } else {
                Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "mute <player>" ) );
            }
        } else {
            Player p = ( Player ) sender;
            if ( p.hasPermission( "staffcore.mutechat" ) ) {
                if ( (args.length == 1 || args.length == 2) && Utils.isOlderVersion( ) ) {
                    Utils.tell( sender , Utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
                    return true;
                }
                if ( args.length == 0 ) {
                    if ( !plugin.chatMuted ) {
                        Bukkit.broadcastMessage( Utils.chat( Utils.getString( "toggle_chat.mute_chat" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) ) );
                        ToggleChat.Mute( true );
                    } else {
                        Bukkit.broadcastMessage( Utils.chat( Utils.getString( "toggle_chat.un_mute_chat" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) ) );
                        ToggleChat.Mute( false );
                    }
                    
                } else {
                    Utils.tell( sender , Utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                    
                }
            } else {
                Utils.tell( sender , Utils.getString( "no_permission" , "lg" , "staff" ) );
            }
        }
        
        return true;
        
    }
    
}
