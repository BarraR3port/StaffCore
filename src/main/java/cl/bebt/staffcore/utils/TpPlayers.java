package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static java.lang.Math.round;

public class TpPlayers {
    
    private static final main plugin = main.plugin;
    
    public static void tpToPlayer( Player sender , String player ){
        if ( utils.isPlayer( player ) ) {
            Player target = Bukkit.getPlayer( player );
            try {
                if ( !sender.equals( target ) ) {
                    sender.teleport( target.getLocation( ) );
                    String message = plugin.getConfig( ).getString( "tp.player_to_player" );
                    message = message.replace( "%target%" , target.getName( ) );
                    utils.tell( sender , utils.getString( "server_prefix" ) + message );
                } else {
                    utils.tell( sender , utils.getString( "server_prefix" ) + utils.getString( "tp.to_yourself" ) );
                }
            } catch ( NullPointerException offline ) {
                utils.tell( sender , utils.getString( "server_prefix" ) + "&cThe player &a" + player + " &cis offline" );
                offline.printStackTrace( );
            }
        } else {
            String message = plugin.getConfig( ).getString( "tp.not_found" );
            message = message.replace( "%player%" , player );
            utils.tell( sender , utils.getString( "server_prefix" ) + message );
        }
    }
    
    public static void tpPlayerToPlayer( CommandSender sender , String player1 , String player2 ){
        if ( utils.isPlayer( player1 ) && utils.isPlayer( player2 ) ) {
            Player from = Bukkit.getPlayer( player1 );
            Player target = Bukkit.getPlayer( player2 );
            boolean bol1 = false;
            if ( from.equals( sender ) ) {
                bol1 = true;
            }
            boolean bol2 = false;
            if ( target.equals( sender ) ) {
                bol2 = true;
            }
            try {
                if ( !from.equals( target ) ) {
                    from.teleport( target.getLocation( ) );
                    if ( !bol2 ) {
                        String message = utils.getString( "tp.tp_other" );
                        message = message.replace( "%player%" , player1 );
                        message = message.replace( "%target%" , player2 );
                        utils.tell( sender , utils.getString( "server_prefix" ) + message );
                    }
                    if ( !bol1 ) {
                        String message2 = utils.getString( "tp.player_to_player" );
                        message2 = message2.replace( "%target%" , player2 );
                        utils.tell( from , utils.getString( "server_prefix" ) + message2 );
                    }
                    if ( utils.getBoolean( "alerts.tp_to_them" ) ) {
                        String message3 = utils.getString( "tp.to_me" );
                        message3 = message3.replace( "%sender%" , player1 );
                        utils.tell( target , utils.getString( "server_prefix" ) + message3 );
                    }
                } else {
                    utils.tell( sender , utils.getString( "server_prefix" ) + utils.getString( "tp.to_yourself" ) );
                }
            } catch ( NullPointerException offline ) {
                utils.tell( sender , utils.getString( "server_prefix" ) + "&cThe player &a" + player1 + " or " + player2 + " &cis offline" );
            }
        } else {
            if ( !utils.isPlayer( player1 ) ) {
                String message = utils.getString( "tp.not_found" );
                message = message.replace( "%player%" , player1 );
                utils.tell( sender , utils.getString( "server_prefix" ) + message );
            }
            if ( !utils.isPlayer( player2 ) ) {
                String message = utils.getString( "tp.not_found" );
                message = message.replace( "%player%" , player2 );
                utils.tell( sender , utils.getString( "server_prefix" ) + message );
            }
        }
    }
    
    public static void tpAll( CommandSender sender , String player ){
        if ( utils.isPlayer( player ) ) {
            try {
                Player target = Bukkit.getPlayer( player );
                if ( Bukkit.getServer( ).getOnlinePlayers( ).size( ) <= 1 ) {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_online_players" ) );
                } else if ( Bukkit.getServer( ).getOnlinePlayers( ).size( ) > 1 ) {
                    for ( Player players : Bukkit.getServer( ).getOnlinePlayers( ) ) {
                        if ( !players.equals( target ) ) {
                            players.teleport( target );
                            if ( utils.getBoolean( "alerts.tp_all_msg" ) ) {
                                String message2 = plugin.getConfig( ).getString( "tp.player_to_player" );
                                message2 = message2.replace( "%target%" , player );
                                utils.tell( players , utils.getString( "server_prefix" ) + message2 );
                            }
                        }
                    }
                    String message = plugin.getConfig( ).getString( "tp.all" );
                    message = message.replace( "%count%" , String.valueOf( (Bukkit.getServer( ).getOnlinePlayers( ).size( ) - 1) ) );
                    utils.tell( target , utils.getString( "server_prefix" ) + message );
                }
            } catch ( NullPointerException offline ) {
                utils.tell( sender , utils.getString( "server_prefix" ) + "&cAn error occurred" );
                offline.printStackTrace( );
            }
        } else {
            String message = plugin.getConfig( ).getString( "tp.not_found" );
            message = message.replace( "%player%" , player );
            utils.tell( sender , utils.getString( "server_prefix" ) + message );
        }
    }
    
