package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;

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
        if ( sender.hasPermission( "staffcore.alts" ) ) {
            if ( args.length == 1 ) {
                try {
                    List < String > alts = alts( args[0] );
                    if ( alts.isEmpty( ) ) {
                        utils.tell( sender , utils.getString( "alts.no_alts" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                    } else {
                        utils.tell( sender , utils.getString( "alts.alts" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                        for ( String alt : alts ) {
                            List < String > ips = ips( alt );
                            if ( !alt.equalsIgnoreCase( args[0] ) ) {
                                utils.tell( sender , "&7  ► &a" + alt );
                                for ( String ip : ips ) {
                                    utils.tell( sender , "&7    ► &a" + ip );
                                }
                            }
                        }
                    }
                } catch ( NullPointerException error ) {
                    utils.tell( sender , utils.getString( "never_seen" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                }
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "alts <player>" ) );
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
            }
        }
        return version;
    }
}
