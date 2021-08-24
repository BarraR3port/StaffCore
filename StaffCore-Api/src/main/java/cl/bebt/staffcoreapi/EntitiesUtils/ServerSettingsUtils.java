/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.EntitiesUtils;

import cl.bebt.staffcoreapi.Entities.ServerSettings;
import cl.bebt.staffcoreapi.utils.DataExporter;
import cl.bebt.staffcoreapi.utils.Utils;
import com.google.gson.Gson;

import java.io.*;
import java.util.UUID;

public class ServerSettingsUtils {
    
    private static ServerSettings settings;
    
    public static void createServerSettings( String ServerName , UUID uuid ){
        if ( !settingsExists( ) ) {
            settings = new ServerSettings( ServerName , uuid , Utils.getPluginVersion( ) );
        }
    }
    
    public static Boolean settingsExists( ){
        return settings != null;
    }
    
    public static ServerSettings getSettings( ){
        return settings;
    }
    
    public static void setSettings( String uuid ){
        settings.setServerUUID( UUID.fromString( uuid ) );
        saveServerSettings( );
    }
    
    public static void saveServerSettings( ){
        try {
            Gson gson = new Gson( );
            File file = new File( Utils.getDataFolder( ) + "/serversettings.json" );
            file.getParentFile( ).mkdir( );
            file.createNewFile( );
            Writer writer = new FileWriter( file , false );
            gson.toJson( settings , writer );
            writer.flush( );
            writer.close( );
        } catch ( IOException error ) {
            error.printStackTrace( );
        }
    }
    
    public static void loadServerSettings( ){
        try {
            Gson gson = new Gson( );
            File file = new File( Utils.getDataFolder( ) + "/serversettings.json" );
            if ( file.exists( ) ) {
                Reader reader = new FileReader( file );
                ServerSettings Settings = gson.fromJson( reader , ServerSettings.class );
                settings = Settings;
            } else {
                DataExporter.createServerStats( );
            }
        } catch ( IOException error ) {
            error.printStackTrace( );
        }
    }
    
}
