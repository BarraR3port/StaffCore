package cl.bebt.staffcore.sql.Queries;

import cl.bebt.staffcore.sql.Mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FreezeQuery {
    
    
    public static void enable( String player ){
        if ( AltsQuery.PlayerExists( player ) ) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_frozen SET Enabled=? WHERE Name=?" );
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
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_frozen SET Enabled=? WHERE Name=?" );
                statement.setString( 1 , "false" );
                statement.setString( 2 , player );
                statement.executeUpdate( );
            } catch ( SQLException ignored ) {
            
            }
        }
    }
    
    public static String isFrozen( String player ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Enabled FROM sc_frozen WHERE Name=?" );
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
