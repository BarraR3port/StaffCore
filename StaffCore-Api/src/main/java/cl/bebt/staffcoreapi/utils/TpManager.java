/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static java.lang.Math.round;

public class TpManager {
    
    public static void tpToPlayer( Player sender , String player ){
        if ( Utils.isPlayer( player ) ) {
            Player target = Bukkit.getPlayer( player );
            try {
                if ( !sender.equals( target ) ) {
                    sender.teleport( target.getLocation( ) );
                    Utils.tell( sender , Utils.getString( "tp.player_to_player" , "lg" , "sv" ).replace( "%target%" , target.getName( ) ) );
                    if ( Utils.getBoolean( "alerts.tp_to_them" ) ) {
                        Utils.tell( target , Utils.getString( "tp.tp_to_them" , "lg" , "sv" ).replace( "%sender%" , player ) );
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "tp.to_yourself" , "lg" , "sv" ) );
                }
            } catch ( NullPointerException offline ) {
                Utils.tell( sender , Utils.getString( "offline" , "lg" , "sv" ).replace( "%player%" , player ) );
            }
        } else {
            Utils.tell( sender , Utils.getString( "tp.not_found" , "lg" , "sv" ).replace( "%target%" , player ) );
        }
    }
    
    public static void tpPlayerToPlayer( CommandSender sender , String player1 , String player2 ){
        if ( Utils.isPlayer( player1 ) && Utils.isPlayer( player2 ) ) {
            Player from = Bukkit.getPlayer( player1 );
            Player target = Bukkit.getPlayer( player2 );
            boolean bol1 = from.equals( sender );
            boolean bol2 = target.equals( sender );
            try {
                if ( !from.equals( target ) ) {
                    from.teleport( target.getLocation( ) );
                    if ( !bol2 ) {
                        Utils.tell( sender , Utils.getString( "tp.tp_other" , "lg" , "sv" ).replace( "%player%" , player1 ).replace( "%target%" , player2 ) );
                    }
                    if ( !bol1 ) {
                        Utils.tell( from , Utils.getString( "tp.player_to_player" , "lg" , "sv" ).replace( "%target%" , player2 ) );
                    }
                    if ( Utils.getBoolean( "alerts.tp_to_them" ) ) {
                        Utils.tell( target , Utils.getString( "tp.tp_to_them" , "lg" , "sv" ).replace( "%sender%" , player1 ) );
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "tp.to_yourself" , "lg" , "sv" ) );
                }
            } catch ( NullPointerException offline ) {
                Utils.tell( sender , Utils.getString( "tp.offline_players" , "lg" , "sv" ).replace( "%target1%" , player1 ).replace( "%target2%" , player2 ) );
            }
        } else {
            if ( !Utils.isPlayer( player1 ) ) {
                Utils.tell( sender , Utils.getString( "tp.not_found" , "lg" , "sv" ).replace( "%player%" , player1 ) );
            }
            if ( !Utils.isPlayer( player2 ) ) {
                Utils.tell( sender , Utils.getString( "tp.not_found" , "lg" , "sv" ).replace( "%player%" , player2 ) );
            }
        }
    }
    
    public static void tpAll( CommandSender sender , String player ){
        if ( Utils.isPlayer( player ) ) {
            try {
                Player target = Bukkit.getPlayer( player );
                if ( Bukkit.getServer( ).getOnlinePlayers( ).size( ) <= 1 ) {
                    Utils.tell( sender , Utils.getString( "no_online_players" , "lg" , "sv" ) );
                } else if ( Bukkit.getServer( ).getOnlinePlayers( ).size( ) > 1 ) {
                    for ( Player players : Bukkit.getServer( ).getOnlinePlayers( ) ) {
                        if ( !players.equals( target ) ) {
                            players.teleport( target );
                            if ( Utils.getBoolean( "alerts.tp_all_msg" ) ) {
                                Utils.tell( players , Utils.getString( "tp.player_to_player" , "lg" , "sv" ).replace( "%target%" , player ) );
                            }
                        }
                    }
                    Utils.tell( target , Utils.getString( "tp.all" , "lg" , "sv" ).replace( "%count%" , String.valueOf( (Bukkit.getServer( ).getOnlinePlayers( ).size( ) - 1) ) ) );
                }
            } catch ( NullPointerException offline ) {
                Utils.tell( sender , Utils.getString( "tp.offline" , "lg" , "sv" ).replace( "%target1%" , player ) );
                offline.printStackTrace( );
            }
        } else {
            Utils.tell( sender , Utils.getString( "tp.not_found" , "lg" , "sv" ).replace( "%player%" , player ) );
        }
    }
    
    public static void tpToCords( Player sender , double x , double y , double z ){
        try {
            sender.teleport( new Location( sender.getWorld( ) , x , y , z ) );
            Utils.tell( sender , Utils.getString( "tp.to_cords" , "lg" , "sv" )
                    .replace( "%x%" , String.valueOf( round( x ) ) )
                    .replace( "%y%" , String.valueOf( round( y ) ) )
                    .replace( "%z%" , String.valueOf( round( z ) ) ) );
            
        } catch ( NullPointerException error ) {
            for ( String s : Utils.getStringList( "tp.wrong" , "alerts" ) ) {
                Utils.tell( sender , s );
            }
            error.printStackTrace( );
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
            Utils.tell( sender , Utils.getString( "tp.to_cords" , "lg" , "sv" )
                    .replace( "%x%" , String.valueOf( round( x ) ) )
                    .replace( "%y%" , String.valueOf( round( y ) ) )
                    .replace( "%z%" , String.valueOf( round( z ) ) )
                    .replace( "%target%" , target.getName( ) ) );
            if ( !bol && Utils.getBoolean( "alerts.tp_to_them" ) ) {
                Utils.tell( target , Utils.getString( "tp.to_cords" , "lg" , "sv" )
                        .replace( "%x%" , String.valueOf( round( x ) ) )
                        .replace( "%y%" , String.valueOf( round( y ) ) )
                        .replace( "%z%" , String.valueOf( round( z ) ) )
                        .replace( "%sender%" , sender.getName( ) ) );
            }
            
            
        } catch ( NullPointerException offline ) {
            Utils.tell( sender , Utils.getString( "offline" , "lg" , "sv" ).replace( "%player%" , target.getName( ) ) );
            offline.printStackTrace( );
        }
    }
    
    public static void tpAllToCords( Player sender , double x , double y , double z ){
        try {
            if ( Bukkit.getServer( ).getOnlinePlayers( ).size( ) <= 1 ) {
                Utils.tell( sender , Utils.getString( "no_online_players" , "lg" , "sv" ) );
            } else if ( Bukkit.getServer( ).getOnlinePlayers( ).size( ) > 1 ) {
                for ( Player players : Bukkit.getServer( ).getOnlinePlayers( ) ) {
                    players.teleport( new Location( sender.getWorld( ) , x , y , z ) );
                    if ( !players.equals( sender ) ) {
                        Utils.tell( players , Utils.getString( "tp.to_cords_by" , "lg" , "sv" )
                                .replace( "%x%" , String.valueOf( round( x ) ) )
                                .replace( "%y%" , String.valueOf( round( y ) ) )
                                .replace( "%z%" , String.valueOf( round( z ) ) )
                                .replace( "%sender%" , sender.getName( ) ) );
                    }
                }
                Utils.tell( sender , Utils.getString( "tp.all_to_cords" , "lg" , "sv" )
                        .replace( "%x%" , String.valueOf( round( x ) ) )
                        .replace( "%y%" , String.valueOf( round( y ) ) )
                        .replace( "%z%" , String.valueOf( round( z ) ) )
                        .replace( "%count%" , String.valueOf( (Bukkit.getServer( ).getOnlinePlayers( ).size( ) - 1) ) ) );
            }
        } catch ( NullPointerException offline ) {
            for ( String s : Utils.getStringList( "tp.wrong" , "alerts" ) ) {
                Utils.tell( sender , s );
            }
            offline.printStackTrace( );
        }
        
    }
}
