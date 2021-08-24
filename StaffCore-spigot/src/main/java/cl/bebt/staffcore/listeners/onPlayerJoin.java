/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.listeners;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.FlyManager;
import cl.bebt.staffcore.utils.FreezeManager;
import cl.bebt.staffcore.utils.StaffManager;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.Entities.User;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.SQL.Queries.AltsQuery;
import cl.bebt.staffcoreapi.SQL.Queries.BansQuery;
import cl.bebt.staffcoreapi.SQL.Queries.FreezeQuery;
import cl.bebt.staffcoreapi.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class onPlayerJoin implements Listener {
    
    private static main plugin;
    
    public onPlayerJoin( main plugin ){
        onPlayerJoin.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerPreJoin( AsyncPlayerPreLoginEvent e ){
        String ip = String.valueOf( e.getAddress( ) ).replace( "/" , "" );
        Bukkit.getScheduler( ).runTaskAsynchronously( plugin , ( ) -> {
            if ( !UserUtils.isSaved( e.getUniqueId( ) ) ) {
                UserUtils.createUser( e.getName( ) , e.getUniqueId( ) , ip );
            }
            User user = UserUtils.findUser( e.getUniqueId( ) );
            if ( UserUtils.isInOtherIP( user , ip ) ) {
                UserUtils.addAlt( user , ip );
            }
            UserUtils.isIPSaved( user , ip );
        } );
        if ( Utils.mysqlEnabled( ) ) {
            Bukkit.getScheduler( ).runTaskAsynchronously( plugin , ( ) -> {
                User user = UserUtils.findUser( e.getUniqueId( ) );
                try {
                    if ( !AltsQuery.PlayerExists( e.getName( ) ) ) {
                        AltsQuery.createAlts( user , ip );
                    } else {
                        List < String > ips = Utils.makeList( AltsQuery.getAltsIps( e.getUniqueId( ) ) );
                        AltsQuery.addIps( e.getName( ) , Utils.stringify( ips , ip ) );
                    }
                } catch ( NullPointerException | IndexOutOfBoundsException Exception ) {
                    Exception.printStackTrace( );
                }
            } );
            if ( BansQuery.isStillBanned( e.getName( ) , ip ) ) {
                int id = BansQuery.getBannedId( e.getName( ) , ip );
                e.disallow( AsyncPlayerPreLoginEvent.Result.KICK_BANNED , KickBannedPlayerSql( BansQuery.getBanInfo( id ) ) );
            }
        } else {
            for ( int i = 1; i <= Utils.count( UpdateType.BAN ); i++ ) {
                try {
                    if ( Objects.equals( Api.bans.getConfig( ).getString( "bans." + i + ".name" ) , e.getName( ) ) &&
                            Objects.equals( Api.bans.getConfig( ).getString( "bans." + i + ".status" ) , "open" ) &&
                            Utils.isStillBanned( i ) ) {
                        e.disallow( AsyncPlayerPreLoginEvent.Result.KICK_BANNED , KickBannedPlayer( i ) );
                        break;
                    }
                    if ( Api.bans.getConfig( ).getBoolean( "bans." + i + ".IP-Banned" ) &&
                            Objects.equals( Api.bans.getConfig( ).getString( "bans." + i + ".IP" ) , ip ) &&
                            Objects.equals( Api.bans.getConfig( ).getString( "bans." + i + ".status" ) , "open" ) &&
                            Utils.isStillBanned( i ) ) {
                        e.disallow( AsyncPlayerPreLoginEvent.Result.KICK_BANNED , KickBannedPlayer( i ) );
                        break;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerJoinEvent( PlayerJoinEvent e ){
        Player p = e.getPlayer( );
        UUID uuid = p.getUniqueId( );
        if ( Utils.currentPlayerWarns( p.getName( ) ) != 0 && Utils.getBoolean( "warns.notify" ) ) {
            ComponentBuilder cb = new ComponentBuilder( Utils.chat( Utils.getString( "warns.join_msg" , "lg" , null ) ) );
            TextComponent dis = new TextComponent( Utils.chat( Utils.getString( "warns.notify" , "lg" , "staff" ).replace( "%amount%" , String.valueOf( Utils.currentPlayerWarns( p.getName( ) ) ) ) ) );
            dis.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
            dis.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , "/warningns" ) );
            p.spigot( ).sendMessage( dis );
        }
        try {
            if ( Utils.mysqlEnabled( ) ) {
                Bukkit.getScheduler( ).runTaskAsynchronously( plugin , ( ) -> {
                    if ( FreezeQuery.isFrozen( p.getName( ) ).equals( "true" ) ) {
                        FreezeManager.enable( uuid , Utils.getConsoleName( ) );
                    }
                } );
            }
            if ( UserUtils.getFly( uuid ) ) {
                FlyManager.enable( uuid );
            } else if ( !UserUtils.getFly( uuid ) && !UserUtils.getStaff( uuid ) && !UserUtils.getVanish( uuid ) ) {
                FlyManager.disable( uuid );
            }
            if ( UserUtils.getStaff( uuid ) ) {
                StaffManager.enable( uuid );
            }
            
            for ( Player player : Bukkit.getOnlinePlayers( ) ) {
                if ( !plugin.getDescription( ).getVersion( ).equals( Api.latestVersion ) ) {
                    if ( p.hasPermission( "staffcore.staff" ) ) {
                        Utils.tellHover( p , plugin.getConfig( ).getString( "server_prefix" ) +
                                        "&cYou are using an StaffCore older version" ,
                                "&aClick to download the version: " + Api.latestVersion ,
                                "http://localhost:82/download" );
                    }
                }
                if ( player != p ) {
                    if ( UserUtils.getVanish( uuid ) ) {
                        if ( player.hasPermission( "staffcore.vanish.see" ) || UserUtils.getStaff( uuid ) ) {
                            player.showPlayer( plugin , p );
                        } else {
                            player.hidePlayer( plugin , p );
                        }
                    }
                    if ( UserUtils.getVanish( uuid ) ) {
                        if ( p.hasPermission( "staffcore.vanish.see" ) || UserUtils.getStaff( uuid ) ) {
                            p.showPlayer( plugin , player );
                        } else {
                            p.hidePlayer( plugin , player );
                        }
                    }
                }
            }
        } catch ( NoSuchMethodError ignored ) {
        }
        if ( Utils.getBoolean( "discord.type.debug.enabled_debugs.commands" ) ) {
            ArrayList < String > dc = new ArrayList <>( );
            dc.add( "**Player:** " + p.getName( ) );
            dc.add( "**Reason:** " + e.getJoinMessage( ) );
            Utils.sendDiscordDebugMsg( e.getPlayer( ) , "⚠ Player Join ⚠" , dc );
        }
    }
    
    String KickBannedPlayerSql( JSONObject json ){
        try {
            String reason = json.getString( "Reason" );
            Date now = new Date( );
            String created = json.getString( "Date" );
            String exp = json.getString( "ExpDate" );
            SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
            String banner = json.getString( "Banner" );
            String banned = json.getString( "Name" );
            Date d2 = null;
            d2 = format.parse( exp );
            long remaining = (d2.getTime( ) - now.getTime( )) / 1000L;
            long Days = TimeUnit.SECONDS.toDays( remaining );
            long Seconds = remaining - TimeUnit.DAYS.toSeconds( Days );
            long Hours = TimeUnit.SECONDS.toHours( Seconds );
            Seconds -= TimeUnit.HOURS.toSeconds( Hours );
            long Minutes = TimeUnit.SECONDS.toMinutes( Seconds );
            Seconds -= TimeUnit.MINUTES.toSeconds( Minutes );
            String ban_msg = "\n";
            for ( String msg : Utils.getStringList( "ban.join" , "alerts" ) ) {
                msg = msg.replace( "%baner%" , banner );
                msg = msg.replace( "%banned%" , banned );
                msg = msg.replace( "%reason%" , reason );
                if ( Days >= 365L ) {
                    msg = msg.replace( "%time_left%" , "&4PERMANENT" );
                } else {
                    msg = msg.replace( "%time_left%" , Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" );
                }
                if ( json.getString( "IP_Banned" ).equals( "true" ) ) {
                    msg = msg.replace( "%IP_BANED%" , "&atrue" );
                } else {
                    msg = msg.replace( "%IP_BANED%" , "&cfalse" );
                }
                msg = msg.replace( "%exp_date%" , exp );
                msg = msg.replace( "%date%" , created );
                ban_msg = ban_msg + msg + "\n";
            }
            return Utils.chat( ban_msg );
        } catch ( ParseException | NullPointerException error ) {
            error.printStackTrace( );
            return null;
        }
    }
    
    String KickBannedPlayer( int Id ){
        try {
            String p = Api.bans.getConfig( ).getString( "bans." + Id + ".banned_by" );
            String banned = Api.bans.getConfig( ).getString( "bans." + Id + ".name" );
            String reason = Api.bans.getConfig( ).getString( "bans." + Id + ".reason" );
            Date now = new Date( );
            String created = Api.bans.getConfig( ).getString( "bans." + Id + ".date" );
            String exp = Api.bans.getConfig( ).getString( "bans." + Id + ".expdate" );
            SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
            Date d2 = null;
            d2 = format.parse( exp );
            long remaining = (d2.getTime( ) - now.getTime( )) / 1000L;
            long Days = TimeUnit.SECONDS.toDays( remaining );
            long Seconds = remaining - TimeUnit.DAYS.toSeconds( Days );
            long Hours = TimeUnit.SECONDS.toHours( Seconds );
            Seconds -= TimeUnit.HOURS.toSeconds( Hours );
            long Minutes = TimeUnit.SECONDS.toMinutes( Seconds );
            Seconds -= TimeUnit.MINUTES.toSeconds( Minutes );
            String ban_msg = "\n";
            for ( String msg : Utils.getStringList( "ban.join" , "alerts" ) ) {
                msg = msg.replace( "%baner%" , p );
                msg = msg.replace( "%banned%" , banned );
                msg = msg.replace( "%reason%" , reason );
                if ( Days >= 365L ) {
                    msg = msg.replace( "%time_left%" , "&4PERMANENT" );
                } else {
                    msg = msg.replace( "%time_left%" , Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" );
                }
                if ( Api.bans.getConfig( ).getBoolean( "bans." + Id + ".IP-Banned" ) ) {
                    msg = msg.replace( "%IP_BANED%" , "&atrue" );
                } else {
                    msg = msg.replace( "%IP_BANED%" , "&cfalse" );
                }
                msg = msg.replace( "%exp_date%" , exp );
                msg = msg.replace( "%date%" , created );
                ban_msg = ban_msg + msg + "\n";
            }
            return Utils.chat( ban_msg );
        } catch ( ParseException | NullPointerException error ) {
            error.printStackTrace( );
            return null;
        }
    }
}
