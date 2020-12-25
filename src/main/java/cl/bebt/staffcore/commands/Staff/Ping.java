package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class Ping implements CommandExecutor {
    
    private final main plugin;
    
    public Ping( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "ping" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&4Wrong usage" );
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&4Use ping <player>" );
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player p = Bukkit.getPlayer( args[0] );
                    try {
                        Object entityPlayer = p.getClass( ).getMethod( "getHandle" ).invoke( p );
                        int ping = ( int ) entityPlayer.getClass( ).getField( "ping" ).get( entityPlayer );
                        utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7The player &r" + args[0] + " &7ping is: &a" + ping );
                    } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e ) {
                        e.printStackTrace( );
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                }
            }
        } else if ( sender instanceof Player ) {
            if ( args.length == 0 ) {
                Player p = ( Player ) sender;
                
                if ( p.hasPermission( "staffcore.ping" ) ) {
                    try {
                        Object entityPlayer = p.getClass( ).getMethod( "getHandle" ).invoke( p );
                        int ping = ( int ) entityPlayer.getClass( ).getField( "ping" ).get( entityPlayer );
                        p.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7Yor ping is: &a" + ping ) );
                    } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e ) {
                        e.printStackTrace( );
                    }
                } else {
                    p.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) ) );
                }
            } else if ( args.length == 1 ) {
                if ( sender.hasPermission( "staffcore.ip" ) ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player p = Bukkit.getPlayer( args[0] );
                        try {
                            Object entityPlayer = p.getClass( ).getMethod( "getHandle" ).invoke( p );
                            int ping = ( int ) entityPlayer.getClass( ).getField( "ping" ).get( entityPlayer );
                            utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7The player &r" + args[0] + " &7ping is: &a" + ping );
                        } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e ) {
                            e.printStackTrace( );
                        }
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
                }
            }
        }
        
        
        return true;
    }
}