package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ip implements CommandExecutor {
    
    private final main plugin;
    
    public Ip( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "ip" ).setExecutor( this );
        
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    String ip = StaffCoreAPI.getIp( Bukkit.getPlayer( args[0] ) );
                    utils.tell( sender , utils.getString( "ip_other" , "lg" , "sv" ).replace( "%ip%" , ip ).replace( "%player%" , args[0] ) );
                } else {
                    utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "ip | ip <player>" ) );
            }
        } else if ( sender instanceof Player ) {
            if ( args.length == 0 ) {
                Player p = ( Player ) sender;
                if ( p.hasPermission( "staffcore.ip" ) ) {
                    utils.tell( sender , utils.getString( "ip" , "lg" , "sv" ).replace( "%ip%" , StaffCoreAPI.getIp( p ) ) );
                } else {
                    utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            } else if ( args.length == 1 ) {
                if ( sender.hasPermission( "staffcore.ip.others" ) ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        String ip = StaffCoreAPI.getIp( Bukkit.getPlayer( args[0] ) );
                        utils.tell( sender , utils.getString( "ip_other" , "lg" , "sv" ).replace( "%ip%" , ip ).replace( "%player%" , args[0] ) );
                    } else {
                        utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            }
        }
        return true;
    }
}

