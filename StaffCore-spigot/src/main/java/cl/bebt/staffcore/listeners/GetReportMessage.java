/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.listeners;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.menu.Bangui.ChoseBanType;
import cl.bebt.staffcore.menu.menu.Warn.WarnTimeChose;
import cl.bebt.staffcoreapi.EntitiesUtils.PersistentDataUtils;
import cl.bebt.staffcoreapi.utils.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class GetReportMessage implements Listener {
    
    private final main plugin;
    
    public GetReportMessage( main plugin ){
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void reportMessage( AsyncPlayerChatEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( main.plugin , "reportmsg" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
            String reason = e.getMessage( );
            String reported = p.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "reportmsg" ) , PersistentDataType.STRING );
            Bukkit.getScheduler( ).runTask( plugin , ( ) -> new ReportManager( ).ReportPlayer( p , reason , reported ) );
            PlayerData.remove( new NamespacedKey( main.plugin , "reportmsg" ) );
        } else if ( PersistentDataUtils.has( p.getUniqueId( ) , "banmsg" ) ) {
            e.setCancelled( true );
            String reason = e.getMessage( );
            String banned = PersistentDataUtils.getString( p.getUniqueId( ) , "banmsg" );
            PersistentDataUtils.remove( p.getUniqueId( ) , "ban-ip" );
            Bukkit.getScheduler( ).runTask( plugin , ( ) -> new ChoseBanType( main.getPlayerMenuUtility( p ) , plugin , p , banned , reason ).open( ) );
            PersistentDataUtils.remove( p.getUniqueId( ) , "banmsg" );
        } else if ( PlayerData.has( new NamespacedKey( main.plugin , "warnmsg" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
            String reason = e.getMessage( );
            String warned = p.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "warnmsg" ) , PersistentDataType.STRING );
            Bukkit.getScheduler( ).runTask( plugin , ( ) -> new WarnTimeChose( main.getPlayerMenuUtility( p ) , plugin , p , warned , reason ).open( ) );
            PlayerData.remove( new NamespacedKey( main.plugin , "warnmsg" ) );
        }
    }
    
}
