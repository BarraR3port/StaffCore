package cl.bebt.staffcore.sql.Queries;

import cl.bebt.staffcore.sql.Mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VanishQuery {
    
    public static void enable( String player ){
        if ( AltsQuery.PlayerExists( player ) ) {
            try {
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_vanish SET Enabled=? WHERE Name=?" );
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
                PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "UPDATE sc_vanish SET Enabled=? WHERE Name=?" );
                statement.setString( 1 , "false" );
                statement.setString( 2 , player );
                statement.executeUpdate( );
            } catch ( SQLException ignored ) {
            
            }
        }
    }
    
    public static List < String > getVanishedPlayers( ){
        List < String > players = new ArrayList <>( );
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT * FROM sc_vanish where Enabled = 'True'" );
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
    
    public static String isVanished( String player ){
        try {
            PreparedStatement statement = Mysql.getConnection( ).prepareStatement( "SELECT Enabled FROM sc_vanish WHERE Name=?" );
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
