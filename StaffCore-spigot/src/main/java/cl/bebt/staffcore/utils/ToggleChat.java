/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.EntitysUtils.UserUtils;
import cl.bebt.staffcore.Exeptions.PlayerNotFundException;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.DataExporter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ToggleChat {
    
    public static void Mute( Boolean bol ){
        if ( bol ) {
            main.plugin.chatMuted = true;
            DataExporter.updateServerStats( "mute" );
        }
        if ( !bol ) {
            main.plugin.chatMuted = false;
        }
    }
    
    public static void unMute( CommandSender p , UUID uuid ){
        try {
            CountdownManager.removeMuteCountdown( uuid );
            if ( p instanceof Player ) {
                utils.tell( uuid , utils.getString( "toggle_chat.un_mute_by_player" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
            } else {
                utils.tell( uuid , utils.getString( "toggle_chat.un_mute_by_console" , "lg" , "staff" ) );
            }
            String name = UserUtils.findUser( uuid ).getName( );
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.mute_player" ) ) {
                    utils.PlaySound( people , "staff_mute_alerts" );
                    for ( String key : utils.getStringList( "chat.toggle" , "alerts" ) ) {
                        if ( p instanceof Player ) {
                            key = key.replace( "%staff%" , p.getName( ) );
                        } else {
                            key = key.replace( "%staff%" , utils.getConsoleName( ) );
                        }
                        key = key.replace( "%muted%" , name );
                        key = key.replace( "%status%" , "&aUn Muted" );
                        utils.tell( people , key );
                    }
                }
            }
            DataExporter.updateServerStats( "mute" );
            UserUtils.setMute( uuid , false );
        } catch ( PlayerNotFundException ignored ) {
        }
    }
    
    public static void MutePlayer( CommandSender p , UUID uuid ){
        try {
            if ( !UserUtils.getMute( uuid ) ) {
                if ( p instanceof Player ) {
                    Player jugador = ( Player ) p;
                    utils.tell( uuid , utils.getString( "toggle_chat.mute_by_player" , "lg" , "sv" ).replace( "%player%" , jugador.getName( ) ) );
                } else {
                    utils.tell( uuid , utils.getString( "toggle_chat.mute_by_player" , "lg" , "sv" ).replace( "%player%" , utils.getConsoleName( ) ) );
                }
                String name = UserUtils.findUser( uuid ).getName( );
                for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                    if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.mute_player" ) ) {
                        utils.PlaySound( people , "staff_mute_alerts" );
                        for ( String key : utils.getStringList( "chat.toggle" , "alerts" ) ) {
                            if ( p instanceof Player ) {
                                key = key.replace( "%staff%" , p.getName( ) );
                            } else {
                                key = key.replace( "%staff%" , utils.getConsoleName( ) );
                            }
                            key = key.replace( "%muted%" , name );
                            key = key.replace( "%status%" , "&cMuted" );
                            utils.tell( people , key );
                        }
                    }
                }
                DataExporter.updateServerStats( "mute" );
                UserUtils.setMute( uuid , true );
            } else {
                unMute( p , uuid );
            }
        } catch ( PlayerNotFundException ignored ) {
        }
    }
    
    
    public static void MuteCooldown( CommandSender sender , Player p , String time , long amount ){
        UUID uuid = p.getUniqueId( );
        switch (time) {
            case "s":
                CountdownManager.setMuteCountdown( uuid , amount );
                sendMessage( ( Player ) sender , p , amount , "s" );
                DataExporter.updateServerStats( "mute" );
                break;
            case "m":
                CountdownManager.setMuteCountdown( uuid , amount * 60 );
                sendMessage( ( Player ) sender , p , amount , "m" );
                DataExporter.updateServerStats( "mute" );
                break;
            case "h":
                CountdownManager.setMuteCountdown( uuid , amount * 3600 );
                sendMessage( ( Player ) sender , p , amount , "h" );
                DataExporter.updateServerStats( "mute" );
                break;
            case "d":
                CountdownManager.setMuteCountdown( uuid , amount * 86400 );
                sendMessage( ( Player ) sender , p , amount , "d" );
                DataExporter.updateServerStats( "mute" );
                break;
            default:
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "mute " + p.getName( ) + " &d10<s/m/h/d>" ) );
                break;
        }
    }
    
    private static void sendMessage( Player p , Player muted , long amount , String quantity ){
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.mute_player" ) || people.equals( muted ) ) {
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
