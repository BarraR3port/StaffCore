/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Warn.WarnMenu;
import cl.bebt.staffcore.sql.Queries.WarnsQuery;
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
    
    
    public static void createWarn( Player p , String warned , String reason , long amount , String time ){
        int id = (main.plugin.warns.getConfig( ).getInt( "count" ) + 1);
        Date now = new Date( );
        Calendar cal = Calendar.getInstance( );
        cal.setTime( now );
        switch (time) {
            case "s":
                cal.add( Calendar.SECOND , ( int ) amount );
                break;
            case "m":
                cal.add( Calendar.MINUTE , ( int ) amount );
                break;
            case "h":
                cal.add( Calendar.HOUR , ( int ) amount );
                break;
            case "d":
                cal.add( Calendar.DAY_OF_MONTH , ( int ) amount );
                break;
        }
        Date ExpDate = cal.getTime( );
        SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
        
        if ( utils.mysqlEnabled( ) ) {
            WarnsQuery.createWarn( warned , p.getName( ) , reason , format.format( now ) , format.format( ExpDate ) , "open" );
        } else {
            if ( main.plugin.warns.getConfig( ).contains( "count" ) ) {
                main.plugin.warns.getConfig( ).set( "count" , id );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".name" , warned );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".warned_by" , p.getName( ) );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".reason" , reason );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".date" , format.format( now ) );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".expdate" , format.format( ExpDate ) );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".status" , "open" );
                main.plugin.warns.getConfig( ).set( "current" , StaffCoreAPI.getCurrentWarns( ) );
                main.plugin.warns.saveConfig( );
            }
        }
        SendMsg.sendWarnAlert( p.getName( ) , warned , reason , amount , time , format.format( ExpDate ) , format.format( now ) , utils.getString( "bungeecord.server" ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( utils.getBoolean( "alerts.warn" ) || people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( people , "warn_alerts" );
                for ( String key : utils.getStringList( "warns.alerts.warn_alerts" , "alerts" ) ) {
                    key = key.replace( "%warner%" , p.getName( ) );
                    key = key.replace( "%warned%" , warned );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%amount%" , String.valueOf( amount ) );
                    key = key.replace( "%time%" , time );
                    key = key.replace( "%exp_date%" , format.format( ExpDate ) );
                    key = key.replace( "%date%" , format.format( now ) );
                    utils.tell( people , key );
                }
            }
        }
        if ( utils.currentPlayerWarns( warned ) >= utils.getInt( "warns.max_warns" , null ) ) {
            BanManager.TempBan( p.getUniqueId( ) , utils.getUUIDFromName( warned ) , reason + " (Max Warn Exceded)" , utils.ConvertDate( amount , time ) , false );
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
        
        if ( utils.mysqlEnabled( ) ) {
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
            plugin.warns.reloadConfig( );
            reason = plugin.warns.getConfig( ).getString( "warns." + Id + ".reason" );
            created = plugin.warns.getConfig( ).getString( "warns." + Id + ".date" );
            exp = plugin.warns.getConfig( ).getString( "warns." + Id + ".expdate" );
            warner = plugin.warns.getConfig( ).getString( "warns." + Id + ".warned_by" );
            warned = plugin.warns.getConfig( ).getString( "warns." + Id + ".name" );
            plugin.warns.getConfig( ).set( "warns." + Id + ".status" , "closed" );
            plugin.warns.getConfig( ).set( "count" , StaffCoreAPI.getCurrentWarns( ) );
            plugin.warns.saveConfig( );
        }
        SendMsg.sendWarnChangeAlert( Id , p.getName( ) , warner , warned , reason , exp , created , status , utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( p , "close_ban" );
                for ( String key : utils.getStringList( "warns.alerts.warn_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%warner%" , warner );
                    key = key.replace( "%warned%" , warned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%warn_status%" , "CLOSED" );
                    utils.tell( people , key );
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
        
        if ( utils.mysqlEnabled( ) ) {
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
            plugin.warns.reloadConfig( );
            reason = plugin.warns.getConfig( ).getString( "warns." + Id + ".reason" );
            created = plugin.warns.getConfig( ).getString( "warns." + Id + ".date" );
            exp = plugin.warns.getConfig( ).getString( "warns." + Id + ".expdate" );
            warner = plugin.warns.getConfig( ).getString( "warns." + Id + ".warned_by" );
            warned = plugin.warns.getConfig( ).getString( "warns." + Id + ".name" );
            plugin.warns.getConfig( ).set( "warns." + Id + ".status" , "open" );
            plugin.warns.getConfig( ).set( "count" , StaffCoreAPI.getCurrentWarns( ) );
            plugin.warns.saveConfig( );
        }
        SendMsg.sendWarnChangeAlert( Id , p.getName( ) , warner , warned , reason , exp , created , status , utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( p , "close_ban" );
                for ( String key : utils.getStringList( "warns.alerts.warn_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%warner%" , warner );
                    key = key.replace( "%warned%" , warned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%warn_status%" , "OPEN" );
                    utils.tell( people , key );
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
        
        if ( utils.mysqlEnabled( ) ) {
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
            plugin.warns.reloadConfig( );
            reason = plugin.warns.getConfig( ).getString( "warns." + Id + ".reason" );
            created = plugin.warns.getConfig( ).getString( "warns." + Id + ".date" );
            exp = plugin.warns.getConfig( ).getString( "warns." + Id + ".expdate" );
            warner = plugin.warns.getConfig( ).getString( "warns." + Id + ".warned_by" );
            warned = plugin.warns.getConfig( ).getString( "warns." + Id + ".name" );
            plugin.warns.getConfig( ).set( "warns." + Id , null );
            plugin.warns.getConfig( ).set( "count" , StaffCoreAPI.getCurrentWarns( ) );
            plugin.warns.saveConfig( );
        }
        SendMsg.sendWarnChangeAlert( Id , p.getName( ) , warner , warned , reason , exp , created , status , utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( p , "close_ban" );
                for ( String key : utils.getStringList( "warns.alerts.warn_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%warner%" , warner );
                    key = key.replace( "%warned%" , warned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%warn_status%" , "DELETED" );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    private static void WipeWarns( String p ){
        HashMap < Integer, Integer > ids = Ids( p );
        for ( int i = 1; i <= ids.size( ); i++ ) {
            if ( ids.get( i ) != null ) {
                plugin.warns.reloadConfig( );
                plugin.warns.getConfig( ).set( "warns." + ids.get( i ) , null );
                plugin.warns.getConfig( ).set( "current" , wipePlayer.count( "warns" ) );
                plugin.warns.saveConfig( );
            }
        }
    }
    
    
    private static HashMap < Integer, Integer > Ids( String p ){
        HashMap < Integer, Integer > ids = new HashMap <>( );
        int num = 0;
        try {
            plugin.warns.reloadConfig( );
            ConfigurationSection inventorySection = plugin.warns.getConfig( ).getConfigurationSection( "warns" );
            for ( String key : inventorySection.getKeys( false ) ) {
                int id = Integer.parseInt( key );
                String name = plugin.warns.getConfig( ).getString( "warns." + id + ".name" );
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
