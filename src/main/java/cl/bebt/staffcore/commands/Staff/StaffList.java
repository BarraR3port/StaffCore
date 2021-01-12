package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Others.StaffListGui;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffList implements CommandExecutor {
    
    private final main plugin;
    
    public StaffList( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "stafflist" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ){
            if ( sender instanceof Player ) {
                Player p = ( Player ) sender;
                if ( p.hasPermission( "staffcore.stafflist" ) ) {
                    if ( utils.getBoolean( "bungeecord.enabled" ) ) {
                        main.staffMembers.clear( );
                        main.playersServerMap.clear( );
                        main.playersServerPingMap.clear( );
                        main.playersServerGamemodesMap.clear( );
                        SendMsg.sendStaffListRequest( p.getName( ) , utils.getString( "bungeecord.server" ) );
                    } else {
                        new StaffListGui( new PlayerMenuUtility( p ) , plugin ).open( p );
                    }
                    
                }
            }
        } else {
            utils.tell(sender,plugin.getConfig( ).getString( "server_prefix" )+"&cThis command can't be executed in older versions");
        }
        return true;
    }
}
