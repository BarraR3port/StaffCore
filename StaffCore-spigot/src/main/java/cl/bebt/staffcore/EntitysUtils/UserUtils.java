/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.EntitysUtils;

import cl.bebt.staffcore.Entitys.User;
import cl.bebt.staffcore.Exeptions.PlayerNotFundException;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.Http;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

public class UserUtils {
    
    private static ArrayList < User > users = new ArrayList <>( );
    
    public static ArrayList < User > getUsers( ){
        return users;
    }
    
    public static void createUser( Player p ){
        if ( !isSaved( p.getUniqueId( ) ) ) {
            HashMap < String, String > alts = new HashMap <>( );
            alts.put( p.getName( ) , p.getAddress( ).getAddress( ).toString( ).replace( "/" , "" ) );
            User user = new User( p.getName( ) , Http.getHead( "http://localhost:82/api/head/" + p.getName( ) , p.getName( ) ) , alts );
            users.add( user );
            saveUsers( );
        }
    }
    
    public static void createUser( String name , UUID uuid , String ip ){
        if ( !isSaved( uuid ) ) {
            HashMap < String, String > alts = new HashMap <>( );
            alts.put( name , ip );
            User user = new User( name , Http.getHead( "http://localhost:82/api/head/" + name , name ) , alts );
            users.add( user );
            saveUsers( );
        }
        
    }
    
    public static void loadUser( User user ){
        users.add( user );
    }
    
    public static User findUser( UUID uuid ){
        for ( User user : users ) {
            if ( user.getUUID( ).equals( uuid ) ) {
                return user;
            }
        }
        return null;
    }
    
    public static User findUser( Player player ){
        for ( User user : users ) {
            if ( user.getUUID( ).equals( player.getUniqueId( ) ) ) {
                return user;
            }
        }
        return null;
    }
    
    public static boolean isSaved( UUID uuid ){
        try {
            for ( User user : users ) {
                if ( user.getUUID( ).equals( uuid ) ) {
                    return true;
                }
            }
        } catch ( NullPointerException ignored ) {
        }
        return false;
    }
    
    public static boolean isSaved( String name ){
        for ( User user : users ) {
            if ( user.getName( ).equals( name ) ) {
                return true;
            }
        }
        return false;
    }
    
    public static void deleteUser( UUID uuid ){
        users.removeIf( user -> user.getUUID( ).equals( uuid ) );
        saveUsers( );
    }
    
