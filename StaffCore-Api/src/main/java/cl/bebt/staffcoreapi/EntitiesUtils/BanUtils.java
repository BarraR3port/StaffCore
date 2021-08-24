/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.EntitiesUtils;

import cl.bebt.staffcoreapi.Entities.Ban;
import cl.bebt.staffcoreapi.utils.Utils;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class BanUtils {
    
    private static ArrayList < Ban > bans = new ArrayList <>( );
    
    public static ArrayList < Ban > getBans( ){
        return bans;
    }
    
    public static void createBan( UUID BannerUUID , UUID BannedUUID , String Reason , Date CreateDate , Date ExpDate , Boolean Permanent , Boolean IpBanned ){
        Ban ban = new Ban( UUID.randomUUID( ) , BannerUUID , BannedUUID , Reason , CreateDate , ExpDate , Permanent , IpBanned , true );
        bans.add( ban );
        saveBans( );
    }
    
    public static Integer getCurrentBans( ){
        return bans.size( );
    }
    
    public static Ban findBan( UUID BanUUID ){
        for ( Ban ban : bans ) {
            if ( ban.getBanId( ).equals( BanUUID ) ) {
                return ban;
            }
        }
        return null;
    }
    
    public static boolean isBanned( UUID BannedUUID ){
        for ( Ban ban : bans ) {
            if ( ban.getBannedUUID( ).equals( BannedUUID ) ) {
                return true;
            }
        }
        return false;
    }
    
    public static void deleteBan( UUID BanUUID ){
        bans.removeIf( ban -> ban.getBanId( ).equals( BanUUID ) );
        saveBans( );
    }
    
    public static void updateBan( UUID BanId , Ban Ban ){
        if ( isBanned( BanId ) ) {
            for ( Ban ban : bans ) {
                if ( ban.getBanId( ).equals( BanId ) ) {
                    ban.setBannedUUID( Ban.getBannedUUID( ) );
                    ban.setBannerUUID( Ban.getBannerUUID( ) );
                    ban.setIpBanned( Ban.getIpBanned( ) );
                    ban.setCreateDate( Ban.getCreateDate( ) );
                    ban.setExpDate( Ban.getExpDate( ) );
                    ban.setPermanent( Ban.getPermanent( ) );
                    ban.setReason( Ban.getReason( ) );
                    saveBans( );
                }
            }
        }
    }
    
    public static void saveBans( ){
        try {
            Gson gson = new Gson( );
            File file = new File( Utils.getDataFolder( ) + "/bans.json" );
            file.getParentFile( ).mkdir( );
            file.createNewFile( );
            Writer writer = new FileWriter( file , false );
            gson.toJson( bans , writer );
            writer.flush( );
            writer.close( );
        } catch ( IOException error ) {
            error.printStackTrace( );
        }
    }
    
    public static void loadBans( ){
        try {
            Gson gson = new Gson( );
            File file = new File( Utils.getDataFolder( ) + "/bans.json" );
            if ( file.exists( ) ) {
                Reader reader = new FileReader( file );
                Ban[] ban = gson.fromJson( reader , Ban[].class );
                bans = new ArrayList <>( Arrays.asList( ban ) );
            } else {
                saveBans( );
            }
        } catch ( IOException error ) {
            error.printStackTrace( );
        }
    }
    
}
