package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpOp implements CommandExecutor {
    
    private final main plugin;
    
    public HelpOp( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "helpop" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        
        if ( sender instanceof Player ) {
            if ( sender.hasPermission( "staffcore.helpop" ) ){
                if ( args.length >=1 ){
                    StringBuilder reason = new StringBuilder( );
                    for ( int i = 0; i < args.length; i++ ) {
                        reason.append( args[i] ).append( " " );
                    }
                    for ( Player p : Bukkit.getOnlinePlayers( ) ){
                        if ( p.hasPermission( "staffcore.helpop" ) ){
                             if ( p != sender ) {
                                 utils.tell( p , utils.getString( "helpop.tostaff" ).replace( "%user%" , sender.getName( ) ) + reason );
                                 utils.PlaySound( p , "helpop" );
                             }
                        }
                    }
                    utils.tell( sender,  utils.getString( "helpop.tousers" ) );
                    SendMsg.helpOp( sender.getName(), String.valueOf( reason ) , utils.getString( "bungeecord.server" ) );
                } else {
                    utils.tell( sender, utils.getString("server_prefix" ) + "&cYou need to write something .-." );
                }
            }  else {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
            }
        } else {
            utils.tell( sender , "&4This command can only be executed by players" );
        }
        return false;
    }
}
