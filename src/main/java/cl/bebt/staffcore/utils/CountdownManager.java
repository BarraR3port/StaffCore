package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class CountdownManager{
    private static final HashMap < Player, Double > countdown = new HashMap <>( );

    public static void setMuteCountdown( Player p , long seconds ){
        PersistentDataContainer persistent = p.getPersistentDataContainer( );
        if ( checkMuteCountdown( p ) ) {
            double delay = System.currentTimeMillis( ) + (seconds * 1000);
            persistent.set( new NamespacedKey( main.plugin , "muted_time" ) , PersistentDataType.DOUBLE , delay );
        } else {
            persistent.remove( new NamespacedKey( main.plugin , "muted_time" ) );
            double delay = System.currentTimeMillis( ) + (seconds * 1000);
            persistent.set( new NamespacedKey( main.plugin , "muted_time" ) , PersistentDataType.DOUBLE , delay );
        }
    }

    public static void removeMuteCountdown( Player p ){
        PersistentDataContainer persistent = p.getPersistentDataContainer( );
        persistent.remove( new NamespacedKey( main.plugin , "muted_time" ) );

    }

    public static long getMuteCountDown( Player p ){
        PersistentDataContainer persistent = p.getPersistentDataContainer( );
        double delay = persistent.get( new NamespacedKey( main.plugin , "muted_time" ) , PersistentDataType.DOUBLE );
        return Math.toIntExact( Math.round( (delay - System.currentTimeMillis( )) / 1000 ) );
    }

    public static boolean checkMuteCountdown( Player p ){
        PersistentDataContainer persistent = p.getPersistentDataContainer( );
        if ( !persistent.has( new NamespacedKey( main.plugin , "muted_time" ) , PersistentDataType.DOUBLE ) ||
                persistent.get( new NamespacedKey( main.plugin , "muted_time" ) , PersistentDataType.DOUBLE ) <= System.currentTimeMillis( ) ) {
            return true;
        }
        return !persistent.has( new NamespacedKey( main.plugin , "muted_time" ) , PersistentDataType.DOUBLE ) ||
                persistent.get( new NamespacedKey( main.plugin , "muted_time" ) , PersistentDataType.DOUBLE ) <= System.currentTimeMillis( );
    }

    public static void setCountDown( Player p , Double seconds ){
        if ( checkMuteCountdown( p ) ) {
            double delay = System.currentTimeMillis( ) + (seconds * 1000);
            countdown.put( p , delay );
        } else {
            countdown.remove( p );
            double delay = System.currentTimeMillis( ) + (seconds * 1000);
            countdown.put( p , delay );
        }
    }

    public static boolean checkCountdown( Player p ){
        return !countdown.containsKey( p ) || countdown.get( p ) <= System.currentTimeMillis( );
    }


}
