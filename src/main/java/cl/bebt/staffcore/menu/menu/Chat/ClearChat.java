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

public class ClearChat extends PaginatedMenu {
    
    private final main plugin;
    
    public ClearChat( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( "&cClear Players chat" );
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ArrayList < Player > players = new ArrayList <>( Bukkit.getServer( ).getOnlinePlayers( ) );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "clearPlayer" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            Player jugador = p.getServer( ).getPlayer( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) );
            utils.ccPlayer( jugador );
            if ( p.equals( jugador ) ) {
                p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4You cleaned your chat" ) );
            } else {
                p.sendMessage( utils.chat( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4You cleaned the chat of: " + jugador.getName( ) ) ) );
                jugador.sendMessage( utils.chat( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4The player&r " + p.getName( ) + " &4cleaned the chat!" ) ) );
            }
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            if ( e.getClick( ).isLeftClick( ) ) {
                p.closeInventory( );
                new ChatSettings( main.getPlayerMenuUtility( p ) , main.plugin ).open( p );
            }
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.DARK_OAK_BUTTON ) ) {
            if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Back" ) ) {
                if ( page == 0 ) {
                    p.sendMessage( ChatColor.GRAY + "You are already on the first page." );
                } else {
                    page = page - 1;
                    super.open( p );
                }
            } else if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Next" ) ) {
                if ( !((index + 1) >= players.size( )) ) {
                    page = page + 1;
                    super.open( p );
                } else {
                    p.sendMessage( ChatColor.GRAY + "You are on the last page." );
                }
            }
        }
    }
    
    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        ArrayList < Player > players = new ArrayList <>( Bukkit.getServer( ).getOnlinePlayers( ) );
        if ( players != null && !players.isEmpty( ) ) {
            for ( int i = 0; i < getMaxItemsPerPage( ); i++ ) {
                index = getMaxItemsPerPage( ) * page + i;
                if ( index >= players.size( ) ) break;
                if ( players.get( index ) != null ) {
                    //////////////////////////////
                    ItemStack p_head = utils.getPlayerHead( players.get( index ).getName( ) );
                    ItemMeta meta = p_head.getItemMeta( );
                    ArrayList < String > lore = new ArrayList <>( );
                    meta.setDisplayName( players.get( index ).getName( ) );
                    lore.add( utils.chat( utils.chat( "&7Clear " ) + players.get( index ).getDisplayName( ) + utils.chat( "'s &7chat" ) ) );
                    meta.setLore( lore );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "clearPlayer" ) , PersistentDataType.STRING , "clearPlayer" );
                    p_head.setItemMeta( meta );
                    inventory.addItem( p_head );
                    /////////////////////////////
                }
            }
        }
    }
}
