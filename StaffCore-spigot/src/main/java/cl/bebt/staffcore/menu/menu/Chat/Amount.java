/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Chat;

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

public class Amount extends PaginatedMenu {
    private final main plugin;
    
    private final Player player;
    
    public Amount( PlayerMenuUtility playerMenuUtility , main plugin , Player player ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.player = player;
    }
    
    public String getMenuName( ){
        return utils.chat( utils.getString( "chat.amount_chat.name" , "menu" , null ) );
    }
    
    public int getSlots( ){
        return 54;
    }
    
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        int amount = utils.getInt( "inventories.time_limit" , null ) + 1;
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( this.plugin , "amount" ) , PersistentDataType.INTEGER ) ) {
            p.closeInventory( );
            int yep = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( this.plugin , "amount" ) , PersistentDataType.INTEGER ).intValue( );
            this.player.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "amount" ) , PersistentDataType.INTEGER , Integer.valueOf( yep ) );
            (new Quantity( this.playerMenuUtility , this.plugin , this.player )).open( );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new MutePlayer( this.playerMenuUtility , plugin ).open( );
            }
        } else if ( e.getCurrentItem( ).equals( back( ) ) ) {
            if ( page == 0 ) {
                utils.tell( p , utils.getString( "menu.already_in_first_page" , "lg" , "sv" ) );
            } else {
                page--;
                p.closeInventory( );
                open( );
            }
        } else if ( e.getCurrentItem( ).equals( next( ) ) ) {
            if ( index + 1 <= amount ) {
                page++;
                p.closeInventory( );
                open( );
            } else {
                utils.tell( p , utils.getString( "menu.already_in_last_page" , "lg" , "sv" ) );
            }
        }
    }
    
    public void setMenuItems( ){
        addMenuBorder( );
        int amount = utils.getInt( "inventories.time_limit" , null ) + 1;
        this.player.getPersistentDataContainer( ).remove( new NamespacedKey( this.plugin , "seconds" ) );
        this.player.getPersistentDataContainer( ).remove( new NamespacedKey( this.plugin , "amount" ) );
        for ( int i = 1; i < getMaxItemsPerPage( ) + 1; i++ ) {
            this.index = getMaxItemsPerPage( ) * this.page + i;
            if ( this.index >= amount )
                break;
            ItemStack clock = new ItemStack( Material.CLOCK , this.index );
            ItemMeta meta = clock.getItemMeta( );
            meta.setDisplayName( utils.chat( "&a" ) + this.index );
            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "amount" ) , PersistentDataType.INTEGER , Integer.valueOf( this.index ) );
            clock.setItemMeta( meta );
            this.inventory.addItem( clock );
        }
    }
}
