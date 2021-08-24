/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.SQL.Queries;


import cl.bebt.staffcoreapi.EntitiesUtils.SqlUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;

public class ServerQuery {
    
    public static HashMap < String, Integer > getServerStatus( ){
        HashMap < String, Integer > serverStatus = new HashMap <>( );
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT (SELECT COUNT(bans.Name) FROM sc_alts LEFT JOIN sc_bans bans ON bans.Name = sc_alts.Name ) AS CurrentBans,(SELECT COUNT(reports.Name) FROM sc_alts LEFT JOIN sc_reports reports ON reports.Name = sc_alts.Name ) AS CurrentReports,(SELECT COUNT(warns.Name) FROM sc_alts LEFT JOIN sc_warns warns ON warns.Name = sc_alts.Name ) AS CurrentWarns,(SELECT COUNT(UUID) FROM sc_alts) AS CurrentPlayers,(SELECT COUNT(frozen.Name) FROM sc_alts LEFT JOIN sc_frozen frozen ON frozen.Name = sc_alts.Name ) AS CurrentFrozen,(SELECT COUNT(staff.Name) FROM sc_alts LEFT JOIN sc_staff staff ON staff.Name = sc_alts.Name ) AS CurrentStaff,(SELECT COUNT(vanish.Name) FROM sc_alts LEFT JOIN sc_vanish vanish ON vanish.Name = sc_alts.Name ) AS CurrentVanished" );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                serverStatus.put( "CurrentBans" , rs.getInt( "CurrentBans" ) );
                serverStatus.put( "CurrentReports" , rs.getInt( "CurrentReports" ) );
                serverStatus.put( "CurrentWarns" , rs.getInt( "CurrentWarns" ) );
                serverStatus.put( "CurrentPlayers" , rs.getInt( "CurrentPlayers" ) );
                serverStatus.put( "CurrentFrozen" , rs.getInt( "CurrentFrozen" ) );
                serverStatus.put( "CurrentStaff" , rs.getInt( "CurrentStaff" ) );
                serverStatus.put( "CurrentVanished" , rs.getInt( "CurrentVanished" ) );
            }
        } catch ( SQLException throwable ) {
            throwable.printStackTrace( );
        }
        return serverStatus;
    }
    
    public static HashMap < String, Integer > getPlayerStatus( String p ){
        HashMap < String, Integer > serverStatus = new HashMap <>( );
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT (SELECT COUNT(bans.Name) FROM sc_alts LEFT JOIN sc_bans bans ON bans.Name = sc_alts.Name WHERE bans.Name LIKE ?) AS CurrentBans,(SELECT COUNT(warns.Name) FROM sc_alts LEFT JOIN sc_warns warns ON warns.Name = sc_alts.Name WHERE warns.Name LIKE ? )  AS CurrentWarns,(SELECT COUNT(reports.Name) FROM sc_alts LEFT JOIN sc_reports reports ON reports.Name = sc_alts.Name WHERE reports.Name LIKE ? ) AS CurrentReports" );
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
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT Name, Skin FROM sc_alts" );
            ResultSet rs = statement.executeQuery( );
            while (rs.next( )) {
                skins.put( rs.getString( "Name" ) , rs.getString( "Skin" ) );
            }
        } catch ( SQLException throwable ) {
            if ( !throwable.getCause( ).equals( new SQLSyntaxErrorException( ) ) ) {
                throwable.printStackTrace( );
            }
        }
        return skins;
    }
    
}
