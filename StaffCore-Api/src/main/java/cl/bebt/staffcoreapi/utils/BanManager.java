/*
 * Copyright (c) 2021-2022. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.utils;

import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.Entities.Ban;
import cl.bebt.staffcoreapi.EntitiesUtils.BanUtils;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.MSGChanel.SendMsg;
import cl.bebt.staffcoreapi.SQL.Queries.AltsQuery;
import cl.bebt.staffcoreapi.SQL.Queries.BansQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class BanManager {
    
    public static void Ban( UUID BannerUUID , UUID BannedUUID , String Reason , Boolean IpBanned ) throws ParseException{
        AtomicReference < String > Banner = new AtomicReference <>( "" );
        AtomicReference < String > Banned = new AtomicReference <>( "" );
        Date CreateDate = new Date( );
        SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
        Date ExpDate = format.parse( "26-05-2102 10:10:10" );
        if ( Utils.mysqlEnabled( ) ) {
            Utils.runAsync( ( ) -> {
                BansQuery.createBanByUUID( BannedUUID , BannerUUID , Reason , format.format( CreateDate ) , format.format( ExpDate ) , IpBanned.toString( ) , "open" );
                Banner.set( AltsQuery.getNameByUUID( BannerUUID ) );
                Banned.set( AltsQuery.getNameByUUID( BannedUUID ) );
            } );
        } else {
            BanUtils.createBan( BannerUUID , BannedUUID , Reason , CreateDate , ExpDate , true , IpBanned );
            Banner.set( UserUtils.findUser( BannerUUID ).getName( ) );
            Banned.set( UserUtils.findUser( BannedUUID ).getName( ) );
        }
        SendMsg.sendBanAlert( Banner.get( ) , Banned.get( ) , Reason , true , IpBanned , ExpDate , CreateDate , Utils.getString( "bungeecord.server" ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( Utils.getBoolean( "alerts.ban" ) || people.hasPermission( "staffcore.staff" ) ) {
                Utils.PlaySound( people , "ban_alerts" );
                for ( String key : Utils.getStringList( "ban.alerts" , "alerts" ) ) {
                    key = key.replace( "%baner%" , Banner.get( ) );
                    key = key.replace( "%banned%" , Banned.get( ) );
                    key = key.replace( "%reason%" , Reason );
                    key = key.replace( "%time_left%" , "&4PERMANENT" );
                    if ( IpBanned ) {
                        key = key.replace( "%IP_BANED%" , "&atrue" );
                    } else {
                        key = key.replace( "%IP_BANED%" , "&cfalse" );
                    }
                    key = key.replace( "%exp_date%" , format.format( ExpDate ) );
                    key = key.replace( "%date%" , format.format( CreateDate ) );
                    Utils.tell( people , key );
                }
            }
        }
        if ( Bukkit.getPlayer( BannedUUID ) != null ) {
            String ban_msg = "\n";
            for ( String msg : Utils.getStringList( "ban.msg" , "alerts" ) ) {
                msg = msg.replace( "%baner%" , Banner.get( ) );
                msg = msg.replace( "%banned%" , Banned.get( ) );
                msg = msg.replace( "%reason%" , Reason );
                msg = msg.replace( "%amount%" , "&4PERMANENT" );
                if ( IpBanned ) {
                    msg = msg.replace( "%IP_BANED%" , "&atrue" );
                } else {
                    msg = msg.replace( "%IP_BANED%" , "&cfalse" );
                }
                msg = msg.replace( "%exp_date%" , format.format( ExpDate ) );
                msg = msg.replace( "%date%" , format.format( CreateDate ) );
                ban_msg = ban_msg + msg + "\n";
            }
            Utils.PlayParticle( Bukkit.getPlayer( BannedUUID ) , "ban" );
            if ( Utils.getBoolean( "wipe.wipe_on_ban" ) ) {
                WipeManager.WipeOnBan( Utils.getSpigot( ) , Banned.get( ) );
            }
            String finalBan_msg = ban_msg;
            Bukkit.getPlayer( BannedUUID ).kickPlayer( Utils.chat( finalBan_msg ) );
        }
    }
    
    public static void TempBan( UUID BannerUUID , UUID BannedUUID , String Reason , Date ExpDate , Boolean IpBanned ){
        AtomicReference < String > Banner = new AtomicReference <>( "" );
        AtomicReference < String > Banned = new AtomicReference <>( "" );
        Date CreateDate = new Date( );
        String TimeLeft = Utils.getTimeLeft( CreateDate , ExpDate );
        SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
        if ( Utils.mysqlEnabled( ) ) {
            Utils.runAsync( ( ) -> {
                BansQuery.createBanByUUID( BannedUUID , BannerUUID , Reason , format.format( CreateDate ) , format.format( ExpDate ) , IpBanned.toString( ) , "open" );
                Banner.set( AltsQuery.getNameByUUID( BannerUUID ) );
                Banned.set( AltsQuery.getNameByUUID( BannedUUID ) );
            } );
        } else {
            BanUtils.createBan( BannerUUID , BannedUUID , Reason , CreateDate , ExpDate , false , IpBanned );
            Banner.set( UserUtils.findUser( BannerUUID ).getName( ) );
            Banned.set( UserUtils.findUser( BannedUUID ).getName( ) );
        }
        SendMsg.sendBanAlert( Banner.get( ) , Banned.get( ) , Reason , false , IpBanned , ExpDate , CreateDate , Utils.getString( "bungeecord.server" ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( Utils.getBoolean( "alerts.ban" ) || people.hasPermission( "staffcore.staff" ) ) {
                Utils.PlaySound( people , "ban_alerts" );
                for ( String key : Utils.getStringList( "ban.alerts" , "alerts" ) ) {
                    key = key.replace( "%baner%" , Banner.get( ) );
                    key = key.replace( "%banned%" , Banned.get( ) );
                    key = key.replace( "%reason%" , Reason );
                    key = key.replace( "%time_left%" , TimeLeft );
                    if ( IpBanned ) {
                        key = key.replace( "%IP_BANED%" , "&atrue" );
                    } else {
                        key = key.replace( "%IP_BANED%" , "&cfalse" );
                    }
                    key = key.replace( "%exp_date%" , format.format( ExpDate ) );
                    key = key.replace( "%date%" , format.format( CreateDate ) );
                    Utils.tell( people , key );
                }
            }
        }
        if ( Bukkit.getPlayer( BannedUUID ) != null ) {
            String ban_msg = "\n";
            for ( String msg : Utils.getStringList( "ban.msg" , "alerts" ) ) {
                msg = msg.replace( "%baner%" , Banner.get( ) );
                msg = msg.replace( "%banned%" , Banned.get( ) );
                msg = msg.replace( "%reason%" , Reason );
                msg = msg.replace( "%time_left%" , TimeLeft );
                if ( IpBanned ) {
                    msg = msg.replace( "%IP_BANED%" , "&atrue" );
                } else {
                    msg = msg.replace( "%IP_BANED%" , "&cfalse" );
                }
                msg = msg.replace( "%exp_date%" , format.format( ExpDate ) );
                msg = msg.replace( "%date%" , format.format( CreateDate ) );
                ban_msg = ban_msg + msg + "\n";
            }
            Utils.PlayParticle( Bukkit.getPlayer( BannedUUID ) , "ban" );
            if ( Utils.getBoolean( "wipe.wipe_on_ban" ) ) {
                WipeManager.WipeOnBan( Utils.getSpigot( ) , Banned.get( ) );
            }
            String finalBan_msg = ban_msg;
            Bukkit.getPlayer( BannedUUID ).kickPlayer( Utils.chat( finalBan_msg ) );
        }
        
    }
    
    public static void unBan( Ban ban , int UnBanner ){
        if ( Utils.mysqlEnabled() ){
            BanUtils.deleteBan( ban.getBanId( ) );
        }
    }
    public static void CloseBan( Player p, Integer Id ){
        String reason = null;
        String created = null;
        String exp = null;
        String baner = null;
        String banned = null;
        if ( Utils.mysqlEnabled( ) ) {
            JSONObject json = BansQuery.getBanInfo( Id );
            if ( !json.getBoolean( "error" ) ) {
                reason = json.getString( "Reason" );
                created = json.getString( "Date" );
                exp = json.getString( "ExpDate" );
                baner = json.getString( "Banner" );
                banned = json.getString( "Name" );
                BansQuery.closeBan( Id );
            }
        } else {
            Api.bans.reloadConfig( );
            reason = Api.bans.getConfig( ).getString( "bans." + Id + ".reason" );
            created = Api.bans.getConfig( ).getString( "bans." + Id + ".date" );
            exp = Api.bans.getConfig( ).getString( "bans." + Id + ".expdate" );
            baner = Api.bans.getConfig( ).getString( "bans." + Id + ".banned_by" );
            banned = Api.bans.getConfig( ).getString( "bans." + Id + ".name" );
            Api.bans.getConfig( ).set( "bans." + Id + ".status" , "closed" );
            Api.bans.getConfig( ).set( "count" , Utils.getCurrentBans( ) );
            Api.bans.saveConfig( );
        }
        SendMsg.sendBanChangeAlert( Id , p.getName( ) , baner , banned , reason , exp , created , "closed" , Utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                Utils.PlaySound( p , "close_ban" );
                for ( String key : Utils.getStringList( "ban.change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%baner%" , baner );
                    key = key.replace( "%banned%" , banned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%ban_status%" , "Closed" );
                    Utils.tell( people , key );
                }
            }
        }
    }
    
    
}
