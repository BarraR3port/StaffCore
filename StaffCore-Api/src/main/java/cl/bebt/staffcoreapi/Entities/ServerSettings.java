/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.Entities;

import java.util.HashMap;
import java.util.UUID;

public class ServerSettings {
    
    private String ServerName;
    
    private UUID ServerUUID;
    
    private HashMap < String, String > Servers;
    
    private String Version;
    
    public ServerSettings( String serverName , UUID uuid , String version ){
        ServerName = serverName;
        ServerUUID = uuid;
        Servers = new HashMap <>( );
        Version = version;
    }
    
    public String getServerName( ){
        return ServerName;
    }
    
    public void setServerName( String serverName ){
        ServerName = serverName;
    }
    
    public UUID getServerUUID( ){
        return ServerUUID;
    }
    
    public void setServerUUID( UUID serverUUID ){
        ServerUUID = serverUUID;
    }
}