    public static void tpToCords( Player sender , double x , double y , double z ){
        try {
            sender.teleport( new Location( sender.getWorld( ) , x , y , z ) );
            String message = plugin.getConfig( ).getString( "tp.to_cords" );
            message = message.replace( "%x%" , String.valueOf( round( x ) ) );
            message = message.replace( "%y%" , String.valueOf( round( y ) ) );
            message = message.replace( "%z%" , String.valueOf( round( z ) ) );
            utils.tell( sender , utils.getString( "server_prefix" ) + message );
            
        } catch ( NullPointerException offline ) {
            utils.tell( sender , utils.getString( "server_prefix" ) + "&cAn error occurred" );
            offline.printStackTrace( );
        }
    }
    
    public static void tpToCordsAndPlayer( CommandSender sender , Player target , double x , double y , double z ){
        try {
            boolean bol = false;
            if ( sender instanceof Player ) {
                Player player = ( Player ) sender;
                if ( player.equals( target ) ) {
                    bol = true;
                }
                target.teleport( new Location( player.getWorld( ) , x , y , z ) );
            } else {
                target.teleport( new Location( target.getWorld( ) , x , y , z ) );
            }
            String message = plugin.getConfig( ).getString( "tp.player_to_cords" );
            message = message.replace( "%x%" , String.valueOf( round( x ) ) );
            message = message.replace( "%y%" , String.valueOf( round( y ) ) );
            message = message.replace( "%z%" , String.valueOf( round( z ) ) );
            message = message.replace( "%target%" , target.getName( ) );
            utils.tell( sender , utils.getString( "server_prefix" ) + message );
            if ( !bol && utils.getBoolean( "alerts.tp_to_them" ) ) {
                String message2 = plugin.getConfig( ).getString( "tp.to_cords_by" );
                message2 = message2.replace( "%x%" , String.valueOf( round( x ) ) );
                message2 = message2.replace( "%y%" , String.valueOf( round( y ) ) );
                message2 = message2.replace( "%z%" , String.valueOf( round( z ) ) );
                message2 = message2.replace( "%sender%" , sender.getName( ) );
                utils.tell( target , utils.getString( "server_prefix" ) + message2 );
            }
            
            
        } catch ( NullPointerException offline ) {
            utils.tell( sender , utils.getString( "server_prefix" ) + "&cAn error occurred" );
            offline.printStackTrace( );
        }
    }
    
    public static void tpAllToCords( Player sender , double x , double y , double z ){
        try {
            if ( Bukkit.getServer( ).getOnlinePlayers( ).size( ) <= 1 ) {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_online_players" ) );
            } else if ( Bukkit.getServer( ).getOnlinePlayers( ).size( ) > 1 ) {
                for ( Player players : Bukkit.getServer( ).getOnlinePlayers( ) ) {
                    players.teleport( new Location( sender.getWorld( ) , x , y , z ) );
                    if ( !players.equals( sender ) ) {
                        String message2 = plugin.getConfig( ).getString( "tp.to_cords_by" );
                        message2 = message2.replace( "%x%" , String.valueOf( round( x ) ) );
                        message2 = message2.replace( "%y%" , String.valueOf( round( y ) ) );
                        message2 = message2.replace( "%z%" , String.valueOf( round( z ) ) );
                        message2 = message2.replace( "%sender%" , sender.getName( ) );
                        utils.tell( players , utils.getString( "server_prefix" ) + message2 );
                    }
                }
                String message = plugin.getConfig( ).getString( "tp.all_to_cords" );
                message = message.replace( "%x%" , String.valueOf( round( x ) ) );
                message = message.replace( "%y%" , String.valueOf( round( y ) ) );
                message = message.replace( "%z%" , String.valueOf( round( z ) ) );
                message = message.replace( "%count%" , String.valueOf( (Bukkit.getServer( ).getOnlinePlayers( ).size( ) - 1) ) );
                utils.tell( sender , utils.getString( "server_prefix" ) + message );
            }
        } catch ( NullPointerException offline ) {
            utils.tell( sender , utils.getString( "server_prefix" ) + "&cAn error occurred" );
            offline.printStackTrace( );
        }
        
    }
}
