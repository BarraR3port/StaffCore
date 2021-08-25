/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.MSGChanel.SendMsg;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;


public class StaffChat implements CommandExecutor {
    
    private final main plugin;
    
    public StaffChat( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "staffchat" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !Utils.isOlderVersion( ) ) {
            if ( !(sender instanceof Player) ) {
                if ( args.length > 0 ) {
                    SendMsg.sendStaffChatMSG( Utils.getConsoleName( ) , String.join( " " , args ) , Utils.getServer( ) );
                    for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                        if ( people.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) || people.hasPermission( "staff.sc" ) ) {
                            Utils.tell( people , Utils.getString( "staff_chat.prefix" , "lg" , null ).replace( "%sender%" , Utils.getConsoleName( ) ).replace( "%msg%" , String.join( " " , args ) ) );
                        }
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "sc <msg>" ) );
                }
            } else {
                if ( args.length > 0 ) {
                    Player p = ( Player ) sender;
                    if ( p.hasPermission( "staffcore.sc" ) ) {
                        SendMsg.sendStaffChatMSG( p.getName( ) , String.join( " " , args ) , Utils.getServer( ) );
                        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                            if ( people.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) || people.hasPermission( "staff.sc" ) ) {
                                Utils.tell( people , Utils.getString( "staff_chat.prefix" , "lg" , null ).replace( "%sender%" , p.getName( ) ).replace( "%msg%" , String.join( " " , args ) ) );
                            }
                        }
                    } else {
                        Utils.tell( sender , Utils.getString( "no_permission" , "lg" , "staff" ) );
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "sc <msg>" ) );
                }
            }
        } else {
            Utils.tell( sender , Utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
}
