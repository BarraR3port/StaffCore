package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.menu.Bangui.BanMenu;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.BanPlayer;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Ban implements TabExecutor {
    private final main plugin;
    
    public Ban( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "ban" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                Player p = ( Player ) sender;
                if ( p.hasPermission( "staffcore.ban" ) ) {
                    p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "ban-ip" ) );
                    if ( args.length == 0 ) {
                        utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7Use /ban <player> <time> <-ip> <reason>" );
                    } else if ( args.length == 1 ) {
                        if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                            new BanMenu( main.getPlayerMenuUtility( p ) , plugin , p , args[0] ).open( p );
                            return true;
                        } else {
                            try {
                                if ( utils.mysqlEnabled( ) ) {
                                    String IP = null;
                                    List < String > ips = utils.makeList( SQLGetter.getAlts( args[0] ) );
                                    IP = ips.subList( ips.size( ) - 1 , ips.size( ) ).toString( );
                                    new BanMenu( main.getPlayerMenuUtility( p ) , plugin , p , args[0] ).open( p );
                                    return true;
                                } else {
                                    String IP = null;
                                    List < ? extends String > ips = plugin.alts.getConfig( ).getStringList( "alts." + args[0] );
                                    IP = ips.subList( ips.size( ) - 1 , ips.size( ) ).toString( );
                                    new BanMenu( main.getPlayerMenuUtility( p ) , plugin , p , args[0] ).open( p );
                                    return true;
                                }
                            } catch ( NullPointerException | IndexOutOfBoundsException ignored ) {
                                String msg = plugin.getConfig( ).getString( "staff.never_seen" );
                                msg = msg.replace( "%player%" , args[0] );
                                utils.tell( p , main.plugin.getConfig( ).getString( "staff.staff_prefix" ) + msg );
                            }
                        }
                    } else {
                        if ( isNormal( args[1] ) ) {
                            String lastWord = args[1].substring( args[1].length( ) - 1 );
                            int amount = Integer.parseInt( args[1].substring( 0 , args[1].length( ) - 1 ) );
                            StringBuilder reason = new StringBuilder( );
                            if ( args[2].equalsIgnoreCase( "-ip" ) ) {
                                for ( int i = 3; i < args.length; i++ ) {
                                    reason.append( args[i] ).append( " " );
                                }
                                p.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "ban-ip" ) , PersistentDataType.STRING , "ban-ip" );
                            } else {
                                for ( int i = 2; i < args.length; i++ ) {
                                    reason.append( args[i] ).append( " " );
                                }
                            }
                            BanPlayer.BanCooldown( p , args[0] , String.valueOf( reason ) , amount , lastWord );
                        } else {
                            StringBuilder reason = new StringBuilder( );
                            if ( args[1].equalsIgnoreCase( "-ip" ) ) {
                                for ( int i = 2; i < args.length; i++ ) {
                                    reason.append( args[i] ).append( " " );
                                }
                                p.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "ban-ip" ) , PersistentDataType.STRING , "ban-ip" );
                            } else {
                                for ( int i = 1; i < args.length; i++ ) {
                                    reason.append( args[i] ).append( " " );
                                }
                            }
                            BanPlayer.BanPlayer( p , args[0] , String.valueOf( reason ) );
                        }
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
                }
            }
        } else {
            utils.tell(sender,plugin.getConfig( ).getString( "server_prefix" )+"&cThis command can't be executed in older versions");
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
    
    private Boolean isNormal( String s ){
        String lastWord = s.substring( s.length( ) - 1 );
        String ncadena = s.substring( 0 , s.length( ) - 1 );
        try {
            int amount = Integer.parseInt( ncadena );
            return lastWord.equalsIgnoreCase( "s" ) ||
                    lastWord.equalsIgnoreCase( "m" ) ||
                    lastWord.equalsIgnoreCase( "h" ) ||
                    lastWord.equalsIgnoreCase( "d" );
        } catch ( NumberFormatException error ) {
            return false;
        }
    }
}
