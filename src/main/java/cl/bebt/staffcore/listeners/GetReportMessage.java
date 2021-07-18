package cl.bebt.staffcore.listeners;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.menu.Bangui.ChoseBanType;
import cl.bebt.staffcore.menu.menu.Warn.WarnTimeChose;
import cl.bebt.staffcore.utils.ReportPlayer;
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
            Bukkit.getScheduler( ).runTask( plugin , ( ) -> new ReportPlayer( p , reason , reported ) );
            PlayerData.remove( new NamespacedKey( main.plugin , "reportmsg" ) );
        } else if ( PlayerData.has( new NamespacedKey( main.plugin , "banmsg" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
            String reason = e.getMessage( );
            String banned = p.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "banmsg" ) , PersistentDataType.STRING );
            Bukkit.getScheduler( ).runTask( plugin , ( ) -> new ChoseBanType( main.getPlayerMenuUtility( p ) , plugin , p , banned , reason ).open( ) );
            PlayerData.remove( new NamespacedKey( main.plugin , "banmsg" ) );
        } else if ( PlayerData.has( new NamespacedKey( main.plugin , "warnmsg" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
            String reason = e.getMessage( );
            String warned = p.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "warnmsg" ) , PersistentDataType.STRING );
            Bukkit.getScheduler( ).runTask( plugin , ( ) -> new WarnTimeChose( main.getPlayerMenuUtility( p ) , plugin , p , warned , reason ).open( ) );
            PlayerData.remove( new NamespacedKey( main.plugin , "warnmsg" ) );
        }
    }
    
}
