package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import cl.bebt.staffcore.utils.wipePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class wipe implements TabExecutor {
    
    private final main plugin;
    
    public wipe( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "wipe" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( sender instanceof Player ) {
            Player p = ( Player ) sender;
            if ( p.hasPermission( "staffcore.wipe" ) ) {
                if ( args.length == 1 ) {
                    if ( utils.isRegistered( args[0] ) ) {
                        new wipePlayer( plugin , sender , args[0] );
                        Bukkit.getScheduler( ).scheduleSyncDelayedTask( plugin , ( ) -> {
                            try {
                                utils.tell( Bukkit.getPlayer( args[0] ) , "&cYour account is Wiping" );
                                String ban_msg = "\n";
                                for ( String msg : main.plugin.getConfig( ).getStringList( "wipe.wipe_kick_msg" ) ) {
                                    msg = msg.replace( "%wiper%" , p.getName( ) );
                                    msg = msg.replace( "%wiped%" , args[0] );
                                    ban_msg = ban_msg + msg + "\n";
                                }
                                Bukkit.getPlayer( args[0] ).kickPlayer( utils.chat( ban_msg ) );
                            } catch ( NullPointerException ignored ) {
                            }
                        } , 6L );
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7The player &c" + args[0] + " &7is not registered" );
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&aPlease use: /wipe <player>" );
                }
            } else {
                utils.tell( p , plugin.getConfig( ).getString( "no_permissions" ) );
            }
        } else {
            if ( args.length == 1 ) {
                if ( utils.isRegistered( args[0] ) ) {
                    new wipePlayer( plugin , sender , args[0] );
                    Bukkit.getScheduler( ).scheduleSyncDelayedTask( plugin , ( ) -> {
                        try {
                            utils.tell( Bukkit.getPlayer( args[0] ) , "&cYour account is Wiping" );
                            String ban_msg = "\n";
                            for ( String msg : main.plugin.getConfig( ).getStringList( "wipe.wipe_kick_msg" ) ) {
                                msg = msg.replace( "%wiper%" , "CONSOLE" );
                                msg = msg.replace( "%wiped%" , args[0] );
                                ban_msg = ban_msg + msg + "\n";
                            }
                            Bukkit.getPlayer( args[0] ).kickPlayer( utils.chat( ban_msg ) );
                        } catch ( NullPointerException ignored ) {
                        }
                    } , 6L );
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7The player &c" + args[0] + " &7is not registered" );
                }
            } else {
                utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&aPlease use: /wipe <player>" );
            }
        }
        return true;
    }
    
    @Override
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        List < String > version = new ArrayList <>( );
        if ( args.length == 1 ) {
            ArrayList < String > Players = utils.getUsers( );
            if ( !Players.isEmpty( ) ) {
                Players.remove( sender.getName( ) );
                version.addAll( Players );
            } else {
                utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&cNo players saved!" );
            }
        }
        return version;
    }
    
}
