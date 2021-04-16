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
    
    private static Boolean isCord( Player p , String x , String y , String z ){
        try {
            double ex;
            double way;
            double ze;
            if ( x.equalsIgnoreCase( "~" ) ){
                ex = p.getLocation().getX();
            } else {
                ex = Double.parseDouble( x );
            }
            if ( y.equalsIgnoreCase( "~" ) ){
                way = p.getLocation().getY();
            } else {
                way = Double.parseDouble( y );
            }
            if ( z.equalsIgnoreCase( "~" ) ){
                ze = p.getLocation().getZ();
            } else {
                ze = Double.parseDouble( z );
            }
            double sum = ex + way + ze;
            return true;
        } catch ( NumberFormatException error ) {
            
            return false;
        }
    }
    
    private static Boolean isCordAndPlayer( String x , String y , String z , String player ){
        if ( Bukkit.getPlayer( player ) instanceof Player ) {
            try {
                double ex;
                double way;
                double ze;
                if ( x.equalsIgnoreCase( "~" ) ){
                    ex = Bukkit.getPlayer( player ).getLocation().getX();
                } else {
                    ex = Double.parseDouble( x );
                }
                if ( y.equalsIgnoreCase( "~" ) ){
                    way = Bukkit.getPlayer( player ).getLocation().getY();
                } else {
                    way = Double.parseDouble( y );
                }
                if ( z.equalsIgnoreCase( "~" ) ){
                    ze = Bukkit.getPlayer( player ).getLocation().getZ();
                } else {
                    ze = Double.parseDouble( z );
                }
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
                double ex;
                double way;
                double ze;
                if ( x.equalsIgnoreCase( "~" ) ){
                    ex = Bukkit.getPlayer( player ).getLocation().getX();
                } else {
                    ex = Double.parseDouble( x );
                }
                if ( y.equalsIgnoreCase( "~" ) ){
                    way = Bukkit.getPlayer( player ).getLocation().getY();
                } else {
                    way = Double.parseDouble( y );
                }
                if ( z.equalsIgnoreCase( "~" ) ){
                    ze = Bukkit.getPlayer( player ).getLocation().getZ();
                } else {
                    ze = Double.parseDouble( z );
                }
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
                    double x;
                    double y;
                    double z;
                    Player target = Bukkit.getPlayer( args[3] );
                    if ( args[0].equalsIgnoreCase( "~" ) ){
                        x = target.getLocation().getX();
                    } else {
                        x = Double.parseDouble( args[0] );
                    }
                    if ( args[1].equalsIgnoreCase( "~" ) ){
                        y = target.getLocation().getY();
                    } else {
                        y = Double.parseDouble( args[1] );
                    }
                    if ( args[2].equalsIgnoreCase( "~" ) ){
                        z = target.getLocation().getZ();
                    } else {
                        z = Double.parseDouble( args[2] );
                    }
                    TpPlayers.tpToCordsAndPlayer( sender , target , x , y , z );
                    return true;
                } else if ( isPlayerAndCord( args[0] , args[1] , args[2] , args[3] ) ) {
                    double x;
                    double y;
                    double z;
                    Player target = Bukkit.getPlayer( args[0] );
                    if ( args[1].equalsIgnoreCase( "~" ) ){
                        x = target.getLocation().getX();
                    } else {
                        x = Double.parseDouble( args[1] );
                    }
                    if ( args[2].equalsIgnoreCase( "~" ) ){
                        y = target.getLocation().getY();
                    } else {
                        y = Double.parseDouble( args[2] );
                    }
                    if ( args[3].equalsIgnoreCase( "~" ) ){
                        z = target.getLocation().getZ();
                    } else {
                        z = Double.parseDouble( args[3] );
                    }
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
                    if ( isCord( p , args[0] , args[1] , args[2] ) ) {
                        double x;
                        double y;
                        double z;
                        if ( args[0].equalsIgnoreCase( "~" ) ){
                            x = p.getLocation().getX();
                        } else {
                            x = Double.parseDouble( args[0] );
                        }
                        if ( args[1].equalsIgnoreCase( "~" ) ){
                            y = p.getLocation().getY();
                        } else {
                            y = Double.parseDouble( args[1] );
                        }
                        if ( args[2].equalsIgnoreCase( "~" ) ){
                            z = p.getLocation().getZ();
                        } else {
                            z = Double.parseDouble( args[2] );
                        }
                        TpPlayers.tpToCords( p , x , y , z );
                    } else {
                        for ( String s : plugin.getConfig( ).getStringList( "tp.wrong" ) ) {
                            utils.tell( sender , s );
                        }
                    }
                    return true;
                } else if ( args.length == 4 ) {
                    if ( args[0].equalsIgnoreCase( "all" ) ) {
                        if ( isCord( p , args[1] , args[2] , args[3] ) ) {
                            double x;
                            double y;
                            double z;
                            if ( args[0].equalsIgnoreCase( "~" ) ){
                                x = p.getLocation().getX();
                            } else {
                                x = Double.parseDouble( args[0] );
                            }
                            if ( args[1].equalsIgnoreCase( "~" ) ){
                                y = p.getLocation().getY();
                            } else {
                                y = Double.parseDouble( args[1] );
                            }
                            if ( args[2].equalsIgnoreCase( "~" ) ){
                                z = p.getLocation().getZ();
                            } else {
                                z = Double.parseDouble( args[2] );
                            }
                            TpPlayers.tpAllToCords( p , x , y , z );
                        } else {
                            for ( String s : plugin.getConfig( ).getStringList( "tp.wrong" ) ) {
                                utils.tell( sender , s );
                            }
                        }
                        return true;
                    } else if ( args[3].equalsIgnoreCase( "all" ) ) {
                        if ( isCord( p , args[0] , args[1] , args[2] ) ) {
                            double x;
                            double y;
                            double z;
                            if ( args[0].equalsIgnoreCase( "~" ) ){
                                x = p.getLocation().getX();
                            } else {
                                x = Double.parseDouble( args[0] );
                            }
                            if ( args[1].equalsIgnoreCase( "~" ) ){
                                y = p.getLocation().getY();
                            } else {
                                y = Double.parseDouble( args[1] );
                            }
                            if ( args[2].equalsIgnoreCase( "~" ) ){
                                z = p.getLocation().getZ();
                            } else {
                                z = Double.parseDouble( args[2] );
                            }
                            TpPlayers.tpAllToCords( p , x , y , z );
                        } else {
                            for ( String s : plugin.getConfig( ).getStringList( "tp.wrong" ) ) {
                                utils.tell( sender , s );
                            }
                        }
                        return true;
                    } else if ( isCordAndPlayer( args[0] , args[1] , args[2] , args[3] ) ) {
                        double x;
                        double y;
                        double z;
                        Player target = Bukkit.getPlayer( args[3] );
                        if ( args[0].equalsIgnoreCase( "~" ) ){
                            x = target.getLocation().getX();
                        } else {
                            x = Double.parseDouble( args[0] );
                        }
                        if ( args[1].equalsIgnoreCase( "~" ) ){
                            y = target.getLocation().getY();
                        } else {
                            y = Double.parseDouble( args[1] );
                        }
                        if ( args[2].equalsIgnoreCase( "~" ) ){
                            z = target.getLocation().getZ();
                        } else {
                            z = Double.parseDouble( args[2] );
                        }
                        TpPlayers.tpToCordsAndPlayer( p , target , x , y , z );
                        return true;
                    } else if ( isPlayerAndCord( args[0] , args[1] , args[2] , args[3] ) ) {
                        double x;
                        double y;
                        double z;
                        Player target = Bukkit.getPlayer( args[0] );
                        if ( args[1].equalsIgnoreCase( "~" ) ){
                            x = p.getLocation().getX();
                        } else {
                            x = Double.parseDouble( args[1] );
                        }
                        if ( args[2].equalsIgnoreCase( "~" ) ){
                            y = p.getLocation().getY();
                        } else {
                            y = Double.parseDouble( args[2] );
                        }
                        if ( args[3].equalsIgnoreCase( "~" ) ){
                            z = p.getLocation().getZ();
                        } else {
                            z = Double.parseDouble( args[3] );
                        }
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
