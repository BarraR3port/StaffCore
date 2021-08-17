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

public class sendWarnChangeAlert {
    public sendWarnChangeAlert( int id , String changer , String sender , String target , String reason , String ExpDate , String date , String status , String server ){
        Collection < ProxiedPlayer > networkPlayers = ProxyServer.getInstance( ).getPlayers( );
        if ( networkPlayers == null || networkPlayers.isEmpty( ) ) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput( );
        out.writeUTF( "WarnChange" );
        out.writeInt( id );
        out.writeUTF( changer );
        out.writeUTF( sender );
        out.writeUTF( target );
        out.writeUTF( reason );
        out.writeUTF( ExpDate );
        out.writeUTF( date );
        out.writeUTF( status );
        out.writeUTF( server );
        for ( Map.Entry < String, ServerInfo > servers : ProxyServer.getInstance( ).getServers( ).entrySet( ) ) {
            try {
                ProxyServer.getInstance( ).getServerInfo( servers.getKey( ) ).sendData( "sc:alerts" , out.toByteArray( ) );
            } catch ( NullPointerException ignored ) {
            }
        }
    }
}
