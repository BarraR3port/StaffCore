/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffbungee.PluginMessageChannel.HubCore;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;
import java.util.Map;


public class sendServerStatus {
    
    public sendServerStatus( String serverTarget , String server ){
        ProxyServer.getInstance( ).getServerInfo( serverTarget ).ping( ( response , error ) -> {
            Collection < ProxiedPlayer > networkPlayers = ProxyServer.getInstance( ).getPlayers( );
            if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
                return;
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput( );
            out.writeUTF( "SServerStatus" );
            out.writeUTF( serverTarget );
            out.writeBoolean( error.getCause( ) == null );
            out.writeUTF( server );
            for ( Map.Entry < String, ServerInfo > testPlayer : ProxyServer.getInstance( ).getServers( ).entrySet( ) ) {
                try {
                    ProxyServer.getInstance( ).getServerInfo( testPlayer.getKey( ) ).sendData( "hc:msg" , out.toByteArray( ) );
                } catch ( NullPointerException ignored ) {
                }
            }
        } );
        
    }
}
