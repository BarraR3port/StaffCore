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
        if ( !utils.isOlderVersion( ) ){
            if ( !(sender instanceof Player) ) {
                if ( args.length > 0 ) {
                    SendMsg.sendStaffChatMSG( "CONSOLE" , String.join( " " , args ) , plugin.getConfig( ).getString( "bungeecord.server" ) );
                    for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                        if ( people.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) || people.hasPermission( "staff.staffchat" ) ) {
                            String message = plugin.getConfig( ).getString( "staff.staff_chat_prefix" );
                            message = message.replace( "%sender%" , "CONSOLE" );
                            message = message.replace( "%msg%" , String.join( " " , args ) );
                            utils.tell( people , message );
                        }
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&ause /sc <msg>" );
                }
            } else {
                if ( args.length > 0 ) {
                    Player p = ( Player ) sender;
                    if ( p.hasPermission( "staffcore.sc" ) ) {
                        SendMsg.sendStaffChatMSG( p.getName( ) , String.join( " " , args ) , plugin.getConfig( ).getString( "bungeecord.server" ) );
                        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                            if ( people.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) || people.hasPermission( "staff.staffchat" ) ) {
                                String message = plugin.getConfig( ).getString( "staff.staff_chat_prefix" );
                                message = message.replace( "%sender%" , p.getName( ) );
                                message = message.replace( "%msg%" , String.join( " " , args ) );
                                utils.tell( people , message );
                            }
                        }
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&ause /sc <msg>" );
                }
            }
        } else {
            utils.tell(sender,plugin.getConfig( ).getString( "server_prefix" )+"&cThis command can't be executed in older versions");
        }
        return true;
    }
}
