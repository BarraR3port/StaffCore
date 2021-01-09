package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class wipePlayer {
    
    private static main plugin;
    
    public wipePlayer( main plugin ){
        this.plugin = plugin;
    }
    public wipePlayer( CommandSender sender , String p ){
        int bans = 0;
        int reports = 0;
        int warns = 0;
        if ( utils.isRegistered( p ) ) {
            if ( utils.mysqlEnabled( ) ) {
                if ( plugin.getConfig( ).getBoolean( "wipe.bans" ) ) {
                    List < Integer > ids = SQLGetter.getPlayersIds( p , "bans" , "BanId" );
                    for ( int i : ids ) {
                        SQLGetter.deleteBans( i );
                    }
                    bans = ids.size( );
                }
                if ( plugin.getConfig( ).getBoolean( "wipe.reports" ) ) {
                    List < Integer > ids = SQLGetter.getPlayersIds( p , "reports" , "ReportId" );
                    for ( int i : ids ) {
                        SQLGetter.deleteReport( i );
                    }
                    reports = ids.size( );
                }
                try {
                    SetStaffItems.Off( Bukkit.getPlayer( p ) );
                } catch ( NullPointerException ignored ) {
                }
                SQLGetter.wipe( p );
                PersistentDataContainer( p, plugin );
                SQLGetter.deleteAlts( p );
            } else {
                if ( plugin.getConfig( ).getBoolean( "wipe.bans" ) ) {
                    HashMap < Integer, Integer > ids = Ids( "bans" , p );
                    for ( int i = 1; i <= ids.size( ); i++ ) {
                        if ( ids.get( i ) != null ) {
                            plugin.bans.reloadConfig( );
                            plugin.bans.reloadConfig( );
                            plugin.bans.getConfig( ).set( "bans." + ids.get( i ) , null );
                            plugin.bans.getConfig( ).set( "current" , count( "bans" ) );
                            plugin.bans.saveConfig( );
                        }
                    }
                    bans = ids.size( );
                }
                if ( plugin.getConfig( ).getBoolean( "wipe.reports" ) ) {
                    HashMap < Integer, Integer > ids = Ids( "report" , p );
                    for ( int i = 1; i <= ids.size( ); i++ ) {
                        if ( ids.get( i ) != null ) {
                            plugin.reports.reloadConfig( );
                            plugin.reports.getConfig( ).set( "reports." + ids.get( i ) , null );
                            plugin.reports.getConfig( ).set( "current" , count( "report" ) );
                            plugin.reports.saveConfig( );
                        }
                    }
                    reports = ids.size( );
                }
                if ( plugin.getConfig( ).getBoolean( "wipe.warns" ) ) {
                    HashMap < Integer, Integer > ids = Ids( "warns" , p );
                    for ( int i = 1; i <= ids.size( ); i++ ) {
                        if ( ids.get( i ) != null ) {
                            plugin.warns.reloadConfig( );
                            plugin.warns.getConfig( ).set( "warns." + ids.get( i ) , null );
                            plugin.warns.getConfig( ).set( "current" , count( "warns" ) );
                            plugin.warns.saveConfig( );
                        }
                    }
                    warns = ids.size( );
                }
                try {
                    SetStaffItems.Off( Bukkit.getPlayer( p ) );
                } catch ( NullPointerException ignored ) {
                }
                PersistentDataContainer( p, plugin );
                plugin.alts.reloadConfig( );
                plugin.alts.getConfig( ).set( "alts." + p , null );
                plugin.alts.saveConfig( );
            }
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.wipe_players" ) ) {
                    for ( String key : main.plugin.getConfig( ).getStringList( "wipe.wipe_msg" ) ) {
                        key = key.replace( "%wiper%" , sender.getName( ) );
                        key = key.replace( "%wiped%" , p );
                        key = key.replace( "%bans%" , String.valueOf( bans ) );
                        key = key.replace( "%warns%" , String.valueOf( warns ) );
                        key = key.replace( "%reports%" , String.valueOf( reports ) );
                        utils.tell( people , key );
                    }
                }
            }
            SendMsg.sendWipeAlert( sender.getName( ) , p , bans , reports , warns , plugin.getConfig( ).getString( "bungeecord.server" ) );
        }
    }
    
    public static void WipeOnBan( main plugin , String p ){
        if ( utils.isRegistered( p ) ) {
            if ( utils.mysqlEnabled( ) ) {
                if ( plugin.getConfig( ).getBoolean( "wipe.reports" ) ) {
                    List < Integer > ids = SQLGetter.getPlayersIds( p , "reports" , "ReportId" );
                    for ( int i : ids ) {
                        SQLGetter.deleteReport( i );
                    }
                }
                SQLGetter.wipe( p );
                SQLGetter.deleteAlts( p );
                SQLGetter.deleteWarns( p );
            } else {
                if ( plugin.getConfig( ).getBoolean( "wipe.reports" ) ) {
                    HashMap < Integer, Integer > ids = Ids( "report" , p );
                    for ( int i = 1; i <= ids.size( ); i++ ) {
                        if ( ids.get( i ) != null ) {
                            plugin.reports.reloadConfig( );
                            plugin.reports.getConfig( ).set( "reports." + ids.get( i ) , null );
                            plugin.reports.getConfig( ).set( "current" , count( "report" ) );
                            plugin.reports.saveConfig( );
                        }
                    }
                }
                HashMap < Integer, Integer > ids = Ids( "warns" , p );
                for ( int i = 1; i <= ids.size( ); i++ ) {
                    if ( ids.get( i ) != null ) {
                        plugin.warns.reloadConfig( );
                        plugin.warns.getConfig( ).set( "warns." + ids.get( i ) , null );
                        plugin.warns.getConfig( ).set( "current" , count( "warns" ) );
                        plugin.warns.saveConfig( );
                    }
                }
                plugin.alts.reloadConfig( );
                plugin.alts.getConfig( ).set( "alts." + p , null );
                plugin.alts.saveConfig( );
            }
            try {
                SetStaffItems.Off( Bukkit.getPlayer( p ) );
            } catch ( NullPointerException ignored ) {
            }
            PersistentDataContainer( p, plugin );
        }
    }
    
    private static void PersistentDataContainer( String p, main plugin ){
        try {
            if ( Bukkit.getPlayer( p ) instanceof Player ) {
                Player player = Bukkit.getPlayer( p );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "vanished" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "flying" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "banmsg" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "reportmsg" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "frozen" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "staff" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "staffchat" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "muted" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "ban-ip" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "seconds" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "minutes" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "hours" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "days" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "amount" ) );
                player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "suicide" ) );
            }
        } catch ( NullPointerException ignored ) {
        }
    }
    
    private static HashMap < Integer, Integer > Ids( String type , String p ){
        HashMap < Integer, Integer > ids = new HashMap <>( );
        int num = 0;
        if ( type.equalsIgnoreCase( "report" ) ) {
            try {
                plugin.reports.reloadConfig( );
                ConfigurationSection inventorySection = plugin.reports.getConfig( ).getConfigurationSection( "reports" );
                for ( String key : inventorySection.getKeys( false ) ) {
                    int id = Integer.parseInt( key );
                    String name = plugin.reports.getConfig( ).getString( "reports." + id + ".name" );
                    if ( p.equalsIgnoreCase( name ) ) {
                        num++;
                        ids.put( num , id );
                    }
                }
            } catch ( NullPointerException ignored ) {
            }
        } else if ( type.equalsIgnoreCase( "bans" ) ) {
            try {
                plugin.bans.reloadConfig( );
                ConfigurationSection inventorySection = plugin.bans.getConfig( ).getConfigurationSection( "bans" );
                for ( String key : inventorySection.getKeys( false ) ) {
                    int id = Integer.parseInt( key );
                    String name = plugin.bans.getConfig( ).getString( "bans." + id + ".name" );
                    if ( p.equalsIgnoreCase( name ) ) {
                        num++;
                        ids.put( num , id );
                    }
                }
            } catch ( NullPointerException ignored ) {
            }
        } else if ( type.equalsIgnoreCase( "warns" ) ) {
            try {
                plugin.warns.reloadConfig( );
                ConfigurationSection inventorySection = plugin.warns.getConfig( ).getConfigurationSection( "warns" );
                for ( String key : inventorySection.getKeys( false ) ) {
                    Bukkit.broadcastMessage( key );
                    int id = Integer.parseInt( key );
                    String name = plugin.warns.getConfig( ).getString( "warns." + id + ".name" );
                    if ( p.equalsIgnoreCase( name ) ) {
                        num++;
                        ids.put( num , id );
                    }
                }
            } catch ( NullPointerException ignored ) {
            }
        }
        return ids;
    }
    
    public static int count( String type ){
        if ( utils.mysqlEnabled( ) ) {
            if ( type.equalsIgnoreCase( "report" ) ) {
                return SQLGetter.getCurrents( "reports" ) + plugin.data.getBanId( );
            } else {
                return SQLGetter.getCurrents( "bans" ) + plugin.data.getBanId( );
            }
        } else {
            ConfigurationSection inventorySection;
            try {
                if ( type.equalsIgnoreCase( "report" ) ) {
                    inventorySection = plugin.reports.getConfig( ).getConfigurationSection( "reports" );
                } else if ( type.equalsIgnoreCase( "bans" ) ) {
                    inventorySection = plugin.bans.getConfig( ).getConfigurationSection( "bans" );
                } else {
                    inventorySection = plugin.warns.getConfig( ).getConfigurationSection( "warns" );
                }
                return inventorySection.getKeys( true ).size( );
            } catch ( NullPointerException ignored ) {
                return 0;
            }
            
        }
    }
}
