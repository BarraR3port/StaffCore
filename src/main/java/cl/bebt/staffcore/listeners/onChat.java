package cl.bebt.staffcore.listeners;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.CountdownManager;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.TimeUnit;

public class onChat implements Listener{
    

    @EventHandler
    public void onChatEvent( AsyncPlayerChatEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer persistent = p.getPersistentDataContainer( );
        if ( !(e.getMessage( ).isEmpty( )) ) {
            e.setMessage( utils.chat( e.getMessage( ) ) );
        }
        if ( persistent.has( new NamespacedKey( main.plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( utils.getBoolean( "freeze.cancel_chat_while_frozen" ) ) {
                utils.tell(p,utils.getString( "server_prefix" ) + "&4You can't talk while &3&lFrozen" );
                e.setCancelled( true );
            }
        }
        if ( persistent.has( new NamespacedKey( main.plugin , "staffchat" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
            String msg = e.getMessage( );
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                if ( people.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staff" ) , PersistentDataType.STRING ) || people.hasPermission( "staff.staffchat" ) ) {
                    String message = main.plugin.getConfig( ).getString( "staff.staff_chat_prefix" );
                    message = message.replace( "%sender%" , p.getName( ) );
                    message = message.replace( "%msg%" , msg );
                    utils.tell( p , message );
                }
            }
            SendMsg.sendStaffChatMSG( p.getName( ) , msg , main.plugin.getConfig( ).getString( "bungeecord.server" ) );
        }

        if ( !CountdownManager.checkMuteCountdown( p ) ) {
            e.setCancelled( true );
            long remaining = CountdownManager.getMuteCountDown( p );
            long days = TimeUnit.SECONDS.toDays( remaining );
            long secondsCount = remaining - TimeUnit.DAYS.toSeconds( days );
            long hours = TimeUnit.SECONDS.toHours( secondsCount );
            secondsCount -= TimeUnit.HOURS.toSeconds( hours );
            long minutes = TimeUnit.SECONDS.toMinutes( secondsCount );
            secondsCount -= TimeUnit.MINUTES.toSeconds( minutes );
            if ( remaining >= 2332800 ) {
                utils.PlaySound( p , "muted_try_to_chat" );
                utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cYou are muted." );
                utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cTime remaining: &a1 Month &cand &a" + (days - 27) + "&cd remaining" );
                return;
            }
            if ( remaining >= 86400 ) {
                utils.PlaySound( p , "muted_try_to_chat" );
                utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cYou are muted." );
                utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cTime remaining: &a" + days + "d&7:&a" + hours + "h&7:&a" + minutes + "m" );
                return;
            }
            if ( remaining >= 3600 ) {
                utils.PlaySound( p , "muted_try_to_chat" );
                utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cYou are muted." );
                utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cTime remaining: &a" + hours + "h&7:&a" + minutes + "m&7&a:" + secondsCount + "s");
                return;
            }
            if ( remaining >= 60 ) {
                utils.PlaySound( p , "muted_try_to_chat" );
                utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cYou are muted." );
                utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cTime remaining: &a" + minutes + "m&7&a:" + secondsCount + "s");
                return;
            }
            if ( remaining == 0 || remaining == 1 ) {
                utils.PlaySound( p , "muted_try_to_chat" );
                utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&aYou were UnMuted!" );
                e.setCancelled( false );
                return;
            }
            utils.PlaySound( p , "muted_try_to_chat" );
            utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cYou are muted. &a" + secondsCount + "&cs remaining" );

        } else if ( main.plugin.chatMuted ) {
            if ( !p.hasPermission( "staffcore.togglechat.bypass" ) ) {
                e.setCancelled( true );
                utils.PlaySound( p , "muted_try_to_chat" );
                utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&7The chat is &cmuted&7, so you can't talk :(" );
            }
        } else {
            if ( persistent.has( new NamespacedKey( main.plugin , "muted" ) , PersistentDataType.STRING ) ) {
                e.setCancelled( true );
                utils.tell( p , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cYou are muted" );
            }
        }

    }
}
