package cl.bebt.staffcore.menu.menu.Chat;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class MutePlayer extends PaginatedMenu {
    private final main plugin;
    
    public MutePlayer( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    public String getMenuName( ){
        return utils.chat( "&cMute Players Chat" );
    }
    
    public int getSlots( ){
        return 54;
    }
    
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ArrayList < Player > players = new ArrayList <>( Bukkit.getServer( ).getOnlinePlayers( ) );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( this.plugin , "mute" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            Player jugador = p.getServer( ).getPlayer( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) );
            p.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "muted_player" ) , PersistentDataType.STRING , jugador.getName( ) );
            (new Amount( main.getPlayerMenuUtility( p ) , this.plugin , p )).open( p );
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            p.closeInventory( );
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.DARK_OAK_BUTTON ) ) {
            if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Back" ) ) {
                if ( this.page == 0 ) {
                    p.sendMessage( ChatColor.GRAY + "You are already on the first page." );
                } else {
                    this.page--;
                    open( p );
                }
            } else if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Next" ) ) {
                if ( this.index + 1 < players.size( ) ) {
                    this.page++;
                    open( p );
                } else {
                    p.sendMessage( ChatColor.GRAY + "You are on the last page." );
                }
            }
        }
    }
    
    public void setMenuItems( ){
        addMenuBorder( );
        ArrayList < Player > players = new ArrayList <>( Bukkit.getServer( ).getOnlinePlayers( ) );
        if ( players != null && !players.isEmpty( ) )
            for ( int i = 0; i < getMaxItemsPerPage( ); i++ ) {
                this.index = getMaxItemsPerPage( ) * this.page + i;
                if ( this.index >= players.size( ) )
                    break;
                if ( players.get( this.index ) != null ) {
                    ItemStack p_head = utils.getPlayerHead( players.get( this.index ).getName( ) );
                    ItemMeta meta = p_head.getItemMeta( );
                    ArrayList < String > lore = new ArrayList <>( );
                    meta.setDisplayName( players.get( this.index ).getName( ) );
                    lore.add( utils.chat( utils.chat( "&cMute &r" ) + players.get( this.index ).getDisplayName( ) ) );
                    meta.setLore( lore );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "mute" ) , PersistentDataType.STRING , "mute" );
                    p_head.setItemMeta( meta );
                    this.inventory.addItem( p_head );
                }
            }
    }
}
