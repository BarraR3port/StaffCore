package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class SetVanish {
    private static final Plugin plugin = main.plugin;
    
    
    public static void setVanish( Player p , Boolean bol ){
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( bol ) {
            if ( utils.mysqlEnabled( ) ) {
                for ( Player player : Bukkit.getServer( ).getOnlinePlayers( ) ) {
                    if ( !player.hasPermission( "staffcore.vanish.see" ) && !player.hasPermission( "staffcore.vanish" ) ) {
                        if ( player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) || SQLGetter.isTrue( player , "vanish" ).equals( "true" ) || SQLGetter.isTrue( player , "staff" ).equals( "true" ) ) {
                            p.showPlayer( plugin , player );
                        } else {
                            player.hidePlayer( plugin , p );
                        }
                    } else {
                        p.showPlayer( plugin , player );
                    }
                }
                SQLGetter.set( p.getName( ) , "vanish" , "true" );
            } else {
                for ( Player player : Bukkit.getOnlinePlayers( ) ) {
                    if ( !player.hasPermission( "staffcore.vanish.see" ) && !player.hasPermission( "staffcore.vanish" ) ) {
                        if ( player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                            p.showPlayer( plugin , player );
                        } else {
                            player.hidePlayer( plugin , p );
                        }
                    } else {
                        p.showPlayer( plugin , player );
                    }
                }
            }
            PlayerData.set( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING , "vanished" );
            p.setAllowFlight( true );
            p.setFlying( true );
            p.setInvulnerable( true );
            p.setFoodLevel( 20 );
            p.setHealth( 20 );
            p.setSaturation( 5f );
            p.setCollidable( false );
            if ( p.getInventory( ).contains( SetStaffItems.vanishOff( ) ) ) {
                p.getInventory( ).remove( SetStaffItems.vanishOff( ) );
                p.getInventory( ).addItem( SetStaffItems.vanishOn( ) );
            }
            if ( PlayerData.has( new NamespacedKey( plugin , "FakeJoinOrLeave" ) , PersistentDataType.STRING ) ) {
                for ( Player players : Bukkit.getOnlinePlayers( ) ) {
                    if ( utils.getBoolean( "alerts.fake_join_leave_msg" ) ) {
                        utils.tell( players , utils.getString( "fake_join_leave_msg.leave_msg" , "lg" , null ).replace( "%player%" , p.getName( ) ) );
                    }
                }
            }
        } else {
            if ( !p.getGameMode( ).equals( GameMode.CREATIVE ) && !PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ||
                    !PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                p.setAllowFlight( false );
                p.setFlying( false );
                p.setInvulnerable( false );
            }
            if ( p.getInventory( ).contains( SetStaffItems.vanishOn( ) ) ) {
                p.getInventory( ).remove( SetStaffItems.vanishOn( ) );
                p.getInventory( ).addItem( SetStaffItems.vanishOff( ) );
            }
            p.setHealth( 20 );
            p.setSaturation( 5f );
            p.setCollidable( true );
            PlayerData.remove( new NamespacedKey( plugin , "vanished" ) );
            if ( utils.getBoolean( "alerts.fake_join_leave_msg" ) ) {
                if ( PlayerData.has( new NamespacedKey( plugin , "FakeJoinOrLeave" ) , PersistentDataType.STRING ) ) {
                    for ( Player players : Bukkit.getOnlinePlayers( ) ) {
                        utils.tell( players , utils.getString( "fake_join_leave_msg.join_msg" , "lg" , null ).replace( "%player%" , p.getName( ) ) );
                    }
                }
            }
            if ( utils.mysqlEnabled( ) ) {
                SQLGetter.set( p.getName( ) , "vanish" , "false" );
            }
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                people.showPlayer( plugin , p );
                if ( !p.hasPermission( "staffcore.vanish" ) || !p.hasPermission( "staffcore.staff" ) ) {
                    for ( String players : StaffCoreAPI.getVanishedPlayers( ) ) {
                        try {
                            p.hidePlayer( plugin , Bukkit.getPlayer( players ) );
                        } catch ( NullPointerException ignored ) { }
                    }
                }
            }
        }
    }
}

