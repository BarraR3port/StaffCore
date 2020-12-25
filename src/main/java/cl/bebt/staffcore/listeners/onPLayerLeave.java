package cl.bebt.staffcore.listeners;


import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.OpenEnderSee;
import cl.bebt.staffcore.utils.OpenInvSee;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class onPLayerLeave implements Listener{
    private final main plugin;

    public onPLayerLeave( main plugin ){
        this.plugin = plugin;
    }

    @EventHandler
    void onPlayerLeaveEvent( PlayerQuitEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
            for ( Player player : Bukkit.getServer( ).getOnlinePlayers( ) ) {
                if ( player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ||
                        player.hasPermission( "staffcore.vanish.see" ) || player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                    player.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + p.getDisplayName( ) + " &3(&dVanished&3)" ) );
                }
            }
        }
        if ( main.invSee.containsValue( p ) ) {
            Player viewer = OpenInvSee.getOwner( p );
            viewer.closeInventory( );
            utils.tell( viewer , utils.getString( "staff.staff_prefix" ) + "&cThe player " + p.getName( ) + " has disconnected and you can't edit offline players inventories." );
            utils.PlaySound( viewer , "invsee_close" );
            main.invSee.remove( p );
        }
        if ( main.enderSee.containsValue( p ) ) {
            Player viewer = OpenEnderSee.getOwner( p );
            viewer.closeInventory( );
            utils.tell( viewer , utils.getString( "staff.staff_prefix" ) + "&cThe player " + p.getName( ) + " has disconnected and you can't edit offline players Ender Chest." );
            utils.PlaySound( viewer , "endersee_close" );
            main.enderSee.remove( p );
        }
    }

}
