package cl.bebt.staffcore.sql.Queries;

import cl.bebt.staffcore.sql.Mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ServerQuery {
    
    public static HashMap < String, Integer > getServerStatus( ){
        HashMap < String, Integer > serverStatus = new HashMap <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT (SELECT COUNT(bans.Name) FROM sc_alts LEFT JOIN sc_bans bans ON bans.Name = sc_alts.Name )  AS CurrentBans, (SELECT COUNT(warns.Name) FROM sc_alts LEFT JOIN sc_warns warns ON warns.Name = sc_alts.Name )  AS CurrentWarns, (SELECT COUNT(reports.Name) FROM sc_alts LEFT JOIN sc_reports reports ON reports.Name = sc_alts.Name ) AS CurrentReports;" );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                serverStatus.put( "currentWarns" , rs.getInt( "CurrentWarns" ) );
                serverStatus.put( "currentBans" , rs.getInt( "CurrentBans" ) );
                serverStatus.put( "currentReports" , rs.getInt( "CurrentReports" ) );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return serverStatus;
    }
    
    public static HashMap < String, Integer > getPlayerStatus( String p ){
        HashMap < String, Integer > serverStatus = new HashMap <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT (SELECT COUNT(bans.Name) FROM sc_alts LEFT JOIN sc_bans bans ON bans.Name = sc_alts.Name WHERE bans.Name LIKE ?) AS CurrentBans,(SELECT COUNT(warns.Name) FROM sc_alts LEFT JOIN sc_warns warns ON warns.Name = sc_alts.Name WHERE warns.Name LIKE ? )  AS CurrentWarns,(SELECT COUNT(reports.Name) FROM sc_alts LEFT JOIN sc_reports reports ON reports.Name = sc_alts.Name WHERE reports.Name LIKE ? ) AS CurrentReports" );
            statement.setString( 1 , p );
            statement.setString( 2 , p );
            statement.setString( 3 , p );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                serverStatus.put( "currentWarns" , rs.getInt( "CurrentWarns" ) );
                serverStatus.put( "currentBans" , rs.getInt( "CurrentBans" ) );
                serverStatus.put( "currentReports" , rs.getInt( "CurrentReports" ) );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return serverStatus;
    }
    
    public static HashMap < String, String > getSavedSkins( ){
        HashMap < String, String > skins = new HashMap <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Name, Skin FROM sc_alts" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                skins.put( rs.getString( "Name" ) , rs.getString( "Skin" ) );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return skins;
    }
    
}
