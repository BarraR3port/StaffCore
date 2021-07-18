package cl.bebt.staffcore.sql.Queries;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Mysql;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportsQuery {
    
    
    /**
     * This will create a Report into the Report Table.
     **/
    public static void createReport( String reported , String reporter , String Reason , String Date , String Status ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "INSERT INTO sc_reports (Name, Reporter, Reason, Date, Status) VALUES (?,?,?,?,?)" );
                statement.setString( 1 , reported );
                statement.setString( 2 , reporter );
                statement.setString( 3 , Reason );
                statement.setString( 4 , Date );
                statement.setString( 5 , Status );
                statement.executeUpdate( );
                break;
            } catch ( SQLException throwable ) {
                main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
                main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Creating the sc_reports table into the database" ) );
                SQLGetter.createReportTable( );
                intento++;
                
            }
        }
    }
    
    
    public static JSONObject getReportInfo( int id ){
        JSONObject json = new JSONObject( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_reports WHERE ReportId LIKE " + id );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                json.put( "error" , false );
                json.put( "ReportId" , rs.getString( "ReportId" ) );
                json.put( "Name" , rs.getString( "Name" ) );
                json.put( "Reporter" , rs.getString( "Reporter" ) );
                json.put( "Reason" , rs.getString( "Reason" ) );
                json.put( "Date" , rs.getString( "Date" ) );
                json.put( "Status" , rs.getString( "Status" ) );
            }
        } catch ( SQLException error ) {
            error.printStackTrace( );
            json.put( "error" , true );
        }
        return json;
    }
    
    public static JSONObject getOpenReportInfo( ){
        JSONObject json = new JSONObject( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_reports WHERE Status LIKE 'open' ORDER BY ReportId" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                int ReportId = rs.getInt( "ReportId" );
                String Name = rs.getString( "Name" );
                String Reporter = rs.getString( "Reporter" );
                String Reason = rs.getString( "Reason" );
                String Date = rs.getString( "Date" );
                String Status = rs.getString( "Status" );
                
                JSONArray senderArray = new JSONArray( );
                JSONObject senderDetail = new JSONObject( );
                senderDetail.put( "Name" , Name );
                senderDetail.put( "Reporter" , Reporter );
                senderDetail.put( "Reason" , Reason );
                senderDetail.put( "Date" , Date );
                senderDetail.put( "Status" , Status );
                
                senderArray.put( senderDetail );
                json.put( String.valueOf( ReportId ) , senderArray );
            }
            
        } catch ( SQLException error ) {
            error.printStackTrace( );
        }
        return json;
    }
    
    public static JSONObject getClosedReportInfo( ){
        JSONObject json = new JSONObject( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_reports WHERE Status LIKE 'close' ORDER BY ReportId" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                int ReportId = rs.getInt( "ReportId" );
                
                String Name = rs.getString( "Name" );
                String Reporter = rs.getString( "Reporter" );
                String Reason = rs.getString( "Reason" );
                String Date = rs.getString( "Date" );
                String Status = rs.getString( "Status" );
                
                JSONArray senderArray = new JSONArray( );
                JSONObject senderDetail = new JSONObject( );
                senderDetail.put( "Name" , Name );
                senderDetail.put( "Reporter" , Reporter );
                senderDetail.put( "Reason" , Reason );
                senderDetail.put( "Date" , Date );
                senderDetail.put( "Status" , Status );
                senderArray.put( senderDetail );
                json.put( String.valueOf( ReportId ) , senderArray );
            }
        } catch ( SQLException error ) {
            error.printStackTrace( );
        }
        return json;
    }
    
    public static ArrayList < Integer > getOpenReports( ){
        ArrayList < Integer > openBans = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT ReportId FROM sc_reports WHERE Status LIKE 'open' group by ReportId order by ReportId" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                openBans.add( rs.getInt( "ReportId" ) );
            }
        } catch ( SQLException error ) {
            error.printStackTrace( );
        }
        return openBans;
    }
    
    public static ArrayList < Integer > getClosedReports( ){
        ArrayList < Integer > closedBans = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT ReportId FROM sc_reports WHERE Status LIKE 'close' group by ReportId order by ReportId" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                closedBans.add( rs.getInt( "ReportId" ) );
            }
        } catch ( SQLException error ) {
            error.printStackTrace( );
        }
        return closedBans;
    }
    
    public static Boolean isOpen( int id ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Status FROM sc_reports WHERE ReportId=?" );
            statement.setInt( 1 , id );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( "Status" ).equalsIgnoreCase( "close" );
            } else {
                return false;
            }
        } catch ( SQLException error ) {
            error.printStackTrace( );
            return false;
        }
    }
    
    public static Integer getCurrentReports( ){
        int currentReports = 0;
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT count(*) AS CurrentReports FROM sc_reports" );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                currentReports = rs.getInt( "CurrentReports" );
            }
        } catch ( SQLException error ) {
            error.printStackTrace( );
        }
        return currentReports;
    }
    
    public static int getCurrentReports( String p ){
        int count = 0;
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_reports" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                String players = rs.getString( 2 );
                if ( players.equalsIgnoreCase( p ) ) {
                    count++;
                }
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return count;
    }
    
    public static List < Integer > getPlayersIds( String p ){
        List < Integer > ids = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT ReportId ,Name FROM sc_reports" );
            ResultSet results = statement.executeQuery( );
            while (results.next( )) {
                String name = results.getString( "Name" );
                int id = results.getInt( "ReportId" );
                if ( name.equalsIgnoreCase( p ) ) {
                    ids.add( id );
                }
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return ids;
    }
    
    public static List < String > getReportedPlayers( ){
        List < String > players = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Name FROM sc_reports" );
            ResultSet results = statement.executeQuery( );
            while (results.next( )) {
                String aDBName = results.getString( 1 );
                if ( !players.contains( aDBName ) ) {
                    players.add( aDBName );
                }
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return players;
    }
    
    public static void closeReport( int id ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_reports SET Status = 'close' WHERE ReportId=?" );
                statement.setInt( 1 , id );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    public static void openReport( int id ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_reports SET Status = 'open' WHERE ReportId=?" );
                statement.setInt( 1 , id );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    public static void deleteReport( int id ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_reports WHERE ReportId = ?" );
                statement.setInt( 1 , id );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    public static Integer wipePlayerReports( String name ){
        int bans = 0;
        try {
            PreparedStatement statement1 = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_reports WHERE Name LIKE ?" );
            statement1.setString( 1 , name );
            ResultSet rs1 = statement1.executeQuery( );
            while (rs1.next( )) {
                bans++;
            }
            PreparedStatement statement2 = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_reports WHERE Name LIKE ?" );
            statement2.setString( 1 , name );
            statement2.executeUpdate( );
            
        } catch ( SQLException ignored ) {
        }
        return bans;
    }
    
}
