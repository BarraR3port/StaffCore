package cl.bebt.staffcore.menu.menu.Bangui;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class AmountBanned extends PaginatedMenu {
    private final main plugin;
    private final Player player;
    private final String banned;
    private final String reason;
    
    public AmountBanned( PlayerMenuUtility playerMenuUtility , main plugin , Player player , String banned , String reason ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.player = player;
        this.banned = banned;
        this.reason = reason;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "bangui.amount_banned.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        int amount = 49;
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER ) ) {
            p.closeInventory( );
            int yep = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER );
            player.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "amount" ) , PersistentDataType.INTEGER , yep );
            new QuantityBanned( playerMenuUtility , plugin , player , banned , reason ).open( p );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ChoseBanType( playerMenuUtility , plugin , player , banned , reason ).open( p );
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
            if ( index + 1 <= amount ) {
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
        int amount = 49;
        player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "seconds" ) );
        player.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "amount" ) );
        for ( int i = 1; i < getMaxItemsPerPage( ) + 1; i++ ) {
            index = getMaxItemsPerPage( ) * page + i;
            if ( index >= amount ) break;
            //////////////////////////////
            ItemStack clock = new ItemStack( Material.CLOCK , index );
            ItemMeta meta = clock.getItemMeta( );
            meta.setDisplayName( utils.chat( "&a" ) + index );
            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "amount" ) , PersistentDataType.INTEGER , index );
            clock.setItemMeta( meta );
            inventory.addItem( clock );
            /////////////////////////////
        }
        
        
    }
}
