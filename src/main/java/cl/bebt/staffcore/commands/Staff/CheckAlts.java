package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CheckAlts implements TabExecutor {
    private static main plugin;
    
    public CheckAlts( main plugin ){
        CheckAlts.plugin = plugin;
        plugin.getCommand( "alts" ).setExecutor( this );
    }
    
    public static List < String > ips( String p ){
        if ( utils.mysqlEnabled( ) ) {
            return utils.makeList( SQLGetter.getAlts( p ) );
        } else {
            return plugin.alts.getConfig( ).getStringList( "alts." + p );
        }
    }
    
    public static List < String > alts( String player ){
        List < String > alts = new ArrayList <>( );
        List < String > accounts = new ArrayList <>( );
        if ( utils.mysqlEnabled( ) ) {
            List < String > ip = utils.makeList( SQLGetter.getAlts( player ) );
            List < String > players = SQLGetter.getPlayersNames( );
            for ( String key : players ) {
                if ( !player.equals( key ) ) {
                    alts.add( key );
                }
            }
            for ( String alt : alts ) {
                List < String > list2 = utils.makeList( SQLGetter.getAlts( alt ) );
                if ( ip.stream( ).anyMatch( list2::contains ) ) {
                    accounts.add( alt );
                }
            }
        } else {
            ConfigurationSection inventorySection = plugin.alts.getConfig( ).getConfigurationSection( "alts" );
            List < String > ip = main.plugin.alts.getConfig( ).getStringList( "alts." + player );
            assert inventorySection != null;
            for ( String key : inventorySection.getKeys( false ) ) {
                if ( !player.equals( key ) ) {
                    alts.add( key );
                }
            }
            for ( String alt : alts ) {
                List < String > list2 = main.plugin.alts.getConfig( ).getStringList( "alts." + alt );
                if ( ip.stream( ).anyMatch( list2::contains ) ) {
                    accounts.add( alt );
                }
            }
        }
        return accounts;
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( sender instanceof Player ) {
            Player p = ( Player ) sender;
            if ( p.hasPermission( "staffcore.alts" ) ) {
                if ( args.length == 1 ) {
                    try {
                        List < String > alts = alts( args[0] );
                        if ( alts.isEmpty( ) ) {
                            utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&a" + args[0] + " &cdon't have alts." );
                        } else {
                            utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&a" + args[0] + "'s &7alts:" );
                            for ( String alt : alts ) {
                                List < String > ips = ips( alt );
                                //String ips_tostring = utils.stringify(ips(alt));
                                if ( !alt.equalsIgnoreCase( args[0] ) ) {
                                    utils.tell( p , "&7  ► &a" + alt );
                                    for ( String ip : ips ) {
                                        utils.tell( p , "&7    ► &a" + ip );
                                    }
                                }
                            }
                        }
                    } catch ( NullPointerException error ) {
                        String msg = plugin.getConfig( ).getString( "staff.never_seen" );
                        msg = msg.replace( "%player%" , args[0] );
                        utils.tell( p , main.plugin.getConfig( ).getString( "staff.staff_prefix" ) + msg );
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7Use /alts <player>" );
                }
            } else {
                utils.tell( p , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
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
