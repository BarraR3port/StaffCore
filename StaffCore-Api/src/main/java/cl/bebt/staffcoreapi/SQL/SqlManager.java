/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.SQL;

import cl.bebt.staffcoreapi.EntitiesUtils.SqlUtils;
import cl.bebt.staffcoreapi.SQL.Queries.AltsQuery;
import cl.bebt.staffcoreapi.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlManager {
    
    
    public static void createTable( String tableName ){
        PreparedStatement ps;
        try {
            ps = SqlUtils.getConnection( ).prepareStatement( "CREATE TABLE IF NOT EXISTS sc_" + tableName + " (Name varchar(100),Enabled varchar(100),PRIMARY KEY (Name))" );
            ps.executeUpdate( );
        } catch ( SQLException ignored ) {
            ignored.printStackTrace( );
        }
        
    }
    
    public static void createAltsTable( ){
        PreparedStatement ps;
        try {
            ps = SqlUtils.getConnection( ).prepareStatement( "CREATE TABLE IF NOT EXISTS sc_alts (UUID VARCHAR(36) PRIMARY KEY,Name varchar(100) ,Ips varchar(200), Skin varchar(1000) )" );
            ps.executeUpdate( );
        } catch ( SQLException ignored ) {
            ignored.printStackTrace( );
        }
        
    }
    
    /**
     * This will create a ReportReport into the SqlUtils database
     **/
    public static void createReportTable( ){
        PreparedStatement ps;
        try {
            ps = SqlUtils.getConnection( ).prepareStatement( "CREATE TABLE IF NOT EXISTS sc_reports (ReportId int NOT NULL AUTO_INCREMENT,Name varchar(20) NOT NULL,Reporter varchar(20) NOT NULL,Reason varchar(100), Date varchar(20), Status varchar(10),PRIMARY KEY (ReportId))" );
            ps.executeUpdate( );
        } catch ( SQLException ignored ) {
            Utils.tellConsole( "&c[&5Staff Core&c] There has been an error with the SQL" );
            Utils.tellConsole( "&c[&5Staff Core&c] Not able to connect to the Database" );
            ignored.printStackTrace( );
        }
    }
    
    /**
     * This will create the BanTable into the SqlUtils database.
     **/
    public static void createBansTable( ){
        PreparedStatement ps;
        try {
            ps = SqlUtils.getConnection( ).prepareStatement( "CREATE TABLE IF NOT EXISTS sc_bans (BanId int NOT NULL AUTO_INCREMENT,Name varchar(20) NOT NULL,Baner varchar(20) NOT NULL,Reason varchar(100)NOT NULL,Date varchar(20)NOT NULL, ExpDate varchar(100)NOT NULL, IP varchar(100)NOT NULL, IP_Banned varchar(10)NOT NULL, Status varchar(10)NOT NULL,PRIMARY KEY (BanId));" );
            ps.executeUpdate( );
        } catch ( SQLException ignored ) {
            Utils.tellConsole( "&c[&5Staff Core&c] There has been an error with the SQL" );
            Utils.tellConsole( "&c[&5Staff Core&c] Not able to connect to the Database" );
            ignored.printStackTrace( );
        }
    }
    
    /**
     * This will create the BanTable into the SqlUtils database.
     **/
    public static void createWarnsTable( ){
        PreparedStatement ps;
        try {
            ps = SqlUtils.getConnection( ).prepareStatement( "CREATE TABLE IF NOT EXISTS sc_warns (WarnId int NOT NULL AUTO_INCREMENT,Name varchar(20) NOT NULL,Warner varchar(20) NOT NULL,Reason varchar(100)NOT NULL,Date varchar(20)NOT NULL, ExpDate varchar(100)NOT NULL, Status varchar(10)NOT NULL,PRIMARY KEY (WarnId));" );
            ps.executeUpdate( );
        } catch ( SQLException ignored ) {
            Utils.tellConsole( "&c[&5Staff Core&c] There has been an error with the SQL" );
            Utils.tellConsole( "&c[&5Staff Core&c] Not able to connect to the Database" );
            ignored.printStackTrace( );
        }
    }
    
    /**
     * This will create the BanTable into the SqlUtils database.
     **/
    public static void createStaffCoreSettingsTable( ){
        PreparedStatement ps;
        try {
            ps = SqlUtils.getConnection( ).prepareStatement( "CREATE TABLE IF NOT EXISTS sc_settings ( ServerName VARCHAR(30) NOT NULL DEFAULT ?, Version VARCHAR(5) NOT NULL DEFAULT '4.5.0', IsUpdated BOOLEAN NOT NULL DEFAULT true)" );
            ps.setString( 1 , Utils.getServer( ) );
            ps.executeUpdate( );
        } catch ( SQLException ignored ) {
            Utils.tellConsole( "&c[&5Staff Core&c] There has been an error with the SQL" );
            Utils.tellConsole( "&c[&5Staff Core&c] Not able to connect to the Database" );
            ignored.printStackTrace( );
        }
    }
    
    public static void createTables( ){
        createTable( "vanish" );
        createTable( "staff" );
        createTable( "frozen" );
        createTable( "staffchat" );
        createAltsTable( );
        createReportTable( );
        createBansTable( );
        createWarnsTable( );
        Utils.runAsync( ( ) -> {
            createStaffCoreSettingsTable( );
            if ( !AltsQuery.checkAltsTable( ) ) {
                Utils.tellConsole( "&cHEY STAFFCORE IS MAKING SOME UPDATES IN THE DB, YOU CAN CHECK THE CHANGES HERE:" );
                Utils.tellConsole( "http://localhost:82/news/alts-migration" );
                AltsQuery.MigrateAltsTable( );
            }
        } );
        
    }
    
}
