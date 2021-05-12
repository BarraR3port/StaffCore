package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportPlayer {
    private static main plugin;
    
    public ReportPlayer( main plugin ){
        ReportPlayer.plugin = plugin;
    }
    
    public ReportPlayer( Player p , String reason , String reported , int id ){
        Date now = new Date( );
        SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
        if ( utils.mysqlEnabled( ) ) {
            plugin.data.createReport( reported , p.getName( ) , reason , format.format( now ) , "open" );
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
    
}
