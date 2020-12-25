package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Warn.WarnMenu;
import cl.bebt.staffcore.sql.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WarnPlayer {
    
    private static main plugin;
    
    public WarnPlayer( Player p , String warned , main plugin ){
        WarnPlayer.plugin = plugin;
        new WarnMenu( new PlayerMenuUtility( p ) , plugin , warned ).open( p );
    }
    
    
    public static void createWarn( Player p , String warned , String reason , long amount , String time ){
        int id = id( );
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
            main.plugin.data.createWarn( warned , p.getName( ) , reason , format.format( now ) , format.format( ExpDate ) , "open" );
        } else {
            if ( main.plugin.warns.getConfig( ).contains( "count" ) ) {
                main.plugin.warns.getConfig( ).set( "count" , id );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".name" , warned );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".warned_by" , p.getName( ) );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".reason" , reason );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".date" , format.format( now ) );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".expdate" , format.format( ExpDate ) );
                main.plugin.warns.getConfig( ).set( "warns." + id + ".status" , "open" );
                main.plugin.warns.getConfig( ).set( "current" , new PlayerMenuUtility( p ).currentWarns( ) );
                main.plugin.warns.saveConfig( );
            }
        }
        SendMsg.sendWarnAlert( p.getName( ) , warned , reason , amount , time , format.format( ExpDate ) , format.format( now ) , main.plugin.getConfig( ).getString( "bungeecord.server" ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( utils.getBoolean( "alerts.warn" ) || people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( people , "warn_alerts" );
                for ( String key : main.plugin.getConfig( ).getStringList( "warns.alerts.warn_alerts" ) ) {
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
        if ( utils.currentPlayerWarns( warned ) >= utils.getInt( "warns.max_warns" ) ) {
            BanPlayer.BanCooldown( p , warned , reason + " (Max Warn Exeded)" , amount , time );
            WipeWarns( warned );
        }
    }
    
    public static void unWarn( Player p , main plugin , int Id ){
        String player = "";
        if ( p instanceof Player ) {
            player = p.getName( );
        } else {
            player = "CONSOLE";
        }
        String reason = null;
        String created = null;
        String exp = null;
        String warner = null;
        String warned = null;
        String status = "un warned";
        if ( utils.mysqlEnabled( ) ) {
            reason = SQLGetter.getWarned( Id , "Reason" );
            created = SQLGetter.getWarned( Id , "Date" );
            exp = SQLGetter.getWarned( Id , "ExpDate" );
            warner = SQLGetter.getWarned( Id , "Warner" );
            warned = SQLGetter.getWarned( Id , "Name" );
            SQLGetter.deleteWarn( Id );
        } else {
            plugin.warns.reloadConfig( );
            reason = plugin.warns.getConfig( ).getString( "warns." + Id + ".reason" );
            created = plugin.warns.getConfig( ).getString( "warns." + Id + ".date" );
            exp = plugin.warns.getConfig( ).getString( "warns." + Id + ".expdate" );
            warner = plugin.warns.getConfig( ).getString( "warns." + Id + ".warned_by" );
            warned = plugin.warns.getConfig( ).getString( "warns." + Id + ".name" );
            plugin.warns.getConfig( ).set( "warns." + Id , null );
            plugin.warns.getConfig( ).set( "current" , utils.currentWarns( ) );
            plugin.warns.saveConfig( );
        }
        SendMsg.sendWarnChangeAlert( Id , p.getName( ) , warner , warned , reason , exp , created , status , plugin.getConfig( ).getString( "bungeecord.server" ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.warn" ) ) {
                utils.PlaySound( people , "un_ban" );
                for ( String key : main.plugin.getConfig( ).getStringList( "warns.alerts.warn_change" ) ) {
                    key = key.replace( "%changed_by%" , player );
                    key = key.replace( "%warner%" , warner );
                    key = key.replace( "%warned%" , warned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%warn_status%" , "UN WARNED" );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    public static int id( ){
        if ( utils.mysqlEnabled( ) ) {
            return SQLGetter.getWarnId( );
        } else {
            int id = main.plugin.warns.getConfig( ).getInt( "count" );
            id++;
            return id;
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
