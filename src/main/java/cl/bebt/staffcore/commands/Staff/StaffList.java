package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Staff.StaffListGui;
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
        if ( !utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                if ( args.length == 0 ) {
                    Player p = ( Player ) sender;
                    if ( p.hasPermission( "staffcore.stafflist" ) ) {
                        if ( utils.getBoolean( "bungeecord.enabled" ) ) {
                            main.staffMembers.clear( );
                            main.playersServerMap.clear( );
                            main.playersServerPingMap.clear( );
                            main.playersServerGamemodesMap.clear( );
                            SendMsg.sendStaffListRequest( p.getName( ) , utils.getString( "bungeecord.server" ) );
                        } else {
                            new StaffListGui( new PlayerMenuUtility( p ) , plugin ).open( );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "stafflist" ) );
                }
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
}
