package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
            utils.tell( muted , main.plugin.getConfig( ).getString( "server_prefix" ) + "&7The player &c" + p.getName( ) + " &aUn Muted &7you" );
        } else {
            utils.tell( muted , main.plugin.getConfig( ).getString( "server_prefix" ) + "&7The &cCONSOLE &aUn Muted &7you" );
        }
        ComponentBuilder cb = new ComponentBuilder( utils.chat( "&7Click to teleport &a✓" ) );
        TextComponent v = new TextComponent( utils.chat( "&a    &3The player &c" + muted.getName( ) + "&3 was &a Un Muted." ) );
        v.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
        v.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , "/tp " + muted.getName( ) ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.mute_player" ) ) {
                utils.PlaySound( people , "staff_mute_alerts" );
                people.sendMessage( " " );
                utils.tell( people , "&b                 &6⚠ &5Mute Alerts &6⚠                                 " );
                people.sendMessage( " " );
                people.spigot( ).sendMessage( v );
                people.sendMessage( " " );
                if ( p instanceof Player ) {
                    utils.tell( people , "&b                 &a► &aUn Muted by: &c" + p.getName( ) );
                } else {
                    
                    utils.tell( people , "&b                 &a► &aUn Muted by: The &cCONSOLE" );
                }
                people.sendMessage( " " );
            }
        }
    }
    
    public static void MutePlayer( CommandSender p , Player muted ){
        PersistentDataContainer data = muted.getPersistentDataContainer( );
        if ( !data.has( new NamespacedKey( main.plugin , "muted" ) , PersistentDataType.STRING ) ) {
            data.set( new NamespacedKey( main.plugin , "muted" ) , PersistentDataType.STRING , "muted" );
            if ( p instanceof Player ) {
                Player jugador = ( Player ) p;
                utils.tell( muted , main.plugin.getConfig( ).getString( "server_prefix" ) + "&7The player &c" + jugador.getName( ) + " &cMuted &7you" );
            } else {
                utils.tell( muted , main.plugin.getConfig( ).getString( "server_prefix" ) + "&7The Console &cMuted &7you" );
            }
            ComponentBuilder cb = new ComponentBuilder( utils.chat( "&7Click to teleport &a✓" ) );
            TextComponent v = new TextComponent( utils.chat( "&a    &3The player &2" + muted.getName( ) + "&3 was &cMuted." ) );
            v.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
            v.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , "/tp " + muted.getName( ) ) );
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.mute_player" ) ) {
                    utils.PlaySound( people , "staff_mute_alerts" );
                    people.sendMessage( " " );
                    utils.tell( people , "&b                 &6⚠ &5Mute Alerts &6⚠                                 " );
                    people.sendMessage( " " );
                    people.spigot( ).sendMessage( v );
                    people.sendMessage( " " );
                    if ( p instanceof Player ) {
                        utils.tell( people , "&b                 &a► &cMuted by: &c" + p.getName( ) );
                    } else {
                        
                        utils.tell( people , "&b                 &a► &cMuted by: The &cCONSOLE" );
                    }
                    people.sendMessage( " " );
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
            utils.tell( sender , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cWrong usage." );
            utils.tell( sender , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cExample /mute &a" + p.getName( ) + " &d10<s/m/h/d>" );
        }
    }
    
    private static void sendMessage( Player p , Player muted , long amount , String quantity ){
        ComponentBuilder cb = new ComponentBuilder( utils.chat( "&7Click to teleport &a✓" ) );
        TextComponent v = new TextComponent( utils.chat( "&a    &3The player &2" + muted.getName( ) + "&3 was muted." ) );
        v.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
        v.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , "/tp " + muted.getName( ) ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.mute_player" ) ) {
                if ( !muted.equals( people ) ) {
                    utils.PlaySound( people , "staff_mute_alerts" );
                    people.sendMessage( " " );
                    utils.tell( people , "&b                 &6⚠ &5Mute Alerts &6⚠" );
                    people.sendMessage( " " );
                    people.spigot( ).sendMessage( v );
                    people.sendMessage( " " );
                    utils.tell( people , "&b                 &a► &bMuted by: &c" + p.getName( ) );
                    people.sendMessage( " " );
                    utils.tell( people , "&b                 &a► &bFor &a" + amount + quantity );
                    people.sendMessage( " " );
                }
            }
        }
        utils.PlaySound( muted , "mute_alerts" );
        muted.sendMessage( " " );
        utils.tell( muted , "&b                 &6⚠ &5Mute Alerts &6⚠                                 " );
        muted.sendMessage( " " );
        utils.tell( muted , "&a            &3You were &cMuted." );
        muted.sendMessage( " " );
        utils.tell( muted , "&b       &a► &bMuted by: &c" + p.getName( ) );
        muted.sendMessage( " " );
        utils.tell( muted , "&b       &a► &bFor &a" + amount + quantity );
        muted.sendMessage( " " );
    }
}
