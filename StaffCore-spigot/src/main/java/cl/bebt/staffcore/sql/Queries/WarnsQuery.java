/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.sql.Queries;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Mysql;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WarnsQuery {
    
    /**
     * This will create a Warn into the Bans Table.
     **/
    public static void createWarn( String warned , String warner , String Reason , String Date , String ExpDate , String Status ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "INSERT INTO sc_warns (Name, Warner, Reason, Date, ExpDate, Status) VALUES (?,?,?,?,?,?)" );
                statement.setString( 1 , warned );
                statement.setString( 2 , warner );
                statement.setString( 3 , Reason );
                statement.setString( 4 , Date );
                statement.setString( 5 , ExpDate );
                statement.setString( 6 , Status );
                statement.executeUpdate( );
                break;
            } catch ( SQLException throwable ) {
                main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
                main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Creating the sc_warns table into the database" ) );
                SQLGetter.createWarnsTable( );
                intento++;
            }
        }
    }
    
    
    public static JSONObject getWarnsInfo( int id ){
        JSONObject json = new JSONObject( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_warns WHERE WarnId LIKE " + id );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                json.put( "error" , false );
                json.put( "WarnId" , rs.getString( "WarnId" ) );
                json.put( "Name" , rs.getString( "Name" ) );
                json.put( "Warner" , rs.getString( "Warner" ) );
                json.put( "Reason" , rs.getString( "Reason" ) );
                json.put( "Date" , rs.getString( "Date" ) );
                json.put( "ExpDate" , rs.getString( "ExpDate" ) );
                json.put( "Status" , rs.getString( "Status" ) );
            }
        } catch ( SQLException error ) {
            error.printStackTrace( );
            json.put( "error" , true );
        }
        return json;
    }
    
    public static JSONObject getPlayerWarnsInfo( String warned ){
        JSONObject json = new JSONObject( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_warns WHERE Name LIKE ? order by WarnId" );
            statement.setString( 1 , warned );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                int WarnId = rs.getInt( "WarnId" );
                String Name = rs.getString( "Name" );
                String Warner = rs.getString( "Warner" );
                String Reason = rs.getString( "Reason" );
                String Date = rs.getString( "Date" );
                String ExpDate = rs.getString( "ExpDate" );
                String Status = rs.getString( "Status" );
                
                JSONArray senderArray = new JSONArray( );
                JSONObject senderDetail = new JSONObject( );
                senderDetail.put( "Name" , Name );
                senderDetail.put( "Warner" , Warner );
                senderDetail.put( "Reason" , Reason );
                senderDetail.put( "Date" , Date );
                senderDetail.put( "ExpDate" , ExpDate );
                java.util.Date now = new Date( );
                Date exp_date = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" ).parse( rs.getString( "ExpDate" ) );
                if ( now.after( exp_date ) ) {
                    senderDetail.put( "Status" , "closed" );
                    Bukkit.getScheduler( ).runTaskAsynchronously( main.plugin , ( ) -> closeWarn( WarnId ) );
                } else {
                    senderDetail.put( "Status" , Status );
                }
                
                senderArray.put( senderDetail );
                json.put( String.valueOf( WarnId ) , senderArray );
            }
        } catch ( SQLException | ParseException error ) {
            error.printStackTrace( );
        }
        return json;
    }
    
    public static List < String > getWarnedPlayers( ){
        List < String > players = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Name FROM sc_warns" );
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
    
    public static int getCurrentWarns( ){
        int currentWarns = 0;
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT count(*) AS CurrentWarns FROM sc_warns" );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                currentWarns = rs.getInt( "CurrentWarns" );
            }
        } catch ( SQLException error ) {
            error.printStackTrace( );
        }
        return currentWarns;
    }
    
    public static Boolean isStillWarned( int id ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT ExpDate, Status FROM sc_warns WHERE WarnId=?" );
            statement.setInt( 1 , id );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                Date now = new Date( );
                Date exp_date = (new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" )).parse( rs.getString( "ExpDate" ) );
                if ( now.after( exp_date ) ) {
                    closeWarn( id );
                    return false;
                }
                return rs.getString( "Status" ).equalsIgnoreCase( "open" );
            } else {
                return false;
            }
        } catch ( SQLException | ParseException error ) {
            error.printStackTrace( );
            return false;
        }
    }
    
    public static ArrayList < Integer > getPlayerWarns( String warned ){
        ArrayList < Integer > playerWarns = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT WarnId FROM sc_warns WHERE Name = ? order by WarnId" );
            statement.setString( 1 , warned );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                playerWarns.add( rs.getInt( "WarnId" ) );
            }
        } catch ( SQLException error ) {
            error.printStackTrace( );
        }
        return playerWarns;
    }
    
    public static void closeWarn( int id ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_warns SET Status = 'closed' WHERE WarnId=?" );
                statement.setInt( 1 , id );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    public static void openWarn( int id ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_warns SET Status = 'open' WHERE WarnId=?" );
                statement.setInt( 1 , id );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    
    public static void deleteWarn( int id ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_warns WHERE WarnId = ?" );
                statement.setInt( 1 , id );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    public static void deleteWarns( String name ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_warns WHERE Name = ?" );
            statement.setString( 1 , name );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
    }
    
    public static ArrayList < Integer > getWarnIds( String p ){
        ArrayList < Integer > WarnIds = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_warns" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                String players = rs.getString( 2 );
                if ( players.equalsIgnoreCase( p ) ) {
                    WarnIds.add( rs.getInt( 1 ) );
                }
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return WarnIds;
    }
    
}
