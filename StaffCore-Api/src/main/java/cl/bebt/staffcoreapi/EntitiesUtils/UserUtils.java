/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.EntitiesUtils;

import cl.bebt.staffcoreapi.Entities.User;
import cl.bebt.staffcoreapi.utils.Http;
import cl.bebt.staffcoreapi.utils.Utils;
import com.google.gson.Gson;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.*;
import java.util.*;

public class UserUtils {
    
    private static ArrayList < User > users = new ArrayList <>( );
    
    public static ArrayList < User > getUsers( ){
        return users;
    }
    
    public static User createUser( String name , UUID uuid , String ip ){
        if ( !isSaved( uuid ) ) {
            HashMap < String, String > alts = new HashMap <>( );
            alts.put( name , ip );
            User user = new User( name , Http.getSkin( "http://localhost:82/api/head/" + name , name ) , alts );
            users.add( user );
            saveUsers( );
            return user;
        }
        return findUser( uuid );
        
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
    
    public static User findUser( ProxiedPlayer player ){
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
            File file = new File( Utils.getDataFolder( ) + "/alts.json" );
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
            File file = new File( Utils.getDataFolder( ) + "/alts.json" );
            if ( file.exists( ) ) {
                Reader reader = new FileReader( file );
                User[] user = gson.fromJson( reader , User[].class );
                users = new ArrayList <>( Arrays.asList( user ) );
            } else {
                saveUsers( );
            }
        } catch ( IOException error ) {
            error.printStackTrace( );
        }
    }
    
    public static void setStaff( UUID uuid , Boolean bol ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setStaff( bol );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void setStaffInventory( UUID uuid , String inventory ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setStaffInventory( inventory );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void setStaffArmor( UUID uuid , String armor ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setStaffArmor( armor );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void setFreezeHelmet( UUID uuid , String helmet ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setFreezeHelmet( helmet );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void setVanish( UUID uuid , Boolean bol ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setVanish( bol );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void setFrozen( UUID uuid , Boolean bol ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setFrozen( bol );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void setFly( UUID uuid , Boolean bol ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setFly( bol );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void setMutedTime( UUID uuid , Double time ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setMutedTime( time );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void setStaffChat( UUID uuid , Boolean bol ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setStaffChat( bol );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void setBan( UUID uuid , Boolean bol ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setBan( bol );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void setMute( UUID uuid , Boolean bol ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setMute( bol );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void addReport( UUID uuid ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setReports( u.getReports( ) + 1 );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void addBans( UUID uuid ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setBans( u.getBans( ) + 1 );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void addWarns( UUID uuid ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setWarns( u.getWarns( ) + 1 );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void addMutes( UUID uuid ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setMutes( u.getMutes( ) + 1 );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void removeReport( UUID uuid ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setReports( u.getReports( ) - 1 );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void removeBans( UUID uuid ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setBans( u.getBans( ) - 1 );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void removeWarns( UUID uuid ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setWarns( u.getWarns( ) - 1 );
                    saveUsers( );
                }
            }
        }
    }
    
    public static void removeMutes( UUID uuid ){
        if ( isSaved( uuid ) ) {
            for ( User u : users ) {
                if ( u.getUUID( ).equals( uuid ) ) {
                    u.setMutes( u.getMutes( ) - 1 );
                    saveUsers( );
                }
            }
        }
    }
    
    public static String getSkin( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getSkin( );
        }
        return null;
    }
    
    public static Boolean getVanish( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getVanish( );
        }
        return false;
    }
    
    public static Boolean getStaff( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getStaff( );
        }
        return false;
    }
    
    public static Boolean getFrozen( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getFrozen( );
        }
        return false;
    }
    
    public static Boolean getFly( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getFly( );
        }
        return false;
    }
    
    public static String getStaffInventory( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getStaffInventory( );
        }
        return null;
    }
    
    public static String getStaffArmor( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getStaffArmor( );
        }
        return null;
    }
    
    public static String getFreezeHelmet( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getFreezeHelmet( );
        }
        return null;
    }
    
    public static Boolean getFakeJoinLeave( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getFakeJoinLeave( );
        }
        return false;
    }
    
    public static Boolean getMute( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getMute( );
        }
        return false;
    }
    
    public static Boolean getStaffChat( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getStaffChat( );
        }
        return false;
    }
    
    public static Double getMutedTime( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            return user.getMutedTime( );
        }
        return 0D;
    }
    
    public static String getIps( UUID uuid ){
        if ( isSaved( uuid ) ) {
            User user = findUser( uuid );
            HashMap < String, String > alts = user.getAlts( );
            String ips = "";
            for ( String s : alts.values( ) ) {
                ips = s + ",";
            }
            return ips;
        }
        return null;
    }
    
    public static HashMap < String, String > getIpsAsHash( UUID uuid ){
        if ( isSaved( uuid ) ) {
            return findUser( uuid ).getAlts( );
        }
        return null;
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
            HashMap < String, String > alts1 = user.getAlts( );
            alts1.put( alt.getName( ) , ip );
            user.setAlts( alts1 );
            updateUser( user.getUUID( ) , user );
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
