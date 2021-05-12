package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ToggleChat {
    
    public static void Mute( Boolean bol ){
        if ( bol ) {
            main.plugin.chatMuted = true;
        }
        if ( !bol ) {
            main.plugin.chatMuted = false;
        }
    }
    
    public static void unMute( CommandSender p , Player muted ){
        PersistentDataContainer data = muted.getPersistentDataContainer( );
        data.remove( new NamespacedKey( main.plugin , "muted" ) );
        CountdownManager.removeMuteCountdown( muted );
        if ( p instanceof Player ) {
            utils.tell( muted , utils.getString( "toggle_chat.un_mute_by_player" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
        } else {
            utils.tell( muted , utils.getString( "toggle_chat.un_mute_by_console" , "lg" , "staff" ) );
        }
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.mute_player") ) {
                utils.PlaySound( people , "staff_mute_alerts" );
                for ( String key : utils.getStringList( "chat.toggle" , "alerts" ) ) {
                    if ( p instanceof Player ) {
                        key = key.replace( "%staff%" , p.getName( ) );
                    } else {
                        key = key.replace( "%staff%" , "CONSOLE" );
                    }
                    key = key.replace( "%muted%" , muted.getName( ) );
                    key = key.replace( "%status%" , "&aUn Muted" );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    public static void MutePlayer( CommandSender p , Player muted ){
        PersistentDataContainer data = muted.getPersistentDataContainer( );
        if ( !data.has( new NamespacedKey( main.plugin , "muted" ) , PersistentDataType.STRING ) ) {
            data.set( new NamespacedKey( main.plugin , "muted" ) , PersistentDataType.STRING , "muted" );
            if ( p instanceof Player ) {
                Player jugador = ( Player ) p;
                utils.tell( muted , utils.getString( "toggle_chat.mute_by_player" , "lg" , "sv" ).replace( "%player%" , jugador.getName( ) ) );
            } else {
                utils.tell( muted , utils.getString( "toggle_chat.mute_by_player" , "lg" , "sv" ).replace( "%player%" , "CONSOLE" ) );
            }
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.mute_player") ) {
                    utils.PlaySound( people , "staff_mute_alerts" );
                    for ( String key : utils.getStringList( "chat.toggle" , "alerts" ) ) {
                        if ( p instanceof Player ) {
                            key = key.replace( "%staff%" , p.getName( ) );
                        } else {
                            key = key.replace( "%staff%" , "CONSOLE" );
                        }
                        key = key.replace( "%muted%" , muted.getName( ) );
                        key = key.replace( "%status%" , "&cMuted" );
                        utils.tell( people , key );
                    }
                }
            }
        } else {
            unMute( p , muted );
        }
    }
    
    
    public static void MuteCooldown( CommandSender sender , Player p , String time , long amount ){
        if ( time.equals( "s" ) ) {
            CountdownManager.setMuteCountdown( p , amount );
            sendMessage( ( Player ) sender , p , amount , "s" );
        } else if ( time.equals( "m" ) ) {
            CountdownManager.setMuteCountdown( p , amount * 60 );
            sendMessage( ( Player ) sender , p , amount , "m" );
        } else if ( time.equals( "h" ) ) {
            CountdownManager.setMuteCountdown( p , amount * 3600 );
            sendMessage( ( Player ) sender , p , amount , "h" );
        } else if ( time.equals( "d" ) ) {
            CountdownManager.setMuteCountdown( p , amount * 86400 );
            sendMessage( ( Player ) sender , p , amount , "d" );
        } else {
            utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "mute " + p.getName( ) + " &d10<s/m/h/d>" ) );
        }
    }
    
    private static void sendMessage( Player p , Player muted , long amount , String quantity ){
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.mute_player") || people.equals( muted ) ) {
                utils.PlaySound( people , "staff_mute_alerts" );
                for ( String key : utils.getStringList( "chat.temporal_mute" , "alerts" ) ) {
                    key = key.replace( "%staff%" , p.getName( ) );
                    key = key.replace( "%muted%" , muted.getName( ) );
                    key = key.replace( "%amount%" , String.valueOf( amount ) );
                    key = key.replace( "%quantity%" , quantity );
                    utils.tell( people , key );
                }
            }
        }
    }
}
