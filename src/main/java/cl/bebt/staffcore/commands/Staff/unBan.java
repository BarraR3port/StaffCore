package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.BanPlayer;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

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
                            for ( Iterator < Integer > iterator = ids.iterator( ); iterator.hasNext( ); ) {
                                int i = iterator.next( ).intValue( );
                                BanPlayer.unBan( p , i );
                            }
                        } catch ( NullPointerException ignored ) {
                            utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&cThe player " + args[0] + " didn't got un banned" );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "staff.staff_prefix" ) + utils.getString( "no_permissions" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&7Use /unban <&aplayer&7>" );
                }
            } else if ( args.length == 1 ) {
                ArrayList < Integer > ids = BanIds( args[0] );
                try {
                    for ( Iterator < Integer > iterator = ids.iterator( ); iterator.hasNext( ); ) {
                        int i = iterator.next( ).intValue( );
                        BanPlayer.unBan( sender , i );
                    }
                } catch ( NullPointerException ignored ) {
                    utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&cThe player " + args[0] + " didn't got un banned" );
                }
            } else {
                utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&7Use /unban <&aplayer&7>" );
            }
        } else {
            utils.tell(sender,plugin.getConfig( ).getString( "server_prefix" )+"&cThis command can't be executed in older versions");
        }
        return true;
    }
    
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        List < String > version = new ArrayList <>( );
        this.bans.clear( );
        if ( args.length == 1 )
            if ( amountOfBanned( ) == 0 ) {
                utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&cThere are no players banned" );
            } else {
                for ( int i = 1; i <= amountOfBanned( ); i++ ) {
                    if ( utils.mysqlEnabled( ) ) {
                        version.add( SQLGetter.getBanned( this.bans.get( Integer.valueOf( i ) ).intValue( ) , "name" ) );
                    } else {
                        version.add( this.plugin.bans.getConfig( ).getString( "bans." + this.bans.get( Integer.valueOf( i ) ) + ".name" ) );
                    }
                }
            }
        return version;
    }
    
    private int count( ){
        if ( utils.mysqlEnabled( ) )
            return SQLGetter.getCurrents( "bans" ) + this.plugin.data.getBanId( );
        return this.plugin.bans.getConfig( ).getInt( "current" ) + this.plugin.bans.getConfig( ).getInt( "count" );
    }
    
    private int amountOfBanned( ){
        int num = 0;
        for ( int id = 0; id <= count( ); ) {
            id++;
            try {
                if ( utils.mysqlEnabled( ) ) {
                    if ( SQLGetter.getBanStatus( id ).equals( "open" ) ) {
                        num++;
                        this.bans.put( Integer.valueOf( num ) , Integer.valueOf( id ) );
                    }
                    continue;
                }
                if ( Objects.equals( this.plugin.bans.getConfig( ).getString( "bans." + id + ".status" ) , "open" ) ) {
                    num++;
                    this.bans.put( Integer.valueOf( num ) , Integer.valueOf( id ) );
                }
            } catch ( NullPointerException nullPointerException ) {
            }
        }
        return num;
    }
    
    private ArrayList < Integer > BanIds( String banned ){
        ArrayList < Integer > BanIDs = new ArrayList <>( );
        if ( utils.mysqlEnabled( ) )
            return SQLGetter.getBanIds( banned );
        for ( int i = 0; i < count( ); i++ ) {
            try {
                if ( this.plugin.bans.getConfig( ).getString( "bans." + i + ".name" ).equalsIgnoreCase( banned ) )
                    BanIDs.add( Integer.valueOf( i ) );
            } catch ( NullPointerException nullPointerException ) {
            }
        }
        return BanIDs;
    }
}
