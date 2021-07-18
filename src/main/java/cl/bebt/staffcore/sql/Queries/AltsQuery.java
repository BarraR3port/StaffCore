package cl.bebt.staffcore.sql.Queries;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Mysql;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.Http;
import cl.bebt.staffcore.utils.UUID.UUIDGetter;
import cl.bebt.staffcore.utils.utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AltsQuery {
    
    
    public static String getAlts( String player ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Ips FROM sc_alts WHERE Name=?" );
            statement.setString( 1 , player );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( "Ips" );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return "Something went wrong :(";
    }
    
    public static void createAlts( String player , String IP , String uuid ){
        int intento = 0;
        while (intento < 4) {
            try {
                String Skin = Http.getHead( "https://staffcore.glitch.me/api/head/" + player , player );
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "INSERT INTO sc_alts(Name, IPs, UUID, Skin) VALUES (?,?,?,?)" );
                statement.setString( 1 , player );
                statement.setString( 2 , IP );
                statement.setString( 3 , uuid );
                statement.setString( 4 , Skin );
                statement.executeUpdate( );
                main.playerSkins.put( player , Skin );
                break;
            } catch ( SQLException throwable ) {
                main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
                main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Creating the sc_alts table into the database" ) );
                intento++;
                throwable.printStackTrace( );
            }
        }
    }
    
    public static boolean PlayerExists( String playerName ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_alts WHERE Name=?" );
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
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Name FROM sc_alts" );
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
    
    public static void addIps( String player , String Ips ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_alts SET Ips=? WHERE Name=?" );
                statement.setString( 1 , Ips );
                statement.setString( 2 , player );
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
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Ips FROM sc_alts Where Name=(?)" );
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
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_frozen WHERE sc_frozen.Name = ?" );
            statement.setString( 1 , player );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_staff WHERE sc_staff.Name = ?" );
            statement.setString( 1 , player );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_staffchat WHERE sc_staffchat.Name = ?" );
            statement.setString( 1 , player );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_vanish WHERE sc_vanish.Name = ?" );
            statement.setString( 1 , player );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
        
    }
    
    
    public static void deleteAlts( String name ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_alts WHERE Name = ?" );
            statement.setString( 1 , name );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
    }
    
    public static boolean checkAltsTable( ){
        PreparedStatement ps;
        try {
            ps = Mysql.getConnection( ).prepareStatement( "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='sc_alts' AND column_name='Skin'" );
            ResultSet rs = ps.executeQuery( );
            return rs.next( );
        } catch ( SQLException ignored ) {
            main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
            main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Not able to connect to the Database" ) );
            ignored.printStackTrace( );
        }
        return false;
    }
    
    public static JSONObject getAlts( ){
        JSONObject json = new JSONObject( );
        int count = 1;
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_alts" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                String Name = rs.getString( "Name" );
                String Ips = rs.getString( "Ips" );
                UUID uuid = UUIDGetter.getUUID( Name );
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
    
    public static void DropOldAltsTable( ){
        PreparedStatement ps;
        try {
            ps = Mysql.getConnection( ).prepareStatement( "DROP TABLE sc_alts" );
            ps.executeUpdate( );
            SQLGetter.createAltsTable( );
        } catch ( SQLException ignored ) {
            main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
            main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Not able to connect to the Database" ) );
            ignored.printStackTrace( );
        }
    }
    
    public static void addAlts( ){
        JSONObject json = getAlts( );
        int length = json.getInt( "total" );
        for ( int i = 1; i <= length; i++ ) {
            String rawAlts = json.get( String.valueOf( i ) ).toString( )
                    .replace( "[" , "" )
                    .replace( "]" , "" );
            
            JSONObject alts = new JSONObject( rawAlts );
            String Name = alts.getString( "Name" );
            String uuid = alts.getString( "UUID" );
            String Ips = alts.getString( "IP" );
            String Skin = Http.getHead( "https://staffcore.glitch.me/api/head/" + Name , Name );
            try {
                PreparedStatement statement;
                statement = Mysql.getConnection( ).prepareStatement( "INSERT INTO sc_alts(UUID, Name, IPs, Skin) VALUES (?,?,?,?)" );
                statement.setString( 1 , uuid );
                statement.setString( 2 , Name );
                statement.setString( 3 , Ips );
                statement.setString( 4 , Skin );
                statement.executeUpdate( );
            } catch ( SQLException ignored ) {
                main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
                main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Not able to connect to the Database" ) );
                ignored.printStackTrace( );
            }
        }
        
    }
    
    
}
