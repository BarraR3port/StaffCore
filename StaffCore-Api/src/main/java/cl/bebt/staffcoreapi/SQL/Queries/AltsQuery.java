/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.SQL.Queries;

import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.Entities.User;
import cl.bebt.staffcoreapi.EntitiesUtils.SqlUtils;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.SQL.SqlManager;
import cl.bebt.staffcoreapi.utils.DataExporter;
import cl.bebt.staffcoreapi.utils.Http;
import cl.bebt.staffcoreapi.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AltsQuery {
    
    
    public static String getAltsIps( UUID uuid ){
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT Ips FROM sc_alts WHERE UUID=?" );
            statement.setString( 1 , uuid.toString( ) );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( "Ips" );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return "Something went wrong :(";
    }
//TODO: CREATE THIS FUNCTION AND MAKE IT WORK.
/*
    public static String getAltsNames( UUID uuid ){
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT Ips FROM sc_alts WHERE UUID=?" );
            statement.setString( 1 , uuid.toString() );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( "Ips" );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return "Something went wrong :(";
    }
*/
    
    public static void createAlts( User user , String ip ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "INSERT INTO sc_alts(Name, IPs, UUID, Skin) VALUES (?,?,?,?)" );
                statement.setString( 1 , user.getName( ) );
                statement.setString( 2 , ip );
                statement.setString( 3 , user.getUUID( ).toString( ) );
                statement.setString( 4 , user.getSkin( ) );
                statement.executeUpdate( );
                Api.playerSkins.put( user.getName( ) , user.getSkin( ) );
                DataExporter.updateServerStats( UpdateType.PLAYER );
                break;
            } catch ( SQLException throwable ) {
                Utils.tellConsole( "&c[&5Staff Core&c] There has been an error with the mysql" );
                Utils.tellConsole( "&c[&5Staff Core&c] Creating the sc_alts table into the database" );
                intento++;
                throwable.printStackTrace( );
            }
        }
    }
    
    public static boolean PlayerExists( String playerName ){
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT * FROM sc_alts WHERE Name=?" );
            statement.setString( 1 , playerName );
            ResultSet results = statement.executeQuery( );
            return results.next( );
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return false;
    }
    
    
    public static List < String > getPlayersNames( ){
        List < String > players = new ArrayList <>( );
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT Name FROM sc_alts" );
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
    
    public static void addIps( String uuid , String Ips ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "UPDATE sc_alts SET Ips=? WHERE UUID=?" );
                statement.setString( 1 , Ips );
                statement.setString( 2 , uuid );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
                ignored.printStackTrace( );
            }
        }
    }
    
    public static String getIp( String name ){
        String IP = "";
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT Ips FROM sc_alts Where Name=(?)" );
            statement.setString( 1 , name );
            ResultSet results = statement.executeQuery( );
            while (results.next( )) {
                IP = results.getString( 1 );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return IP;
    }
    
    public static void wipe( String player ){
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "DELETE FROM sc_frozen WHERE sc_frozen.Name = ?" );
            statement.setString( 1 , player );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "DELETE FROM sc_staff WHERE sc_staff.Name = ?" );
            statement.setString( 1 , player );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "DELETE FROM sc_staffchat WHERE sc_staffchat.Name = ?" );
            statement.setString( 1 , player );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "DELETE FROM sc_vanish WHERE sc_vanish.Name = ?" );
            statement.setString( 1 , player );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
        
    }
    
    
    public static void deleteAlts( String name ){
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "DELETE FROM sc_alts WHERE Name = ?" );
            statement.setString( 1 , name );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
    }
    
    public static boolean checkAltsTable( ){
        PreparedStatement ps;
        try {
            ps = SqlUtils.getConnection( ).prepareStatement( "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='sc_alts' AND column_name='Skin'" );
            ResultSet rs = ps.executeQuery( );
            return rs.next( );
        } catch ( SQLException ignored ) {
            Utils.tellConsole( "&c[&5Staff Core&c] There has been an error with the mysql" );
            Utils.tellConsole( "&c[&5Staff Core&c] Not able to connect to the Database" );
            ignored.printStackTrace( );
        }
        return false;
    }
    
    public static JSONObject getAltsIps( ){
        JSONObject json = new JSONObject( );
        int count = 1;
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT * FROM sc_alts" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                String Name = rs.getString( "Name" );
                String Ips = rs.getString( "Ips" );
                UUID uuid = Utils.getUUID( Name );
                JSONArray senderArray = new JSONArray( );
                JSONObject senderDetail = new JSONObject( );
                senderDetail.put( "Name" , Name );
                senderDetail.put( "IP" , Ips );
                senderDetail.put( "UUID" , uuid );
                senderArray.put( senderDetail );
                json.put( String.valueOf( count ) , senderArray );
                count++;
            }
            json.put( "total" , count - 1 );
        } catch ( SQLException error ) {
            error.printStackTrace( );
        }
        return json;
    }
    
    public static void MigrateAltsTable( ){
        PreparedStatement ps;
        try {
            JSONObject alts = getAltsIps( );
            ps = SqlUtils.getConnection( ).prepareStatement( "DROP TABLE sc_alts" );
            ps.executeUpdate( );
            SqlManager.createAltsTable( );
            addAlts( alts );
        } catch ( SQLException ignored ) {
            Utils.tellConsole( "&c[&5Staff Core&c] There has been an error with the mysql" );
            Utils.tellConsole( "&c[&5Staff Core&c] Not able to connect to the Database" );
            ignored.printStackTrace( );
        }
    }
    
    public static void addAlts( JSONObject json ){
        int length = json.getInt( "total" );
        for ( int i = 1; i <= length; i++ ) {
            String rawAlts = json.get( String.valueOf( i ) ).toString( )
                    .replace( "[" , "" )
                    .replace( "]" , "" );
            
            JSONObject alts = new JSONObject( rawAlts );
            String Name = alts.getString( "Name" );
            String uuid = alts.getString( "UUID" );
            String Ips = alts.getString( "IP" );
            String Skin = Http.getSkin( "http://localhost:82/api/head/" + Name , Name );
            try {
                PreparedStatement statement;
                statement = SqlUtils.getConnection( ).prepareStatement( "INSERT INTO sc_alts(UUID, Name, IPs, Skin) VALUES (?,?,?,?)" );
                statement.setString( 1 , uuid );
                statement.setString( 2 , Name );
                statement.setString( 3 , Ips );
                statement.setString( 4 , Skin );
                statement.executeUpdate( );
            } catch ( SQLException ignored ) {
                Utils.tellConsole( "&c[&5Staff Core&c] There has been an error with the mysql" );
                Utils.tellConsole( "&c[&5Staff Core&c] Not able to connect to the Database" );
                ignored.printStackTrace( );
            }
        }
        
    }
    
    public static String getNameByUUID( UUID uuid ){
        String name = "";
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT Name FROM sc_alts Where UUID=(?)" );
            statement.setString( 1 , uuid.toString( ) );
            ResultSet results = statement.executeQuery( );
            while (results.next( )) {
                name = results.getString( 1 );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return name;
    }
    
    public static UUID getUUIDByName( String name ){
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT UUID FROM sc_alts Where Name=(?)" );
            statement.setString( 1 , name );
            ResultSet results = statement.executeQuery( );
            if ( results.next( ) ) {
                return UUID.fromString( results.getString( 1 ) );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return null;
    }
    
    
}
