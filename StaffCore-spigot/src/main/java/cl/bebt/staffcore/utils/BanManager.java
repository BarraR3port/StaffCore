/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.Entities.Ban;
import cl.bebt.staffcoreapi.EntitiesUtils.BanUtils;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.SQL.Queries.AltsQuery;
import cl.bebt.staffcoreapi.SQL.Queries.BansQuery;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
            Bukkit.getScheduler( ).runTaskAsynchronously( main.plugin , ( ) -> {
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
                    key = key.replace( "%amount%" , "&4PERMANENT" );
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
            if ( main.plugin.getConfig( ).getBoolean( "wipe.wipe_on_ban" ) ) {
                wipePlayer.WipeOnBan( main.plugin , Banned.get( ) );
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
            if ( main.plugin.getConfig( ).getBoolean( "wipe.wipe_on_ban" ) ) {
                wipePlayer.WipeOnBan( main.plugin , Banned.get( ) );
            }
            String finalBan_msg = ban_msg;
            Bukkit.getPlayer( BannedUUID ).kickPlayer( Utils.chat( finalBan_msg ) );
        }
        
    }
    
    public void UnBan( Ban ban , UUID UnBanner ){
        
        BanUtils.deleteBan( ban.getBanId( ) );
    }
    
    
}
