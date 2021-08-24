/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffbungee.listeners;

import cl.bebt.staffbungee.utils.utils;
import cl.bebt.staffcoreapi.Entities.User;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class onPlayerJoin implements Listener {
    
    @EventHandler
    public void onPlayerJoin( LoginEvent e ){
        String ip = String.valueOf( e.getConnection( ).getSocketAddress( ) ).replace( "/" , "" );
        if ( !UserUtils.isSaved( e.getConnection( ).getUniqueId( ) ) ) {
            User user = UserUtils.createUser( e.getConnection( ).getName( ) , e.getConnection( ).getUniqueId( ) , ip );
            utils.Broadcast( user.getName( ) );
        }
        User user = UserUtils.findUser( e.getConnection( ).getUniqueId( ) );
        if ( UserUtils.isInOtherIP( user , ip ) ) {
            UserUtils.addAlt( user , ip );
        }
        UserUtils.isIPSaved( user , ip );
        for ( User u : UserUtils.getUsers( ) ) {
            utils.tell( ProxyServer.getInstance( ).getConsole( ) , u.getName( ) );
        }
        
        
    }
    
}
