/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Warn.WarnMenu;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.SQL.Queries.WarnsQuery;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WarnPlayer {
    
    private static main plugin;
    
    public WarnPlayer( Player p , String warned , main plugin ){
        WarnPlayer.plugin = plugin;
        new WarnMenu( new PlayerMenuUtility( p ) , plugin , warned ).open( );
    }
    
    
    public static void createWarn( Player p , String warned , String reason , int amount , String time ){
        int id = (Api.warns.getConfig( ).getInt( "count" ) + 1);
        Date now = new Date( );
        Calendar cal = Calendar.getInstance( );
        cal.setTime( now );
        switch (time) {
            case "s" -> cal.add( Calendar.SECOND , amount );
            case "m" -> cal.add( Calendar.MINUTE , amount );
            case "h" -> cal.add( Calendar.HOUR , amount );
            case "d" -> cal.add( Calendar.DAY_OF_MONTH , amount );
        }
        Date ExpDate = cal.getTime( );
        SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
        
        if ( Utils.mysqlEnabled( ) ) {
            WarnsQuery.createWarn( warned , p.getName( ) , reason , format.format( now ) , format.format( ExpDate ) , "open" );
        } else {
            if ( Api.warns.getConfig( ).contains( "count" ) ) {
                Api.warns.getConfig( ).set( "count" , id );
                Api.warns.getConfig( ).set( "warns." + id + ".name" , warned );
                Api.warns.getConfig( ).set( "warns." + id + ".warned_by" , p.getName( ) );
                Api.warns.getConfig( ).set( "warns." + id + ".reason" , reason );
                Api.warns.getConfig( ).set( "warns." + id + ".date" , format.format( now ) );
                Api.warns.getConfig( ).set( "warns." + id + ".expdate" , format.format( ExpDate ) );
                Api.warns.getConfig( ).set( "warns." + id + ".status" , "open" );
                Api.warns.getConfig( ).set( "current" , Utils.getCurrentWarns( ) );
                Api.warns.saveConfig( );
            }
        }
        SendMsg.sendWarnAlert( p.getName( ) , warned , reason , amount , time , format.format( ExpDate ) , format.format( now ) , Utils.getString( "bungeecord.server" ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( Utils.getBoolean( "alerts.warn" ) || people.hasPermission( "staffcore.staff" ) ) {
                Utils.PlaySound( people , "warn_alerts" );
                for ( String key : Utils.getStringList( "warns.alerts.warn_alerts" , "alerts" ) ) {
                    key = key.replace( "%warner%" , p.getName( ) );
                    key = key.replace( "%warned%" , warned );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%amount%" , String.valueOf( amount ) );
                    key = key.replace( "%time%" , time );
                    key = key.replace( "%exp_date%" , format.format( ExpDate ) );
                    key = key.replace( "%date%" , format.format( now ) );
                    Utils.tell( people , key );
                }
            }
        }
        if ( Utils.currentPlayerWarns( warned ) >= Utils.getInt( "warns.max_warns" , null ) ) {
            BanManager.TempBan( p.getUniqueId( ) , Utils.getUUIDFromName( warned ) , reason + " (Max Warn Exceded)" , Utils.ConvertDate( amount , time ) , false );
            WipeWarns( warned );
        }
    }
    
    public static void CloseWarn( Player p , Integer Id ){
        String reason = null;
        String created = null;
        String exp = null;
        String warner = null;
        String warned = null;
        String status = "closed";
        
        if ( Utils.mysqlEnabled( ) ) {
            JSONObject json = WarnsQuery.getWarnsInfo( Id );
            if ( !json.getBoolean( "error" ) ) {
                reason = json.getString( "Reason" );
                created = json.getString( "Date" );
                exp = json.getString( "ExpDate" );
                warner = json.getString( "Warner" );
                warned = json.getString( "Name" );
                WarnsQuery.closeWarn( Id );
            }
        } else {
            Api.warns.reloadConfig( );
            reason = Api.warns.getConfig( ).getString( "warns." + Id + ".reason" );
            created = Api.warns.getConfig( ).getString( "warns." + Id + ".date" );
            exp = Api.warns.getConfig( ).getString( "warns." + Id + ".expdate" );
            warner = Api.warns.getConfig( ).getString( "warns." + Id + ".warned_by" );
            warned = Api.warns.getConfig( ).getString( "warns." + Id + ".name" );
            Api.warns.getConfig( ).set( "warns." + Id + ".status" , "closed" );
            Api.warns.getConfig( ).set( "count" , Utils.getCurrentWarns( ) );
            Api.warns.saveConfig( );
        }
        SendMsg.sendWarnChangeAlert( Id , p.getName( ) , warner , warned , reason , exp , created , status , Utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                Utils.PlaySound( p , "close_ban" );
                for ( String key : Utils.getStringList( "warns.alerts.warn_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%warner%" , warner );
                    key = key.replace( "%warned%" , warned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%warn_status%" , "CLOSED" );
                    Utils.tell( people , key );
                }
            }
        }
    }
    
    public static void OpenWarn( Player p , Integer Id ){
        String reason = null;
        String created = null;
        String exp = null;
        String warner = null;
        String warned = null;
        String status = "open";
        
        if ( Utils.mysqlEnabled( ) ) {
            JSONObject json = WarnsQuery.getWarnsInfo( Id );
            if ( !json.getBoolean( "error" ) ) {
                reason = json.getString( "Reason" );
                created = json.getString( "Date" );
                exp = json.getString( "ExpDate" );
                warner = json.getString( "Warner" );
                warned = json.getString( "Name" );
                WarnsQuery.openWarn( Id );
            }
        } else {
            Api.warns.reloadConfig( );
            reason = Api.warns.getConfig( ).getString( "warns." + Id + ".reason" );
            created = Api.warns.getConfig( ).getString( "warns." + Id + ".date" );
            exp = Api.warns.getConfig( ).getString( "warns." + Id + ".expdate" );
            warner = Api.warns.getConfig( ).getString( "warns." + Id + ".warned_by" );
            warned = Api.warns.getConfig( ).getString( "warns." + Id + ".name" );
            Api.warns.getConfig( ).set( "warns." + Id + ".status" , "open" );
            Api.warns.getConfig( ).set( "count" , Utils.getCurrentWarns( ) );
            Api.warns.saveConfig( );
        }
        SendMsg.sendWarnChangeAlert( Id , p.getName( ) , warner , warned , reason , exp , created , status , Utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                Utils.PlaySound( p , "close_ban" );
                for ( String key : Utils.getStringList( "warns.alerts.warn_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%warner%" , warner );
                    key = key.replace( "%warned%" , warned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%warn_status%" , "OPEN" );
                    Utils.tell( people , key );
                }
            }
        }
    }
    
    public static void DeleteWarn( Player p , Integer Id ){
        String reason = null;
        String created = null;
        String exp = null;
        String warner = null;
        String warned = null;
        String status = "deleted";
        
        if ( Utils.mysqlEnabled( ) ) {
            JSONObject json = WarnsQuery.getWarnsInfo( Id );
            if ( !json.getBoolean( "error" ) ) {
                reason = json.getString( "Reason" );
                created = json.getString( "Date" );
                exp = json.getString( "ExpDate" );
                warner = json.getString( "Warner" );
                warned = json.getString( "Name" );
                WarnsQuery.deleteWarn( Id );
            }
        } else {
            Api.warns.reloadConfig( );
            reason = Api.warns.getConfig( ).getString( "warns." + Id + ".reason" );
            created = Api.warns.getConfig( ).getString( "warns." + Id + ".date" );
            exp = Api.warns.getConfig( ).getString( "warns." + Id + ".expdate" );
            warner = Api.warns.getConfig( ).getString( "warns." + Id + ".warned_by" );
            warned = Api.warns.getConfig( ).getString( "warns." + Id + ".name" );
            Api.warns.getConfig( ).set( "warns." + Id , null );
            Api.warns.getConfig( ).set( "count" , Utils.getCurrentWarns( ) );
            Api.warns.saveConfig( );
        }
        SendMsg.sendWarnChangeAlert( Id , p.getName( ) , warner , warned , reason , exp , created , status , Utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                Utils.PlaySound( p , "close_ban" );
                for ( String key : Utils.getStringList( "warns.alerts.warn_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%warner%" , warner );
                    key = key.replace( "%warned%" , warned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%warn_status%" , "DELETED" );
                    Utils.tell( people , key );
                }
            }
        }
    }
    
    private static void WipeWarns( String p ){
        HashMap < Integer, Integer > ids = Ids( p );
        for ( int i = 1; i <= ids.size( ); i++ ) {
            if ( ids.get( i ) != null ) {
                Api.warns.reloadConfig( );
                Api.warns.getConfig( ).set( "warns." + ids.get( i ) , null );
                Api.warns.getConfig( ).set( "current" , Utils.count( UpdateType.WARN ) );
                Api.warns.saveConfig( );
            }
        }
    }
    
    
    private static HashMap < Integer, Integer > Ids( String p ){
        HashMap < Integer, Integer > ids = new HashMap <>( );
        int num = 0;
        try {
            Api.warns.reloadConfig( );
            ConfigurationSection inventorySection = Api.warns.getConfig( ).getConfigurationSection( "warns" );
            for ( String key : inventorySection.getKeys( false ) ) {
                int id = Integer.parseInt( key );
                String name = Api.warns.getConfig( ).getString( "warns." + id + ".name" );
                if ( p.equalsIgnoreCase( name ) ) {
                    num++;
                    ids.put( num , id );
                }
            }
        } catch ( NullPointerException ignored ) {
        }
        return ids;
    }
    
}
