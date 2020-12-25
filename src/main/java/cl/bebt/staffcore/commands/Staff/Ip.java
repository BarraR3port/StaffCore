package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.InetAddress;

public class Ip implements CommandExecutor{

    private final main plugin;

    public Ip( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "ip" ).setExecutor( this );

    }

    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&4&lWrong usage." );
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&4&lUse ping <player>" );
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player p = Bukkit.getPlayer( args[0] );
                    InetAddress address = p.getAddress( ).getAddress( );
                    String ip = address.toString( );
                    ip = ip.replace( "/" , "" );
                    String message = plugin.getConfig( ).getString( "staff.ip_other" );
                    message = message.replace( "%ip%" , ip );
                    message = message.replace( "%target%" , args[0] );
                    utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + message );
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                }
            }
        } else if ( sender instanceof Player ) {
            if ( args.length == 0 ) {
                Player p = ( Player ) sender;
                if ( p.hasPermission( "staffcore.ip" ) ) {
                    InetAddress address = p.getAddress( ).getAddress( );
                    String ip = address.toString( );
                    ip = ip.replace( "/" , "" );
                    String message = plugin.getConfig( ).getString( "staff.ip" );
                    message = message.replace( "%ip%" , ip );
                    utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + message );
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
                }
            } else if ( args.length == 1 ) {
                if ( sender.hasPermission( "staffcore.ip" ) ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player player = Bukkit.getPlayer( args[0] );
                        InetAddress address = player.getAddress( ).getAddress( );
                        String ip = address.toString( );
                        ip = ip.replace( "/" , "" );
                        String message = plugin.getConfig( ).getString( "staff.ip_other" );
                        message = message.replace( "%ip%" , ip );
                        message = message.replace( "%target%" , args[0] );
                        utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + message );
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

