package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.BanPlayer;
import cl.bebt.staffcore.utils.utils;
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
        if ( !utils.isOlderVersion( ) ) {
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
                            utils.tell( sender , utils.getString( "error" , "lg" , "staff" ) );
                            error.printStackTrace( );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "unabn <player>" ) );
                }
            } else if ( args.length == 1 ) {
                ArrayList < Integer > ids = BanIds( args[0] );
                try {
                    for ( int i : ids ) {
                        BanPlayer.unBan( sender , i );
                    }
                } catch ( NullPointerException error ) {
                    utils.tell( sender , utils.getString( "error" , "lg" , "staff" ) );
                    error.printStackTrace( );
                }
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "unban <player>" ) );
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
    
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        List < String > version = new ArrayList <>( );
        this.bans.clear( );
        if ( args.length == 1 )
            if ( StaffCoreAPI.getBannedPlayers( ).size( ) == 0 ) {
                utils.tell( sender , utils.getString( "bans.no_banned_players" , "lg" , "staff" ) );
            } else {
                version.addAll( StaffCoreAPI.getBannedPlayers( ) );
            }
        return version;
    }
    
    private int count( ){
        if ( utils.mysqlEnabled( ) ) {
            return SQLGetter.getCurrents( "bans" ) + this.plugin.data.getBanId( );
        } else {
            return this.plugin.bans.getConfig( ).getInt( "current" ) + this.plugin.bans.getConfig( ).getInt( "count" );
        }
    }
    
    private ArrayList < Integer > BanIds( String banned ){
        ArrayList < Integer > BanIDs = new ArrayList <>( );
        if ( utils.mysqlEnabled( ) )
            return SQLGetter.getBanIds( banned );
        for ( int i = 0; i < count( ); i++ ) {
            try {
                if ( this.plugin.bans.getConfig( ).getString( "bans." + i + ".name" ).equalsIgnoreCase( banned ) )
                    BanIDs.add( i );
            } catch ( NullPointerException ignored ) {
            }
        }
        return BanIDs;
    }
}
