/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.utils;

import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ToggleChat {
    
    public static void Mute( Boolean bol ){
        if ( bol ) {
            Api.chatMuted = true;
            DataExporter.updateServerStats( UpdateType.MUTE );
        }
        if ( !bol ) {
            Api.chatMuted = false;
            DataExporter.updateServerStats( UpdateType.MUTE );
        }
    }
    
    public static void unMute( CommandSender p , UUID uuid ){
        CountdownManager.removeMuteCountdown( uuid );
        if ( p instanceof Player ) {
            Utils.tell( uuid , Utils.getString( "toggle_chat.un_mute_by_player" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
        } else {
            Utils.tell( uuid , Utils.getString( "toggle_chat.un_mute_by_console" , "lg" , "staff" ) );
        }
        String name = UserUtils.findUser( uuid ).getName( );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || Utils.getBoolean( "alerts.mute_player" ) ) {
                Utils.PlaySound( people , "staff_mute_alerts" );
                for ( String key : Utils.getStringList( "chat.toggle" , "alerts" ) ) {
                    if ( p instanceof Player ) {
                        key = key.replace( "%staff%" , p.getName( ) );
                    } else {
                        key = key.replace( "%staff%" , Utils.getConsoleName( ) );
                    }
                    key = key.replace( "%muted%" , name );
                    key = key.replace( "%status%" , "&aUn Muted" );
                    Utils.tell( people , key );
                }
            }
        }
        DataExporter.updateServerStats( UpdateType.MUTE );
        UserUtils.setMute( uuid , false );
        
    }
    
    public static void MutePlayer( CommandSender p , UUID uuid ){
        if ( !UserUtils.getMute( uuid ) ) {
            if ( p instanceof Player ) {
                Player jugador = ( Player ) p;
                Utils.tell( uuid , Utils.getString( "toggle_chat.mute_by_player" , "lg" , "sv" ).replace( "%player%" , jugador.getName( ) ) );
            } else {
                Utils.tell( uuid , Utils.getString( "toggle_chat.mute_by_player" , "lg" , "sv" ).replace( "%player%" , Utils.getConsoleName( ) ) );
            }
            String name = UserUtils.findUser( uuid ).getName( );
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                if ( people.hasPermission( "staffcore.staff" ) || Utils.getBoolean( "alerts.mute_player" ) ) {
                    Utils.PlaySound( people , "staff_mute_alerts" );
                    for ( String key : Utils.getStringList( "chat.toggle" , "alerts" ) ) {
                        if ( p instanceof Player ) {
                            key = key.replace( "%staff%" , p.getName( ) );
                        } else {
                            key = key.replace( "%staff%" , Utils.getConsoleName( ) );
                        }
                        key = key.replace( "%muted%" , name );
                        key = key.replace( "%status%" , "&cMuted" );
                        Utils.tell( people , key );
                    }
                }
            }
            DataExporter.updateServerStats( UpdateType.MUTE );
            UserUtils.setMute( uuid , true );
        } else {
            unMute( p , uuid );
        }
        
    }
    
    
    public static void MuteCooldown( CommandSender sender , Player p , String time , long amount ){
        UUID uuid = p.getUniqueId( );
        switch (time) {
            case "s":
                CountdownManager.setMuteCountdown( uuid , amount );
                sendMessage( ( Player ) sender , p , amount , "s" );
                DataExporter.updateServerStats( UpdateType.MUTE );
                break;
            case "m":
                CountdownManager.setMuteCountdown( uuid , amount * 60 );
                sendMessage( ( Player ) sender , p , amount , "m" );
                DataExporter.updateServerStats( UpdateType.MUTE );
                break;
            case "h":
                CountdownManager.setMuteCountdown( uuid , amount * 3600 );
                sendMessage( ( Player ) sender , p , amount , "h" );
                DataExporter.updateServerStats( UpdateType.MUTE );
                break;
            case "d":
                CountdownManager.setMuteCountdown( uuid , amount * 86400 );
                sendMessage( ( Player ) sender , p , amount , "d" );
                DataExporter.updateServerStats( UpdateType.MUTE );
                break;
            default:
                Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "mute " + p.getName( ) + " &d10<s/m/h/d>" ) );
                break;
        }
    }
    
    private static void sendMessage( Player p , Player muted , long amount , String quantity ){
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || Utils.getBoolean( "alerts.mute_player" ) || people.equals( muted ) ) {
                Utils.PlaySound( people , "staff_mute_alerts" );
                for ( String key : Utils.getStringList( "chat.temporal_mute" , "alerts" ) ) {
                    key = key.replace( "%staff%" , p.getName( ) );
                    key = key.replace( "%muted%" , muted.getName( ) );
                    key = key.replace( "%amount%" , String.valueOf( amount ) );
                    key = key.replace( "%quantity%" , quantity );
                    Utils.tell( people , key );
                }
            }
        }
    }
}
