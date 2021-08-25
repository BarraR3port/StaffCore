/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.listeners;

import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.MSGChanel.SendMsg;
import cl.bebt.staffcoreapi.utils.CountdownManager;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class onChat implements Listener {
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatEvent( AsyncPlayerChatEvent e ){
        Player p = e.getPlayer( );
        if ( !e.getMessage( ).isEmpty( ) ) {
            if ( p.hasPermission( "staffcore.chatcolor" ) ) {
                e.setMessage( Utils.chat( e.getMessage( ) ) );
            }
        }
        if ( Utils.getBoolean( "discord.type.debug.enabled_debugs.commands" ) ) {
            ArrayList < String > dc = new ArrayList <>( );
            dc.add( "**Player:** " + p.getName( ) + ": " + e.getMessage( ) );
            Utils.sendDiscordDebugMsg( p , "⚠ Chat ⚠" , dc );
        }
        
        UUID uuid = p.getUniqueId( );
        if ( UserUtils.getFrozen( uuid ) ) {
            if ( Utils.getBoolean( "freeze.cancel_chat_while_frozen" ) ) {
                Utils.tell( p , Utils.getString( "freeze.talk_while_frozen" , "lg" , "staff" ) );
                e.setCancelled( true );
            }
        }
        if ( Utils.getBoolean( "staff.staffchat.enable_use_custom" ) ) {
            if ( e.getMessage( ).substring( 0 , 1 ).equals( Utils.getString( "staff.staffchat.custom_character" ) ) ) {
                if ( p.hasPermission( "staff.sc" ) ) {
                    e.setCancelled( true );
                    String msg = e.getMessage( ).substring( 1 );
                    for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                        if ( UserUtils.getStaff( people.getUniqueId( ) ) || people.hasPermission( "staff.sc" ) || UserUtils.getStaffChat( people.getUniqueId( ) ) ) {
                            String message = Utils.getString( "staff_chat.prefix" , "lg" , null );
                            message = message.replace( "%sender%" , p.getName( ) );
                            message = message.replace( "%msg%" , msg );
                            Utils.tell( people , message );
                        }
                    }
                    SendMsg.sendStaffChatMSG( p.getName( ) , msg , Utils.getString( "bungeecord.server" ) );
                    return;
                }
            }
        }
        if ( UserUtils.getStaffChat( uuid ) ) {
            e.setCancelled( true );
            String msg = e.getMessage( );
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                if ( UserUtils.getStaff( people.getUniqueId( ) ) || people.hasPermission( "staff.sc" ) ) {
                    String message = Utils.getString( "staff_chat.prefix" , "lg" , null );
                    message = message.replace( "%sender%" , p.getName( ) );
                    message = message.replace( "%msg%" , msg );
                    Utils.tell( people , message );
                }
            }
            SendMsg.sendStaffChatMSG( p.getName( ) , msg , Utils.getString( "bungeecord.server" ) );
        }
        if ( !CountdownManager.checkMuteCountdown( p.getUniqueId( ) ) ) {
            e.setCancelled( true );
            long remaining = CountdownManager.getMuteCountDown( p.getUniqueId( ) );
            long days = TimeUnit.SECONDS.toDays( remaining );
            long secondsCount = remaining - TimeUnit.DAYS.toSeconds( days );
            long hours = TimeUnit.SECONDS.toHours( secondsCount );
            secondsCount -= TimeUnit.HOURS.toSeconds( hours );
            long minutes = TimeUnit.SECONDS.toMinutes( secondsCount );
            secondsCount -= TimeUnit.MINUTES.toSeconds( minutes );
            Utils.PlaySound( p , "muted_try_to_chat" );
            Utils.tell( p , Utils.getString( "toggle_chat.muted_try_to_chat" , "lg" , "sv" ) );
            Utils.tell( p , Utils.getString( "toggle_chat.time_remaining" , "lg" , "sv" )
                    .replace( "%days%" , String.valueOf( days ) )
                    .replace( "%hours%" , String.valueOf( hours ) )
                    .replace( "%minutes%" , String.valueOf( minutes ) )
                    .replace( "%seconds%" , String.valueOf( secondsCount ) ) );
            
        } else {
            if ( UserUtils.getMute( uuid ) ) {
                e.setCancelled( true );
                Utils.tell( p , Utils.getString( "toggle_chat.muted_try_to_chat" , "lg" , "sv" ) );
            }
        }
        
        if ( Api.chatMuted ) {
            if ( !p.hasPermission( "staffcore.togglechat.bypass" ) ) {
                e.setCancelled( true );
                Utils.PlaySound( p , "muted_try_to_chat" );
                Utils.tell( p , Utils.getString( "toggle_chat.chat_muted" , "lg" , "sv" ) );
            }
        }
        
    }
}
