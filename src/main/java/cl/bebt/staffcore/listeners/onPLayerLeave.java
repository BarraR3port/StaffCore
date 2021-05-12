package cl.bebt.staffcore.listeners;


import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.OpenEnderSee;
import cl.bebt.staffcore.utils.OpenInvSee;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class onPLayerLeave implements Listener {
    private final main plugin;
    
    public onPLayerLeave( main plugin ){
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerLeaveEvent( PlayerQuitEvent e ){
        Player p = e.getPlayer( );
        if ( main.invSee.containsValue( p ) ) {
            Player viewer = OpenInvSee.getOwner( p );
            viewer.closeInventory( );
            utils.tell( viewer , utils.getString( "invsee.disconnected" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
            utils.PlaySound( viewer , "invsee_close" );
            main.invSee.remove( p );
        }
        if ( main.enderSee.containsValue( p ) ) {
            Player viewer = OpenEnderSee.getOwner( p );
            viewer.closeInventory( );
            utils.tell( viewer , utils.getString( "invsee.disconnected" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
            utils.PlaySound( viewer , "endersee_close" );
            main.enderSee.remove( p );
        }
        if ( utils.getBoolean( "discord.type.debug.enabled_debugs.commands" ) ){
            ArrayList < String > dc = new ArrayList <>( );
            dc.add( "**Player:** " + p.getName( ) );
            dc.add( "**Reason:** " + e.getQuitMessage( ) );
            utils.sendDiscordDebugMsg( e.getPlayer( ) ,"⚠ Player Leave ⚠" , dc );
        }
    }
    
}
