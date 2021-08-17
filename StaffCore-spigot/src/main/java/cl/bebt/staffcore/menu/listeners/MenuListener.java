/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.listeners;

import cl.bebt.staffcore.menu.InventoryMenu;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.MenuC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;


public class MenuListener implements Listener {
    @EventHandler
    public void onMenuClick( InventoryClickEvent e ){
        InventoryHolder holder = e.getInventory( ).getHolder( );
        if ( holder instanceof Menu ) {
            e.setCancelled( true );
            if ( e.getCurrentItem( ) == null ) {
                return;
            }
            Menu menu = ( Menu ) holder;
            menu.handleMenu( e );
        }
        if ( holder instanceof MenuC ) {
            e.setCancelled( true );
            if ( e.getCurrentItem( ) == null ) {
                return;
            }
            MenuC menu = ( MenuC ) holder;
            menu.handleMenu( e );
        }
        if ( holder instanceof InventoryMenu ) {
            if ( e.getSlotType( ) == InventoryType.SlotType.OUTSIDE ) return;
            InventoryMenu menu = ( InventoryMenu ) holder;
            menu.handleMenu( e );
        }
    }
}
