package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class SetFly {
    private static final Plugin plugin = main.plugin;
    private static final SQLGetter data = main.plugin.data;
    
    public static void SetFly( Player p , Boolean bol ){
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( bol ) {
            if ( !(p.getGameMode( ) == GameMode.CREATIVE || p.getGameMode( ) == GameMode.SPECTATOR || PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING )) ) {
                p.setAllowFlight( true );
                p.setFlying( true );
            }
            if ( utils.mysqlEnabled( ) ) {
                SQLGetter.setTrue( p.getName( ) , "flying" , "true" );
            }
        } else {
            if ( !(p.getGameMode( ) == GameMode.CREATIVE || p.getGameMode( ) == GameMode.SPECTATOR || PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING )) ) {
                p.setAllowFlight( false );
                p.setFlying( false );
                if ( plugin.getConfig( ).getBoolean( "staff.fly_invincible" ) ) {
                    p.setInvulnerable( false );
                }
            }
            if ( utils.mysqlEnabled( ) ) {
                SQLGetter.setTrue( p.getName( ) , "flying" , "false" );
            }
        }
    }
}

