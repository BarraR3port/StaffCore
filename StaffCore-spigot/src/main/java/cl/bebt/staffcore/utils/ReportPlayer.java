/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.SQL.Queries.ReportsQuery;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportPlayer {
    private static main plugin;
    
    public ReportPlayer( main plugin ){
        ReportPlayer.plugin = plugin;
    }
    
    public ReportPlayer( Player p , String reason , String reported ){
        Integer id = (Api.reports.getConfig( ).getInt( "count" ) + 1);
        Date now = new Date( );
        SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
        if ( Utils.mysqlEnabled( ) ) {
            ReportsQuery.createReport( reported , p.getName( ) , reason , format.format( now ) , "open" );
        } else {
            if ( Api.reports.getConfig( ).contains( "count" ) ) {
                Api.reports.getConfig( ).set( "count" , id );
                Api.reports.getConfig( ).set( "reports." + id + ".name" , reported );
                Api.reports.getConfig( ).set( "reports." + id + ".reported_by" , p.getName( ) );
                Api.reports.getConfig( ).set( "reports." + id + ".reason" , reason );
                Api.reports.getConfig( ).set( "reports." + id + ".time" , format.format( now ) );
                Api.reports.getConfig( ).set( "reports." + id + ".status" , "open" );
                Api.reports.getConfig( ).set( "current" , Utils.getCurrentReports( ) );
                Api.reports.saveConfig( );
            }
        }
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || !people.equals( p ) || Utils.getBoolean( "alerts.report" ) ) {
                Utils.PlaySound( people , "reports_alerts" );
                for ( String key : Utils.getStringList( "report.report_alerts" , "alerts" ) ) {
                    key = key.replace( "%reporter%" , p.getName( ) );
                    key = key.replace( "%reported%" , reported );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%date%" , format.format( now ) );
                    Utils.tell( people , key );
                }
            }
        }
        SendMsg.sendReportAlert( id , p.getName( ) , reported , reason , format.format( now ) , Utils.getServer( ) );
    }
    
    public static void CloseReport( Player p , Integer id ){
        String reporter = null;
        String reported = null;
        String reason = null;
        String date = null;
        String status = "closed";
        if ( Utils.mysqlEnabled( ) ) {
            JSONObject json = ReportsQuery.getReportInfo( id );
            if ( !json.getBoolean( "error" ) ) {
                reporter = json.getString( "Reporter" );
                reported = json.getString( "Name" );
                reason = json.getString( "Reason" );
                date = json.getString( "Date" );
                ReportsQuery.closeReport( id );
            }
        } else {
            Api.reports.reloadConfig( );
            reason = Api.reports.getConfig( ).getString( "reports." + id + ".reason" );
            date = Api.reports.getConfig( ).getString( "reports." + id + ".time" );
            reporter = Api.reports.getConfig( ).getString( "reports." + id + ".reported_by" );
            reported = Api.reports.getConfig( ).getString( "reports." + id + ".name" );
            Api.reports.getConfig( ).set( "reports." + id + ".status" , "close" );
            Api.reports.saveConfig( );
            Api.reports.getConfig( ).set( "count" , Utils.getCurrentReports( ) );
            Api.reports.saveConfig( );
        }
        SendMsg.sendReportChangeAlert( id , p.getName( ) , reporter , reported , reason , date , status , Utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                Utils.PlaySound( p , "close_report" );
                for ( String key : Utils.getStringList( "report.report_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%reporter%" , reporter );
                    key = key.replace( "%reported%" , reported );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%report_status%" , status );
                    Utils.tell( people , key );
                }
            }
        }
    }
    
    public static void OpenReport( Player p , Integer id ){
        String reporter = null;
        String reported = null;
        String reason = null;
        String date = null;
        String status = "open";
        if ( Utils.mysqlEnabled( ) ) {
            JSONObject json = ReportsQuery.getReportInfo( id );
            if ( !json.getBoolean( "error" ) ) {
                reporter = json.getString( "Reporter" );
                reported = json.getString( "Name" );
                reason = json.getString( "Reason" );
                date = json.getString( "Date" );
                ReportsQuery.closeReport( id );
            }
        } else {
            Api.reports.reloadConfig( );
            reason = Api.reports.getConfig( ).getString( "reports." + id + ".reason" );
            date = Api.reports.getConfig( ).getString( "reports." + id + ".time" );
            reporter = Api.reports.getConfig( ).getString( "reports." + id + ".reported_by" );
            reported = Api.reports.getConfig( ).getString( "reports." + id + ".name" );
            Api.reports.getConfig( ).set( "reports." + id + ".status" , status );
            Api.reports.saveConfig( );
            Api.reports.getConfig( ).set( "count" , Utils.getCurrentReports( ) );
            Api.reports.saveConfig( );
        }
        SendMsg.sendReportChangeAlert( id , p.getName( ) , reporter , reported , reason , date , status , Utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                Utils.PlaySound( p , "open_report" );
                for ( String key : Utils.getStringList( "report.report_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%reporter%" , reporter );
                    key = key.replace( "%reported%" , reported );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%report_status%" , status );
                    Utils.tell( people , key );
                }
            }
        }
    }
    
    public static void DeleteReport( Player p , Integer id ){
        String reporter = null;
        String reported = null;
        String reason = null;
        String date = null;
        String status = "deleted";
        if ( Utils.mysqlEnabled( ) ) {
            JSONObject json = ReportsQuery.getReportInfo( id );
            if ( !json.getBoolean( "error" ) ) {
                reporter = json.getString( "Reporter" );
                reported = json.getString( "Name" );
                reason = json.getString( "Reason" );
                date = json.getString( "Date" );
                ReportsQuery.deleteReport( id );
            }
            ReportsQuery.deleteReport( id );
        } else {
            Api.reports.reloadConfig( );
            reason = Api.reports.getConfig( ).getString( "reports." + id + ".reason" );
            date = Api.reports.getConfig( ).getString( "reports." + id + ".time" );
            reporter = Api.reports.getConfig( ).getString( "reports." + id + ".reported_by" );
            reported = Api.reports.getConfig( ).getString( "reports." + id + ".name" );
            Api.reports.getConfig( ).set( "reports." + id , null );
            Api.reports.saveConfig( );
            Api.reports.getConfig( ).set( "current" , Utils.getCurrentReports( ) );
            Api.reports.saveConfig( );
        }
        SendMsg.sendReportChangeAlert( id , p.getName( ) , reporter , reported , reason , date , status , Utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                Utils.PlaySound( p , "delete_report" );
                for ( String key : Utils.getStringList( "report.report_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%reporter%" , reporter );
                    key = key.replace( "%reported%" , reported );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%report_status%" , "DELETED" );
                    Utils.tell( people , key );
                }
            }
        }
    }
}
