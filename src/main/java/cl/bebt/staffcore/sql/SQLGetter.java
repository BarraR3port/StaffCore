package cl.bebt.staffcore.sql;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGetter {
    
    private static main plugin;
    
    public SQLGetter( main plugin ){
        SQLGetter.plugin = plugin;
    }
    
    public static String get( String tableName , int id , String row ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT " + row + " FROM sc_" + tableName + " WHERE ReportId=?" );
            statement.setInt( 1 , id );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( row );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return "Something went wrong :(";
    }
    
    public static String getBannedIp( int id ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT IP FROM sc_bans WHERE BanId=?" );
            statement.setInt( 1 , id );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( "IP" );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return "Something went wrong :(";
    }
    
    public static String getBanned( int id , String row ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT " + row + " FROM sc_bans WHERE BanId=?" );
            statement.setInt( 1 , id );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( row );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
            return "Something went wrong :(";
        }
        return "Something went wrong :(";
    }
    
    public static String getWarned( int id , String row ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT " + row + " FROM sc_warns WHERE WarnId=?" );
            statement.setInt( 1 , id );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( row );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
            return "Something went wrong :(";
        }
        return "Something went wrong :(";
    }
    
    public static boolean BansTableExists( ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_bans" );
            ResultSet results = statement.executeQuery( );
            return results.next( );
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
            return false;
        }
    }
    
    public static boolean WarnsTableExists( ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_warns" );
            ResultSet results = statement.executeQuery( );
            return results.next( );
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
            return false;
        }
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
    
    public static List < String > getBannedPlayers( ){
        List < String > players = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Name FROM sc_bans" );
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
    
    public static List < Integer > getPlayersIds( String p , String table , String typeId ){
        List < Integer > ids = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT " + typeId + ",Name FROM sc_" + table );
            ResultSet results = statement.executeQuery( );
            while (results.next( )) {
                String name = results.getString( "Name" );
                int id = results.getInt( typeId );
                if ( name.equalsIgnoreCase( p ) ) {
                    ids.add( id );
                }
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return ids;
    }
    
    public static String getReportStatus( int id ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Status FROM sc_reports WHERE ReportId=?" );
            statement.setInt( 1 , id );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( "Status" );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return "Something went wrong :(";
    }
    
    public static String getBanStatus( int id ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Status FROM sc_bans WHERE BanId=?" );
            statement.setInt( 1 , id );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( "Status" );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return "Something went wrong :(";
    }
    
    public static void createAlts( String player , String IP ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "INSERT INTO sc_alts(Name, IPs) VALUES (?,?)" );
                statement.setString( 1 , player );
                statement.setString( 2 , IP );
                statement.executeUpdate( );
                break;
            } catch ( SQLException throwable ) {
                main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
                main.plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Creating the sc_alts table into the database" ) );
                intento++;
            }
        }
    }
    
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
    
    public static void deleteReport( int id ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_reports WHERE sc_reports.ReportId = ?" );
                statement.setInt( 1 , id );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    public static void wipe( String player ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_flying WHERE sc_flying.Name = ?" );
            statement.setString( 1 , player );
            statement.executeUpdate( );
        } catch ( SQLException ignored ) {
        }
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
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_alts WHERE sc_alts.Name = ?" );
                statement.setString( 1 , name );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    public static void deleteWarns( String name ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_warns WHERE sc_warns.Name = ?" );
                statement.setString( 1 , name );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    public static void deleteBans( int id ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_bans WHERE sc_bans.BanId = ?" );
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
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "DELETE FROM sc_warns WHERE sc_warns.WarnId = ?" );
                statement.setInt( 1 , id );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    public static void createAltsTable( ){
        PreparedStatement ps;
        try {
            ps = Mysql.getConnection( ).prepareStatement( "CREATE TABLE IF NOT EXISTS sc_alts (Name varchar(100),Ips varchar(200),PRIMARY KEY (Name))" );
            ps.executeUpdate( );
        } catch ( SQLException ignored ) {
            ignored.printStackTrace( );
        }
        
    }
    
    //////////////////////////////NORMAL////////////////////////////////////////////////
    public static void createTable( String tableName ){
        PreparedStatement ps;
        try {
            ps = Mysql.getConnection( ).prepareStatement( "CREATE TABLE IF NOT EXISTS sc_" + tableName + " (Name varchar(100),Enabled varchar(100),PRIMARY KEY (Name))" );
            ps.executeUpdate( );
        } catch ( SQLException ignored ) {
            ignored.printStackTrace( );
        }
        
    }
    
    /**
     * This will create a ReportReport into the Mysql database
     **/
    public static void createReportTable( ){
        PreparedStatement ps;
        try {
            ps = Mysql.getConnection( ).prepareStatement( "CREATE TABLE IF NOT EXISTS sc_reports (ReportId int NOT NULL AUTO_INCREMENT,Name varchar(20) NOT NULL,Reporter varchar(20) NOT NULL,Reason varchar(100), Date varchar(20), Status varchar(10),PRIMARY KEY (ReportId))" );
            ps.executeUpdate( );
        } catch ( SQLException ignored ) {
            plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
            plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Not able to connect to the Database" ) );
            //ignored.printStackTrace();
        }
    }
    
    /**
     * This will create the BanTable into the Mysql database.
     **/
    public static void createBansTable( ){
        PreparedStatement ps;
        try {
            ps = Mysql.getConnection( ).prepareStatement( "CREATE TABLE IF NOT EXISTS sc_bans (BanId int NOT NULL AUTO_INCREMENT,Name varchar(20) NOT NULL,Baner varchar(20) NOT NULL,Reason varchar(100)NOT NULL,Date varchar(20)NOT NULL, ExpDate varchar(100)NOT NULL, IP varchar(100)NOT NULL, IP_Banned varchar(10)NOT NULL, Status varchar(10)NOT NULL,PRIMARY KEY (BanId));" );
            ps.executeUpdate( );
        } catch ( SQLException ignored ) {
            plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
            plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Not able to connect to the Database" ) );
            //ignored.printStackTrace();
        }
    }
    
    /**
     * This will create the BanTable into the Mysql database.
     **/
    public static void createWarnsTable( ){
        PreparedStatement ps;
        try {
            ps = Mysql.getConnection( ).prepareStatement( "CREATE TABLE IF NOT EXISTS sc_warns (WarnId int NOT NULL AUTO_INCREMENT,Name varchar(20) NOT NULL,Warner varchar(20) NOT NULL,Reason varchar(100)NOT NULL,Date varchar(20)NOT NULL, ExpDate varchar(100)NOT NULL, Status varchar(10)NOT NULL,PRIMARY KEY (WarnId));" );
            ps.executeUpdate( );
        } catch ( SQLException ignored ) {
            plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
            plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Not able to connect to the Database" ) );
            //ignored.printStackTrace();
        }
    }
    
    public static boolean PlayerExists( String tableName , String playerName ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_" + tableName + " WHERE Name=?" );
            statement.setString( 1 , playerName );
            ResultSet results = statement.executeQuery( );
            return results.next( );
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return false;
    }
    
    public static void createPlayer( Player player , String tableName , String bol ){
        try {
            if ( !(PlayerExists( tableName , player.getName( ) )) ) {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "INSERT INTO sc_" + tableName + " VALUE (?,?)" );
                statement.setString( 1 , player.getName( ) );
                statement.setString( 2 , bol );
                statement.executeUpdate( );
            }
        } catch ( SQLException throwable ) {
            
            throwable.printStackTrace( );
        }
    }
    
    public static void createPlayer( String player , String tableName , String bol ){
        try {
            if ( !(PlayerExists( tableName , player )) ) {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "INSERT INTO sc_" + tableName + " VALUE (?,?)" );
                statement.setString( 1 , player );
                statement.setString( 2 , bol );
                statement.executeUpdate( );
            }
        } catch ( SQLException throwable ) {
            
            throwable.printStackTrace( );
        }
    }
    
    public static String isTrue( Player player , String tableName ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Enabled FROM sc_" + tableName + " WHERE Name=?" );
            statement.setString( 1 , player.getName( ) );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( "Enabled" );
            }
        } catch ( SQLException ignored ) {
            if ( !PlayerExists( tableName , player.getName( ) ) ) {
                ignored.printStackTrace( );
                createPlayer( player , tableName , "false" );
            }
        }
        return "false";
    }
    
    public static String isTrue( String player , String tableName ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Enabled FROM sc_" + tableName + " WHERE Name=?" );
            statement.setString( 1 , player );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( "Enabled" );
            }
        } catch ( SQLException ignored ) {
            if ( !PlayerExists( tableName , player ) ) {
                ignored.printStackTrace( );
                createPlayer( player , tableName , "false" );
            }
        }
        return "false";
    }
    
    public static int getCurrents( String tableName ){
        int count = 0;
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_" + tableName );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                count++;
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return count;
    }
    
    public static int getCurrentWarns( String p ){
        int count = 0;
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_warns" );
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
    
    public static ArrayList < Integer > getBanIds( String p ){
        ArrayList < Integer > BanIDs = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_bans" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                String players = rs.getString( 2 );
                if ( players.equalsIgnoreCase( p ) ) {
                    BanIDs.add( rs.getInt( 1 ) );
                }
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return BanIDs;
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
    
    public static ArrayList < Integer > getWarnIds( ){
        ArrayList < Integer > WarnIds = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_warns" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                WarnIds.add( rs.getInt( 1 ) );
                
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return WarnIds;
    }
    
    public static int getWarnId( ){
        int id = 1;
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT WarnId FROM sc_warns" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                id = rs.getInt( 1 );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return id;
    }
    
    public static void setBan( int id , String Status ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_bans SET Status=? WHERE BanId=?" );
                statement.setString( 1 , Status );
                statement.setInt( 2 , id );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    public static void setWarn( int id , String Status ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_warns SET Status=? WHERE WarnId=?" );
                statement.setString( 1 , Status );
                statement.setInt( 2 , id );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
    
    public static void setTrue( String player , String tableName , String bol ){
        int intento = 0;
        while (intento < 4) {
            if ( PlayerExists( tableName , player ) ) {
                try {
                    PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_" + tableName + " SET Enabled=? WHERE Name=?" );
                    statement.setString( 1 , bol );
                    statement.setString( 2 , player );
                    statement.executeUpdate( );
                    break;
                } catch ( SQLException ignored ) {
                    intento++;
                }
            } else {
                createPlayer( player , tableName , bol );
                intento++;
            }
        }
    }
    
    /**
     * This will create a Report into the Report Table.
     **/
    public void createReport( String reported , String reporter , String Reason , String Date , String Status ){
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
                plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
                plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Creating the sc_reports table into the database" ) );
                createReportTable( );
                intento++;
                
            }
        }
    }
    
    /**
     * This will create a Ban into the Bans Table.
     **/
    public void createBan( String banned , String baner , String Reason , String Date , String ExpDate , String IP , String IP_Banned , String Status ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "INSERT INTO sc_bans (Name, Baner, Reason, Date, ExpDate, IP, IP_Banned, Status) VALUES (?,?,?,?,?,?,?,?)" );
                statement.setString( 1 , banned );
                statement.setString( 2 , baner );
                statement.setString( 3 , Reason );
                statement.setString( 4 , Date );
                statement.setString( 5 , ExpDate );
                statement.setString( 6 , IP );
                statement.setString( 7 , IP_Banned );
                statement.setString( 8 , Status );
                statement.executeUpdate( );
                break;
            } catch ( SQLException throwable ) {
                plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
                plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Creating the sc_bans table into the database" ) );
                createBansTable( );
                intento++;
            }
        }
    }
    
    /**
     * This will create a Warn into the Bans Table.
     **/
    public void createWarn( String warned , String warner , String Reason , String Date , String ExpDate , String Status ){
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
                plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] There has been an error with the mysql" ) );
                plugin.getServer( ).getConsoleSender( ).sendMessage( utils.chat( "&c[&5Staff Core&c] Creating the sc_warns table into the database" ) );
                createWarnsTable( );
                intento++;
            }
        }
    }
    
    public int getReportId( ){
        int id = 1;
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT ReportId FROM sc_reports" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                id = rs.getInt( 1 );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return id;
    }
    
    public int getBanId( ){
        int id = 1;
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT BanId FROM sc_bans" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                id = rs.getInt( 1 );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return id;
    }
    
    public void set( String tableName , int id , String Status ){
        int intento = 0;
        while (intento < 4) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_" + tableName + " SET Status=? WHERE ReportId=?" );
                statement.setString( 1 , Status );
                statement.setInt( 2 , id );
                statement.executeUpdate( );
                break;
            } catch ( SQLException ignored ) {
                intento++;
            }
        }
    }
}
