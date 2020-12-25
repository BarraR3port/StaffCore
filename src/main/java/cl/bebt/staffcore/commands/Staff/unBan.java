package cl.bebt.staffcore.commands.Staff;

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
import java.util.Objects;

public class unBan implements TabExecutor{

    private final main plugin;

    private final HashMap < Integer, Integer > bans = new HashMap <>( );

    public unBan( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "unban" ).setExecutor( this );
    }

    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( sender instanceof Player ) {
            if ( args.length == 1 ) {
                Player p = ( Player ) sender;
                if ( p.hasPermission( "staffcore.unban" ) ) {
                    ArrayList < Integer > ids = BanIds( args[0] );
                    try {
                        for ( int i : ids ) {
                            BanPlayer.unBan( p , i );
                        }
                    } catch ( NullPointerException ignored ) {
                        utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&cThe player " + args[0] + " didn't got un baned" );
                    }
                } else {
                    utils.tell( sender , utils.getString( "staff.staff_prefix" ) + utils.getString( "no_permissions" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&7Use /unban <&aplayer&7>" );
            }

        } else {
            if ( args.length == 1 ) {
                ArrayList < Integer > ids = BanIds( args[0] );
                try {
                    for ( int i : ids ) {
                        BanPlayer.unBan( sender , i );
                    }
                } catch ( NullPointerException ignored ) {
                    utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&cThe player " + args[0] + " didn't got un baned" );
                }
            } else {
                utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&7Use /unban <&aplayer&7>" );
            }
        }
        return true;
    }

    @Override
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        List < String > version = new ArrayList <>( );
        bans.clear( );
        if ( args.length == 1 ) {
            if ( amountOfBanned( ) == 0 ) {
                utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&cThere are no players baned" );
            } else {
                for ( int i = 1; i <= amountOfBanned( ); i++ ) {
                    if ( utils.mysqlEnabled( ) ) {
                        version.add( SQLGetter.getBaned( bans.get( i ) , "name" ) );
                    } else {
                        version.add( plugin.baned.getConfig( ).getString( "bans." + bans.get( i ) + ".name" ) );
                    }
                }
            }
        }
        return version;
    }


    private int count( ){
        if ( utils.mysqlEnabled( ) ) {
            return SQLGetter.getCurrents( "bans" ) + plugin.data.getBanId( );
        } else {
            return plugin.baned.getConfig( ).getInt( "current" ) + plugin.baned.getConfig( ).getInt( "count" );
        }
    }


    private int amountOfBanned( ){
        int num = 0;
        for ( int id = 0; id <= count( ); ) {
            id++;
            try {
                if ( utils.mysqlEnabled( ) ) {
                    if ( SQLGetter.getBanStatus( id ).equals( "open" ) ) {
                        num++;
                        bans.put( num , id );
                    }
                } else {
                    if ( Objects.equals( plugin.baned.getConfig( ).getString( "bans." + id + ".status" ) , "open" ) ) {
                        num++;
                        bans.put( num , id );
                    }
                }
            } catch ( NullPointerException ignored ) {
            }
        }
        return num;
    }

    private ArrayList < Integer > BanIds( String baned ){
        ArrayList < Integer > BanIDs = new ArrayList <>( );
        if ( utils.mysqlEnabled( ) ) {
            return SQLGetter.getBanIds( baned );
        } else {
            for ( int i = 0; i < count( ); i++ ) {
                try {
                    if ( plugin.baned.getConfig( ).getString( "bans." + i + ".name" ).equalsIgnoreCase( baned ) ) {
                        BanIDs.add( i );
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
        }


        return BanIDs;
    }
}
