package cl.bebt.staffcore.menu.menu.Chat;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
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
        return utils.chat( utils.getString( "chat.clear_chat.name" , "menu" , null ) );
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
                utils.tell( p , utils.getString( "clear_chat.own" , "lg" , "staff" ) );
            } else {
                utils.tell( p , utils.getString( "clear_chat.player" , "lg" , "staff" ).replace( "%player%",jugador.getName( ) ) );
                utils.tell( jugador , utils.getString( "clear_chat.global" , "lg" , "sv" ).replace( "%player%",p.getName( ) ) );
            }
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ChatSettings( main.getPlayerMenuUtility( p ) , main.plugin ).open( p );
            }
        } else if ( e.getCurrentItem( ).equals( back( ) ) ) {
            if ( page == 0 ) {
                utils.tell( p , utils.getString( "menu.already_in_first_page" , "lg" , "sv" ) );
            } else {
                page--;
                p.closeInventory( );
                open( p );
            }
        } else if ( e.getCurrentItem( ).equals( next( ) ) ) {
            if ( index + 1 <= players.size( ) ) {
                page++;
                p.closeInventory( );
                open( p );
            } else {
                utils.tell( p , utils.getString( "menu.already_in_last_page" , "lg" , "sv" ) );
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
