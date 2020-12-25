package cl.bebt.staffcore.menu;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;

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
        inventory = Bukkit.createInventory( this , getSlots( ) , getMenuName( ) );
        
        //grab all the items specified to be used for this menu and add to inventory
        this.setMenuItemsPlayer( p );
        //open the inventory for the player
        playerMenuUtility.getOwner( ).openInventory( inventory );
        
    }
    
    public ItemStack makeItem( Material material , String displayName , String... lore ){
        
        ItemStack item = new ItemStack( material );
        ItemMeta itemMeta = item.getItemMeta( );
        itemMeta.setDisplayName( utils.chat( displayName ) );
        itemMeta.setLore( Arrays.asList( lore ) );
        if ( item.getType( ).equals( Material.BARRIER ) ) {
            ArrayList < String > go_back = new ArrayList <>( );
            go_back.add( utils.chat( "&aRight click to close" ) );
            go_back.add( utils.chat( "&aLeft click to go back" ) );
            itemMeta.setLore( go_back );
            itemMeta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "BARRIER" ) , PersistentDataType.STRING , "BARRIER" );
        }
        item.setItemMeta( itemMeta );
        
        return item;
    }
    
    public ItemStack makeItem( Material material , String displayName , ArrayList < String > lore ){
        ItemStack item = new ItemStack( material );
        ItemMeta itemMeta = item.getItemMeta( );
        itemMeta.setDisplayName( utils.chat( displayName ) );
        itemMeta.setLore( lore );
        item.setItemMeta( itemMeta );
        return item;
    }
    
    @Override
    public Inventory getInventory( ){
        return inventory;
    }
    
}
