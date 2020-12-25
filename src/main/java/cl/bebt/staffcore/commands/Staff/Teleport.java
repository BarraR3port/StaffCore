package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.TpPlayers;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teleport implements CommandExecutor {
    
    private final main plugin;
    
    public Teleport( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "tp" ).setExecutor( this );
    }
    
    private static Boolean isCord( String x , String y , String z ){
        try {
            double ex = Double.parseDouble( x );
            double way = Double.parseDouble( y );
            double ze = Double.parseDouble( z );
            double sum = ex + way + ze;
            return true;
        } catch ( NumberFormatException | NullPointerException error ) {
            return false;
        }
    }
    
    private static Boolean isCordAndPlayer( String x , String y , String z , String player ){
        if ( Bukkit.getPlayer( player ) instanceof Player ) {
            try {
                double ex = Double.parseDouble( x );
                double way = Double.parseDouble( y );
                double ze = Double.parseDouble( z );
                double sum = ex + way + ze;
                return true;
            } catch ( NumberFormatException | NullPointerException error ) {
                return false;
            }
        } else {
            return false;
        }
    }
    
    private static Boolean isPlayerAndCord( String player , String x , String y , String z ){
        if ( Bukkit.getPlayer( player ) instanceof Player ) {
            try {
                double ex = Double.parseDouble( x );
                double way = Double.parseDouble( y );
                double ze = Double.parseDouble( z );
                double sum = ex + way + ze;
                return true;
            } catch ( NumberFormatException | NullPointerException error ) {
                return false;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 || ((args.length == 1) && (args[0].equalsIgnoreCase( "all" ))) || args.length == 3 ||
                    (args.length == 4 && (args[0].equalsIgnoreCase( "all" ) || args[3].equalsIgnoreCase( "all" ))) ) {
                for ( String s : plugin.getConfig( ).getStringList( "tp.wrong" ) ) {
                    utils.tell( sender , s );
                }
                return true;
            } else if ( args.length == 2 ) {
                if ( args[0].equalsIgnoreCase( "all" ) ) {
                    TpPlayers.tpAll( sender , args[1] );
                    return true;
                } else if ( args[1].equalsIgnoreCase( "all" ) ) {
                    TpPlayers.tpAll( sender , args[0] );
                    return true;
                }
                TpPlayers.tpPlayerToPlayer( sender , args[0] , args[1] );
                return true;
            } else if ( args.length == 4 ) {
                if ( isCordAndPlayer( args[0] , args[1] , args[2] , args[3] ) ) {
                    double x = Double.parseDouble( args[0] );
                    double y = Double.parseDouble( args[1] );
                    double z = Double.parseDouble( args[2] );
                    Player target = Bukkit.getPlayer( args[3] );
                    TpPlayers.tpToCordsAndPlayer( sender , target , x , y , z );
                    return true;
                } else if ( isPlayerAndCord( args[0] , args[1] , args[2] , args[3] ) ) {
                    Player target = Bukkit.getPlayer( args[0] );
                    double x = Double.parseDouble( args[1] );
                    double y = Double.parseDouble( args[2] );
                    double z = Double.parseDouble( args[3] );
                    TpPlayers.tpToCordsAndPlayer( sender , target , x , y , z );
                    return true;
                } else {
                    for ( String s : plugin.getConfig( ).getStringList( "tp.wrong" ) ) {
                        utils.tell( sender , s );
                    }
                    return true;
                }
            }
        } else {
            Player p = ( Player ) sender;
            if ( p.hasPermission( "staffcore.tp" ) ) {
                if ( args.length == 0 ) {
                    for ( String s : plugin.getConfig( ).getStringList( "tp.wrong" ) ) {
                        utils.tell( sender , s );
                    }
                } else if ( args.length == 1 ) {
                    if ( args[0].equalsIgnoreCase( "all" ) ) {
                        if ( p.hasPermission( "staffcore.tp.all" ) ) {
                            TpPlayers.tpAll( p , p.getName( ) );
                            return true;
                        }
                    } else {
                        TpPlayers.tpToPlayer( p , args[0] );
                        return true;
                    }
                } else if ( args.length == 2 ) {
                    if ( p.hasPermission( "staffcore.tp.all" ) ) {
                        if ( args[0].equalsIgnoreCase( "all" ) ) {
                            TpPlayers.tpAll( p , args[1] );
                            return true;
                        } else if ( args[1].equalsIgnoreCase( "all" ) ) {
                            TpPlayers.tpAll( p , args[0] );
                            return true;
                        }
                    } else {
                        utils.tell( sender , utils.getString( "server_prefix" ) + utils.getString( "no_permissions" ) );
                        return true;
                    }
                    TpPlayers.tpPlayerToPlayer( p , args[0] , args[1] );
                } else if ( args.length == 3 ) {
                    if ( isCord( args[0] , args[1] , args[2] ) ) {
                        double x = Double.parseDouble( args[0] );
                        double y = Double.parseDouble( args[1] );
                        double z = Double.parseDouble( args[2] );
                        TpPlayers.tpToCords( p , x , y , z );
                    } else {
                        for ( String s : plugin.getConfig( ).getStringList( "tp.wrong" ) ) {
                            utils.tell( sender , s );
                        }
                    }
                    return true;
                } else if ( args.length == 4 ) {
                    if ( args[0].equalsIgnoreCase( "all" ) ) {
                        if ( isCord( args[1] , args[2] , args[3] ) ) {
                            double x = Double.parseDouble( args[1] );
                            double y = Double.parseDouble( args[2] );
                            double z = Double.parseDouble( args[3] );
                            TpPlayers.tpAllToCords( p , x , y , z );
                        } else {
                            for ( String s : plugin.getConfig( ).getStringList( "tp.wrong" ) ) {
                                utils.tell( sender , s );
                            }
                        }
                        return true;
                    } else if ( args[3].equalsIgnoreCase( "all" ) ) {
                        if ( isCord( args[0] , args[1] , args[2] ) ) {
                            double x = Double.parseDouble( args[0] );
                            double y = Double.parseDouble( args[1] );
                            double z = Double.parseDouble( args[2] );
                            TpPlayers.tpAllToCords( p , x , y , z );
                        } else {
                            for ( String s : plugin.getConfig( ).getStringList( "tp.wrong" ) ) {
                                utils.tell( sender , s );
                            }
                        }
                        return true;
                    } else if ( isCordAndPlayer( args[0] , args[1] , args[2] , args[3] ) ) {
                        double x = Double.parseDouble( args[0] );
                        double y = Double.parseDouble( args[1] );
                        double z = Double.parseDouble( args[2] );
                        Player target = Bukkit.getPlayer( args[3] );
                        TpPlayers.tpToCordsAndPlayer( p , target , x , y , z );
                        return true;
                    } else if ( isPlayerAndCord( args[0] , args[1] , args[2] , args[3] ) ) {
                        Player target = Bukkit.getPlayer( args[0] );
                        double x = Double.parseDouble( args[1] );
                        double y = Double.parseDouble( args[2] );
                        double z = Double.parseDouble( args[3] );
                        TpPlayers.tpToCordsAndPlayer( p , target , x , y , z );
                        return true;
                    } else {
                        for ( String s : plugin.getConfig( ).getStringList( "tp.wrong" ) ) {
                            utils.tell( sender , s );
                        }
                        return true;
                    }
                }
            } else {
                utils.tell( sender , utils.getString( "server_prefix" ) + utils.getString( "no_permissions" ) );
            }
        }
        return true;
    }
    
}
