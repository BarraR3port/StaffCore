package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
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
        if ( !utils.isOlderVersion( ) ) {
            if ( !(sender instanceof Player) ) {
                if ( args.length > 0 ) {
                    SendMsg.sendStaffChatMSG( "CONSOLE" , String.join( " " , args ) , utils.getServer( ) );
                    for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                        if ( people.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) || people.hasPermission( "staff.staffchat" ) ) {
                            utils.tell( people , utils.getString( "staff_chat.prefix" , "lg" , null ).replace( "%sender%" , "CONSOLE" ).replace( "%msg%" , String.join( " " , args ) ) );
                        }
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "sc <msg>" ) );
                }
            } else {
                if ( args.length > 0 ) {
                    Player p = ( Player ) sender;
                    if ( p.hasPermission( "staffcore.sc" ) ) {
                        SendMsg.sendStaffChatMSG( p.getName( ) , String.join( " " , args ) , utils.getServer( ) );
                        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                            if ( people.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) || people.hasPermission( "staff.staffchat" ) ) {
                                utils.tell( people , utils.getString( "staff_chat.prefix" , "lg" , null ).replace( "%sender%" , p.getName( ) ).replace( "%msg%" , String.join( " " , args ) ) );
                            }
                        }
                    } else {
                        utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "sc <msg>" ) );
                }
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
}
