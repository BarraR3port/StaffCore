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
    
    public HelpOp( main plugin ){
        plugin.getCommand( "helpop" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( sender instanceof Player ) {
            if ( sender.hasPermission( "staffcore.helpop" ) ) {
                if ( args.length >= 1 ) {
                    Player player = (Player) sender;
                    StringBuilder reason = new StringBuilder( );
                    for ( String arg : args ) {
                        reason.append( arg ).append( " " );
                    }
                    for ( Player p : Bukkit.getOnlinePlayers( ) ) {
                        if ( p.hasPermission( "staffcore.staff" ) ) {
                            if ( p != sender ) {
                                utils.tell( p , utils.getString( "helpop.tostaff" , "lg" , null ).replace( "%player%" , player.getName( ) ) + reason );
                                utils.PlaySound( p , "helpop" );
                            }
                        }
                    }
                    utils.tell( sender , utils.getString( "helpop.tousers" , "lg" , null ) );
                    SendMsg.helpOp( sender.getName( ) , String.valueOf( reason ) , utils.getString( "bungeecord.server" , null , null ) );
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "sv" ).replace( "%command%" , "helpop <reason>" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
            }
        } else {
            utils.tell( sender , utils.getString( "only_players" , "lg" , "sv" ) );
        }
        return false;
    }
}
