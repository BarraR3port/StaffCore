package cl.bebt.staffcore.menu;

import cl.bebt.staffcore.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class InventoryMenu implements InventoryHolder {
    
    protected Inventory inventory;
    
    protected PlayerMenuUtility playerMenuUtility;
    
    public InventoryMenu( PlayerMenuUtility playerMenuUtility ){
        this.playerMenuUtility = playerMenuUtility;
    }
    
    
    public abstract String getMenuName( );
    
    public abstract int getSlots( );
    
    public abstract void handleMenu( InventoryClickEvent e );
    
    public abstract void handleMenu( InventoryDragEvent e );
    
    public abstract void handleMenu( InventoryCloseEvent e );
    
    public abstract void setMenuItemsPlayer( Player p );
    
    public void open( Player p ){
        Bukkit.getScheduler( ).runTask( main.plugin , ( ) -> {
            inventory = Bukkit.createInventory( this , getSlots( ) , getMenuName( ) );
            this.setMenuItemsPlayer( p );
            playerMenuUtility.getOwner( ).openInventory( inventory );
        } );
        
    }
    
    @Override
    public Inventory getInventory( ){
        return inventory;
    }
    
}
