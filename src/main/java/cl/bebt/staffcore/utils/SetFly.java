package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class SetFly {
    private static final Plugin plugin = main.plugin;
    
    public SetFly( Player p , Boolean bol ){
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( bol ) {
            p.setAllowFlight( true );
            p.setFlying( true );
        } else {
            if ( !PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || !PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                if ( p.getGameMode( ) == GameMode.ADVENTURE || p.getGameMode( ) == GameMode.SURVIVAL ) {
                    p.setAllowFlight( false );
                    p.setFlying( false );
                    if ( plugin.getConfig( ).getBoolean( "staff.fly_invincible" ) ) {
                        p.setInvulnerable( false );
                    }
                }
            }
        }
    }
}

