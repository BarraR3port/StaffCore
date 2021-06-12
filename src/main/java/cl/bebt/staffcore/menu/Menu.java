package cl.bebt.staffcore.menu;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Menu implements InventoryHolder {
    
    protected Inventory inventory;
    
    protected PlayerMenuUtility playerMenuUtility;
    
    public Menu( PlayerMenuUtility playerMenuUtility ){
        this.playerMenuUtility = playerMenuUtility;
    }
    
    protected ItemStack redPanel( ){
        ItemStack panel = new ItemStack( Material.RED_STAINED_GLASS_PANE );
        ItemMeta panel_meta = panel.getItemMeta( );
        panel_meta.setDisplayName( " " );
        panel_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "panel" ) , PersistentDataType.STRING , "panel" );
        panel.setItemMeta( panel_meta );
        return panel;
    }
    
    protected ItemStack greenPanel( ){
        ItemStack panel = new ItemStack( Material.LIME_STAINED_GLASS_PANE );
        ItemMeta panel_meta = panel.getItemMeta( );
        panel_meta.setDisplayName( " " );
        panel_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "panel" ) , PersistentDataType.STRING , "panel" );
        panel.setItemMeta( panel_meta );
        return panel;
    }
    
    protected ItemStack bluePanel( ){
        ItemStack panel = new ItemStack( Material.CYAN_STAINED_GLASS_PANE );
        ItemMeta panel_meta = panel.getItemMeta( );
        panel_meta.setDisplayName( " " );
        panel_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "panel" ) , PersistentDataType.STRING , "panel" );
        panel.setItemMeta( panel_meta );
        return panel;
    }
    
    protected ItemStack next( ){
        ItemStack next = new ItemStack( Material.getMaterial( utils.getString( "menu_items.next.material" , "item" , null ) ) );
        ItemMeta next_meta = next.getItemMeta( );
        next_meta.setDisplayName( utils.chat( utils.getString( "menu_items.back.name" , "item" , null ) ) );
        ArrayList < String > lore = new ArrayList <>( );
        for ( String key : utils.getStringList( "menu_items.next.lore" , "item" ) ) {
            lore.add( utils.chat( key ) );
        }
        next_meta.setLore( lore );
        next_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "next" ) , PersistentDataType.STRING , "next" );
        next.setItemMeta( next_meta );
        return next;
    }
    
    protected ItemStack back( ){
        ItemStack back = new ItemStack( Material.getMaterial( utils.getString( "menu_items.back.material" , "item" , null ) ) );
        ItemMeta back_meta = back.getItemMeta( );
        back_meta.setDisplayName( utils.chat( utils.getString( "menu_items.back.name" , "item" , null ) ) );
        ArrayList < String > lore = new ArrayList <>( );
        for ( String key : utils.getStringList( "menu_items.back.lore" , "item" ) ) {
            lore.add( utils.chat( key ) );
        }
        back_meta.setLore( lore );
        back_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "back" ) , PersistentDataType.STRING , "back" );
        back.setItemMeta( back_meta );
        return back;
    }
    
    protected ItemStack close( ){
        ItemStack close = new ItemStack( Material.getMaterial( utils.getString( "menu_items.close.material" , "item" , null ) ) );
        ItemMeta close_meta = close.getItemMeta( );
        close_meta.setDisplayName( utils.chat( utils.getString( "menu_items.close.name" , "item" , null ) ) );
        ArrayList < String > lore = new ArrayList <>( );
        for ( String key : utils.getStringList( "menu_items.close.lore" , "item" ) ) {
            lore.add( utils.chat( key ) );
        }
        close_meta.setLore( lore );
        close_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "BARRIER" ) , PersistentDataType.STRING , "BARRIER" );
        close.setItemMeta( close_meta );
        return close;
    }
    
    public abstract String getMenuName( );
    
    public abstract int getSlots( );
    
    public abstract void handleMenu( InventoryClickEvent e );
    
    public abstract void setMenuItems( );
    
    public void open( ){
        inventory = Bukkit.createInventory( this , getSlots( ) , getMenuName( ) );
        Bukkit.getScheduler( ).scheduleSyncDelayedTask( main.plugin , ( ) -> {
            this.setMenuItems( );
            playerMenuUtility.getOwner( ).openInventory( inventory );
        } , 1L );
        
    }
    
    public ItemStack makeItem( Material material , String displayName , String... lore ){
        ItemStack item = new ItemStack( material );
        ItemMeta itemMeta = item.getItemMeta( );
        itemMeta.setDisplayName( displayName );
        itemMeta.setLore( Arrays.asList( lore ) );
        item.setItemMeta( itemMeta );
        return item;
    }
    
    @Override
    public Inventory getInventory( ){
        return inventory;
    }
    
}
