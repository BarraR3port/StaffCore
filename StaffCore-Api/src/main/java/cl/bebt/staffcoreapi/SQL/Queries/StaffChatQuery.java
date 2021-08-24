/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.SQL.Queries;

import cl.bebt.staffcoreapi.EntitiesUtils.SqlUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffChatQuery {
    
    
    public static void enable( String player ){
        if ( AltsQuery.PlayerExists( player ) ) {
            try {
                PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "UPDATE sc_staffchat SET Enabled=? WHERE Name=?" );
                statement.setString( 1 , "true" );
                statement.setString( 2 , player );
                statement.executeUpdate( );
            } catch ( SQLException ignored ) {
            
            }
        }
    }
    
    public static void disable( String player ){
        if ( AltsQuery.PlayerExists( player ) ) {
            try {
                PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "UPDATE sc_staffchat SET Enabled=? WHERE Name=?" );
                statement.setString( 1 , "false" );
                statement.setString( 2 , player );
                statement.executeUpdate( );
            } catch ( SQLException ignored ) {
            
            }
        }
    }
    
    public static String isStaffChat( String player ){
        try {
            PreparedStatement statement = SqlUtils.getConnection( ).prepareStatement( "SELECT Enabled FROM sc_staff WHERE Name=?" );
            statement.setString( 1 , player );
            ResultSet rs = statement.executeQuery( );
            if ( rs.next( ) ) {
                return rs.getString( "Enabled" );
            }
        } catch ( SQLException ignored ) {
            if ( !AltsQuery.PlayerExists( player ) ) {
                ignored.printStackTrace( );
            }
        }
        return "false";
    }
}
