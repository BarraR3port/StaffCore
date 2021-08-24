/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.BanPlayer;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.SQL.Queries.BansQuery;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class unBan implements TabExecutor {
    private final main plugin;
    
    private final HashMap < Integer, Integer > bans = new HashMap <>( );
    
    public unBan( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "unban" ).setExecutor( this );
    }
    
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !Utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                if ( args.length == 1 ) {
                    Player p = ( Player ) sender;
                    if ( p.hasPermission( "staffcore.unban" ) ) {
                        ArrayList < Integer > ids = BanIds( args[0] );
                        try {
                            for ( int i : ids ) {
                                BanPlayer.unBan( p , i );
                            }
                        } catch ( NullPointerException error ) {
                            Utils.tell( sender , Utils.getString( "error" , "lg" , "staff" ) );
                            error.printStackTrace( );
                        }
                    } else {
                        Utils.tell( sender , Utils.getString( "no_permission" , "lg" , "staff" ) );
                    }
                } else {
                    Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "unabn <player>" ) );
                }
            } else if ( args.length == 1 ) {
                ArrayList < Integer > ids = BanIds( args[0] );
                try {
                    for ( int i : ids ) {
                        BanPlayer.unBan( sender , i );
                    }
                } catch ( NullPointerException error ) {
                    Utils.tell( sender , Utils.getString( "error" , "lg" , "staff" ) );
                    error.printStackTrace( );
                }
            } else {
                Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "unban <player>" ) );
            }
        } else {
            Utils.tell( sender , Utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
    
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        List < String > version = new ArrayList <>( );
        this.bans.clear( );
        if ( args.length == 1 )
            if ( Utils.getBannedPlayers( ).size( ) == 0 ) {
                Utils.tell( sender , Utils.getString( "bans.no_banned_players" , "lg" , "staff" ) );
            } else {
                version.addAll( Utils.getBannedPlayers( ) );
            }
        return version;
    }
    
    
    private ArrayList < Integer > BanIds( String banned ){
        ArrayList < Integer > BanIDs = new ArrayList <>( );
        if ( Utils.mysqlEnabled( ) )
            return BansQuery.getBanIds( banned );
        for ( int i = 0; i < BanPlayer.currentBans( ); i++ ) {
            try {
                if ( Api.bans.getConfig( ).getString( "bans." + i + ".name" ).equalsIgnoreCase( banned ) )
                    BanIDs.add( i );
            } catch ( NullPointerException ignored ) {
            }
        }
        return BanIDs;
    }
}