    public static void updateUser( UUID uuid , User user ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setAlts( user.getAlts( ) );
                    u.setName( user.getName( ) );
                    u.setStaffChat( user.getStaffChat( ) );
                    u.setSkin( user.getSkin( ) );
                    u.setStaff( user.getStaff( ) );
                    u.setFly( user.getFly( ) );
                    u.setReports( user.getReports( ) );
                    u.setStaffArmor( user.getStaffArmor( ) );
                    u.setStaff( user.getStaff( ) );
                    u.setFreezeHelmet( user.getFreezeHelmet( ) );
                    u.setWarns( user.getWarns( ) );
                    u.setBans( user.getBans( ) );
                    u.setBan( user.getBan( ) );
                    u.setVanish( user.getVanish( ) );
                    u.setFakeJoinLeave( user.getFakeJoinLeave( ) );
                    u.setMutes( user.getMutes( ) );
                    u.setMute( user.getMute( ) );
                    u.setFrozen( user.getFrozen( ) );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void updateUser( User user ){
        users.removeIf( u -> u.getUUID( ).equals( user.getUUID( ) ) );
        users.add( user );
        saveUsers( );
    }
    
    public static void saveUsers( ){
        try {
            Gson gson = new Gson( );
            File file = new File( main.plugin.getDataFolder( ).getAbsolutePath( ) + "/alts.json" );
            file.getParentFile( ).mkdir( );
            file.createNewFile( );
            Writer writer = new FileWriter( file , false );
            gson.toJson( users , writer );
            writer.flush( );
            writer.close( );
        } catch ( IOException error ) {
            error.printStackTrace( );
        }
    }
    
    public static void loadUsers( ){
        try {
            Gson gson = new Gson( );
            File file = new File( main.plugin.getDataFolder( ).getAbsolutePath( ) + "/alts.json" );
            if ( file.exists( ) ) {
                Reader reader = new FileReader( file );
                User[] user = gson.fromJson( reader , User[].class );
                users = new ArrayList <>( Arrays.asList( user ) );
            }
        } catch ( IOException error ) {
            error.printStackTrace( );
        }
    }
    
    public static void setStaff( UUID uuid , Boolean bol ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setStaff( bol );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void setStaffInventory( UUID uuid , String inventory ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setStaffInventory( inventory );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void setStaffArmor( UUID uuid , String armor ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setStaffArmor( armor );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void setFreezeHelmet( UUID uuid , String helmet ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setFreezeHelmet( helmet );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void setVanish( UUID uuid , Boolean bol ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setVanish( bol );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void setFrozen( UUID uuid , Boolean bol ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setFrozen( bol );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void setFly( UUID uuid , Boolean bol ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setFly( bol );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void setMutedTime( UUID uuid , Double time ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setMutedTime( time );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void setStaffChat( UUID uuid , Boolean bol ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setStaffChat( bol );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void setBan( UUID uuid , Boolean bol ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setBan( bol );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void setMute( UUID uuid , Boolean bol ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setMute( bol );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void addReport( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setReports( u.getReports( ) + 1 );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void addBans( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setBans( u.getBans( ) + 1 );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void addWarns( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setWarns( u.getWarns( ) + 1 );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void addMutes( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setMutes( u.getMutes( ) + 1 );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void removeReport( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setReports( u.getReports( ) - 1 );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void removeBans( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setBans( u.getBans( ) - 1 );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void removeWarns( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setWarns( u.getWarns( ) - 1 );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static void removeMutes( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setMutes( u.getMutes( ) - 1 );
                    saveUsers( );
                }
            }
        } else {
            throw new PlayerNotFundException( "&4The UUID: " + uuid.toString( ) + " couldn't be found" );
        }
    }
    
    public static String getSkin( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getSkin( );
        } else {
            throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
        }
    }
    
    public static Boolean getVanish( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getVanish( );
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static Boolean getStaff( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getStaff( );
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static Boolean getFrozen( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getFrozen( );
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static Boolean getFly( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getFly( );
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static String getStaffInventory( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getStaffInventory( );
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static String getStaffArmor( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getStaffArmor( );
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static String getFreezeHelmet( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getFreezeHelmet( );
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static Boolean getFakeJoinLeave( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getFakeJoinLeave( );
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static Boolean getMute( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getMute( );
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static Boolean getStaffChat( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getStaffChat( );
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static Double getMutedTime( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getMutedTime( );
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static String getIps( UUID uuid ) throws PlayerNotFundException{
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            HashMap < String, String > alts = user.getAlts( );
            String ips = "";
            for ( String s : alts.values( ) ) {
                ips = s + ",";
            }
            return ips;
        }
        throw new PlayerNotFundException( "The Player with the UUID: " + uuid + " could not be found" );
    }
    
    public static Boolean isInOtherIP( User user , String ip ){
        return !user.getAlts( ).containsValue( ip );
    }
    
    public static void isIPSaved( User user , String ip ){
        for ( User users : users ) {
            if ( users.getAlts( ).containsValue( ip ) ) {
                addAlt( user , users , ip );
            }
        }
    }
    
    public static void addAlt( User user , String ip ){
        if ( ip.equals( "127.0.0.1" ) ) return;
        HashMap < String, String > alts = user.getAlts( );
        alts.put( user.getName( ) , ip );
        user.setAlts( alts );
        updateUser( user );
        
    }
    
    public static void addAlt( User user , User alt , String ip ){
        if ( Objects.equals( user , alt ) ) return;
        if ( isSaved( user.getUUID( ) ) ) {
            Bukkit.broadcastMessage( "Adding alt of:" + user.getName( ) + " with " + alt.getName( ) );
            HashMap < String, String > alts1 = user.getAlts( );
            alts1.put( alt.getName( ) , ip );
            user.setAlts( alts1 );
            updateUser( user.getUUID( ) , user );
            Bukkit.broadcastMessage( "Adding alt of:" + alt.getName( ) + " with " + user.getName( ) );
            HashMap < String, String > alts2 = alt.getAlts( );
            alts2.put( user.getName( ) , ip );
            alt.setAlts( alts2 );
            updateUser( alt.getUUID( ) , alt );
        }
    }
    
    public static UUID getUUIDFromName( String name ){
        for ( User user : users ) {
            if ( user.getName( ).equals( name ) ) {
                return user.getUUID( );
            }
        }
        return null;
    }
    
    
}
