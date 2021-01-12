package cl.bebt.staffcore.utils;

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
                        if ( !player.hasPermission( "staffcore.vanish.see" ) && !player.hasPermission( "staffcore.vanish" ) ) {
                            if ( !player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || !PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) || SQLGetter.isTrue( player , "vanish" ).equals( "false" ) || SQLGetter.isTrue( player , "staff" ).equals( "false" ) ) {
                                player.hidePlayer( plugin , p );
                            } else {
                                p.showPlayer( plugin , player );
                            }
                        } else {
                            p.showPlayer( plugin , player );
                        }
                    }
                }
                SQLGetter.setTrue( p.getName( ) , "vanish" , "true" );
            } else {
                for ( Player player : Bukkit.getOnlinePlayers( ) ) {
                    if ( !player.hasPermission( "staffcore.vanish.see" ) && !player.hasPermission( "staffcore.vanish" ) ) {
                        if ( !player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || !PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                            player.hidePlayer( plugin , p );
                        } else {
                            p.showPlayer( plugin , player );
                            
                        }
                    } else {
                        p.showPlayer( plugin , player );
                    }
                }
            }
            PlayerData.set( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING , "vanished" );
            if ( !(p.getGameMode( ) == GameMode.CREATIVE || p.getGameMode( ) == GameMode.SPECTATOR ||
                    PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ||
                    PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING )) ) {
                p.setAllowFlight( true );
                p.setFlying( true );
                p.setInvulnerable( true );
            }
            if ( p.getInventory( ).contains( SetStaffItems.vanishOff( ) ) ) {
                p.getInventory( ).remove( SetStaffItems.vanishOff( ) );
                p.getInventory( ).addItem( SetStaffItems.vanishOn( ) );
            }
            p.setFoodLevel( 20 );
            p.setHealth( 20 );
            p.setSaturation( 5f );
        } else {
            if ( !(p.getGameMode( ) == GameMode.CREATIVE || p.getGameMode( ) == GameMode.SPECTATOR ||
                    PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ||
                    PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING )) ) {
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
            PlayerData.remove( new NamespacedKey( plugin , "vanished" ) );
            if ( utils.mysqlEnabled( ) ) {
                SQLGetter.setTrue( p.getName( ) , "vanish" , "false" );
            }
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                people.showPlayer( plugin , p );
            }
        }
    }
}

