/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffbungee.PluginMessageChannel.StaffCore;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;
import java.util.Map;

public class sendSLRequest {
    public sendSLRequest( String sender , String server ){
        Collection < ProxiedPlayer > networkPlayers = ProxyServer.getInstance( ).getPlayers( );
        if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput( );
        out.writeUTF( "SendSLReceive" );
        out.writeUTF( sender );
        out.writeUTF( server );
        
        for ( Map.Entry < String, ServerInfo > testPlayer : ProxyServer.getInstance( ).getServers( ).entrySet( ) ) {
            try {
                ProxyServer.getInstance( ).getServerInfo( testPlayer.getKey( ) ).sendData( "sc:stafflist" , out.toByteArray( ) );
            } catch ( NullPointerException ignored ) {
            }
        }
    }
}
