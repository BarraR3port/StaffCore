package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Queries.ReportsQuery;
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
        Integer id = (plugin.reports.getConfig( ).getInt( "count" ) + 1);
        Date now = new Date( );
        SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
        if ( utils.mysqlEnabled( ) ) {
            ReportsQuery.createReport( reported , p.getName( ) , reason , format.format( now ) , "open" );
        } else {
            if ( plugin.reports.getConfig( ).contains( "count" ) ) {
                plugin.reports.getConfig( ).set( "count" , id );
                plugin.reports.getConfig( ).set( "reports." + id + ".name" , reported );
                plugin.reports.getConfig( ).set( "reports." + id + ".reported_by" , p.getName( ) );
                plugin.reports.getConfig( ).set( "reports." + id + ".reason" , reason );
                plugin.reports.getConfig( ).set( "reports." + id + ".time" , format.format( now ) );
                plugin.reports.getConfig( ).set( "reports." + id + ".status" , "open" );
                plugin.reports.getConfig( ).set( "current" , StaffCoreAPI.getCurrentReports( ) );
                plugin.reports.saveConfig( );
            }
        }
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || !people.equals( p ) || utils.getBoolean( "alerts.report" ) ) {
                utils.PlaySound( people , "reports_alerts" );
                for ( String key : utils.getStringList( "report.report_alerts" , "alerts" ) ) {
                    key = key.replace( "%reporter%" , p.getName( ) );
                    key = key.replace( "%reported%" , reported );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%date%" , format.format( now ) );
                    utils.tell( people , key );
                }
            }
        }
        SendMsg.sendReportAlert( id , p.getName( ) , reported , reason , format.format( now ) , utils.getServer( ) );
    }
    
    public static void CloseReport( Player p , Integer id ){
        String reporter = null;
        String reported = null;
        String reason = null;
        String date = null;
        String status = "closed";
        if ( utils.mysqlEnabled( ) ) {
            JSONObject json = ReportsQuery.getReportInfo( id );
            if ( !json.getBoolean( "error" ) ) {
                reporter = json.getString( "Reporter" );
                reported = json.getString( "Name" );
                reason = json.getString( "Reason" );
                date = json.getString( "Date" );
                ReportsQuery.closeReport( id );
            }
        } else {
            plugin.reports.reloadConfig( );
            reason = plugin.reports.getConfig( ).getString( "reports." + id + ".reason" );
            date = plugin.reports.getConfig( ).getString( "reports." + id + ".time" );
            reporter = plugin.reports.getConfig( ).getString( "reports." + id + ".reported_by" );
            reported = plugin.reports.getConfig( ).getString( "reports." + id + ".name" );
            plugin.reports.getConfig( ).set( "reports." + id + ".status" , "close" );
            plugin.reports.saveConfig( );
            plugin.reports.getConfig( ).set( "count" , StaffCoreAPI.getCurrentReports( ) );
            plugin.reports.saveConfig( );
        }
        SendMsg.sendReportChangeAlert( id , p.getName( ) , reporter , reported , reason , date , status , utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( p , "close_report" );
                for ( String key : utils.getStringList( "report.report_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%reporter%" , reporter );
                    key = key.replace( "%reported%" , reported );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%report_status%" , status );
                    utils.tell( people , key );
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
        if ( utils.mysqlEnabled( ) ) {
            JSONObject json = ReportsQuery.getReportInfo( id );
            if ( !json.getBoolean( "error" ) ) {
                reporter = json.getString( "Reporter" );
                reported = json.getString( "Name" );
                reason = json.getString( "Reason" );
                date = json.getString( "Date" );
                ReportsQuery.closeReport( id );
            }
        } else {
            plugin.reports.reloadConfig( );
            reason = plugin.reports.getConfig( ).getString( "reports." + id + ".reason" );
            date = plugin.reports.getConfig( ).getString( "reports." + id + ".time" );
            reporter = plugin.reports.getConfig( ).getString( "reports." + id + ".reported_by" );
            reported = plugin.reports.getConfig( ).getString( "reports." + id + ".name" );
            plugin.reports.getConfig( ).set( "reports." + id + ".status" , status );
            plugin.reports.saveConfig( );
            plugin.reports.getConfig( ).set( "count" , StaffCoreAPI.getCurrentReports( ) );
            plugin.reports.saveConfig( );
        }
        SendMsg.sendReportChangeAlert( id , p.getName( ) , reporter , reported , reason , date , status , utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( p , "open_report" );
                for ( String key : utils.getStringList( "report.report_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%reporter%" , reporter );
                    key = key.replace( "%reported%" , reported );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%report_status%" , status );
                    utils.tell( people , key );
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
        if ( utils.mysqlEnabled( ) ) {
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
            plugin.reports.reloadConfig( );
            reason = plugin.reports.getConfig( ).getString( "reports." + id + ".reason" );
            date = plugin.reports.getConfig( ).getString( "reports." + id + ".time" );
            reporter = plugin.reports.getConfig( ).getString( "reports." + id + ".reported_by" );
            reported = plugin.reports.getConfig( ).getString( "reports." + id + ".name" );
            plugin.reports.getConfig( ).set( "reports." + id , null );
            plugin.reports.saveConfig( );
            plugin.reports.getConfig( ).set( "current" , StaffCoreAPI.getCurrentReports( ) );
            plugin.reports.saveConfig( );
        }
        SendMsg.sendReportChangeAlert( id , p.getName( ) , reporter , reported , reason , date , status , utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( p , "delete_report" );
                for ( String key : utils.getStringList( "report.report_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%reporter%" , reporter );
                    key = key.replace( "%reported%" , reported );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%report_status%" , "DELETED" );
                    utils.tell( people , key );
                }
            }
        }
    }
}
