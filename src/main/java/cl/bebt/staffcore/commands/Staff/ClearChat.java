package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChat implements CommandExecutor {
    
    private final main plugin;
    
    public ClearChat( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "clearchat" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command command , String label , String[] args ){
        if ( sender.hasPermission( "staffcore.clearchat" ) ) {
            if ( args.length == 0 ) {
                utils.ccAll( );
                Bukkit.broadcastMessage( utils.chat( utils.getString( "clear_chat.global" , "lg" , "sv" ).replace( "%player%" , sender.getName( ) ) ) );
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player p = Bukkit.getPlayer( args[0] );
                    if ( sender.getName( ).equals( p.getName( ) ) ) {
                        utils.ccPlayer( p );
                        utils.tell( sender , utils.getString( "clear_chat.own" , "lg" , "sv" ) );
                    } else {
                        utils.ccPlayer( p );
                        utils.tell( sender , utils.getString( "clear_chat.global" , "lg" , "sv" ).replace( "%player%" , sender.getName( ) ) );
                        utils.tell( p , utils.getString( "clear_chat.player" , "lg" , "sv" ) );
                        
                    }
                } else {
                    utils.tell( sender , utils.chat( utils.getString( "p_dont_exist" , "lg" , "sv" ) ) );
                }
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "cc | cc <player>" ) );
            }
        } else {
            utils.tell( sender , utils.chat( utils.getString( "no_permission" , "lg" , "staff" ) ) );
        }
        return true;
    }
    
    
}